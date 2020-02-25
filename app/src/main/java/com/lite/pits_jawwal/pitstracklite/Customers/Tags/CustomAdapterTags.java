package com.lite.pits_jawwal.pitstracklite.Customers.Tags;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapterTags extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<TagsValue> list_all;
    private Context activity;
    public CustomAdapterTags(Context activity, List<TagsValue> list){
        this.list_all = list;
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView tag_name;
        RelativeLayout tagcell;
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
            view = inflater.inflate(R.layout.custom_tag, null);
            holder.tag_name =  view.findViewById(R.id.tag_name);
            holder.tagcell =  view.findViewById(R.id.tagcell);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        DateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy", new Locale("en"));
        holder.tag_name.setText(list_all.get(position).getTagname());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.tag_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list_all.get(position).getTagcolor())));
        }
        if(list_all.get(position).isSelect()){
            holder.tagcell.setBackgroundColor(Color.parseColor("#E0E0E0"));
        }else{
            holder.tagcell.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.tag_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsValue tagsValue=list_all.get(position);
                tagsValue.setSelect(!tagsValue.isSelect());
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public List<FieldsValue> getSelectTag(){
        List<FieldsValue> tags=new ArrayList<>();
        for (int i=0;i<list_all.size();i++){
            if(list_all.get(i).isSelect()){
                tags.add(new FieldsValue("tagid", "", list_all.get(i).getTagname(), list_all.get(i).getTagcolor(),false,list_all.get(i).getTagmulti()));
                tags.addAll(list_all.get(i).getTagsValues()) ;
            }
        }
        return tags;
    }
    public void removeFieldSelect(String tagid){

    }


}
