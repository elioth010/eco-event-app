package com.ecoeventos.bs.service;

import android.content.Intent;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.UserDTO;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.activity.AutenticacionActivity;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

/**
 * Created by elioth010 on 6/5/15.
 */
public class RegistrarTask extends AbstractGetTask<UserDTO, Void, Boolean> {

    public RegistrarTask(AbstractActivity abstractActivity) {
        super(abstractActivity);
    }

    @Override
    protected Boolean doInBackground(UserDTO... params) {
        ResponseEntity<Boolean> response = null;
        try {
            final String url = super.BASE_URL + "/login/register";
            RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());
            // Create the request body as a MultiValueMap
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

        return response.getBody();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean){
            parentActivity.show("Registro realizado exitosamente");
            Intent intent = new Intent(parentActivity, AutenticacionActivity.class);
            parentActivity.startActivity(intent);
            parentActivity.finish();
        }

    }
}
