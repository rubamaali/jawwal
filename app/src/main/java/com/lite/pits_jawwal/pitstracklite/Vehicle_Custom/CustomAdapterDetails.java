package com.lite.pits_jawwal.pitstracklite.Vehicle_Custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruba-PITS on 8/6/2017.
 */

public class CustomAdapterDetails extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Vehicle_Details> details_list = null;
    private ArrayList<Vehicle_Details> arraylist;

    public CustomAdapterDetails(Activity activity, List<Vehicle_Details> list){
        this.details_list = list;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(details_list);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView name,value;
        ImageView img;
    }
    @Override
    public int getCount() {
        return details_list.size();
    }

    @Override
    public Vehicle_Details getItem(int position) {
        return details_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new CustomAdapterDetails.ViewHolder();
            view = inflater.inflate(R.layout.custom_ve_details, null);
            holder.name =  view.findViewById(R.id.name);
            holder.value =  view.findViewById(R.id.value);
            holder.img =  view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (CustomAdapterDetails.ViewHolder) view.getTag();
        }
        holder.name.setText(details_list.get(position).getdetails_name());
        holder.value.setText(details_list.get(position).getdetails_id());
        switch (details_list.get(position).getdetails_value()){
            case "1": holder.img.setBackgroundResource(R.drawable.vech_batt2);break;
            case "2": holder.img.setBackgroundResource(R.drawable.int_battery);break;
            case "3": holder.img.setBackgroundResource(R.drawable.key2);break;
            case "4": holder.img.setBackgroundResource(R.drawable.door);break;
            case "5": holder.img.setBackgroundResource(R.drawable.emergency2);break;
            case "6": holder.img.setBackgroundResource(R.drawable.entrance);break;
            case "7": holder.img.setBackgroundResource(R.drawable.entrance);break;
            case "8": holder.img.setBackgroundResource(R.drawable.fuel2);break;
            case "9": holder.img.setBackgroundResource(R.drawable.temp);break;
            case "10": holder.img.setBackgroundResource(R.drawable.siren);break;
            case "11": holder.img.setBackgroundResource(R.drawable.close);break;
            case "12": holder.img.setBackgroundResource(R.drawable.open2);break;
            case "13": holder.img.setBackgroundResource(R.drawable.stop2);break;
            case "14": holder.img.setBackgroundResource(R.drawable.direction2);break;
            case "15": holder.img.setBackgroundResource(R.drawable.hight2);break;
        }

        return view;
    }

}
