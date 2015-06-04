package com.bytesw.consultadecuentas.view.fragment;

import java.io.Serializable;
import java.util.List;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.activity.CardViewActivity;
import com.bytesw.consultadecuentas.view.activity.EstadoCuentaActivity;
import com.bytesw.consultadecuentas.view.adapter.ProductAdapter;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class CuentasClienteFragment extends Fragment {

	protected List<ProductoDTO> productoDTOs;
	protected LinearLayoutManager mLayoutManager = null;
	private RecyclerView mRecyclerView;
	static private ProgressBar progressBar;

	public CuentasClienteFragment() {
		super();
	}

	public CuentasClienteFragment(List<ProductoDTO> productoDTOs) {
		this.productoDTOs = productoDTOs;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		progressBar = (ProgressBar) ((View)container.getParent()).findViewById(R.id.wait_progress_bar);
		mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recyclerview, container, false);
		mRecyclerView.setHasFixedSize(true);
	
		DisplayMetrics displaymetrics = new DisplayMetrics();
		float cardViewWidth = getResources().getDimension(R.dimen.cardview_minimum_width);
		System.out.println(cardViewWidth);
	
		if (isAdded()) {
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	
			float scaleFactor = displaymetrics.density;
			int width = displaymetrics.widthPixels;
			float widthDp = width / scaleFactor;
	
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				if ((widthDp / (cardViewWidth / scaleFactor)) <= 2.9)
					mLayoutManager = new GridLayoutManager(getActivity(), 2);
				else
					mLayoutManager = new GridLayoutManager(getActivity(), 3);
			} else {
				if ((widthDp / (cardViewWidth / scaleFactor)) < 2)
					mLayoutManager = new GridLayoutManager(getActivity(), 1);
				else
					mLayoutManager = new GridLayoutManager(getActivity(), 2);
			}
			mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
			mRecyclerView.setLayoutManager(mLayoutManager);
			ProductAdapter mAdapter = new ProductAdapter(productoDTOs, (AbstractActivity) getActivity());
			mAdapter.setCallBack(mCallBack);
			mRecyclerView.setAdapter(mAdapter);
		}	
		return mRecyclerView;
	}

	private ProductAdapter.CallBack mCallBack = new ProductAdapter.CallBack() {

		@Override
		public void onClick(final View v, final ProductoDTO dto) {
			ImageView bannerAccount = (ImageView) v.findViewById(R.id.cardview_background);
			final CardViewActivity activity = (CardViewActivity) getActivity();
			if (activity.getSizeOfScreenInInches() < 7) {
				if (Build.VERSION.SDK_INT >= 21) {
					
					Intent intent = new Intent(getActivity(),EstadoCuentaActivity.class);
					intent.putExtra("startOrientation", getActivity().getResources().getConfiguration().orientation);
					intent.putExtra("producto", (Serializable)dto);
					String transitionName = getActivity().getString(R.string.transition_account_view);
					
					int[] location = new int[2];
					
					bannerAccount.getLocationInWindow(location);
	                Point epicenter = new Point(location[0] + bannerAccount.getMeasuredWidth() / 2, location[1] + bannerAccount.getMeasuredHeight() / 2);
	                intent.putExtra(EstadoCuentaActivity.EXTRA_EPICENTER, epicenter);
	                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), bannerAccount, transitionName);
					//ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
					getActivity().startActivity(intent, options.toBundle());
				} else {
					Intent intent = new Intent(getActivity(), EstadoCuentaActivity.class);
					intent.putExtra("startOrientation", getActivity().getResources().getConfiguration().orientation);
					intent.putExtra("producto", (Serializable)dto);
					getActivity().startActivity(intent);
				}
			} else {
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(productoDTOs != null){
								Fragment fragment = new MasterDetailCuentaClienteFragment(productoDTOs, dto);
								FragmentManager fm = getFragmentManager();
								FragmentTransaction ft = fm.beginTransaction();
								ft.replace(R.id.container, fragment);
								fm.executePendingTransactions();
								ft.commit();
							}else{
								
							}	
						}
					});
				} else {
					if (Build.VERSION.SDK_INT >= 21) {
						Intent intent = new Intent(getActivity(),EstadoCuentaActivity.class);
						intent.putExtra("startOrientation", getActivity().getResources().getConfiguration().orientation);
						intent.putExtra("producto", (Serializable)dto);
						String transitionName = getActivity().getString(R.string.transition_account_view);
						
						int[] location = new int[2];
						
						bannerAccount.getLocationInWindow(location);
		                Point epicenter = new Point(location[0] + bannerAccount.getMeasuredWidth() / 2, location[1] + bannerAccount.getMeasuredHeight() / 2);
		                intent.putExtra(EstadoCuentaActivity.EXTRA_EPICENTER, epicenter);
		                
		                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), bannerAccount, transitionName);
						//ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
						getActivity().startActivity(intent, options.toBundle());
					} else {
						Intent intent = new Intent(getActivity(), EstadoCuentaActivity.class);
						intent.putExtra("startOrientation", getActivity().getResources().getConfiguration().orientation);
						intent.putExtra("producto", (Serializable) dto);
						getActivity().startActivity(intent);
					}
				}
			}
		}
	};

	public List<ProductoDTO> getProductoDTOs() {
		return productoDTOs;
	}

	public void setProductoDTOs(List<ProductoDTO> productoDTOs) {
		this.productoDTOs = productoDTOs;
	}
}
