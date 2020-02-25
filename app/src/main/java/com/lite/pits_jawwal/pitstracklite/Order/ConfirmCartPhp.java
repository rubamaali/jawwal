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


public class ConfirmCartPhp extends AsyncTask<Object, Object, ArrayList<CartValues>> {
    public AsyncResponseCart delegate = null;
    private SharedPreferences prefs;
    private ArrayList<CartValues>  cartList;
    private String name,password,time,type,customerid,index;

    public ConfirmCartPhp(String name, String password, AsyncResponseCart delegate, Context context, String time) {
        this.delegate=delegate;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        cartList=new ArrayList<>();
        this.password=password;
        this.name=name;
        this.time=time;
        this.type="time";
    }
    public ConfirmCartPhp(String name, String password, AsyncResponseCart delegate, Context context, String index, String customerid, String type) {
        this.delegate=delegate;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        cartList=new ArrayList<>();
        this.password=password;
        this.name=name;
        this.index=index;
        this.type=type;
        this.customerid=customerid;

    }
    @Override
    protected ArrayList<CartValues> doInBackground(Object... data) {
        try {
            String url = prefs.getString(Preferences.URL, "");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url+"/mobilelite.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("USER",name));
            nameValuePairs.add(new BasicNameValuePair("PASS", password));
            nameValuePairs.add(new BasicNameValuePair("page", "confirm_cart"));
            nameValuePairs.add(new BasicNameValuePair("type", type));
            if(type.equals("time")) {
                nameValuePairs.add(new BasicNameValuePair("time", time));
            }else{
                nameValuePairs.add(new BasicNameValuePair("customerid", customerid));
                nameValuePairs.add(new BasicNameValuePair("index", index));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String resString = EntityUtils.toString(resEntity);
                if (resString != null) {
                    JSONObject jsonObject = new JSONObject(resString);
                    JSONArray v = jsonObject.getJSONArray("confirm_cart");
                    for(int i=0; i < v.length(); i++) {
                        JSONObject jsonObject2 = v.getJSONObject(i);
                        cartList.add(new CartValues(jsonObject2.optString("RId"),
                                jsonObject2.optString("type"),jsonObject2.optString("createtime"),
                                jsonObject2.optString("totalPrice"),jsonObject2.optString("updatetime")
                                ,jsonObject2.optString("geozoneID"),jsonObject2.optString("stockId")
                                ,jsonObject2.optString("quantity") ,jsonObject2.optString("customerId"),
                                jsonObject2.optString("stockeffecy_id"),jsonObject2.optString("AName"),
                                        jsonObject2.optString("EName"),jsonObject2.optString("createdby"))
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
    protected void onPostExecute(ArrayList<CartValues> result) {
        if (delegate!=null) {
            delegate.processFinishCart(result);
        }
    }

}

