package com.ecoeventos.bs.service;

import android.util.Log;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.eis.dto.RateDTO;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.activity.CardViewActivity;
import com.ecoeventos.view.adapter.EventAdapter;

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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elioth010 on 6/5/15.
 */
public class GetEvents extends AbstractGetTask<Void, Void, List<EventDTO>> {

    private List<EventDTO> eventDTOs = new ArrayList<>();

    public GetEvents(AbstractActivity abstractActivity) {
        super(abstractActivity);
    }

    @Override
    protected List<EventDTO> doInBackground(Void... params) {
        ResponseEntity<List<EventDTO>> response=null;
        ResponseEntity<List<RateDTO>> response2=null;
        try {
            final String url = super.BASE_URL + "/event/all";
            RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ParameterizedTypeReference<List<EventDTO>> paramTypeReference = new ParameterizedTypeReference<List<EventDTO>>() {};
            response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List<EventDTO>>(null,null), paramTypeReference);
            eventDTOs.addAll(response.getBody());

            List<EventDTO> newsEvents = new ArrayList<>();
            final String url2 = super.BASE_URL + "/rate/all";
            restTemplate = new RestTemplate(super.clientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ParameterizedTypeReference<List<RateDTO>> paramTypeReference2 = new ParameterizedTypeReference<List<RateDTO>>() {};
            response2 = restTemplate.exchange(url2, HttpMethod.GET, new HttpEntity<List<RateDTO>>(null,null), paramTypeReference2);
            for(EventDTO evento : eventDTOs){
                Double rates = 0.0;
                for(RateDTO rate : response2.getBody()){
                    if(rate.getEvent().getId() == evento.getId()){
                        rates+=rate.getRate();
                    }
                }
                evento.setRate(rates/response2.getBody().size());
                newsEvents.add(evento);
            }
            return newsEvents;
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
    protected void onError(String msg, Exception e) {
        if (e != null) {
            Log.e(TAG, "Exception: ", e);
        }
        parentActivity.show(msg);
        parentActivity.handleException(e); // muestra mensaje de error
    }


    @Override
    protected void onPostExecute(List<EventDTO> eventDTOs) {
        super.onPostExecute(eventDTOs);
        EventAdapter mAdapter = new EventAdapter(eventDTOs, parentActivity);
        ((CardViewActivity)parentActivity).getmRecyclerView().setAdapter(mAdapter);
    }
}
