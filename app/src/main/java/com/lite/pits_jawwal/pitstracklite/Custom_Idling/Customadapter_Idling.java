package com.lite.pits_jawwal.pitstracklite.Custom_Idling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Reports;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Customadapter_Idling extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<Idling_value> list_all;
    public Customadapter_Idling(Context activity, ArrayList<Idling_value> list){
        this.list_all = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView trip, time,add,ignontime,ignoff,point,stoptotaltime;
        ImageView imgpoint;
    }
    @Override
    public int getCount() {
        return list_all.size();
    }

    @Override
    public Idling_value getItem(int position) {
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
            view = inflater.inflate(R.layout.custom_idling, null);
            holder.trip =  view.findViewById(R.id.trip);
            holder.time =  view.findViewById(R.id.time);
            holder.add =  view.findViewById(R.id.add);
            holder.ignontime =  view.findViewById(R.id.ignontime);
            holder.ignoff =  view.findViewById(R.id.ignoff);
            holder.point =  view.findViewById(R.id.point);
            holder.imgpoint =  view.findViewById(R.id.imgpoint);
            holder.stoptotaltime =  view.findViewById(R.id.stoptotaltime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.trip.setText(list_all.get(position).getTrip());
        holder.time.setText(list_all.get(position).getTime());
        holder.add.setText(list_all.get(position).getAdd());
        holder.ignontime.setText(list_all.get(position).getIgnontime());
        holder.ignoff.setText(list_all.get(position).getIgnoff());
        if(list_all.get(position).getPoint().isEmpty()){
            holder.imgpoint.setVisibility(View.GONE);
            holder.point.setVisibility(View.GONE);
        }else {
            holder.imgpoint.setVisibility(View.VISIBLE);
            holder.point.setVisibility(View.VISIBLE);
            holder.point.setVisibility(View.VISIBLE);
            holder.point.setText(list_all.get(position).getPoint());
        }
        holder.stoptotaltime.setText(list_all.get(position).getStoptotaltime());
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
