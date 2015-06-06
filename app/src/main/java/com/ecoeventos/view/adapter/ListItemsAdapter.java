package com.ecoeventos.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoeventos.R;
import com.ecoeventos.eis.dto.RateDTO;
import com.ecoeventos.view.beans.ItemBean;

import java.util.ArrayList;
import java.util.List;

public class ListItemsAdapter extends BaseAdapter {

    private Activity mContext;
    private List<ItemBean> rowItem;
    private RateDTO rateDTO;

    public ListItemsAdapter(Activity context, List<ItemBean> rowItem) {
        super();
        this.mContext = context;
        this.rowItem = rowItem;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_list_items, null);
        }
        try {
            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_items_title);
            TextView username = (TextView) convertView.findViewById(R.id.list_items_user);
            TextView detailTextView = (TextView) convertView.findViewById(R.id.list_items_detail);
            TextView rate = (TextView) convertView.findViewById(R.id.list_items_rate);

            final ItemBean row = rowItem.get(position);
            titleTextView.setText(row.getTitle());
            detailTextView.setText(row.getDetail());
            username.setText(row.getUser());
            rate.setText(row.getRate());

        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }

        return convertView;
    }

}
