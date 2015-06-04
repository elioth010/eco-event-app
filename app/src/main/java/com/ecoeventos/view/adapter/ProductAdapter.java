package com.bytesw.consultadecuentas.view.adapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.view.activity.AbstractActivity;
import com.bytesw.consultadecuentas.view.res.CircularImageView;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
	
	protected List<ProductoDTO> productoDTOs;
	private AbstractActivity mContext;
	
	static CallBack mCallBack;

	public interface CallBack {
		void onClick(View v, ProductoDTO dto);
	}
	
	public ProductAdapter() {
		super();
	}
	
	public ProductoDTO getItem(int position){
		return productoDTOs.get(position);
	}
	
	public ProductAdapter(List<ProductoDTO> productoDTOs, AbstractActivity activity) {
		this.productoDTOs = productoDTOs;
		this.mContext = activity;
	}

	@Override
	public int getItemCount() {
		if(productoDTOs==null)
			return 0; 
		return this.productoDTOs.size();
	}

	@Override
	public void onBindViewHolder(ProductViewHolder productViewHolder, int position) {
		ProductoDTO dto = productoDTOs.get(position);
		
		productViewHolder.productName.setText(dto.getDescripcionProducto());	
		productViewHolder.accountNumber.setText(dto.getNumeroCuenta());
		productViewHolder.productAlias.setText(dto.getNombreCuenta());
		
		Locale locale = new Locale(mContext.getString(R.string.app_language), mContext.getString(R.string.app_country));
		NumberFormat guatemalaFormat = NumberFormat.getCurrencyInstance(locale);
		
		productViewHolder.moneyAvailable.setText(guatemalaFormat.format(dto.getSaldo1()));
		productViewHolder.moneyAccountant.setText(guatemalaFormat.format(dto.getSaldo2()));
		productViewHolder.moneyReserve.setText(guatemalaFormat.format(dto.getSaldo3()));
		productViewHolder.moneyWithholding.setText(guatemalaFormat.format(dto.getSaldo4()));
		if(dto.getTipoProducto() == 1){
			productViewHolder.accountType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_piggy_bank));
		}else if(dto.getTipoProducto() == 2){
			productViewHolder.accountType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checkbook));
		}else{
			productViewHolder.accountType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_loan));
		}
	}
	
	public CallBack getCallBack() {
		return mCallBack;
	}

	public void setCallBack(CallBack mCallBack) {
		ProductAdapter.mCallBack = mCallBack;
	}

	@Override
	public ProductViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View cardView = LayoutInflater.from(mContext).inflate(R.layout.fragment_cardview, parent, false);
		return new ProductViewHolder(cardView, parent, productoDTOs);
	}
	
	public static class ProductViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

		protected TextView productName;
		protected TextView accountNumber;
		protected TextView productAlias;
		protected TextView moneyAvailable;
		protected TextView moneyAccountant;
		protected TextView moneyReserve;
		protected TextView moneyWithholding;
		protected ImageView bannerAccount;
		protected CircularImageView accountType;
		
		//private ViewGroup parent;
		protected List<ProductoDTO> items;
		
		public ProductViewHolder(View itemView, ViewGroup parent, List<ProductoDTO> list) {
			super(itemView);
			productName = (TextView) itemView.findViewById(R.id.card_product_name);
			accountNumber = (TextView) itemView.findViewById(R.id.card_product_number);
			productAlias = (TextView) itemView.findViewById(R.id.card_product_alias);
			moneyAvailable = (TextView) itemView.findViewById(R.id.card_product_money_available_value);
			moneyAccountant = (TextView) itemView.findViewById(R.id.card_product_money_accountant_value);
			moneyReserve = (TextView) itemView.findViewById(R.id.card_product_money_reserve_value);
			moneyWithholding = (TextView) itemView.findViewById(R.id.card_product_money_withholding_value);
			bannerAccount = (ImageView) itemView.findViewById(R.id.cardview_background);
			accountType = (CircularImageView) itemView.findViewById(R.id.icon_account_type);
			if (Build.VERSION.SDK_INT >= 21) {
				((ViewGroup) bannerAccount.getParent()).setTransitionGroup(false);
			}
			items = list;
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v) {
			if(mCallBack != null){
				mCallBack.onClick(v, items.get(super.getPosition()));
			}
		}
	}

}
