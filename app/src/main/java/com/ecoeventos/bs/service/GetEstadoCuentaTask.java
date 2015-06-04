package com.bytesw.consultadecuentas.bs.service;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.util.Log;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.activity.AutenticacionActivity;
import com.bytesw.mobile.demo.eis.dto.EstadoCuentaDTO;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;
import com.bytesw.standard.core.eis.util.Link;

public class GetEstadoCuentaTask extends AbstractGetTask<Void, Void, List<EstadoCuentaDTO>> {
	
	private static final String TAG = "CuentasClienteTask";
	protected AbstractActivity parentActivity;
	
	private String token;
	private String tokenType;
	private String user;
	private ProductoDTO cuenta; 
	private int countLinks;
	private String fecha;
	
	private List<EstadoCuentaDTO> estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
	//private ProgressDialog dialog;
	
	public GetEstadoCuentaTask(AbstractActivity parentActivity, String token, String user, String tokenType, ProductoDTO producto, int countLinks, String fecha) {
		super(parentActivity);
		this.parentActivity = parentActivity;
		this.token=token;
		this.user = user;
		this.tokenType = tokenType;
		this.cuenta = producto;
		this.countLinks = countLinks;
		this.fecha = fecha;
	}
	
	@Override
	protected void onPreExecute() {
		//dialog = ProgressDialog.show(parentActivity, parentActivity.getString(R.string.app_progress_dialog_loading_title), parentActivity.getString(R.string.app_progress_dialog_wait_message), true);
	}
	
	@Override
	protected List<EstadoCuentaDTO> doInBackground(Void... params) {
		countLinks++;
		if(countLinks == 1 || cuenta.getLinks().size() == 0){
			try {
				String url = super.BASE_URL+"/byteapp/client/"+user+"/products/"+cuenta.getNumeroCuenta()+"/"+fecha;
				Log.i(TAG, "No links: "+url);
				RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				ParameterizedTypeReference<List<EstadoCuentaDTO>> paramTypeReference = new ParameterizedTypeReference<List<EstadoCuentaDTO>>() {};
				ResponseEntity<List<EstadoCuentaDTO>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List<EstadoCuentaDTO>>(getBearerHeaders(tokenType, token)), paramTypeReference);
				estadoCuentaDTOs = fetchListClient(response);
				return estadoCuentaDTOs;
			} catch (Exception ex) {
				if (ex instanceof ResourceAccessException) {
					if (ex.getCause() instanceof ConnectException) {
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message),ex);
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
					}else if(ex.getCause() instanceof ConnectTimeoutException){
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
						return null;
					}
				}
				if (ex instanceof HttpClientErrorException) {
					if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
						System.out.println(((HttpClientErrorException) ex).getResponseBodyAsString());
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_session_expired), ex);
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
						Intent intent = new Intent(this.parentActivity, AutenticacionActivity.class);
						this.parentActivity.startActivity(intent);
						this.parentActivity.finish();
						return estadoCuentaDTOs;
					}
				} else {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex);
					estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
				}
			}
		}else {
			try {
				Link url = cuenta.getLinks().get(countLinks);
				Log.i(TAG, "Clase Cuenta Link: "+url.getHref());
				RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				ParameterizedTypeReference<List<EstadoCuentaDTO>> paramTypeReference = new ParameterizedTypeReference<List<EstadoCuentaDTO>>() {};
				ResponseEntity<List<EstadoCuentaDTO>> response = restTemplate.exchange(url.getHref(), HttpMethod.GET, new HttpEntity<List<EstadoCuentaDTO>>(getBearerHeaders(tokenType, token)), paramTypeReference);
				estadoCuentaDTOs = fetchListClient(response);
				return estadoCuentaDTOs;
			} catch (Exception ex) {
				if (ex instanceof ResourceAccessException) {
					if (ex.getCause() instanceof ConnectException) {
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message),ex);
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
					}else if(ex.getCause() instanceof ConnectTimeoutException){
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
						return null;
					}
				}
				if (ex instanceof HttpClientErrorException) {
					if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
						System.out.println(((HttpClientErrorException) ex).getResponseBodyAsString());
						onError(this.parentActivity.getResources().getString(R.string.app_toast_error_login_message), ex);
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
					}
				} else {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex);
					estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
				}
			}
		}
		
		return estadoCuentaDTOs;
	}
	
	public List<EstadoCuentaDTO> fetchListClient(ResponseEntity<List<EstadoCuentaDTO>> response) throws IOException {
		List<EstadoCuentaDTO> estadoCuentaclientes = new ArrayList<EstadoCuentaDTO>();
		if (response.getBody() != null) {
			for (EstadoCuentaDTO estadoClienteDTO : response.getBody()) {
				estadoCuentaclientes.add((EstadoCuentaDTO) estadoClienteDTO);
			}
		}
		return estadoCuentaclientes;
	}
	
	@Override
	protected void onPostExecute(List<EstadoCuentaDTO> result) {
		//dialog.cancel();
	}

	@Override
	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		parentActivity.handleException(e); 
		return;// muestra mensaje de error
	}
	
	

}
