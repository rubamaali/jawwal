package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Reports;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Customadapter_report_trip extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<Report_Trip_value> list_all;
    public Customadapter_report_trip(Context activity, ArrayList<Report_Trip_value> list){
        this.list_all = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView txt_start_time,txt_total_time,txt_end_time,txt_start_odometer,txt_end_odometer,txt_odometer,txt_speed,txt_from_address,txt_to_address,txt_trip;
    }
    @Override
    public int getCount() {
        return list_all.size();
    }

    @Override
    public Report_Trip_value getItem(int position) {
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
            view = inflater.inflate(R.layout.report_trip, null);
            holder.txt_start_time =  view.findViewById(R.id.txt_start_time);
            holder.txt_total_time =  view.findViewById(R.id.txt_total_time);
            holder.txt_end_time =  view.findViewById(R.id.txt_end_time);
            holder.txt_start_odometer =  view.findViewById(R.id.txt_start_odometer);
            holder.txt_end_odometer =  view.findViewById(R.id.txt_end_odometer);
            holder.txt_odometer =  view.findViewById(R.id.txt_odometer);
            holder.txt_speed =  view.findViewById(R.id.txt_speed);
            holder.txt_from_address =  view.findViewById(R.id.txt_from_address);
            holder.txt_to_address =  view.findViewById(R.id.txt_to_address);
            holder.txt_trip =  view.findViewById(R.id.txt_trip);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txt_start_time.setText(list_all.get(position).getFrom());
        holder.txt_total_time.setText(list_all.get(position).getTime());
        holder.txt_end_time.setText(list_all.get(position).getTo());
        holder.txt_start_odometer.setText(list_all.get(position ).getOdo_start());
        holder.txt_end_odometer.setText(list_all.get(position).getOdo_end());
        holder.txt_odometer.setText(list_all.get(position).getOdometer());
        holder.txt_speed.setText(list_all.get(position).getMaxspeed());
        holder.txt_from_address.setText(list_all.get(position).getAfffrom());
        holder.txt_to_address.setText(list_all.get(position).getAddto());
        holder.txt_trip.setText(list_all.get(position).getTrip());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Reports main=Reports.getIns();
                main.show_trip(list_all.get(position).getTrip());
            }
        });

        return view;
    }
    public void sortlist() {
        Collections.reverse(list_all);
        notifyDataSetChanged();

    }

}
