package com.lite.pits_jawwal.pitstracklite.Firebase;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lite.pits_jawwal.pitstracklite.File_login.AsyncResponseLogin;
import com.lite.pits_jawwal.pitstracklite.File_login.Login;
import com.lite.pits_jawwal.pitstracklite.PitsTrack;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.Utils;

import java.io.IOException;


/**
 * Created by WIN on 9/17/2016.
 */
public class PitsRegistrationIntentService extends IntentService implements AsyncResponseLogin {
    public static String token;
    public static Context c;
    private static final String TAG = PitsRegistrationIntentService.class.getName();
    public PitsRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
           final boolean a = sharedPreferences.getBoolean(Preferences.HAS_LOGIN, false);
            c = this;
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }else{
                                token = task.getResult().getToken();
                                Log.i(TAG, "GCM Registration Token: " + token);
                                sendRegistrationToServer(token);
                                if(!a) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                FirebaseInstanceId.getInstance().deleteInstanceId();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    sharedPreferences.edit().putBoolean(Preferences.SENT_TOKEN_TO_SERVER, true).apply();
                                }
                                Intent registrationComplete = new Intent(Preferences.REGISTRATION_COMPLETE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                            }

                        }
                    });

        } catch (Exception e) {
            sharedPreferences.edit().putBoolean(Preferences.SENT_TOKEN_TO_SERVER, false).apply();
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public void sendRegistrationToServer(String token) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String user = prefs.getString(Preferences.USER_NAME, "");
        String pass = prefs.getString(Preferences.PASSWORD, "");

//        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        String imei = Utils.getIMEI(this);
        if(imei!=null && imei.length()>2 && token!=null && token.length()>2 && Utils.isOnline(this)) {
            Login login = new Login(user, pass, prefs.getString(Preferences.URL, ""), imei, token, this,this);
            login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    @Override
    public void processFinish(String output) {
        if(output!=null && output.equals("0")){
            PitsTrack pitsTrack=PitsTrack.getInstance();
            pitsTrack.show_dialog();
        }
    }
}
