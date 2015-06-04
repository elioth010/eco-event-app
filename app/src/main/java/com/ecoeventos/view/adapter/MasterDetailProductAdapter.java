package com.bytesw.consultadecuentas.view.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class MasterDetailProductAdapter extends ProductAdapter {
	
	public MasterDetailProductAdapter() {

	}
	
	public MasterDetailProductAdapter(List<ProductoDTO> productoDTOs, AbstractActivity activity) {
		super(productoDTOs, activity);
	}
	
	@Override
	public ProductViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cardview, parent, false);
		return new MasterProductViewHolder(cardView, parent, this.productoDTOs);
	}

	public static class MasterProductViewHolder extends ProductViewHolder implements OnClickListener{
		
		public MasterProductViewHolder(View itemView, ViewGroup parent, List<ProductoDTO> items) {
			super(itemView, parent, items);
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v) {
			if(mCallBack != null){
				mCallBack.onClick(v, super.items.get(super.getPosition()));
			}
		}
	}

}
