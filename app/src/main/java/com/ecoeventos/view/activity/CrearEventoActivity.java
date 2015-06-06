package com.ecoeventos.view.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.ecoeventos.R;
import com.ecoeventos.bs.service.CrearEventoTask;
import com.ecoeventos.eis.dto.EventDTO;
import com.ecoeventos.security.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by elioth010 on 6/5/15.
 */
public class CrearEventoActivity extends AbstractActivity {

    private Toolbar mToolbar;
    private EditText editName;
    private EditText editDescription;
    private EditText editDate;
    private EditText editLugar;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    private EventDTO eventDTO = new EventDTO();

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyC1o8dHz0x4LaoRzvnu2bvGD8SK47BPkQ4";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_screens_menu_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_screens_menu_title));
        }

        editName = (EditText) findViewById(R.id.edit_name);
        editDescription = (EditText) findViewById(R.id.edit_description);
        editDate = (EditText) findViewById(R.id.edit_date);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setmCallBack(mCallBack);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        editLugar = (EditText) findViewById(R.id.edit_lugar);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.edit_lugar);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                show(str);
                //editLugar.setText(str);
                eventDTO.setPlace(str);
            }
        });
    }

    DatePickerFragment.CallBack mCallBack = new DatePickerFragment.CallBack() {
        @Override
        public void onSetDate(String date) {
            editDate.setText(date);
        }
    };

    public EditText getEditName() {
        return editName;
    }

    public void setEditName(EditText editName) {
        this.editName = editName;
    }

    public EditText getEditDescription() {
        return editDescription;
    }

    public void setEditDescription(EditText editDescription) {
        this.editDescription = editDescription;
    }

    public EditText getEditDate() {
        return editDate;
    }

    public void setEditDate(EditText editDate) {
        this.editDate = editDate;
    }

    public EditText getEditLugar() {
        return editLugar;
    }

    public void setEditLugar(EditText editLugar) {
        this.editLugar = editLugar;
    }

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public interface CallBack {
            public void onSetDate(String date);
        }

        static CallBack mCallBack;

        protected String date = "";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            getmCallBack().onSetDate(year + "/" + (String.format("%02d",(month+1))) + "/" + (String.format("%02d",day)));
        }

        public static CallBack getmCallBack() {
            return mCallBack;
        }

        public static void setmCallBack(CallBack mCallBack) {
            DatePickerFragment.mCallBack = mCallBack;
        }
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
                if(e!=null){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CrearEventoActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }

    public void createEvent(View v) {
        if (editName.getText().toString().equalsIgnoreCase("")
                || editDescription.getText().toString().equalsIgnoreCase("")
                || editDate.getText().toString().equalsIgnoreCase("")
                || editLugar.getText().toString().equalsIgnoreCase("")) {
            show("Todos los campos son requeridos");
        } else {
            eventDTO.setPlace(editLugar.getText().toString());
            try {
                eventDTO.setDate(df.parse(editDate.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            eventDTO.setDescription(editDescription.getText().toString());
            eventDTO.setName(editName.getText().toString());
            eventDTO.setUserCreated(((SessionManager)getApplication()).getUser());
            new CrearEventoTask(this).execute(eventDTO);
        }
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=true&key=" + API_KEY);
            sb.append("&components=country:gt");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
