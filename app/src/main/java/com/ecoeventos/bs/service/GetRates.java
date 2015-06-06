package com.ecoeventos.bs.service;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.eis.dto.RateDTO;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.activity.CardViewActivity;
import com.ecoeventos.view.adapter.EventAdapter;
import com.ecoeventos.view.beans.ItemBean;
import com.ecoeventos.view.fragment.ListModulesFragment;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elioth010 on 6/5/15.
 */
public class GetRates extends AbstractGetTask<Void, Void, List<RateDTO>> {

    private List<RateDTO> eventDTOs = new ArrayList<>();

    public GetRates(AbstractActivity abstractActivity) {
        super(abstractActivity);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");

    @Override
    protected List<RateDTO> doInBackground(Void... params) {
        ResponseEntity<List<RateDTO>> response=null;
        ResponseEntity<List<RateDTO>> response2=null;
        try {
            final String url = super.BASE_URL + "/rate/all";
            RestTemplate restTemplate = new RestTemplate(super.clientHttpRequestFactory());

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ParameterizedTypeReference<List<RateDTO>> paramTypeReference = new ParameterizedTypeReference<List<RateDTO>>() {};
            response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<List<RateDTO>>(null,null), paramTypeReference);
            eventDTOs.addAll(response.getBody());
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
    protected void onPostExecute(List<RateDTO> eventDTOs) {
        super.onPostExecute(eventDTOs);
        ListModulesFragment fragment = new ListModulesFragment();
        List<ItemBean> itemBeans = new ArrayList<>();
        for(RateDTO event :eventDTOs){
            ItemBean bean = new ItemBean(event.getComment(),event.getUser().getUsername(), dateFormat.format(event.getDate()), String.valueOf(event.getRate()));
            itemBeans.add(bean);
        }
        fragment.setItemBeans(itemBeans);
        FragmentManager fm = parentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.list_rate_container, fragment);
        ft.commit();
    }
}
