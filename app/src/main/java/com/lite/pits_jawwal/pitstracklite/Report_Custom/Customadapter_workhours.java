package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.PersonalHistoryReport.HisroryReport;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Replay_Report;
import com.lite.pits_jawwal.pitstracklite.Reports;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Customadapter_workhours extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private ArrayList<Work_Report_Value> list_all;
    private boolean ishistory;

    private Context activity;

    public Customadapter_workhours(Context activity, ArrayList<Work_Report_Value> list, boolean ishistory) {
        this.list_all = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ishistory=ishistory;
    }
    public class ViewHolder {
        TextView address, txt_time, name, txt_report, num_replay, customer, report_type, replies,skip,write_dis,done_diss,done_add,done_time;
        RelativeLayout replay,historyReport,donrelative;
        CheckBox checkbox;
        CardView cardView;
        ImageView map,open_info;
        LinearLayout more_data,lin1,lin4,lin5;
    }
    @Override
    public int getCount() {
        return list_all.size();
    }
    @Override
    public Work_Report_Value getItem(int position) {
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
            view = inflater.inflate(R.layout.customreport, null);
            holder.name = view.findViewById(R.id.name);
            holder.skip = view.findViewById(R.id.skip);
            holder.address = view.findViewById(R.id.address);
            holder.num_replay = view.findViewById(R.id.num_replay);
            holder.checkbox = view.findViewById(R.id.checkbox);
            holder.txt_time = view.findViewById(R.id.txt_time);
            holder.txt_report = view.findViewById(R.id.txt_report);
            holder.customer = view.findViewById(R.id.customer);
            holder.report_type = view.findViewById(R.id.report_type);
            holder.replies = view.findViewById(R.id.replies);
            holder.replay = view.findViewById(R.id.replay);
            holder.cardView =  view.findViewById(R.id.cardView);
            holder.map =  view.findViewById(R.id.map);
            holder.historyReport =  view.findViewById(R.id.historyReport);
            holder.donrelative =  view.findViewById(R.id.donrelative);
            holder.write_dis =  view.findViewById(R.id.write_dis);
            holder.done_diss =  view.findViewById(R.id.done_diss);
            holder.done_add =  view.findViewById(R.id.done_add);
            holder.done_time =  view.findViewById(R.id.done_time);
            holder.more_data =  view.findViewById(R.id.more_data);
            holder.open_info =  view.findViewById(R.id.open_info);
            holder.lin1 =  view.findViewById(R.id.lin1);
            holder.lin4 =  view.findViewById(R.id.lin4);
            holder.lin5 =  view.findViewById(R.id.lin5);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String name = list_all.get(position).getPer_name();
        holder.name.setText(name);
        if(list_all.get(position).getAddress().length()>1) {
            holder.address.setText(list_all.get(position).getAddress());
            holder.address.setVisibility(View.VISIBLE);
        }else{
            holder.address.setVisibility(View.GONE);
        }
        holder.txt_time.setText(list_all.get(position).getCreatedTime());
        String txt = list_all.get(position).getReport();
        String type_name = list_all.get(position).getName();
        holder.txt_report.setText(txt);
        holder.report_type.setText(type_name);
        if(type_name.equals("without")){
            holder.report_type.setVisibility(View.GONE);
        }else{
            holder.report_type.setVisibility(View.VISIBLE);
        }
        String customer_name = list_all.get(position).getCustomer();
        if (customer_name != null && customer_name.length() > 0 && !customer_name.equals("null")) {
            holder.customer.setVisibility(View.VISIBLE);
            holder.customer.setText(customer_name);
        } else {
            holder.customer.setVisibility(View.GONE);
        }
        if (!list_all.get(position).getReport_type().equals("1")) {
            holder.donrelative.setVisibility(View.VISIBLE);
            //holder.open_info.setVisibility(View.VISIBLE);
            holder.lin1.setVisibility(View.VISIBLE);
            holder.lin4.setVisibility(View.VISIBLE);
            holder.lin5.setVisibility(View.VISIBLE);
            holder.checkbox.setEnabled(false);
            if (list_all.get(position).getDone().equals("1")) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
        } else {
            holder.donrelative.setVisibility(View.GONE);
            //holder.open_info.setVisibility(View.GONE);
            holder.lin1.setVisibility(View.GONE);
            holder.lin4.setVisibility(View.GONE);
            holder.lin5.setVisibility(View.GONE);

        }
        String total = list_all.get(position).getTotal();
        String seen = list_all.get(position).getSeen();
        if (total.equals(seen)) {
            holder.num_replay.setBackgroundResource(R.drawable.circle2_);
        } else {
            holder.num_replay.setBackgroundResource(R.drawable.circle_);
        }
        String tt = String.valueOf(Integer.parseInt(total) - Integer.parseInt(seen));
        if (!(tt.equals("0"))) {
            holder.num_replay.setText(tt);
        }else{
            holder.num_replay.setText("");
        }
        if(list_all.get(position).getSkip().equals("1")){
            holder.skip.setVisibility(View.VISIBLE);
        }else{
            holder.skip.setVisibility(View.GONE);
        }
        String isskip = list_all.get(position).getSkip();
        if(isskip.equals("1")){
            holder.cardView.setAlpha(0.6f);
        }else{
            holder.cardView.setAlpha(1f);
        }
        holder.replies.setText(list_all.get(position).getAll_replies());
        String dis_w=list_all.get(position).getDistance_write();
        holder.write_dis.setText(dis_w+" km");
        if(list_all.get(position).getDoneaddress().equals("null")){
            holder.done_diss.setText("");
            holder.done_add.setText("");
            holder.done_time.setText("");
        }
        else {
            holder.done_diss.setText(list_all.get(position).getDistance_done()+" km");
            holder.done_add.setText(list_all.get(position).getDoneaddress());
            holder.done_time.setText(list_all.get(position).getDone_timestamp());
        }
        try {
            if (Double.parseDouble(list_all.get(position).getDistance_write()) > 0.2) {
                holder.map.setImageResource(R.drawable.pin_red);
            } else {
                holder.map.setImageResource(R.drawable.pin_new);
            }
        }catch (Exception e){}
        holder.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(activity!=null ) {
                        Intent intent = new Intent(activity, Replay_Report.class);
                        Work_Report_Value value = list_all.get(position);
                        list_all.get(position).setSeen(list_all.get(position).getTotal());
                        intent.putExtra("report", value);
                        activity.startActivity(intent);
                    }
                }catch (Exception exe){

                }
            }
        });
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Reports main = Reports.getIns();
                main.expand2(list_all.get(position).getLat(), list_all.get(position).getLon());
            }
        });
        holder.historyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHistoryReport(position);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openHistoryReport(position);
                 return false;
            }
        });
        holder.open_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.more_data.getVisibility()==View.VISIBLE){
                    holder.more_data.setVisibility(View.GONE);
                }else{
                    holder.more_data.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    public void openHistoryReport(int position){
        if(!ishistory) {
            Work_Report_Value value = list_all.get(position);
            Intent intent = new Intent(activity, HisroryReport.class);
            intent.putExtra("geof_name", value.getCustomer());
            intent.putExtra("geof_id", value.getGeozoneid());
            intent.putExtra("offlinLoc_id", value.getOfflinLoc_id());
            activity.startActivity(intent);
        }
    }
    public void sortlist() {
        Collections.reverse(list_all);
        notifyDataSetChanged();

    }
}
