package com.lite.pits_jawwal.pitstracklite.File_login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.lite.pits_jawwal.pitstracklite.Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AsyncTask<Void, Void, String> {
    private String _userName,_password,_url,_imei,_token;
    public AsyncResponseLogin delegate = null;
    private Context context;
    public Login(String userName, String password, String url, String imei, String token, AsyncResponseLogin delegate, Context context) {
        _userName = userName;
        _password = password;
        _url = url;
        _imei=imei;
        _token=token;
        this.delegate = delegate;
        this.context=context;
    }
    @Override
    protected String doInBackground(Void... data) {
        String urlFormat = _url +"/loginclean_lite.php?USER=" + _userName + "&PASS=" + _password + "&redirect=0&key=" + _token + "&imei=" + _imei+ "&app=1";
        try {
            URL url = new URL(urlFormat);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("");
            writer.flush();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonObject = new JSONObject(line);
            JSONArray v = jsonObject.getJSONArray("login_result");
            JSONObject jsonObject2 = v.getJSONObject(0);
            String success = jsonObject2.getString("success");
            String reportlist = jsonObject2.getString("reportlist");
            String pitsonal="0";
            if (jsonObject2.has("pitsonal")) {
                 pitsonal = jsonObject2.getString("pitsonal");
            }
            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString(Preferences.REPORTLIST, reportlist);
            editor.putString(Preferences.PITSONAL, pitsonal);
            editor.apply();
            writer.close();
            reader.close();
            return success;
        }
        catch (java.net.ProtocolException e) {
            return null;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }catch (Exception e){
            return null;
        }
    }

    protected void onPostExecute(String result) {
        if (delegate!=null ) {
            delegate.processFinish(result);
        }

    }

}

