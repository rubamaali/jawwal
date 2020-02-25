package com.lite.pits_jawwal.pitstracklite.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ruba-PITS on 10/21/2017.
 */

public class CustomListList extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Vehicles_list> NamesList = null;
    private ArrayList<Vehicles_list> arraylist;
    private ArrayList<Vehicles_list> list_all;


    public CustomListList(Activity activity,List<Vehicles_list> list){
        this.NamesList = list;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(NamesList);
        this.list_all.addAll(NamesList);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView name;

    }
    @Override
    public int getCount() {
        return NamesList.size();
    }

    @Override
    public Vehicles_list getItem(int position) {
        return NamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final CustomListList.ViewHolder holder;
        if (view == null) {
            holder = new CustomListList.ViewHolder();
            view = inflater.inflate(R.layout.list_list_cus, null);
            holder.name =  view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (CustomListList.ViewHolder) view.getTag();
        }
        holder.name.setText(NamesList.get(position).getName());
        return view;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist.addAll(list_all);
            NamesList.clear();
            if (charText.length() == 0) {
                NamesList.addAll(list_all);
            } else {
                for (Vehicles_list wp : arraylist) {
                    boolean tag=false;
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        NamesList.add(wp);
                    }


            }
            arraylist.clear();
            arraylist.addAll(NamesList);
        }
        notifyDataSetChanged();
    }

}
