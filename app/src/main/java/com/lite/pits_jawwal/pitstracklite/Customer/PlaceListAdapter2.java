package com.lite.pits_jawwal.pitstracklite.Customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.Locale;

public class PlaceListAdapter2 extends BaseAdapter {

    private ArrayList<PlaceData> places;
    private ArrayList<PlaceData> arraylist;
    private ArrayList<PlaceData> list_all;
    private static LayoutInflater inflater = null;

    public PlaceListAdapter2(Context context, ArrayList<PlaceData> places, int image) {
        // TODO Auto-generated constructor stub
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.places = places;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(places);
        this.list_all.addAll(places);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return places.size();
    }

    @Override
    public PlaceData getItem(int position) {
        // TODO Auto-generated method stub
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_customer, null);
        final TextView text =  vi.findViewById(R.id.textV);
        text.setText(places.get(position).getPlaceName());



        return vi;
    }



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist.addAll(list_all);
        places.clear();
        if (charText.length() == 0) {
            places.addAll(list_all);
        } else {
            for (PlaceData wp : arraylist) {
                if (wp.getPlaceName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    places.add(wp);
                }
            }
            arraylist.clear();
            arraylist.addAll(places);
        }
        notifyDataSetChanged();
    }

    public String getid(int position) {
        PlaceData data=getItem(position);
        return data.getPlaceId();
    }
    public String getiid(int position) {
        PlaceData data=getItem(position);
        return data.getIid();
    }

    public String getlon(int position) {
        PlaceData data=getItem(position);
        return String.valueOf(data.getPlaceLon());
    }

    public String getlat(int position) {
        PlaceData data=getItem(position);
        return String.valueOf(data.getPlaceLat());
    }

    public String  getname(int position) {
        PlaceData data=getItem(position);
        return data.getPlaceName();
    }
}