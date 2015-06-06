package com.ecoeventos.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoeventos.R;
import com.ecoeventos.bs.service.GetRates;
import com.ecoeventos.bs.service.PuntearEventoTask;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.eis.dto.RateDTO;
import com.ecoeventos.security.SessionManager;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by elioth010 on 6/5/15.
 */
public class EventDetailActivity extends AbstractActivity {

    protected List<RateDTO> rateDTOs;
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
    private Toolbar mToolbar;
    private EventDTO eventDTO;

    protected TextView eventName;
    protected TextView eventDescription;
    protected TextView eventDate;
    protected TextView eventRate;
    protected TextView eventRateValue;
    protected RatingBar ratingBar;
    protected RateDTO myRate = new RateDTO();

    protected AlertDialog.Builder alertDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_screens_menu_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_screens_menu_title));
        }

        eventName = (TextView) findViewById(R.id.card_event_name);
        eventDescription = (TextView) findViewById(R.id.card_event_description);
        eventDate = (TextView) findViewById(R.id.card_event_date);
        eventRate = (TextView) findViewById(R.id.card_event_rate);
        eventRateValue = (TextView) findViewById(R.id.card_event_rate_value);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        try {
            eventDTO = (EventDTO) getIntent().getSerializableExtra("evento");
            eventName.setText(eventDTO.getName());
            eventDescription.setText(eventDTO.getDescription());
            eventDate.setText(dateFormat.format(eventDTO.getDescription()));
            eventRateValue.setText(String.valueOf(eventDTO.getRate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        alertDialog = new AlertDialog.Builder(EventDetailActivity.this);
        alertDialog.setTitle("Agregar Valoracion");
        alertDialog.setMessage("Agrega un comentario.");
        final View inputView = EventDetailActivity.this.getLayoutInflater().inflate(R.layout.fragment_rate, null);
        alertDialog.setView(inputView);
            /*
            handling the buttons :
             */
        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                EditText comment = (EditText) inputView.findViewById(R.id.edit_comment);
                if (comment == null || comment.getText().toString().equalsIgnoreCase("")) {
                    EventDetailActivity.this.show("Debes escribir un comentario");
                } else {
                    myRate.setComment(comment.getText().toString());
                    myRate.setUser(((SessionManager) getSession()).getUser());
                    myRate.setEvent(eventDTO);
                    myRate.setDate(GregorianCalendar.getInstance().getTime());
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                myRate.setRate(Integer.parseInt(String.valueOf(rating)));
                alertDialog.show();
                new PuntearEventoTask(EventDetailActivity.this).execute(myRate);
            }
        });

        new GetRates(this).execute();
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
                Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }
}
