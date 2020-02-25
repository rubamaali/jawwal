package com.lite.pits_jawwal.pitstracklite.Type;

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

public class CustomLocationType extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Location_type> NamesList = null;
    private ArrayList<Location_type> arraylist;
    private ArrayList<Location_type> list_all;
    public CustomLocationType(Context activity, List<Location_type> list){
        this.NamesList = list;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(NamesList);
        this.list_all.addAll(NamesList);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public String getid(int position) {
        Location_type location_type=getItem(position);
        return location_type.getKey();
    }

    public String getname(int position) {
        Location_type location_type=getItem(position);
        return location_type.getName();
    }


    public class ViewHolder {
        TextView name;
    }
    @Override
    public int getCount() {
        return NamesList.size();
    }
    @Override
    public Location_type getItem(int position) {
        return NamesList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final CustomLocationType.ViewHolder holder;
        if (view == null) {
            holder = new CustomLocationType.ViewHolder();
            view = inflater.inflate(R.layout.list_list_cus, null);
            holder.name =  view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (CustomLocationType.ViewHolder) view.getTag();
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
                for (Location_type wp : arraylist) {
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
