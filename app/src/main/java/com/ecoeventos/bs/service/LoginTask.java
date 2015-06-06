package com.ecoeventos.bs.service;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.ecoeventos.R;
import com.ecoeventos.eis.bo.UserToken;
import com.ecoeventos.eis.dto.UserDTO;
import com.ecoeventos.security.SessionManager;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.activity.CardViewActivity;
import com.ecoeventos.view.activity.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginTask extends AbstractGetTask<Void, Void, UserDTO> {

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
	protected UserDTO doInBackground(Void... params) {
		ResponseEntity<UserDTO> response=null;
		try {
			final String url = super.BASE_URL + "/login/login?username="+user+"&password="+getMD5(password);
			RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
			// Create the request body as a MultiValueMap
			response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(null), UserDTO.class);
			SessionManager manager = this.parentActivity.getSession();
			manager.setUsername(user);
			manager.setId(response.getBody().getId());
			return response.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
			if (ex instanceof ResourceAccessException) {
				if (ex.getCause() instanceof ConnectException) {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message), ex, true);
					return null;
				} else if (ex.getCause() instanceof ConnectTimeoutException) {
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

		return response.getBody();
	}
	@Override
	protected void onPostExecute(UserDTO result) {
		dialog.cancel();
		if(result!=null) {
			try {
				Intent intent = new Intent(parentActivity, MainActivity.class);
				parentActivity.startActivity(intent);
				parentActivity.finish();
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			onError(this.parentActivity.getResources().getString(R.string.app_toast_error_login_message), null, true);
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
