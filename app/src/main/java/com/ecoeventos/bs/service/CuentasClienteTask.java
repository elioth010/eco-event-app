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
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class CuentasClienteTask extends AbstractGetTask<Void, Void, List<ProductoDTO>> {
	private static final String TAG = "CuentasClienteTask";
	protected AbstractActivity parentActivity;
	
	private String token;
	private String tokenType;
	private String user;
	private List<ProductoDTO> productosDtos = new ArrayList<ProductoDTO>();

	public CuentasClienteTask(AbstractActivity parentActivity, String token, String user, String tokenType) {
		super(parentActivity);
		this.parentActivity = parentActivity;
		this.token=token;
		this.user = user;
		this.tokenType = tokenType;
	}
	
	@Override
	protected void onPreExecute() {
		
	}

	@Override
	protected List<ProductoDTO> doInBackground(Void... params) {
		try {
			final String url = super.BASE_URL+"/byteapp/client/"+user+"/products";
			System.out.println(url);
			RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			ParameterizedTypeReference<List<ProductoDTO>> paramTypeReference = new ParameterizedTypeReference<List<ProductoDTO>>() {};
			ResponseEntity<List<ProductoDTO>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List<ProductoDTO>>(getBearerHeaders(tokenType, token)), paramTypeReference);
			productosDtos = fetchListClient(response);
			return productosDtos;
		} catch (Exception ex) {
			if (ex instanceof ResourceAccessException) {
				if (ex.getCause() instanceof ConnectException) {
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message),ex);
					productosDtos = new ArrayList<ProductoDTO>();
					return productosDtos;
				}else if(ex.getCause() instanceof ConnectTimeoutException){
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
					return null;
				}
			}
			if (ex instanceof HttpClientErrorException) {
				if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
					System.out.println(((HttpClientErrorException) ex).getResponseBodyAsString());
					onError(this.parentActivity.getResources().getString(R.string.app_toast_error_session_expired), ex);
					productosDtos = new ArrayList<ProductoDTO>();
					Intent intent = new Intent(this.parentActivity, AutenticacionActivity.class);
					this.parentActivity.startActivity(intent);
					this.parentActivity.finish();
					return productosDtos;
				}
			} else {
				onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex);
				productosDtos = new ArrayList<ProductoDTO>();
				return productosDtos;
			}
		}

		return productosDtos;
	}
	
	public List<ProductoDTO> fetchListClient(ResponseEntity<List<ProductoDTO>> response) throws IOException {
		List<ProductoDTO> clientes = new ArrayList<ProductoDTO>();
		if (response.getBody() != null) {
			for (ProductoDTO clienteDTO : response.getBody()) {
				clientes.add((ProductoDTO) clienteDTO);
				System.out.println(clienteDTO);
			}
		}
		return clientes;
	}

	@Override
	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		parentActivity.show(msg);
		parentActivity.handleException(e); // muestra mensaje de error
	}
	
	@Override
	protected void onPostExecute(List<ProductoDTO> result) {

	}
	
}