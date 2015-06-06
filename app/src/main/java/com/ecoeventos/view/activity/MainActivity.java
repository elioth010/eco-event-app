package com.ecoeventos.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.ecoeventos.R;


public class MainActivity extends AbstractActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }

    public void verEventos(View v) {
        Intent intent = new Intent(this, CardViewActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void crearEventos(View v) {

    }

    public void salir(View v) {
        this.finish();
    }
}
