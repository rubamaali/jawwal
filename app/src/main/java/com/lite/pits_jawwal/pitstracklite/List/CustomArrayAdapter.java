package com.lite.pits_jawwal.pitstracklite.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.PitsTrack;
import com.lite.pits_jawwal.pitstracklite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class CustomArrayAdapter extends BaseAdapter {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Vehicles_list> NamesList = null;
    private ArrayList<Vehicles_list> arraylist;
    private ArrayList<Vehicles_list> list_all;
    private Context context;


    public CustomArrayAdapter(Activity activity, List<Vehicles_list> list,Context context) {
        this.NamesList = list;
        this.arraylist = new ArrayList<>();
        this.list_all = new ArrayList<>();
        this.arraylist.addAll(NamesList);
        this.list_all.addAll(NamesList);
        this.context=context;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        TextView name, last_updte, address;
        ImageView on_off;
        TextView v_speed,txt_driver_phone;
        TextView driver_name;

    }

    @Override
    public int getCount() {
        return NamesList.size();
    }

    @Override
    public Vehicles_list getItem(int position) {
        return NamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, null);
            holder.name = view.findViewById(R.id.name);
            holder.last_updte = view.findViewById(R.id.last_updte);
            holder.address = view.findViewById(R.id.address);
            holder.on_off = view.findViewById(R.id.img_on_off);
            holder.v_speed = view.findViewById(R.id.v_speed);
            holder.driver_name = view.findViewById(R.id.txt_driver_name);
            holder.txt_driver_phone = view.findViewById(R.id.txt_driver_phone);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String name = NamesList.get(position).getName();
        holder.name.setText(name);
        if (NamesList.get(position).getDriver_name().length() > 1) {
            holder.driver_name.setText("(" + NamesList.get(position).getDriver_name() + ")");
        } else {
            holder.driver_name.setText("");
        }
        holder.last_updte.setText(NamesList.get(position).getLast_update());
        SimpleDateFormat Formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("en-US"));
        try {
            Date last_date = Formater.parse(NamesList.get(position).getLast_update());
            long lastupdate=last_date.getTime()/1000;
            long current=System.currentTimeMillis()/1000;
            Log.d(TAG, "getView: "+lastupdate);
            Log.d(TAG, "getView: "+current);
            long diff=current-lastupdate;
//            if(diff>630 || diff<-630){
//                holder.last_updte.setBackgroundColor(Color.parseColor("#F5D5D5"));
//            }else{
//                holder.last_updte.setBackgroundColor(Color.parseColor("#E2E1E1"));
//            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.address.setText(NamesList.get(position).getAddress());
        holder.v_speed.setText(NamesList.get(position).getSpeed());
        String phone=NamesList.get(position).getPhone();
        if(phone.length()>2) {
            holder.txt_driver_phone.setText(phone);
        }else{
            holder.txt_driver_phone.setVisibility(View.VISIBLE);
            holder.txt_driver_phone.setVisibility(View.GONE);
        }
        Double int_speed = Double.parseDouble(NamesList.get(position).getSpeed());
        String keyy = NamesList.get(position).getKey();
        String on = "1,2,5,4,9,7,8,-1";
        switch (NamesList.get(position).getIcon()) {
            case "12":
                holder.on_off.setBackgroundResource(R.drawable.pp_off2);
                if (on.contains(keyy) && int_speed < 1) {
                    holder.on_off.setBackgroundResource(R.drawable.p_offf);
                }
                if (on.contains(keyy) && int_speed >= 1 && int_speed <= 10) {
                    holder.on_off.setBackgroundResource(R.drawable.p_walking);
                } else if (on.contains(keyy) && int_speed >= 11) {
                    holder.on_off.setBackgroundResource(R.drawable.p_running);
                } else if (keyy.contains("10")) {
                    holder.on_off.setBackgroundResource(R.drawable.p_timer);

                }
                break;

            case "4":
                holder.on_off.setBackgroundResource(R.drawable.icon_4_off2);
                if (keyy.contains("1") && int_speed >= 0 && int_speed <= 5) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_4_yellow);
                } else if (keyy.contains("1") && int_speed >= 121) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_4_red);
                } else if (keyy.contains("1") && int_speed >= 6 && int_speed <= 90) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_4_on);
                } else if (keyy.contains("1") && int_speed >= 91 && int_speed <= 120) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_4_orange);
                }
                break;

            case "8":
                holder.on_off.setBackgroundResource(R.drawable.icon_8_off2);
                if (keyy.contains("1") && int_speed >= 0 && int_speed <= 5) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_8_yellow);
                } else if (keyy.contains("1") && int_speed >= 121) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_8_red);
                } else if (keyy.contains("1") && int_speed >= 6 && int_speed <= 90) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_8_on);
                } else if (keyy.contains("1") && int_speed >= 91 && int_speed <= 120) {
                    holder.on_off.setBackgroundResource(R.drawable.icon_8_orange);
                }
                break;

            default:
                holder.on_off.setBackgroundResource(R.drawable.car_off2);
                if (keyy.contains("1") && int_speed >= 0 && int_speed <= 5) {
                    holder.on_off.setBackgroundResource(R.drawable.car_yellow);
                } else if (keyy.contains("1") && int_speed >= 121) {
                    holder.on_off.setBackgroundResource(R.drawable.car_red);
                } else if (keyy.contains("1") && int_speed >= 6 && int_speed <= 90) {
                    holder.on_off.setBackgroundResource(R.drawable.car_on);
                } else if (keyy.contains("1") && int_speed >= 91 && int_speed <= 120) {
                    holder.on_off.setBackgroundResource(R.drawable.car_orange);
                }
                break;

        }
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final PitsTrack main = PitsTrack.getInstance();
                    main.selectItemFromDrawer(1, String.valueOf(get_index(position)));
                } catch (Exception e) {
                }
            }
        });
        holder.txt_driver_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_call(list_all.get(position).getPhone());
            }
        });
        return view;
    }
    public  void show_call(final String num){
        String text = "";
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle("");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(context.getString(R.string.str40));
        alertDialog.setPositiveButton(context.getString(R.string.call), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+num));
                if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);

            }
        });
        alertDialog.setNegativeButton(context.getString(R.string.msg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+num));
                context.startActivity(sendIntent);
                dialog.dismiss();
            }
        });
        alertDialog.setNeutralButton(context.getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        String[] words = charText.split(" ");
        arraylist.clear();
        arraylist.addAll(list_all);
        for (int i = 0; i < words.length; i++) {

            NamesList.clear();
            if (words[i].length() == 0) {
                NamesList.addAll(list_all);
            } else if (words[i].equals("off")) {
                filter_key("0,3");
            } else if (words[i].equals("on")) {
                filter_key("1,2,5,4,9");
            } else {
                for (Vehicles_list wp : arraylist) {
                    boolean tag = false;
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(words[i])) {
                        tag = true;
                    }
                    if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(words[i])) {
                        tag = true;
                    }
                    if (wp.getLast_update().toLowerCase(Locale.getDefault()).contains(words[i])) {
                        tag = true;
                    }
                    if (tag) {
                        NamesList.add(wp);
                    }
                }
            }
            arraylist.clear();
            arraylist.addAll(NamesList);
        }
        notifyDataSetChanged();
    }

    public void filter_key(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        NamesList.clear();
        if (charText.length() == 0) {
            NamesList.addAll(arraylist);
        } else {
            for (Vehicles_list wp : arraylist) {
                if (charText.contains(wp.getKey().toLowerCase(Locale.getDefault()))) {
                    NamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public String get_id(int index) {
        return NamesList.get(index).getDeviceid();
    }

    public int get_index(int index) {
        return NamesList.get(index).getIndex();
    }
}
