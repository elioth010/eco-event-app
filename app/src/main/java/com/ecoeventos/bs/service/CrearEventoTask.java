package com.ecoeventos.bs.service;

import android.content.Intent;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.view.activity.AbstractActivity;
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
public class CrearEventoTask extends AbstractGetTask<EventDTO, Void, Boolean> {

    public CrearEventoTask(AbstractActivity abstractActivity) {
        super(abstractActivity);
    }

    @Override
    protected Boolean doInBackground(EventDTO... params) {
        ResponseEntity<Boolean> response = null;
        try {
            //GEOLOCATION
            final String geometryURL = "https://maps.googleapis.com/maps/api/geocode/json?address="+params[0].getPlace()+"&components=country:GT&sensor=true&key=AIzaSyCiEDEt0ss_81hvneO5PIh5dF0b8bYLdzo";
            RestTemplate geoRestTemplate = new RestTemplate(super.clientHttpRequestFactory());
            ResponseEntity<String> georesponse = geoRestTemplate.exchange(geometryURL, HttpMethod.GET, new HttpEntity<String>(null,null), String.class);
            Map<String, String> tokenInfo = fetchToken(georesponse);
            params[0].setPlace(tokenInfo.get("lat")+tokenInfo.get("lng"));
            System.out.println(params[0].getUserCreated());
            final String url = super.BASE_URL + "/event/create";
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

    private Map<String, String> fetchToken(ResponseEntity<String> response) throws IOException {
        HashMap<?, ?> myMap;
        HashMap<?, ?> result;
        HashMap<?, ?> geometry;

        ObjectMapper objectMapper = new ObjectMapper();
        String lat = null;
        String lng = null;
        JsonFactory factory = objectMapper.getJsonFactory();
        JsonParser jp = factory.createJsonParser(response.getBody().toString());
        JsonNode input = objectMapper.readTree(jp);
        JsonNode resultsArray = input.get("results");
        for (JsonNode node: resultsArray ) {
            JsonNode geometryNode = node.get("geometry");
            JsonNode location = geometryNode.get("location");
            lat = location.get("lat").toString();
            lng = location.get("lng").toString();
        }
        System.out.println("Map: " + lat);
        System.out.println("Map: " + lng);
        Map<String, String> position = new HashMap<String, String>();
        position.put("lat", lat);
        position.put("lng", lng);
        return position;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean!=null){
            parentActivity.show("Evento creado realizado exitosamente");
            Intent intent = new Intent(parentActivity, MainActivity.class);
            parentActivity.startActivity(intent);
            parentActivity.finish();
        }else{
            parentActivity.show("No se pudo crear evento");
        }

    }
}
