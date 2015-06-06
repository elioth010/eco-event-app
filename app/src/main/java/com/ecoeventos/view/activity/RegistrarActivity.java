package com.ecoeventos.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ecoeventos.R;
import com.ecoeventos.bs.service.RegistrarTask;
import com.ecoeventos.eis.dto.UserDTO;

/**
 * Created by elioth010 on 6/5/15.
 */
public class RegistrarActivity extends AbstractActivity {

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
        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editName = (EditText) findViewById(R.id.edit_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editAge = (EditText) findViewById(R.id.edit_edad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void userRegister(View v){
        if(editUsername.getText().toString().equalsIgnoreCase("")
                || editPassword.getText().toString().equalsIgnoreCase("")
                || editName.getText().toString().equalsIgnoreCase("")
                || editEmail.getText().toString().equalsIgnoreCase("")
                || editAge.getText().toString().equalsIgnoreCase("")){
            show("Todos los campos son requeridos");
        }else{
            UserDTO userDTO = new UserDTO(null,editUsername.getText().toString(),getMD5(editPassword.getText().toString()),editName.getText().toString(), editEmail.getText().toString(), Integer.parseInt(editAge.getText().toString()));
            new RegistrarTask(this).execute(userDTO);
        }
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
                Toast.makeText(RegistrarActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }

}
