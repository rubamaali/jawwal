package com.lite.pits_jawwal.pitstracklite.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsValue;
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
import java.util.ArrayList;
import java.util.List;

public class GeofenceList extends AsyncTask<Object, Object, ArrayList<PlaceData>>  {
    ArrayList<PlaceData> list = new ArrayList<PlaceData>();
    private SharedPreferences prefs;
    private AsyncResponseCustomer delegate = null;
    private String _pass, _username,start,getall;

    public GeofenceList(String _username, String _pass, Context context,AsyncResponseCustomer delegate,String start,String getall) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this._username = _username;
        this._pass = _pass;
        this.delegate = delegate;
        this.getall=getall;
        this.start=start;
    }

    @Override
    protected ArrayList<PlaceData> doInBackground(Object... data) {
        try {
            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url + "/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("USER", _username));
            nameValuePairs.add(new BasicNameValuePair("PASS", _pass));
            nameValuePairs.add(new BasicNameValuePair("page", "customer"));
            nameValuePairs.add(new BasicNameValuePair("start", start));
            nameValuePairs.add(new BasicNameValuePair("getAll", getall));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            list.clear();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("customer");
                    for (int i = 0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);

                        JSONObject v7 = jsonObject2.getJSONObject("tags");
                        JSONArray v5 = v7.getJSONArray("tags");
                        ArrayList<TagsValue> listtagsValues = new ArrayList<>();
                        ArrayList<FieldsValue> listfieldsValues = new ArrayList<>();
                        listtagsValues.clear();
                        for(int ii=0; ii < v5.length(); ii++) {
                            JSONObject jsonObject22 = v5.getJSONObject(ii);
                            JSONArray v2 = jsonObject22.getJSONArray("tagfields");
                            listfieldsValues.clear();

                            ArrayList<FieldsValue> listfieldsValues2 = new ArrayList<>();
                            for(int j=0; j < v2.length(); j++) {
                                JSONObject jsonObject3 = v2.getJSONObject(j);
                                listfieldsValues2.add(new FieldsValue(jsonObject3.optString("fieldid"),
                                        jsonObject22.optString("tagid"),jsonObject3.optString("fieldname"),
                                        jsonObject22.optString("tagcolor"),
                                        false,jsonObject22.optString("tagmulti"))
                                );
                            }
                            listtagsValues.add(new TagsValue(jsonObject22.optString("tagid"),
                                    jsonObject22.optString("tagname"),jsonObject22.optString("tagcolor"),
                                    jsonObject22.optString("tagtype"),jsonObject22.optString("tagmulti"),
                                    listfieldsValues2,false
                            ));
                        }

                        list.add(new PlaceData(jsonObject2.optString("name"),
                                jsonObject2.optString("type"),
                                Double.parseDouble(jsonObject2.optString("lon")),
                                Double.parseDouble(jsonObject2.optString("lat")),0.0,"0",jsonObject2.optString("geo_id"),jsonObject2.optString("tell"),jsonObject2.optString("email"),
                                jsonObject2.optString("website"),jsonObject2.optString("fax"),jsonObject2.optString("distance"),jsonObject2.optString("classificationId")
                                ,jsonObject2.optString("customerName"),
                                jsonObject2.optString("customerPhone"),listtagsValues,
                                jsonObject2.optString("offlineloc_id"),false,jsonObject2.getString("gacount")));
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
        if (delegate!=null) {
            delegate.processFinish(result);
        }
    }

}