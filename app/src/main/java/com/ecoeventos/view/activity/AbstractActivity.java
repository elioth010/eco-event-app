package com.ecoeventos.view.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.ecoeventos.R;
import com.ecoeventos.security.SessionManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractActivity extends ActionBarActivity {


    public static final String SCOPE = "trust";
    public static final String GRANT_TYPE = "client_credentials";
    public DisplayMetrics metrics = new DisplayMetrics();
    private boolean attached = false;

    /**
     * muestra la informacion en tiempo de ejeccucion del error en la Interfaz
     */
    public abstract void show(final String message);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        attached = true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        attached = false;
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        attached = false;
    }

    /**
     * verifica el hilo y maneja la exception y la muestra en el gui
     */
    public abstract void handleException(final Exception e);

    public SessionManager getSession() {
        return (SessionManager) getApplication();
    }

    /**
     * verifica si el dispositivo tiene coneccion a internet
     */
    public boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public int getSizeOfScreenInInches() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        float cardViewWidth = getResources().getDimension(R.dimen.cardview_minimum_width);
        System.out.println(cardViewWidth);
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int widthPixels = displaymetrics.widthPixels;
        int heightPixels = displaymetrics.heightPixels;

        float widthDpi = displaymetrics.xdpi;
        float heightDpi = displaymetrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double sizeScreenInch = Math.hypot(widthInches, heightInches);
        return ((Long) Math.round(sizeScreenInch)).intValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_task) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
