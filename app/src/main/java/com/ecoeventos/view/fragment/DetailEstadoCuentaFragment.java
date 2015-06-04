package com.bytesw.consultadecuentas.view.fragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.bs.service.GetEstadoCuentaTask;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.res.NotifyingScrollView;
import com.bytesw.mobile.demo.eis.dto.EstadoCuentaDTO;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class DetailEstadoCuentaFragment extends Fragment {

	
	
	private RelativeLayout cardViewHeader;
	private Drawable mActionBarBackgroundDrawable;
	private ImageView banner;
	private ProductoDTO producto;
	private List<EstadoCuentaDTO> estadoCuentaDTOs;
	private NotifyingScrollView scrollView;
	private DetailEstadoCuentaTableFragment mTableFragment;
	
	protected TextView productName;
	protected TextView accountNumber;
	protected TextView productAlias;
	protected TextView dateAccountState;
	protected TextView moneyAvailable;
	protected TextView moneyAccountant;
	protected TextView moneyReserve;
	protected TextView moneyWithholding;
	protected String date;
	
	private int countLinks = 0; 
	
	private View rootView;
	
	public DetailEstadoCuentaFragment() {
		
	}
	
	public DetailEstadoCuentaFragment(ProductoDTO producto) {
		this.producto = producto;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_detalle_cuenta, container, false);
		
		if(isAdded() && rootView != null){
			try{
				productName = (TextView) rootView.findViewById(R.id.static_card_product_name);
				accountNumber = (TextView) rootView.findViewById(R.id.static_card_product_number);
				productAlias = (TextView) rootView.findViewById(R.id.static_card_product_alias);
				dateAccountState = (TextView) rootView.findViewById(R.id.static_card_date_account_state);
				moneyAvailable = (TextView) rootView.findViewById(R.id.card_product_money_available_value);
				moneyAccountant = (TextView) rootView.findViewById(R.id.card_product_money_accountant_value);
				moneyReserve = (TextView) rootView.findViewById(R.id.card_product_money_reserve_value);
				moneyWithholding = (TextView) rootView.findViewById(R.id.card_product_money_withholding_value);
				cardViewHeader = (RelativeLayout) rootView.findViewById(R.id.cardview_header_detail);
				banner = (ImageView) rootView.findViewById(R.id.image_header);
				scrollView = ((NotifyingScrollView) rootView.findViewById(R.id.scroll_view));
				
				if(producto!=null){
					productName.setText(producto.getDescripcionProducto());	
					accountNumber.setText(producto.getNumeroCuenta());
					productAlias.setText(producto.getNombreCuenta());
					
					Locale locale = new Locale(this.getString(R.string.app_language), this.getString(R.string.app_country));
					NumberFormat guatemalaFormat = NumberFormat.getCurrencyInstance(locale);
					
					moneyAvailable.setText(guatemalaFormat.format(producto.getSaldo1()));
					moneyAccountant.setText(guatemalaFormat.format(producto.getSaldo2()));
					moneyReserve.setText(guatemalaFormat.format(producto.getSaldo3()));
					moneyWithholding.setText(guatemalaFormat.format(producto.getSaldo4()));
					
				}
				
				Drawable.Callback mDrawableCallback = new Drawable.Callback() {
					@Override
				    public void invalidateDrawable(Drawable who) {
				    	cardViewHeader.setBackground(who);
				    }
		
				    @Override
				    public void scheduleDrawable(Drawable who, Runnable what, long when) {
				    
				    }
		
				    @Override
				    public void unscheduleDrawable(Drawable who, Runnable what) {
				    }
				};
				
				mActionBarBackgroundDrawable = new ColorDrawable(getResources().getColor(R.color.primary));
				mActionBarBackgroundDrawable.setAlpha(0);
				mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
				if(cardViewHeader !=null)
					cardViewHeader.setBackground(mActionBarBackgroundDrawable);
				scrollView.setOnScrollChangedListener(mOnScrollChangedListener);
				
				Handler handler = new Handler();
				handler.postDelayed(new Runnable(){
					
					@Override
					public void run(){
						FragmentManager fm = getChildFragmentManager();
						mTableFragment = new DetailEstadoCuentaTableFragment(fillEstadoCuenta());
						FragmentTransaction ft = fm.beginTransaction();
						ft.replace(R.id.container, mTableFragment);
						if(isAdded()){
							ft.commit();
						}
						
						if(!scrollView.canScrollVertically(1)){
							addMoreItemsToFill();
						}
					}
				}, 1000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return rootView;
	}

	private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
		public void onScrollChanged(ScrollView who, int l, int t, int oldl,int oldt) {
			if(cardViewHeader!=null){
				try{
					final int headerHeight = banner.getHeight() - cardViewHeader.getHeight();
					final float ratio = (float) Math.min(Math.max(t, 0), headerHeight)/ headerHeight;
					final int newAlpha = (int) (ratio * 255);
					mActionBarBackgroundDrawable.setAlpha(newAlpha);
					
					View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
				    int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

				    // if diff is zero, then the bottom has been reached
				    if (diff == 0) {
				    	addMoreItemsScrollView();
				    	return;
				    }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	};
	
	private List<EstadoCuentaDTO> fillEstadoCuenta(){
		try{
			if(isAdded()){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date fecha = GregorianCalendar.getInstance().getTime();
				String formatedDate = dateFormat.format(fecha);
				String reportDate = formatedDate.split("/")[0]+formatedDate.split("/")[1];
				if(date==null){
					//String reportDate = "201302";
					this.date = reportDate;
				}
				dateAccountState.setText(formatedDate.split("/")[0]+"/"+formatedDate.split("/")[1]);
				if(isAdded()){
					GetEstadoCuentaTask task = new GetEstadoCuentaTask(((AbstractActivity)getActivity()), ((AbstractActivity)getActivity()).getSession().getToken(), ((AbstractActivity)getActivity()).getSession().getUser(), ((AbstractActivity)getActivity()).getSession().getTokenType(), this.producto, this.countLinks, date);
					try {
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
						estadoCuentaDTOs.addAll(task.execute().get());
					} catch (InterruptedException e) {
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
						e.printStackTrace();
					} catch (ExecutionException e) {
						estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
						e.printStackTrace();
					}
				}
				countLinks++;
			}else{
				estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return estadoCuentaDTOs;
	}
	
	protected List<EstadoCuentaDTO> fillEstadoCuentaBackMonth(){
		
		String currentDate = date;
		String month = currentDate.substring(4, 6);
		String newDate="";
		String year = currentDate.substring(0,4);
		int currYear = Integer.parseInt(year);
		int newmonth = Integer.parseInt(month)-1;
		if(newmonth <0){
			newmonth = 12;
			currYear=currYear-1;
		}
		
		if(newmonth < 10){
			newDate = currYear+"0"+newmonth;
		}else{
			newDate = currYear+""+newmonth;
		}
		this.date = newDate;
		if(isAdded()){
			GetEstadoCuentaTask task = new GetEstadoCuentaTask(((AbstractActivity)getActivity()), ((AbstractActivity)getActivity()).getSession().getToken(), ((AbstractActivity)getActivity()).getSession().getUser(), ((AbstractActivity)getActivity()).getSession().getTokenType(), this.producto, countLinks, newDate);
			try {
				estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
				estadoCuentaDTOs.addAll(task.execute().get());
			} catch (InterruptedException e) {
				estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
				e.printStackTrace();
			} catch (ExecutionException e) {
				estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
				e.printStackTrace();
			}
		}	
		countLinks++;
		return estadoCuentaDTOs;
		
	}
	
	public void addMoreItemsToFill(){
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(!scrollView.canScrollVertically(1)){
					System.out.println("Vengo a Buscar otra vz para terminar de llenar: "+countLinks);
					addMoreItemsScrollView();
					if(!scrollView.canScrollVertically(1)){
						
						if(countLinks < 6)
							addMoreItemsToFill();
						
						System.out.println("No lleno voy a buscar");
					}
				}
			}
		}, 600);	
	}
	
	public void addMoreItemsScrollView() {
		if(isAdded()){
			if(countLinks < 6){
				final Handler handler = new Handler();
				
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(isAdded())
							mTableFragment.addMoreItemsTable(getActivity().getLayoutInflater(), (ViewGroup) rootView.findViewById(R.id.container), fillEstadoCuentaBackMonth());
						else
							handler.removeCallbacks(this);
					}
				}, 600);
			}else{
				Toast.makeText(getActivity(), "No hay mas datos disponibles", Toast.LENGTH_LONG).show();
			}
		}
	}
}
