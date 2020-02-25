package com.lite.pits_jawwal.pitstracklite.NotiReplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Replay_Report;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class CustomadapterNotiReplay extends BaseAdapter  {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<noti_value> list_all;
    private ArrayList<noti_value> arraylist;
    private ArrayList<noti_value> list_;
    private Context activity;
    private int last_index;
    private String last_done;
    public CustomadapterNotiReplay(Context activity, ArrayList<noti_value> list){
        this.list_all = list;
        this.list_ = new ArrayList<>();
        this.arraylist = new ArrayList<>();
        list_.addAll(list_all);
        arraylist.addAll(list_all);
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView txt_msg,txt_date,txt_replay;
        ImageView img_type;
        RelativeLayout cardview;
    }
    @Override
    public int getCount() {
        return list_all.size();
    }
    @Override
    public noti_value getItem(int position) {
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
            view = inflater.inflate(R.layout.notilistview, null);
            holder.img_type =  view.findViewById(R.id.img_type);
            holder.txt_msg =  view.findViewById(R.id.txt_msg);
            holder.txt_date =  view.findViewById(R.id.txt_date);
            holder.txt_replay =  view.findViewById(R.id.txt_replay);
            holder.cardview =  view.findViewById(R.id.cardview);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txt_date.setText(list_all.get(position).getCreationtime());
        String msg;
        if(list_all.get(position).getReplayid().equals("0")){
            if(list_all.get(position).getReport_type().equals("2")) {
                holder.img_type.setBackgroundResource(R.drawable.task_pitstrack);//1
                msg = activity.getString(R.string.str45);
            }else{
                holder.img_type.setBackgroundResource(R.drawable.report_pitstrack);//2
                msg = activity.getString(R.string.str46);
            }

        }else {
            if (list_all.get(position).getCommandtype().equals("true")) {
                holder.img_type.setBackgroundResource(R.drawable.image_pitstrack);//3
                msg = activity.getString(R.string.str44);
            } else {
                holder.img_type.setBackgroundResource(R.drawable.text_pitstrack);//4
                msg = activity.getString(R.string.str43);
            }

        }
        msg+=" "+"<b>" +list_all.get(position).getCreatedby()+ "</b>";
        holder.txt_msg.setText(Html.fromHtml(msg));
        holder.txt_replay.setText(list_all.get(position).getReply());

        if(list_all.get(position).getSeen().equals("0")){
            holder.cardview.setBackgroundColor(Color.parseColor("#E3E8E6"));
        }else{
            holder.cardview.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, Replay_Report.class);
                Work_Report_Value value = new Work_Report_Value("", "", "", "", "",
                        "", "", list_all.get(position).getReportid(), "",
                        "", "", "", "", "", "", "",
                        "","","",list_all.get(position).getOfflineID(),"","","","","");
                list_all.get(position).setSeen("1");
                notifyDataSetChanged();
                intent.putExtra("report",value);
                activity.startActivity(intent);
                ActivityReplayNoti activityReplayNoti=ActivityReplayNoti.getInstance();
                activityReplayNoti.minEventCount();
            }
        });


        return view;
    }
    public void filter(String charText,boolean Stask,boolean Sreport,boolean Sreplay,boolean Sattachment) {
        arraylist.clear();
        arraylist.addAll(list_);
        list_all.clear();
        if (charText.length()> 0 || (Stask || Sreport || Sreplay || Sattachment)) {
            for (noti_value wp : arraylist) {
                boolean tag = false;
                if (charText.length()>0 && wp.getReply().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list_all.add(wp);
                }
                if(Stask && wp.getType().equals("1"))list_all.add(wp);
                if(Sreport && wp.getType().equals("2"))list_all.add(wp);
                if(Sreplay && wp.getType().equals("3"))list_all.add(wp);
                if(Sattachment && wp.getType().equals("4"))list_all.add(wp);
            }
            arraylist.clear();
            arraylist.addAll(list_all);

        } else {
            list_all.addAll(list_);
        }
        notifyDataSetChanged();
    }

}
