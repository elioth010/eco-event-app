package com.bytesw.consultadecuentas.view.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.bs.service.LogOutTask;
import com.bytesw.consultadecuentas.bs.service.sugar.SugarService;
import com.bytesw.consultadecuentas.bs.service.sugar.impl.SugarServiceImpl;
import com.bytesw.consultadecuentas.eis.bo.BaseBO;
import com.bytesw.consultadecuentas.security.SessionManager;

import java.util.List;

public abstract class AbstractActivity extends ActionBarActivity {


    public static final String SCOPE = "trust";
    public static final String  GRANT_TYPE = "client_credentials";
    private SugarService service;
    public DisplayMetrics metrics = new DisplayMetrics();
    private boolean attached = false;

    /**
     * muestra la informacion en tiempo de ejeccucion del error en la Interfaz
     */
    public abstract void show(final String message);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new SugarServiceImpl(getSession().getSugarDAO());
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

    public void closeSession() {
        SessionManager sessionManager = (SessionManager) getApplicationContext();
        logOutTask(this, sessionManager.getToken(), sessionManager.getTokenType()).execute();
    }

    private LogOutTask logOutTask(AbstractActivity parentActivity, String token, String tokenType) {
        return new LogOutTask(parentActivity, token, tokenType);
    }

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
            this.closeSession();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private SugarService getService() {
        return service;
    }

    public <T> BaseBO save(BaseBO baseBO) {
        return getService().save(baseBO);
    }

    public void update(BaseBO baseBO) {
        getService().update(baseBO);
    }

    public void delete(BaseBO baseBO) {
        getService().delete(baseBO);
    }

    public <T> BaseBO findById(Class<? extends BaseBO> clazz, Integer id) {
        return getService().findById(clazz, id);
    }

    public <T> List<T> findAll(Class<? extends BaseBO> clazz) {
        return getService().findAll(clazz);
    }

    public <T> List<T> findBySQLQuery(Class<? extends BaseBO> clazz, String query) {
        return getService().findBySQLQuery(clazz, query);
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

}
