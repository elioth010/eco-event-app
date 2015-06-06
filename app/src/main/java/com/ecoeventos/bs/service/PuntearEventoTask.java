package com.ecoeventos.bs.service;

import android.content.Intent;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.eis.dto.RateDTO;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.activity.CardViewActivity;
import com.ecoeventos.view.activity.EventDetailActivity;
import com.ecoeventos.view.activity.MainActivity;

import org.apache.http.conn.ConnectTimeoutException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elioth010 on 6/5/15.
 */
public class PuntearEventoTask extends AbstractGetTask<RateDTO, Void, Boolean> {

    public PuntearEventoTask(AbstractActivity abstractActivity) {
        super(abstractActivity);
    }

    @Override
    protected Boolean doInBackground(RateDTO... params) {
        ResponseEntity<Boolean> response = null;
        try {
            //GEOLOCATION
            final String url = super.BASE_URL + "/rate/create";
            RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
            response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(params[0]), Boolean.class);
            return response.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex instanceof ResourceAccessException) {
                if (ex.getCause() instanceof ConnectException) {
                    onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_connection_message), ex);
                    return null;
                } else if (ex.getCause() instanceof ConnectTimeoutException) {
                    onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_timeout_message), ex);
                    return null;
                }
            }
            if (ex instanceof HttpClientErrorException) {
                if (((HttpClientErrorException) ex).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    System.out.println(((HttpClientErrorException) ex).getResponseBodyAsString());
                    onError(this.parentActivity.getResources().getString(R.string.app_toast_error_login_message), ex);
                    return null;
                }
            } else {
                onError(this.parentActivity.getResources().getString(R.string.app_toast_error_server_unknow_message), ex);
                return null;
            }
        }
        if(response!=null){
            return response.getBody();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean!=null){
            parentActivity.show("Valoracion creada exitosamente");
            Intent intent = new Intent(parentActivity, CardViewActivity.class);
            parentActivity.startActivity(intent);
            parentActivity.finish();
        }else{
            parentActivity.show("No se pudo crear puntuacion");
        }

    }
}
