package com.lite.pits_jawwal.pitstracklite.Order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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


public class ItemConfirmCartPhp extends AsyncTask<Object, Object, ArrayList<ItemOrderValue>> {
    public AsyncResponseItemOrder delegate = null;
    private SharedPreferences prefs;
    private ArrayList<ItemOrderValue>  cartList;
    private String name,password,refid;

    public ItemConfirmCartPhp(String name, String password, AsyncResponseItemOrder delegate, Context context, String refid) {
        this.delegate=delegate;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        cartList=new ArrayList<>();
        this.password=password;
        this.name=name;
        this.refid=refid;//admin or user
    }

    @Override
    protected ArrayList<ItemOrderValue> doInBackground(Object... data) {
        try {
            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url+"/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("USER",name));
            nameValuePairs.add(new BasicNameValuePair("PASS", password));
            nameValuePairs.add(new BasicNameValuePair("page", "order_item"));
            nameValuePairs.add(new BasicNameValuePair("refid", refid));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("order_item");
                    for(int i=0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);
                        cartList.add(new ItemOrderValue(jsonObject2.optString("AName"),
                                jsonObject2.optString("quantity"),jsonObject2.optString("price"),
                                jsonObject2.optString("EName"))
                        );
                    }
                }
            }
            return cartList;
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
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ItemOrderValue> result) {
        if (delegate!=null) {
            delegate.processFinishCart(result);
        }
    }

}

