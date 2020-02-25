package com.lite.pits_jawwal.pitstracklite;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.lite.pits_jawwal.pitstracklite.Preferences.URL_ALL;

/**
 * Created by Ruba-PITS on 5/25/2017.
 */

public class SendRatingToDB {
    public String bb;

    public String sendfeedback(int rating, String note, String imie, String image, String userName, String paswword) {
        PostTask task = new PostTask(rating, note, imie, image, userName, paswword);
        task.execute();
        return bb;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "0";
        }
        return telephonyManager.getDeviceId();
    }
    public class PostTask extends AsyncTask<Void, Void, Void> {

        private String _note;
        private String _imie;
        private String _image;
        private String _userName;
        private int _rating;
        private String _password;
        public PostTask(int rating, String note, String imie, String image, String userName, String password) {
            _note=note;
            _imie=imie;
            _image=image;
            _userName=userName;
            _rating=rating;
            _password=password;
       }
        @Override
        protected Void doInBackground(Void... data) {
            String urlFormat =URL_ALL+"/feedback.php?imei=" + _imie + "&rating=" + _rating+ "&note=" + _note+ "&image_url=" + _image+"&appname=PITSTrack&USER="+ _userName + "&PASS=" + _password;
            urlFormat = urlFormat.replaceAll(" ", "");

            try {
                URL url = new URL(urlFormat);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("");
                writer.flush();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                writer.close();
                reader.close();
                bb="OK";
           } catch (java.net.ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}
