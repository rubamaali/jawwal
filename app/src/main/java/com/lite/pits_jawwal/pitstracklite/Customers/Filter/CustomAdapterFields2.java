package com.lite.pits_jawwal.pitstracklite.Customers.Filter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomAdapterFields2 extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<FieldsValue> list_all,arraylist,arrayfields;
    public CustomAdapterFields2(Context activity, List<FieldsValue> list){
        this.list_all = list;
        this.arraylist = new ArrayList<>();
        this.arrayfields = new ArrayList<>();
        this.arraylist.addAll(list_all);
        this.arrayfields.addAll(list_all);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView tag_name;
        RelativeLayout tagcell;
        CardView cardView;
        LinearLayout lin_fields;
    }
    @Override
    public int getCount() {
        return  list_all.size();
    }
    @Override
    public Object getItem(int i) {
        return list_all.get(i);
    }
    @Override
    public long getItemId(int i) {
         return i;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.custom_fields2, null);
            holder.tag_name =  view.findViewById(R.id.tag_name);
            holder.tagcell =  view.findViewById(R.id.tagcell);
            holder.cardView =  view.findViewById(R.id.cardView);
            holder.lin_fields =  view.findViewById(R.id.lin_fields);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tag_name.setText(list_all.get(position).getFieldname());
        holder.cardView.setCardBackgroundColor(Color.parseColor(list_all.get(position).getColor()));

        if(list_all.get(position).isSelect()){
            holder.tagcell.setBackgroundColor(Color.parseColor("#EBEBEB"));
            holder.lin_fields.setBackgroundColor(Color.parseColor("#EBEBEB"));
        }else{
            holder.tagcell.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.lin_fields.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.tag_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFieldValue(position);
            }
        });
        return view;
    }
    public void setFieldValue(int position){
        String mul=list_all.get(position).getTagmulti();
        FieldsValue fieldsValue = list_all.get(position);
         fieldsValue.setSelect(!fieldsValue.isSelect());
        notifyDataSetChanged();
    }

    public ArrayList<FieldsValue> getSelectFeilds(){
        ArrayList<FieldsValue> tags=new ArrayList<>();
        for (int i=0;i<list_all.size();i++){
            if(list_all.get(i).isSelect()){
                tags.add(list_all.get(i)) ;
            }
        }
        return tags;
    }
    public void removeSelect(String tagid,boolean all){
        for (int i = 0; i < list_all.size(); i++) {
            if(all){
                list_all.get(i).setSelect(false);
            }else {
                if (list_all.get(i).getTagid().equals(tagid)) {
                    list_all.get(i).setSelect(false);
                }
            }

        }
        notifyDataSetChanged();
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist.addAll(arrayfields);
        list_all.clear();
        if (charText.length() == 0) {
            list_all.addAll(arrayfields);
        } else {
            for (FieldsValue wp : arraylist) {
                boolean tag = false;
                if (wp.getFieldname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list_all.add(wp);
                }
            }
            arraylist.clear();
            arraylist.addAll(list_all);
        }
        notifyDataSetChanged();
    }
    public String getSearch() {
        String fields = "";
        for (int i = 0; i < list_all.size(); i++) {
            if(list_all.get(i).isSelect())
                fields += list_all.get(i).getFieldid() + ",";
        }
        fields += "0";
        return fields;
    }
    public void setSelect( String[] listfields){
        if(listfields!=null) {
            for (int i = 0; i < list_all.size(); i++) {
                FieldsValue fieldsValue = list_all.get(i);
                if (Arrays.asList(listfields).contains(fieldsValue.getFieldid())) {
                    fieldsValue.setSelect(true);
                }
            }
        }
    }
}
