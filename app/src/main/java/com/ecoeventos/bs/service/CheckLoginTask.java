package com.bytesw.consultadecuentas.bs.service;

import java.net.ConnectException;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.util.Log;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.eis.bo.UserToken;
import com.bytesw.consultadecuentas.security.SessionManager;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.activity.CardViewActivity;

public class CheckLoginTask extends AbstractGetTask<Void, Void, Boolean> {

	protected AbstractActivity parentActivity;

	private String token;
	private String tokenType;
	
	public CheckLoginTask(AbstractActivity abstractActivity, String token, String tokenType) {
		super(abstractActivity);
		this.parentActivity = abstractActivity;
		this.token = token;
		this.tokenType = tokenType;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			final String url = super.BASE_URL+"/oauth/token/revoke";
			RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(getBearerHeaders(tokenType, token));
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
			
			return response.getBody();
		} catch (Exception ex) {
			if (ex instanceof ResourceAccessException) {
				if (ex.getCause() instanceof ConnectException) {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message),ex);
				}
				else if(ex.getCause() instanceof ConnectTimeoutException){
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
					return false;
				}
			}
			if (ex instanceof HttpClientErrorException) {
				if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
					SessionManager sessionManager = (SessionManager) this.parentActivity.getApplicationContext();
					sessionManager.setToken("");
					sessionManager.setTokenType("");
					try{
						UserToken token = (UserToken) this.parentActivity.findAll(UserToken.class).get(0);
						token.delete();
						return false;
					}catch(Exception e){
						Log.e(TAG, "No hay token almacenado");
					}
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result!=null){
			if(result){
				Intent logout = new Intent(this.parentActivity, CardViewActivity.class);
				this.parentActivity.startActivity(logout);
				super.onPostExecute(result);
				this.parentActivity.finish();
			}
		}
	}

}
