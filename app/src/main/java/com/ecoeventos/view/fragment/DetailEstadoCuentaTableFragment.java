package com.bytesw.consultadecuentas.view.fragment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bytesw.consultadecuentas.R;
import com.bytesw.mobile.demo.eis.dto.EstadoCuentaDTO;

public class DetailEstadoCuentaTableFragment extends Fragment {

	private List<EstadoCuentaDTO> estadoDTOs;
	private TableLayout mTableLayout;

	public DetailEstadoCuentaTableFragment() {

	}

	public DetailEstadoCuentaTableFragment(List<EstadoCuentaDTO> cuentaDTOs) {
		this.estadoDTOs = cuentaDTOs;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(this.mTableLayout == null ){
			this.mTableLayout = (TableLayout) inflater.inflate(R.layout.fragment_estado_cuenta_items,	container, false);
			this.fillData(inflater, container);
			return this.mTableLayout;
		}else{
			ViewGroup parent = (ViewGroup)mTableLayout.getParent();
			if(parent!=null){
				parent.removeView(mTableLayout);
			}
			return mTableLayout;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putSerializable("tableLayout", this.mTableLayout);
	}
	
	public void fillData(LayoutInflater inflater,  ViewGroup container){
		if(estadoDTOs!=null && estadoDTOs.size()>0 && isAdded()){
			
			int count = 0;
			for (EstadoCuentaDTO estadoCuentaDTO : estadoDTOs) {
				TableRow tableRow =(TableRow) inflater.inflate(R.layout.fragment_estado_cuenta_row, container, false);
				
				if(count%2 == 0){
					tableRow.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_light));
				}else{
					tableRow.setBackgroundColor(getActivity().getResources().getColor(R.color.icons));
				}
				
				EstadoCuentaDTO row = estadoCuentaDTO;
				System.out.println(row);
				
				TextView ef = (TextView) tableRow.findViewById(R.id.text_view_ef);
				ef.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				ef.setTextSize(16);
				ef.setText(parseDate(row.getDiaOperacion()+"/"+row.getMesOperacion()+"/"+row.getAnioOperacion()));
				
				TextView transaccion = (TextView) tableRow.findViewById(R.id.text_view_trans);
				transaccion.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				transaccion.setTextSize(16);
				transaccion.setText(row.getAbreviaturaTransaccion().toString());
				
				TextView referencia = (TextView) tableRow.findViewById(R.id.text_view_ref);
				referencia.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				referencia.setTextSize(16);
				referencia.setText(row.getReferencia().toString());
				
				TextView movimiento = (TextView) tableRow.findViewById(R.id.text_view_value);
				if(row.getDebito()){
					movimiento.setTextColor(container.getContext().getResources().getColor(R.color.light_red));
				}
				Locale locale = new Locale(container.getContext().getResources().getString(R.string.app_language), container.getContext().getResources().getString(R.string.app_country));
				NumberFormat guatemalaFormat = NumberFormat.getCurrencyInstance(locale);
				movimiento.setTextSize(16);
				movimiento.setText(guatemalaFormat.format(row.getValorTransaccion()));
				
				mTableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				count++;
			}
		}else{
			Toast.makeText(getActivity(), "No hay movimientos este mes", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void addMoreItemsTable(LayoutInflater inflater, ViewGroup container, List<EstadoCuentaDTO> cuentaDTOs) {
		if(mTableLayout==null){
			this.mTableLayout = (TableLayout) inflater.inflate(R.layout.fragment_estado_cuenta_items,	container, false);
		}
		if(cuentaDTOs!=null && cuentaDTOs.size()>0 && isAdded()){
			int count =0;
			for (EstadoCuentaDTO estadoCuentaDTO : cuentaDTOs) {
				TableRow tableRow =(TableRow) inflater.inflate(R.layout.fragment_estado_cuenta_row, container, false);
				if(count%2 == 0){
					tableRow.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_light));
				}else{
					tableRow.setBackgroundColor(getActivity().getResources().getColor(R.color.icons));
				}
				EstadoCuentaDTO row = estadoCuentaDTO;
				System.out.println(row);
				
				TextView ef = (TextView) tableRow.findViewById(R.id.text_view_ef);
				ef.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				ef.setTextSize(16);
				ef.setText(parseDate(row.getDiaOperacion()+"/"+row.getMesOperacion()+"/"+row.getAnioOperacion()));
				
				TextView transaccion = (TextView) tableRow.findViewById(R.id.text_view_trans);
				transaccion.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				transaccion.setTextSize(16);
				transaccion.setText(row.getAbreviaturaTransaccion().toString());
				
				TextView referencia = (TextView) tableRow.findViewById(R.id.text_view_ref);
				referencia.setTextColor(container.getContext().getResources().getColor(R.color.secondary_text));
				referencia.setTextSize(16);
				referencia.setText(row.getReferencia().toString());
				
				TextView movimiento = (TextView) tableRow.findViewById(R.id.text_view_value);
				if(row.getDebito()){
					movimiento.setTextColor(container.getContext().getResources().getColor(R.color.light_red));
				}
				Locale locale = new Locale(container.getContext().getResources().getString(R.string.app_language), container.getContext().getResources().getString(R.string.app_country));
				NumberFormat guatemalaFormat = NumberFormat.getCurrencyInstance(locale);
				movimiento.setTextSize(16);
				movimiento.setText(guatemalaFormat.format(row.getValorTransaccion()));
				
				mTableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				//estadoDTOs.addAll(cuentaDTOs);
				count++;
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((DetailEstadoCuentaFragment)getParentFragment()).addMoreItemsToFill();
	}
	
	public String parseDate(String date){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date newDate = df.parse(date);
			return df.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}	
	}

	public List<EstadoCuentaDTO> getEstadoDTOs() {
		return estadoDTOs;
	}

	public void setEstadoDTOs(List<EstadoCuentaDTO> estadoDTOs) {
		this.estadoDTOs = estadoDTOs;
	}
}
