package com.bytesw.consultadecuentas.view.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.adapter.MasterDetailProductAdapter;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class SingleGridCuentasClienteFragment extends CuentasClienteFragment {
	
	private MasterDetailProductAdapter mAdapter;
	public SingleGridCuentasClienteFragment() {

	}
	
	private MasterDetailProductAdapter.CallBack mCallback;

	public SingleGridCuentasClienteFragment(List<ProductoDTO> productoDTOs, MasterDetailProductAdapter.CallBack callback) {
		super(productoDTOs);
		mCallback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RecyclerView mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recyclerview, container, false);
		mRecyclerView.setHasFixedSize(true);
		if(isAdded()){
			super.mLayoutManager = new GridLayoutManager(getActivity(), 1);
			super.mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
			mRecyclerView.setLayoutManager(super.mLayoutManager);
			mAdapter = new MasterDetailProductAdapter(super.productoDTOs, (AbstractActivity) getActivity());
			mAdapter.setCallBack(mCallback);
			mRecyclerView.setAdapter(mAdapter);
		}
		return mRecyclerView;
	}

}
