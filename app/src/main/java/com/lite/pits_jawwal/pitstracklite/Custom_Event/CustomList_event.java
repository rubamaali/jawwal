package com.lite.pits_jawwal.pitstracklite.Custom_Event;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Event;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ruba-PITS on 9/17/2017.
 */

public class CustomList_event extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Event_item> details_list;
    private ArrayList<Event_item> arraylist;
    private ArrayList<Event_item> list_all;
    private Context context;

    public CustomList_event(Activity activity, List<Event_item> list,Context context) {
        this.details_list = list;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(details_list);
        this.list_all.addAll(details_list);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
    }

    public class ViewHolder {
        TextView name_event, vehicle_name, address_event, time_event, speed_v, km;
        ImageView img_event;
    }

    @Override
    public int getCount() {
        return details_list.size();
    }

    @Override
    public Event_item getItem(int position) {
        return details_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final CustomList_event.ViewHolder holder;
        if (view == null) {
            holder = new CustomList_event.ViewHolder();
            view = inflater.inflate(R.layout.custom_list_event, null);
            holder.name_event = view.findViewById(R.id.name_event);
            holder.vehicle_name = view.findViewById(R.id.vehicle_name);
            holder.address_event = view.findViewById(R.id.address_event);
            holder.time_event = view.findViewById(R.id.time_event);
            holder.speed_v = view.findViewById(R.id.speed_v);
            holder.km = view.findViewById(R.id.km);
            holder.img_event = view.findViewById(R.id.img_event);

            view.setTag(holder);
        } else {
            holder = (CustomList_event.ViewHolder) view.getTag();
        }
        holder.name_event.setText(details_list.get(position).getevent_name());
        holder.vehicle_name.setText(details_list.get(position).getVehicle_name());
        holder.address_event.setText(details_list.get(position).getAddress());
        holder.time_event.setText(details_list.get(position).getTime());
        holder.speed_v.setText(details_list.get(position).getSpeed());
        if (details_list.get(position).getSpeed().equals("")) {
            holder.km.setVisibility(View.INVISIBLE);
        } else {
            holder.km.setVisibility(View.VISIBLE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Event main = Event.getIns();
                main.expand2(details_list.get(position).getlat(), details_list.get(position).getlon(), details_list.get(position).getevent_name(), details_list.get(position).getAddress());
            }
        });
        int image=R.drawable.imgevent2;
        String nameEvent=details_list.get(position).getevent_name().trim();
        if(nameEvent.equals("Ignition On") || nameEvent.equals("تشغيل المركبة")){
            image=R.drawable.ignition;
        }else if(nameEvent.equals("Speed Violation") || nameEvent.equals("تجاوز السرعة")){
            image=R.drawable.overspeed;
        }else if(nameEvent.equals("Accident") || nameEvent.equals("حادث")){
            image=R.drawable.accedent2;
        }else if(nameEvent.equals("Harsh Braking") || nameEvent.equals("بريك مفاجئ")){
            image=R.drawable.harsh_break;
        }else if(nameEvent.equals("Main Power Low") || nameEvent.equals("خلل في البطارية")){
            image=R.drawable.battery_event;
        }else if(nameEvent.equals("Entering perimeter") || nameEvent.equals("دخول")){
            image=R.drawable.check_in;
        }else if(nameEvent.equals("Entering perimeter") || nameEvent.equals("خروج")){
            image=R.drawable.check_out;
        }else if(nameEvent.contains("Entering Perimeter  Route") || nameEvent.equals("دخول  المسار")){
            image=R.drawable.route_out;
        }else if(nameEvent.contains("Leaving Perimeter  Route") || nameEvent.equals("خروج  المسار")){
            image=R.drawable.route_in;
        }else if(nameEvent.contains("logout") || nameEvent.equals("تسجيل الخروج")){
            image=R.drawable.eventlogout;
        }
        if(image==0){
            holder.img_event.setVisibility(View.GONE);
        }else{
            holder.img_event.setVisibility(View.VISIBLE);
            holder.img_event.setImageResource(image);
        }
        return view;
    }

    public boolean filter(String charText) {
        boolean found = false;
        if (charText != null) {
            charText = charText.toLowerCase(Locale.getDefault());
            String[] words = charText.split(",");
            arraylist.clear();
            arraylist.addAll(list_all);
            if (words.length > 0) {
                for (String word : words) {
                    details_list.clear();
                    if (word.length() == 0) {
                        details_list.addAll(list_all);
                    } else {
                        for (Event_item wp : arraylist) {
                            boolean tag = false;
                            if (wp.getevent_name().toLowerCase(Locale.getDefault()).contains(word)) {
                                tag = true;
                            }
                            if (wp.getVehicle_name().toLowerCase(Locale.getDefault()).contains(word)) {
                                tag = true;
                            }
                            if (tag) {
                                details_list.add(wp);
                            }
                        }
                    }
                    arraylist.clear();
                    arraylist.addAll(details_list);
                    found = true;
                }
            } else {
                details_list.clear();
                details_list.addAll(list_all);
                found = details_list.size() > 0;

            }
        }
        if (details_list.size() == 0) {
            found = false;
        }
        notifyDataSetChanged();
        return found;
    }

    public void sortlist() {
        Collections.reverse(details_list);
        notifyDataSetChanged();
    }
}
