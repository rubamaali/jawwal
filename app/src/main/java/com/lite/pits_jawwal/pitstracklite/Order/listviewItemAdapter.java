package com.lite.pits_jawwal.pitstracklite.Order;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;

public class listviewItemAdapter extends BaseAdapter {

    public ArrayList<ItemOrderValue> productList;
    Activity activity;
    private String lan;
    private SharedPreferences prefs;

    public listviewItemAdapter(Activity activity, ArrayList<ItemOrderValue> productList, Context context) {
        super();
        this.activity = activity;
        this.productList = productList;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        lan=prefs.getString(Preferences.language1,"0");
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView product;
        TextView quantity;
        TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_item, null);
            holder = new ViewHolder();
            holder.product = (TextView) convertView.findViewById(R.id.product);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemOrderValue item = productList.get(position);

        holder.quantity.setText(item.getQuantity());
        holder.price.setText(item.getPtice());
        String name;
        if(lan.equals("1")){//eng
            name=item.getProductE();
        }else{
            name=item.getProduct();
        }
        holder.product.setText(name);
        return convertView;
    }
}
