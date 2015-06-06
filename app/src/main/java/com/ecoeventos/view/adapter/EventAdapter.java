package com.ecoeventos.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.view.activity.AbstractActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by elioth010 on 6/5/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    protected List<EventDTO> eventDTOs;
    private AbstractActivity mContext;

    static CallBack mCallBack;

    public interface CallBack {
        void onClick(View v, EventDTO dto);
    }

    public EventAdapter() {
        super();
    }

    public EventDTO getItem(int position){
        return eventDTOs.get(position);
    }

    public EventAdapter(List<EventDTO> eventDTOs, AbstractActivity activity) {
        this.eventDTOs = eventDTOs;
        this.mContext = activity;
    }

    @Override
    public int getItemCount() {
        if(eventDTOs==null)
            return 0;
        return this.eventDTOs.size();
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int position) {
        EventDTO dto = eventDTOs.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        eventViewHolder.eventName.setText(dto.getName());
        eventViewHolder.eventDescription.setText(dto.getDescription());
        eventViewHolder.eventDate.setText(dateFormat.format(dto.getDate()));
        eventViewHolder.eventRate.setText(String.format("%.1f",dto.getRate()));
    }

    public CallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(CallBack mCallBack) {
        EventAdapter.mCallBack = mCallBack;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View cardView = LayoutInflater.from(mContext).inflate(R.layout.fragment_cardview, parent, false);
        return new EventViewHolder(cardView, parent, eventDTOs);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView eventName;
        protected TextView eventDescription;
        protected TextView eventDate;
        protected TextView eventRate;
        protected TextView eventRateValue;

        //private ViewGroup parent;
        protected List<EventDTO> items;

        public EventViewHolder(View itemView, ViewGroup parent, List<EventDTO> list) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.card_event_name);
            eventDescription = (TextView) itemView.findViewById(R.id.card_event_description);
            eventDate = (TextView) itemView.findViewById(R.id.card_event_date);
            eventRate = (TextView) itemView.findViewById(R.id.card_event_rate);
            eventRateValue = (TextView) itemView.findViewById(R.id.card_event_rate_value);
            items = list;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mCallBack != null){
                mCallBack.onClick(v, items.get(super.getPosition()));
            }
        }
    }

}