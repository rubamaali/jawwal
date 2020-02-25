package com.lite.pits_jawwal.pitstracklite;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by WIN on 3/6/2016.
 */
public class Utils {

    public static boolean isOnline(Context activity){
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            if(cm.getActiveNetworkInfo()!=null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Log.d(activity.getClass().getSimpleName(), "Internet is available ");
                    return true;
                }
            }
        }
        return false;
    }
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei="0";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            imei= "0";
            //return telephonyManager.getDeviceId();
        }
        if (telephonyManager.getDeviceId() != null && telephonyManager.getDeviceId().length()>0){
            imei= telephonyManager.getDeviceId();
        }else{
            imei= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return imei;

    }



}
