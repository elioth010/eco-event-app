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

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.eis.bo.UserToken;
import com.bytesw.consultadecuentas.security.SessionManager;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.activity.AutenticacionActivity;

public class LogOutTask extends AbstractGetTask<Void, Void, Void> {
	private static final String TAG = "LogoutTask";
	protected AbstractActivity parentActivity;
	private ProgressDialog dialog;
	private String token;
	private String tokenType;

	public LogOutTask(AbstractActivity parentActivity, String token, String tokenType) {
		super(parentActivity);
		this.parentActivity = parentActivity;
		this.token=token;
		this.tokenType = tokenType;
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(parentActivity, parentActivity.getResources().getString(R.string.app_progress_dialog_logout_title), parentActivity.getResources().getString(R.string.app_progress_dialog_wait_message), true);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			final String url = super.BASE_URL+"/oauth/token/revoke";
			RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
			HttpEntity<String> httpEntity = new HttpEntity<String>(token, getBearerHeaders(tokenType, token));
            ResponseEntity<Boolean> revoke = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Boolean.class);
			
			if(revoke.getBody()){
				Intent logout = new Intent(this.parentActivity, AutenticacionActivity.class);
				SessionManager sessionManager = (SessionManager) this.parentActivity.getApplicationContext();
				sessionManager.setToken("");
				sessionManager.setTokenType("");
				try{
					UserToken token = (UserToken) this.parentActivity.findAll(UserToken.class).get(0);
					token.delete();
				}catch(Exception e){
					Log.e(TAG, "No hay token almacenado");
				}
				this.parentActivity.startActivity(logout);
				this.parentActivity.finish();
			}	
		} catch (Exception ex) {
			if (ex instanceof ResourceAccessException) {
				if (ex.getCause() instanceof ConnectException) {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message),ex);
				}
				else if(ex.getCause() instanceof ConnectTimeoutException){
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
					return null;
				}
			}
			if (ex instanceof HttpClientErrorException) {
				if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
					//TODO Cambiar por Clase a la que se va a loguear de momento apunta a menu
					Intent logout = new Intent(this.parentActivity, AutenticacionActivity.class);
					SessionManager sessionManager = (SessionManager) this.parentActivity.getApplicationContext();
					sessionManager.setToken("");
					sessionManager.setTokenType("");
					this.parentActivity.startActivity(logout);
					this.parentActivity.finish();
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_login_message), ex);
				}
			} else {
				onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex);
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		dialog.cancel();
	}
	
	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		parentActivity.show(msg);
		parentActivity.handleException(e);
		return;// muestra mensaje de error
	}
}
