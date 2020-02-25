package com.lite.pits_jawwal.pitstracklite;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lite.pits_jawwal.pitstracklite.Type.Location_type;

import java.util.ArrayList;

public class ALL {
    public static void set_number_notification(Context context){
        try {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(Preferences.NUMBER_NOTIFICATION, String.valueOf("0"));
            editor.apply();
        }catch (Exception e){

        }
    }
    public static void show_custom_msg(Context context, Activity activity, String msg){
        LayoutInflater inflater=activity.getLayoutInflater();
        View customToastroot =inflater.inflate(R.layout.mycustom_toast, null);
        TextView msg_txt=customToastroot.findViewById(R.id.toast_msg);
        msg_txt.setText(msg);
        Toast customtoast=new Toast(context);
        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL|Gravity.FILL_HORIZONTAL,0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
        sound(context);
    }
    public static void sound(Context context){
        Uri notification = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.noti2);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
    public static ArrayList<Location_type> get_location_type(Context context){
       ArrayList<Location_type>type_loc = new ArrayList<>();
       type_loc.add(new Location_type(context.getResources().getString(R.string.ATM), "1"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Bank), "2"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Cinema), "3"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Clothing_Store), "4"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Coffee_Shop), "5"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Company), "6"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Court), "7"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Factory), "8"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Fitness_Center), "9"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Home), "10"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Hospital), "11"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Hotel), "12"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Library), "13"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Medical_Laboratory), "14"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Mosque), "15"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Museum), "16"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Park), "17"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Petrol_Station), "18"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Pharmacy), "19"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Physician), "20"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Police_Office), "21"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Restaurant), "22"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.School), "23"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Shopping_Center), "24"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Square_Roundabout), "25"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Stadium), "26"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Street), "27"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Supermarket), "28"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.University), "29"));
       type_loc.add(new Location_type(context.getResources().getString(R.string.Other), "30"));
       return type_loc;
    }


}
