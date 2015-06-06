package com.ecoeventos.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ecoeventos.R;
import com.ecoeventos.bs.service.LoginTask;

import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;

public class AutenticacionActivity extends AbstractActivity {

    EditText passwordTextView;
    EditText userTextView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_autenticacion);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.app_screns_login_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_screns_login_title));
        }

        passwordTextView = (EditText) findViewById(R.id.edit_password);
        userTextView = (EditText) findViewById(R.id.edit_username);

        passwordTextView.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    userAuthentication(v);
                    handled = true;
                }
                return handled;
            }
        });
    }


    /**
     * Llamado desde el boton en el layout
     */
    public void userAuthentication(View view) {
        if (this.passwordTextView.getText().toString().equalsIgnoreCase("") || this.userTextView.getText().toString().equalsIgnoreCase("")) {
            this.show("Algunos datos son requeridos");
        } else {
            getUsername();
            // Intent intent = new Intent(this,
            // com.bytesw.perfil.view.activity.MenuPrincipalActivity.class);
            // this.startActivity(intent);
            // this.finish();
        }
    }

    /**
     * Obtenemos el usuario. Si no se conoce el usuario se levanta un dialogo y
     * que mueestre las cuentas disponibles.
     */
    private void getUsername() {
        if (isDeviceOnline()) {
            getTask(this, userTextView.getText().toString(), passwordTextView.getText().toString(), SCOPE, GRANT_TYPE).execute();
        } else {
            Toast.makeText(this, "No estas Conectado a internet", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * muestra la informacion en tiempo de ejeccucion del error en la Interfaz
     */
    @Override
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AutenticacionActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * verifica el hilo y maneja la exception y la muestra en el gui
     */
    @Override
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof ResourceAccessException) {
                    if (e.getCause() instanceof ConnectException) {
                        show(AutenticacionActivity.this.getResources().getString(R.string.app_toast_error_server_connection_message));
                    } else {
                        show(AutenticacionActivity.this.getResources().getString(R.string.app_toast_error_server_unknow_message));
                    }
                } else {
                    show(AutenticacionActivity.this.getResources().getString(R.string.app_toast_error_login_message));
                }
            }
        });
    }

    private LoginTask getTask(AutenticacionActivity activity, String user, String password, String scope, String grantType) {
        return new LoginTask(activity, user, password, scope, grantType);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void userRegister(View v){
        Intent intent = new Intent(this, RegistrarActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
