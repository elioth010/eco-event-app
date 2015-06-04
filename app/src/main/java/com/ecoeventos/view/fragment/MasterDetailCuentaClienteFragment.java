package com.bytesw.consultadecuentas.view.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.adapter.MasterDetailProductAdapter;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class MasterDetailCuentaClienteFragment extends Fragment {
	
	protected List<ProductoDTO> productoDTOs;
	protected ProductoDTO actualDTOSelected;
	private View rootView;	
	public MasterDetailCuentaClienteFragment(List<ProductoDTO> productos, ProductoDTO dto) {
		this.productoDTOs = productos;
		this.actualDTOSelected = dto;
	}
	
	public MasterDetailCuentaClienteFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_master_detail_estado_cuenta, container, false);
		SingleGridCuentasClienteFragment fragment = new SingleGridCuentasClienteFragment(productoDTOs, mCallBack);
		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.master_container, fragment);
		if(isAdded())
			ft.commit();
		
		if(this.actualDTOSelected!=null){
			DetailEstadoCuentaFragment detailFragment = new DetailEstadoCuentaFragment(this.actualDTOSelected);
			fm = getChildFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.detail_container, detailFragment);
			if(isAdded())
				ft.commit();
		}
		
		return rootView;
	}
	
	private MasterDetailProductAdapter.CallBack mCallBack = new MasterDetailProductAdapter.CallBack() {
		
		@Override
		public void onClick(View v, ProductoDTO dto) {
			//Toast.makeText(getActivity(), "Click Handled WithCallBack "+dto.getNumeroCuenta(), Toast.LENGTH_SHORT).show();
			DetailEstadoCuentaFragment fragment = new DetailEstadoCuentaFragment(dto);
			FragmentManager fm = getChildFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.detail_container, fragment);
			if(isAdded())
				ft.commit();
		}
	};

}
