package com.lite.pits_jawwal.pitstracklite;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Ruba-PITS on 5/25/2017.
 */

public class Lang {
    public String bb;
  public String lan() {
      PitsTrack main= PitsTrack.getInstance();
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(main);
      String userName = prefs.getString(Preferences.USER_NAME, "");
      String password = prefs.getString(Preferences.PASSWORD, "");
      String url=prefs.getString(Preferences.URL, "");
      Langvalue task = new Langvalue(userName,password,url);
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
         return bb;

  }
    public class Langvalue extends AsyncTask<Void, Void, Void> {
        private String _userName;
        private String _password;
        private String _url;
             public Langvalue(String userName, String password,String url){
                 _userName = userName;
                 _password = password;
                 _url=url;
             }

        @Override
        protected Void doInBackground(Void... data) {
            String urlFormat2 =_url+"/lan.php?USER="+ _userName + "&PASS=" + _password;
            urlFormat2 = urlFormat2.replaceAll(" ", "");
            try{
            URL url2 = new URL(urlFormat2);
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
            conn2.setRequestMethod("POST");
            conn2.setDoOutput(true);
            OutputStreamWriter writer2 = new OutputStreamWriter(conn2.getOutputStream());
            writer2.write("");
            writer2.flush();
            BufferedReader reader2 = new BufferedReader(new
                    InputStreamReader(conn2.getInputStream()));
            String line2 = reader2.readLine();
            try{
                if(line2.contains("1")){
                    line2="1";
                }
                else{line2="0";}
            }
            catch (Exception e){
                line2="error";
            }
            writer2.close();
            reader2.close();
            Log.d("line", line2);
            PitsTrack main= PitsTrack.getInstance();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(main);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Preferences.language1, line2);
            editor.apply();

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
