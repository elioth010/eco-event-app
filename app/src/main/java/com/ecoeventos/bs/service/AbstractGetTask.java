package com.bytesw.consultadecuentas.bs.service;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;

public abstract class AbstractGetTask<Params, Progress, Result> extends AsyncTask <Params, Progress, Result> {
	
	protected static final String TAG = "ActivityTask";
    //"http://servicio.bytesw.com:8280/services/";
	protected final String BASE_URL = "http://servicio.bytesw.com:8280/services";
	protected AbstractActivity parentActivity;
	private ProgressDialog dialog;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = ProgressDialog.show(parentActivity, parentActivity.getString(R.string.app_progress_dialog_loading_title), parentActivity.getString(R.string.app_progress_dialog_wait_message), true);
	}
	
	public AbstractGetTask(AbstractActivity abstractActivity) {
		this.parentActivity = abstractActivity;
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		dialog.dismiss();
	}

	public static HttpHeaders getHeaders(final String username, final String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}
	
	public static ClientHttpRequestFactory clientHttpRequestFactory(){
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(20000);
        return factory;
	}

	public static HttpHeaders getBearerHeaders(final String tokenType, final String token) {
		return new HttpHeaders() {
			{
				String authHeader = tokenType+" " + token;
				set("Authorization", authHeader);
			}
		};
	}

	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		parentActivity.handleException(e); // muestra mensaje de error
	}

}
