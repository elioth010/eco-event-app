package com.bytesw.consultadecuentas.bs.service;

import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.eis.bo.UserToken;
import com.bytesw.consultadecuentas.security.SessionManager;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.activity.CardViewActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginTask extends AbstractGetTask<Void, Void, Void> {

	private static final String TAG = "LoginTask";
	protected AbstractActivity parentActivity;
	private ProgressDialog dialog;

	private String scope;
	private String user;
	private String password;
	private String grantType;

	public LoginTask(AbstractActivity parentActivity, String user, String password, String scope, String grantType) {
		super(parentActivity);
		this.parentActivity = parentActivity;
		this.scope = scope;
		this.user = user;
		this.password = password;
		this.grantType = grantType;
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(parentActivity, parentActivity.getResources().getString(R.string.app_progress_dialog_login_title), parentActivity.getResources().getString(R.string.app_progress_dialog_wait_message), true);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			final String url = super.BASE_URL+"/oauth/token?grant_type=" + this.grantType + "&scope=" + this.scope;
			RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(getHeaders(this.user, this.password)), String.class);
			Map<String, String> tokenInfo = fetchToken(restTemplate, response);

			SessionManager manager = this.parentActivity.getSession();
			manager.setToken(tokenInfo.get("token"));
			manager.setUser(user);
			manager.setTokenType(tokenInfo.get("tokenType"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			UserToken userLogged = new UserToken(1, user, manager.getToken(), manager.getTokenType(), dateFormat.format(new GregorianCalendar().getTime()));
			this.parentActivity.save(userLogged);
		} catch (Exception ex) {
			if (ex instanceof ResourceAccessException) {
				if (ex.getCause() instanceof ConnectException) {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message), ex, true);
					return null;
				}else if(ex.getCause() instanceof ConnectTimeoutException){
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex, true);
					return null;
				}
			}
			if (ex instanceof HttpClientErrorException) {
				if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
					System.out.println(((HttpClientErrorException) ex).getResponseBodyAsString());
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_login_message), ex, true);
					return null;
				}
			} else {
				onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex, false);
				return null;
			}
		}

		return null;
	}

	private Map<String, String> fetchToken(RestTemplate restTemplate, ResponseEntity<String> response) throws IOException {
		HashMap<?, ?> myMap;
		ObjectMapper objectMapper = new ObjectMapper();
		String token = null;
		String tokenType = null;
		String userID = null;
		try {
			myMap = objectMapper.readValue(response.getBody().toString(),HashMap.class);
			System.out.println("Map: " + myMap);
			token = (String) myMap.get("access_token");
			tokenType = (String) myMap.get("token_type");
			userID = (String) myMap.get("user_id");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, String> tokenInfo = new HashMap<String, String>();
		tokenInfo.put("token", token);
		tokenInfo.put("tokenType", tokenType);
		tokenInfo.put("userId", userID);

		Intent intent = new Intent(this.parentActivity, CardViewActivity.class);
		this.parentActivity.startActivity(intent);
		this.parentActivity.finish();
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("Token: " + token);
		return tokenInfo;
	}

	@Override
	protected void onPostExecute(Void result) {
		dialog.cancel();
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onError(String msg, Exception e, boolean handled) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		if(handled){
			parentActivity.show(msg);
		}else{
			parentActivity.handleException(e);
		}
		return;// muestra mensaje de error
	}

}
