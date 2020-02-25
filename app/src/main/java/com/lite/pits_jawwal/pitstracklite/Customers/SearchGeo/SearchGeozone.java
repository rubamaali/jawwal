package com.lite.pits_jawwal.pitstracklite.Customers.SearchGeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.Preferences;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SearchGeozone extends AsyncTask<Object, Object, ArrayList<PlaceData>> {
    ArrayList<PlaceData> list = new ArrayList<PlaceData>();
    public AsyncResponseSearchGeof delegate = null;
    private SharedPreferences prefs;
    private Context context;
    private String lon, lat, searchText;


    public SearchGeozone(AsyncResponseSearchGeof delegate, Context context, String searchText) {
        this.delegate = delegate;
        this.context = context;
        this.searchText = searchText;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected ArrayList<PlaceData> doInBackground(Object... data) {
        String lon = "0";
        String lat = "0";
        try {
            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url + "/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("USER", prefs.getString(Preferences.USER_NAME, "")));
            nameValuePairs.add(new BasicNameValuePair("PASS", prefs.getString(Preferences.PASSWORD, "")));
            nameValuePairs.add(new BasicNameValuePair("page", "search_geozone"));
            nameValuePairs.add(new BasicNameValuePair("search_text", searchText));
            if (!lon.equals("0") && !lat.equals("0")) {
                nameValuePairs.add(new BasicNameValuePair("lon", lon));
                nameValuePairs.add(new BasicNameValuePair("lat", lat));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            list.clear();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("geozones");

                    for (int i = 0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);
                        double distance = Double.parseDouble(jsonObject2.optString("distanceInKilo"));
                        distance = Double.parseDouble(round_(distance));
                        list.add(new PlaceData(jsonObject2.optString("name"),
                                jsonObject2.optString("type"),
                                Double.parseDouble(jsonObject2.optString("lon")),
                                Double.parseDouble(jsonObject2.optString("lat")), distance, "0", jsonObject2.optString("geo_id"), jsonObject2.optString("tell"), jsonObject2.optString("email"),
                                jsonObject2.optString("website"), jsonObject2.optString("fax"), jsonObject2.optString("distance"), jsonObject2.optString("classificationId")
                                , jsonObject2.optString("customerName"), jsonObject2.optString("customerPhone"),
                                null,jsonObject2.optString("offlineloc_id"),false,jsonObject2.getString("gacount")));
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
    protected void onPostExecute(ArrayList<PlaceData> result) {
        if (delegate != null) {
            delegate.processFinish(result);
        }
    }

    public String round_(double gps_speed) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return String.valueOf(round(gps_speed, 2));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

