package com.lite.pits_jawwal.pitstracklite.Customers.Tags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
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


public class TagsPhp extends AsyncTask<Object, Object, ArrayList<TagsValue>> {
    ArrayList<TagsValue> listtagsValues = new ArrayList<>();
    ArrayList<FieldsValue> listfieldsValues = new ArrayList<>();
    public AsyncResponseTags delegate = null;
    private SharedPreferences prefs;

    private ArrayList<FieldsValue>  tagsListSelect;

    public TagsPhp(AsyncResponseTags delegate, Context context) {
        this.delegate=delegate;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public TagsPhp(AsyncResponseTags delegate, Context context, ArrayList<FieldsValue>  tagsListSelect) {
        this.delegate=delegate;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.tagsListSelect=tagsListSelect;
    }
    @Override
    protected ArrayList<TagsValue> doInBackground(Object... data) {
        try {
            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url + "/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("USER", prefs.getString(Preferences.USER_NAME,"")));
            nameValuePairs.add(new BasicNameValuePair("PASS", prefs.getString(Preferences.PASSWORD,"")));
            nameValuePairs.add(new BasicNameValuePair("page", "tags"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            listtagsValues.clear();
            listfieldsValues.clear();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("tags");
                    for(int i=0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);
                        JSONArray v2 = jsonObject2.getJSONArray("tagfields");
                        listfieldsValues.clear();
                        ArrayList<FieldsValue> listfieldsValues2 = new ArrayList<>();
                        boolean selectTags=false;
                        for(int j=0; j < v2.length(); j++) {
                            JSONObject jsonObject3 = v2.getJSONObject(j);
                            String fielid=jsonObject3.optString("fieldid");
                            boolean select=false;
                            if(tagsListSelect!=null){
                                for(int in=0;in<tagsListSelect.size();in++){
                                    if(tagsListSelect.get(in).getFieldid().equals(fielid)){
                                        select=true;
                                        selectTags=true;
                                        break;                                    }
                                }
                            }
                            listfieldsValues2.add(new FieldsValue(fielid,
                                    jsonObject2.optString("tagid"),jsonObject3.optString("fieldname"),
                                    jsonObject2.optString("tagcolor"),
                                    select,jsonObject2.optString("tagmulti"))
                            );
                        }
                        listtagsValues.add(new TagsValue(jsonObject2.optString("tagid"),
                                jsonObject2.optString("tagname"),jsonObject2.optString("tagcolor"),
                                jsonObject2.optString("tagtype"),jsonObject2.optString("tagmulti"),
                                listfieldsValues2,selectTags
                        ));
                    }
                     return listtagsValues;
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
        }
        catch (Exception ignored){
            Log.d("TAG", "doInBackground: "+ignored);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<TagsValue> result) {
        if (delegate!=null) {
            delegate.processFinishTag(result);
        }
    }

}

