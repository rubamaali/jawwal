package com.lite.pits_jawwal.pitstracklite.CustomStopHours;

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

public class Customadapter_StopHours extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<StopHours_value> list_all;
    private ArrayList<StopHours_value> list_;

    public Customadapter_StopHours(Context activity, ArrayList<StopHours_value> list){
        this.list_all = list;
        this.list_ = new ArrayList<>();
        list_.addAll(list_all);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        TextView total_time,txt_trip,txt_from,address,txt_to;
         }
    @Override
    public int getCount() {
        return list_all.size();
    }
    @Override
    public StopHours_value getItem(int position) {
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
            view = inflater.inflate(R.layout.custom_stophours, null);
            holder.total_time =  view.findViewById(R.id.total_time);
            holder.txt_trip =  view.findViewById(R.id.txt_trip);
            holder.address =  view.findViewById(R.id.address);
            holder.txt_from =  view.findViewById(R.id.txt_from);
            holder.txt_to =  view.findViewById(R.id.txt_to);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txt_from.setText(list_all.get(position).getFrom());
        holder.txt_to.setText(list_all.get(position).getTo());
        holder.txt_trip.setText(list_all.get(position).getTrip());
        holder.address.setText(list_all.get(position).getAddress());
        holder.total_time.setText(list_all.get(position).getTime());
        return view;
    }

}
