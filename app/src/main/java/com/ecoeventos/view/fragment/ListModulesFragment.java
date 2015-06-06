package com.ecoeventos.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ecoeventos.R;
import com.ecoeventos.view.activity.AbstractActivity;
import com.ecoeventos.view.adapter.ListItemsAdapter;
import com.ecoeventos.view.beans.ItemBean;

import java.util.ArrayList;
import java.util.List;

public class ListModulesFragment extends Fragment {
	
	private ListView mListView;
	private List<ItemBean> itemBeans;
	private ListItemsAdapter mItemsAdapter;
	
	public ListModulesFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mListView = (ListView) inflater.inflate(R.layout.fragment_list_view, container, false);
		this.mItemsAdapter = new ListItemsAdapter(getActivity(), this.itemBeans);
		this.mListView.setAdapter(this.mItemsAdapter);
		return this.mListView;
	}

	public ListView getmListView() {
		return mListView;
	}

	public void setmListView(ListView mListView) {
		this.mListView = mListView;
	}

	public List<ItemBean> getItemBeans() {
		return itemBeans;
	}

	public void setItemBeans(List<ItemBean> itemBeans) {
		this.itemBeans = itemBeans;
		this.mItemsAdapter = new ListItemsAdapter(getActivity(), this.itemBeans);
		if(mListView!=null){
			this.mListView.setAdapter(this.mItemsAdapter);
		}
	}

	public ListItemsAdapter getmItemsAdapter() {
		return mItemsAdapter;
	}

	public void setmItemsAdapter(ListItemsAdapter mItemsAdapter) {
		this.mItemsAdapter = mItemsAdapter;
	}
}
