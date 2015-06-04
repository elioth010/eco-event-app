package com.bytesw.consultadecuentas.view.activity;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.client.ResourceAccessException;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.bs.service.CuentasClienteTask;
import com.bytesw.consultadecuentas.view.adapter.NavigationItemsAdapter;
import com.bytesw.consultadecuentas.view.fragment.CuentasClienteFragment;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class CardViewActivity extends AbstractActivity {

	private Toolbar mToolbar;

	private RecyclerView mRecyclerView;
	private NavigationItemsAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private DrawerLayout mDrawer;
	private List<ProductoDTO> dtos;
	private int originalOrientation;
	//private SavedState fragmentSavedState;

	private ActionBarDrawerToggle mDrawerToggle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			initTransitions();
		mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				getSupportActionBar().setElevation(8*metrics.scaledDensity);
			}
				
			Log.e("MENU", "toolbar set OK");
		}
		
		if(savedInstanceState!=null){
			dtos = (List<ProductoDTO>) savedInstanceState.getSerializable("dtos");
			originalOrientation = savedInstanceState.getInt("orientation");
		}
		
		mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
		mRecyclerView.setHasFixedSize(true);

		mAdapter = new NavigationItemsAdapter(getResources().getStringArray(R.array.navigation_menu_items), getResources().getString(R.string.resouce_name), getResources().getString(R.string.resouce_secondary_text));
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mAdapter);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open,	R.string.navigation_drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

		};
		mDrawer.setDrawerListener(mDrawerToggle);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		if(isDeviceOnline()){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(originalOrientation == getResources().getConfiguration().orientation ){
						FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
						Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.container);
						if(fragment2 ==null){
							if(dtos!=null){
								CuentasClienteFragment fragment = new CuentasClienteFragment(dtos);
								//getSupportFragmentManager().executePendingTransactions();
								ft.replace(R.id.container, fragment);
								ft.commit();
							}else{
								CuentasClienteFragment fragment = new CuentasClienteFragment(fillListProducts());
								//getSupportFragmentManager().executePendingTransactions();
								ft.replace(R.id.container, fragment);
								ft.commit();
							}
						}else{
							fragment2.onResume();
							//getSupportFragmentManager().executePendingTransactions();
							ft.replace(R.id.container, fragment2);
							ft.commit();
						}		
					}else{
						FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
						if(dtos!=null){
							CuentasClienteFragment fragment = new CuentasClienteFragment(dtos);
							//getSupportFragmentManager().executePendingTransactions();
							ft.replace(R.id.container, fragment);
							ft.commit();
						}else{
							CuentasClienteFragment fragment = new CuentasClienteFragment(fillListProducts());
							//getSupportFragmentManager().executePendingTransactions();
							ft.replace(R.id.container, fragment);
							ft.commit();
						}
					}
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "No estas conectado a Internet", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTransitions() {
		getWindow().setExitTransition(null);
		getWindow().setReenterTransition(null);
	}
	
	public List<ProductoDTO> fillListProducts(){
		CuentasClienteTask task = new CuentasClienteTask(this, super.getSession().getToken(), super.getSession().getUser(), super.getSession().getTokenType());
		dtos = new ArrayList<ProductoDTO>();
		try {
			dtos = task.execute().get();
		} catch (InterruptedException e) {
			dtos = new ArrayList<ProductoDTO>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			dtos = new ArrayList<ProductoDTO>();
			e.printStackTrace();
		}
		if(dtos.size() == 0){
			show("No tiene cuentas asociadas");
		}
		return dtos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		dtos = (List<ProductoDTO>) savedInstanceState.getSerializable("dtos");
		originalOrientation = savedInstanceState.getInt("orientation");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("dtos", (Serializable)dtos);
		outState.putInt("orientation", originalOrientation);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		originalOrientation = getResources().getConfiguration().orientation;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isDrawerOpen(Gravity.LEFT)) {
			mDrawer.closeDrawers();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void show(final String message) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(CardViewActivity.this, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void handleException(final Exception e) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (e instanceof ResourceAccessException) {
					if (e.getCause() instanceof ConnectException) {
						show(CardViewActivity.this.getResources().getString(R.string.app_toast_error_server_connection_message));
					}
				}
			}
		});
	}

}
