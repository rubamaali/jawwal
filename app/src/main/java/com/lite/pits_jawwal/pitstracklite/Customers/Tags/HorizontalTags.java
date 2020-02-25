package com.lite.pits_jawwal.pitstracklite.Customers.Tags;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.Customers.Filter.FilterActivity;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalTags extends RecyclerView.Adapter<HorizontalTags.GroceryViewHolder>{
    private List<TagsValue> tagsValues;
    Context context;

    public HorizontalTags(List<TagsValue> tagsValues, Context context){
        this.tagsValues= tagsValues;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_tag, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.txt_feilds.setText(tagsValues.get(position).getTagname());
        holder.cardFields.setCardBackgroundColor(Color.parseColor(tagsValues.get(position).getTagcolor()));
        holder.txt_feilds.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if(tagsValues.get(position).isSelect()){
            holder.layout_cell.setBackgroundColor(Color.parseColor("#D4D5CD"));
        }else{
            holder.layout_cell.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.txt_feilds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsValue tagsValue=tagsValues.get(position);
                boolean lsttagselectvalue=tagsValue.isSelect();
                if(lsttagselectvalue){
                    removeFieldsValue(tagsValue.getTagid());
                }
                tagsValue.setSelect(!tagsValue.isSelect());
                notifyDataSetChanged();
                try {
                    FilterActivity filterActivity= FilterActivity.getInstance();
                    filterActivity.setFields();
                }catch (Exception e){}
            }
        });
    }


    @Override
    public int getItemCount() {
        return tagsValues.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView txt_feilds;
        CardView cardFields;
        RelativeLayout layout_cell;
        public GroceryViewHolder(View view) {
            super(view);
            txt_feilds=view.findViewById(R.id.txt_feilds);
            cardFields=view.findViewById(R.id.cardFields);
            layout_cell=view.findViewById(R.id.layout_cell);

        }
    }
    public ArrayList<FieldsValue> getSelectTag(){
        ArrayList<FieldsValue> tags=new ArrayList<>();
        for (int i=0;i<tagsValues.size();i++){
            if(tagsValues.get(i).isSelect()){
                tags.addAll(tagsValues.get(i).getTagsValues()) ;
            }
        }
        return tags;
    }
    public ArrayList<FieldsValue> getFields(){
        ArrayList<FieldsValue> tags=new ArrayList<>();
        for (int i=0;i<tagsValues.size();i++){
            tags.addAll(tagsValues.get(i).getTagsValues()) ;
        }
        return tags;
    }
    private void removeFieldsValue(String tagid) {
        FilterActivity filterActivity=FilterActivity.getInstance();
        filterActivity.removeSelectFields(tagid);
    }

}
