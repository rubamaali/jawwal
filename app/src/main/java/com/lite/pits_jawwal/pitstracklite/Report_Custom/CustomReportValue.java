package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.R;

import java.util.List;

/**
 * Created by Ruba-PITS on 10/21/2017.
 */

public class CustomReportValue extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<ReportList> report = null;


    public CustomReportValue(Activity activity, List<ReportList> list){
        this.report = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView name;

    }
    @Override
    public int getCount() {
        return report.size();
    }

    @Override
    public ReportList getItem(int position) {
        return report.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final CustomReportValue.ViewHolder holder;
        if (view == null) {
            holder = new CustomReportValue.ViewHolder();
            view = inflater.inflate(R.layout.spinner_item, null);
            holder.name =  view.findViewById(R.id.name_report);
            view.setTag(holder);
        } else {
            holder = (CustomReportValue.ViewHolder) view.getTag();
        }
        holder.name.setText(report.get(position).getName());
        return view;
    }
    public int getIndex(String key){
        for (int i=0;i<report.size();i++){
            if(report.get(i).getId().equals(key))
                return i;
        }
        return -1;
    }
}
