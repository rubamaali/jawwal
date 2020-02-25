package com.lite.pits_jawwal.pitstracklite.Order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by NgocTri on 10/22/2016.
 */

public class ListCartAdapter extends BaseAdapter  {
    private String lan,flag;
    private List<CartValues> itemList;
    private Context context;
    private String price;
    private  SharedPreferences prefs;
    public ListCartAdapter(Context context, List<CartValues> itemList, String flag) {
        this.context=context;
        this.itemList=itemList;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        lan=prefs.getString(Preferences.language1,"0");
        price=context.getResources().getString(R.string.price);
        this.flag=flag;
    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public CartItemValue getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_cart, null);
        }
        final CartValues cart = itemList.get(position);
        TextView txt_customer =  v.findViewById(R.id.txt_customer);
        TextView txt_user =  v.findViewById(R.id.txt_user);
        TextView txt_time =  v.findViewById(R.id.txt_time);
        TextView txt_price =  v.findViewById(R.id.txt_price);
        TextView txt_action =  v.findViewById(R.id.txt_action);
        LinearLayout lin_user =  v.findViewById(R.id.lin_user);

        txt_customer.setText(cart.getGeozoneID());
        txt_time.setText(cart.getCreatetime());
        txt_price.setText(cart.getTotalPrice());
        if(cart.getCreatedby().isEmpty()){
            lin_user.setVisibility(View.GONE);
        }else {
            lin_user.setVisibility(View.VISIBLE);
            txt_user.setText(cart.getCreatedby());
        }
        String action;
        if(lan.equals("1")){//eng
            action=cart.getEName();
        }else{
            action=cart.getAName();
        }
        txt_action.setText(action);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openshowitem(cart);

            }
        });

        return v;
    }

    private void openshowitem(CartValues cart) {
        Intent intent=new Intent(context, ActivityItemInCart.class);
        intent.putExtra("cart", (Serializable) cart);
        ((Activity) context).startActivity(intent);
    }


}
