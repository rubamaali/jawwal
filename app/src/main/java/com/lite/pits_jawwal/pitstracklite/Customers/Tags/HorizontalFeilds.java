package com.lite.pits_jawwal.pitstracklite.Customers.Tags;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.Customers.FragmentCustomer;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.List;

public class HorizontalFeilds extends RecyclerView.Adapter<HorizontalFeilds.GroceryViewHolder> {
    private List<FieldsValue> fieldsValues;
    Context context;
    private boolean select;

    public HorizontalFeilds(List<FieldsValue> fieldsValues, Context context, boolean select) {
        this.fieldsValues = fieldsValues;
        this.context = context;
        this.select = select;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_listview, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.txt_feilds.setText(fieldsValues.get(position).getFieldname());
        holder.cardFields.setCardBackgroundColor(Color.parseColor(fieldsValues.get(position).getColor()));
        if (select) {

            holder.txt_feilds.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (fieldsValues.get(position).isSelect()) {
                holder.layout_cell.setBackgroundColor(Color.parseColor("#D4D5CD"));
            } else {
                holder.layout_cell.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        } else {
            holder.txt_feilds.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel2, 0, 0, 0);
            holder.layout_cell.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.txt_feilds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select) {
                    FieldsValue fieldsValue = fieldsValues.get(position);
                    fieldsValue.setSelect(!fieldsValue.isSelect());
                    notifyDataSetChanged();
                    getSearch();
                } else {
                    delete_tags(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return fieldsValues.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txt_feilds;
        CardView cardFields;
        RelativeLayout layout_cell;

        public GroceryViewHolder(View view) {
            super(view);
            txt_feilds = view.findViewById(R.id.txt_feilds);
            cardFields = view.findViewById(R.id.cardFields);
            layout_cell = view.findViewById(R.id.layout_cell);
        }
    }

    private void delete_tags(int position) {
        fieldsValues.remove(position);
        notifyDataSetChanged();
    }

    private void getSearch() {
        String fields = "";
        for (int i = 0; i < fieldsValues.size(); i++) {
            if(fieldsValues.get(i).isSelect())
                fields += fieldsValues.get(i).getFieldid() + ",";
        }
        fields += "0";
        FragmentCustomer fragmentCustomer = FragmentCustomer.getinstance();
        fragmentCustomer.searchTags(fields);
    }
public void removeSelect(String tagid,boolean all){
    for (int i = 0; i < fieldsValues.size(); i++) {
        if(all){
            fieldsValues.get(i).setSelect(false);
        }else {
            if (fieldsValues.get(i).getTagid().equals(tagid)) {
                fieldsValues.get(i).setSelect(false);
            }
        }

    }
    notifyDataSetChanged();
    getSearch();
}


}
