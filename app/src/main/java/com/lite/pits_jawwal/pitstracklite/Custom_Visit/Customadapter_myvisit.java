package com.lite.pits_jawwal.pitstracklite.Custom_Visit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Customadapter_myvisit extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<Visit_value> list_all;
    private ArrayList<Visit_value> list_;

    public Customadapter_myvisit(Context activity, ArrayList<Visit_value> list){
        this.list_all = list;
        this.list_ = new ArrayList<>();
        list_.addAll(list_all);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        TextView total_time,time_out,time_in,address;
         }
    @Override
    public int getCount() {
        return list_all.size();
    }
    @Override
    public Visit_value getItem(int position) {
        return list_all.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.custom_visit, null);
            holder.total_time =  view.findViewById(R.id.total_time);
            holder.time_out =  view.findViewById(R.id.time_out);
            holder.address =  view.findViewById(R.id.address);
            holder.time_in =  view.findViewById(R.id.time_in);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String t_out=list_all.get(position).getTime_out();
        String total=list_all.get(position).getTotaltime();
        if(t_out.contains("1970")){
            t_out="...";
            total="...";
        }
        holder.time_out.setText(t_out);
        holder.time_in.setText(list_all.get(position).getTime_in());
        holder.address.setText(list_all.get(position).getAddress());
        holder.total_time.setText(total);
        return view;
    }

}
