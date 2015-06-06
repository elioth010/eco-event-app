package com.ecoeventos.bs.service;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ecoeventos.R;
import com.ecoeventos.view.activity.AbstractActivity;

public abstract class AbstractGetTask<Params, Progress, Result> extends AsyncTask <Params, Progress, Result> {
	
	protected static final String TAG = "ActivityTask";
	protected final String BASE_URL = "http://173.208.154.230:8080/restful";
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
	
	public static ClientHttpRequestFactory clientHttpRequestFactory(){
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(20000);
        return factory;
	}

	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		parentActivity.handleException(e); // muestra mensaje de error
	}

	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
