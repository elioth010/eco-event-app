package com.ecoeventos.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ecoeventos.R;

/**
 * Created by elioth010 on 6/5/15.
 */
public class CrearEventoActivity extends AbstractActivity {

    private Toolbar mToolbar;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editName;
    private EditText editEmail;
    private EditText editAge;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_screens_menu_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_screens_menu_title));
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
                show(e.getMessage());
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

    public void createEvent(View v){

    }
}
