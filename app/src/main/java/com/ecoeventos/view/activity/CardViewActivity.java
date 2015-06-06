package com.ecoeventos.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoeventos.R;
import com.ecoeventos.bs.service.GetEvents;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.view.adapter.EventAdapter;

import java.util.List;

public class CardViewActivity extends AbstractActivity {

    protected List<EventDTO> eventDTOs;
    protected LinearLayoutManager mLayoutManager = null;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_screens_menu_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_screens_menu_title));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        EventAdapter mAdapter = new EventAdapter(eventDTOs, this);
        mAdapter.setCallBack(mCallBack);
        mRecyclerView.setAdapter(mAdapter);

        new GetEvents(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(e.getMessage());
            }
        });
    }

    @Override
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CardViewActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }

    private EventAdapter.CallBack mCallBack = new EventAdapter.CallBack() {

        @Override
        public void onClick(final View v, final EventDTO dto) {
            ImageView bannerAccount = (ImageView) v.findViewById(R.id.cardview_background);
            final CardViewActivity activity = CardViewActivity.this;

            Intent intent = new Intent(activity, EventDetailActivity.class);
            intent.putExtra("evento", dto);
            activity.startActivity(intent);
        }
    };

    public List<EventDTO> getEventDTOs() {
        return eventDTOs;
    }

    public void setEventDTOs(List<EventDTO> EventDTOs) {
        this.eventDTOs = EventDTOs;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }
}
