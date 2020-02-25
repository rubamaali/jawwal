package com.lite.pits_jawwal.pitstracklite.RelatedReport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Report_List extends AsyncTask<Object, Object, ArrayList<Work_Report_Value>> {
    private String _userName,_password,_geoid,start,quantity,offlinLoc_id;
    ArrayList<Work_Report_Value> list = new ArrayList<Work_Report_Value>();
    private SharedPreferences prefs;
    private AsyncResponseRelated delegate = null;
    public Report_List(String userName, String password, String geoid, String start, String quantity,
                       Context context,AsyncResponseRelated delegate,String offlinLoc_id) {
        _userName = userName;
        _password = password;
        _geoid = geoid;
        this.offlinLoc_id=offlinLoc_id;
        this.start=start;
        this.quantity=quantity;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.delegate=delegate;
    }

    @Override
    protected ArrayList<Work_Report_Value> doInBackground(Object... data) {
        try {

            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url + "/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("USER", _userName));
            nameValuePairs.add(new BasicNameValuePair("PASS", _password));
            nameValuePairs.add(new BasicNameValuePair("page", "personal_reporthistory"));
            nameValuePairs.add(new BasicNameValuePair("geoid", _geoid));
            nameValuePairs.add(new BasicNameValuePair("offlinLoc_id", offlinLoc_id));
            nameValuePairs.add(new BasicNameValuePair("start", start));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            list.clear();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("report");
                    for (int i = 0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);
                        list.add(new Work_Report_Value(jsonObject2.optString("userid"), jsonObject2.optString("per_id"),
                                jsonObject2.optString("report"), jsonObject2.optString("createdTime"),
                                jsonObject2.optString("lon"), jsonObject2.optString("lat"),
                                jsonObject2.optString("address"), jsonObject2.optString("msg_id"),
                                jsonObject2.optString("userid"), jsonObject2.optString("type"),
                                jsonObject2.optString("name"), jsonObject2.optString("done"),
                                jsonObject2.optString("total"), jsonObject2.optString("notseen"),
                                jsonObject2.optString("report_type"), jsonObject2.optString("customer"),
                                jsonObject2.optString("all_replies"), jsonObject2.optString("skip"),
                                jsonObject2.optString("geozoneid") ,jsonObject2.optString("offlineID"),
                                jsonObject2.optString("offlineloc_id"),
                                jsonObject2.optString("doneaddress"),jsonObject2.optString("distance_write"),
                                jsonObject2.optString("distance_done"),jsonObject2.optString("done_timestamp")));
                    }
                    return list;
                }
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Work_Report_Value> result) {
        if (delegate!=null) {
            delegate.processFinish2(result);
        }
    }
}
