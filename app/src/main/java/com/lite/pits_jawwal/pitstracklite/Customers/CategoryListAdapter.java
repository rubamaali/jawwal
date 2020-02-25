package com.lite.pits_jawwal.pitstracklite.Customers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryListAdapter extends BaseAdapter {
    Context context;
    private  ArrayList<Location_type> type_loc;
    private static LayoutInflater inflater = null;
    private ArrayList<Location_type> arraylist;
    private ArrayList<Location_type> list_all;
    public CategoryListAdapter(Context context, ArrayList<Location_type> type_loc) {
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type_loc=type_loc;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(type_loc);
        this.list_all.addAll(type_loc);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return type_loc.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return type_loc.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_layout, null);
        final TextView text = vi.findViewById(R.id.textV);
        text.setText(type_loc.get(position).getName());
        ImageView img =  vi.findViewById(R.id.imgV);
        img.setImageResource(type_loc.get(position).getImage());
        return vi;
    }
    public String get_id(int position){
        return type_loc.get(position).getKey();
    }
    public String get_name(int position){
        return type_loc.get(position).getName();
    }
    public int getImage(int position) {
        return type_loc.get(position).getImage();
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist.addAll(list_all);
        type_loc.clear();
        if (charText.length() == 0) {
            type_loc.addAll(list_all);
        } else {
            for (Location_type wp : arraylist) {
                boolean tag=false;
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    type_loc.add(wp);
                }
            }
            arraylist.clear();
            arraylist.addAll(type_loc);
        }
        notifyDataSetChanged();
    }

}