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

public class Customadapter_report_workhours extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<Report_work_hour_value> list_all;

    private Context activity;

    public Customadapter_report_workhours(Context activity, ArrayList<Report_work_hour_value> list){
        this.list_all = list;
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView time, igntion,speed,reason,odometer,battary,address,driver,lon,lat;
    }
    @Override
    public int getCount() {
        return list_all.size();
    }

    @Override
    public Report_work_hour_value getItem(int position) {
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
            view = inflater.inflate(R.layout.report_work_hour, null);
            holder.time =  view.findViewById(R.id.txt_time);
            holder.igntion =  view.findViewById(R.id.txt_status);
            holder.speed =  view.findViewById(R.id.txt_speed);
            holder.reason =  view.findViewById(R.id.txt_traking);
            holder.odometer =  view.findViewById(R.id.txt_odometer);
            holder.battary =  view.findViewById(R.id.txt_battery);
            holder.address =  view.findViewById(R.id.txt_address);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.time.setText(list_all.get(position).getTime());
        holder.igntion.setText(list_all.get(position).getIgntion());
        holder.speed.setText(list_all.get(position).getSpeed());
        holder.reason.setText(list_all.get(position ).getReason());
        holder.odometer.setText(list_all.get(position ).getOdometer());
        holder.battary.setText(list_all.get(position ).getBattary());
        holder.address.setText(list_all.get(position ).getAddress());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Reports main=Reports.getIns();
                main.expand2(list_all.get(position).getLat(),list_all.get(position).getLon());
            }
        });

        return view;
    }
    public void sortlist() {
        Collections.reverse(list_all);
        notifyDataSetChanged();
    }

}
