package com.lite.pits_jawwal.pitstracklite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lite.pits_jawwal.pitstracklite.Vehicle_Custom.CustomAdapterDetails;
import com.lite.pits_jawwal.pitstracklite.Vehicle_Custom.Vehicle_Details;

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
import java.util.Locale;

/**
 * Created by Ruba-PITS on 7/29/2017.
 */

public class Car_Info extends AppCompatActivity implements View.OnClickListener {
    private Button veh_details,veh_control,but_status,but_control,but_meter;
    private View dim,dim2;
    private View slideView_d,slideView_c,slideView;
    private Slideup slideUp_d,slideUp_c;
    ListView listView;
    private Spinner spinner;
    WebView webview;
    private String speed;
    CustomAdapterDetails adapter;
    private TableRow tab_ideling;
    private TextView address_vehicles,txt_car_name,txt_driver_name,txt_reason,txt_phone,txt_las_location,txt_last_update
            ,txt_odometer,txt_version,veh_details2,veh_control2,stop_time,ide_time;
    private String butt_clic="0";
    private String name,pass,device_id;
    private String lon,lat;
    private TextView text_car,text_car2;
  //  Locale locale;
    private ImageView img_time,img_idel;
    private View line99;
    private String vehicle_control,vehicle_status,vehicle_odometer="0";
    TableRow cont,stat,odom;
    List<Vehicle_Details> list=new ArrayList<Vehicle_Details>();;
    public Car_Info() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        setContentView(R.layout.fragment_car_info);
        getSupportActionBar().setTitle(getResources().getString(R.string.Vehicle_Information));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tab_ideling =  findViewById(R.id.tab_ideling);
        veh_details =  findViewById(R.id.veh_details);
        veh_details.setOnClickListener(this);
        device_id=getIntent().getStringExtra("device_id");
        webview =  findViewById(R.id.speed);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:///android_asset/speed.html?speed="+120 );
        veh_details2 =  findViewById(R.id.veh_details2);
        veh_details2.setOnClickListener(this);

        veh_control2 =  findViewById(R.id.text_veh);
        veh_control2.setOnClickListener(this);

        stop_time =  findViewById(R.id.stop_time);
        ide_time =  findViewById(R.id.ide_time);
        img_time =  findViewById(R.id.img_time);
        img_idel =  findViewById(R.id.img_idel);
        line99 =  findViewById(R.id.line99);

        veh_control =  findViewById(R.id.veh_control);
        veh_control.setOnClickListener(this);
        slideView_d = findViewById(R.id.v_details);
        slideView_c =  findViewById(R.id.v_control);
        slideView =  findViewById(R.id.main);
        address_vehicles=findViewById(R.id.address_vehicles);
        address_vehicles.setOnClickListener(this);
        txt_car_name=findViewById(R.id.txt_car_name);
        txt_driver_name=findViewById(R.id.txt_driver_name);
        txt_reason=findViewById(R.id.txt_reason);
        txt_phone=findViewById(R.id.txt_phone);
        txt_las_location=findViewById(R.id.txt_las_location);
        txt_last_update=findViewById(R.id.txt_last_update);
        txt_odometer=findViewById(R.id.txt_odometer);
        txt_version=findViewById(R.id.txt_version);
        text_car=findViewById(R.id.text_car);
        text_car2=findViewById(R.id.text_car2);
        but_status=findViewById(R.id.but_status);
        but_status.setOnClickListener(this);
        but_control=findViewById(R.id.but_control);
        but_control.setOnClickListener(this);
        but_meter=findViewById(R.id.but_meter);
        but_meter.setOnClickListener(this);
        cont=findViewById(R.id.vehicle_control);
        stat=findViewById(R.id.vehicle_status);
        odom=findViewById(R.id.vehicle_odometer);
        dim =  findViewById(R.id.dim);
        dim2 =  findViewById(R.id.dim2);
        slideUp_d = new Slideup(slideView_d);
        slideUp_c = new Slideup(slideView_c);
        slideUp_d.hideImmediately();
        slideUp_c.hideImmediately();
        list.add(new Vehicle_Details(getResources().getString(R.string.Vehicle_Battery), "1", "12.69"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Internal_Battery), "2", "94%"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Key), "3", "OFF"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Immobilizer), "4", "Not Active"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Direction), "5", "229 Southwest"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Hight), "6", "880 M"));
        list.add(new Vehicle_Details(getResources().getString(R.string.Emergency), "7", "OFF"));
        listView =  findViewById(R.id.listView);
        adapter = new CustomAdapterDetails(this, list);
        spinner =  findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item);
        dataAdapter.add(getResources().getString(R.string.Vehicle_off));
        dataAdapter.add(getResources().getString(R.string.Vehicle_on));
        dataAdapter.add(getResources().getString(R.string.Door_close));
        dataAdapter.add(getResources().getString(R.string.Door_open));

        spinner.setAdapter(dataAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String lang = prefs.getString(Preferences.language1, "");
//        if(lang.trim().equals("1")) {
//            locale=new Locale("en-US");
//        }else{
//            locale=new Locale("ar");
//        }
        name=prefs.getString(Preferences.USER_NAME,"");
        pass=prefs.getString(Preferences.PASSWORD,"");
        if (Utils.isOnline(getApplicationContext())) {
            Car_Info_List car_info_list = new Car_Info_List(name, pass, device_id);
            car_info_list.execute();
        }else{
            ALL.show_custom_msg(getApplicationContext(),Car_Info.this,getResources().getString(R.string.str12));
           }
        slideUp_d.setSlideListener(new Slideup.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim.setAlpha(1 - (v / 100));
                veh_details.setVisibility(View.GONE);
                veh_control.setVisibility(View.GONE);
            }

            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    veh_details.setVisibility(View.VISIBLE);
                    veh_control.setVisibility(View.VISIBLE);
                }
            }
        });
        slideUp_c.setSlideListener(new Slideup.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim2.setAlpha(1 - (v / 100));
                veh_details.setVisibility(View.GONE);
            }
            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    veh_details.setVisibility(View.VISIBLE);
                    veh_control.setVisibility(View.VISIBLE);
                }
            }
        });
        veh_control.setEnabled(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.veh_details||view.getId()==R.id.veh_details2){
            if(slideUp_d.isVisible()){
                slideUp_d.animateOut();
           }
            else {
                if(slideUp_c.isVisible()) {
                    slideUp_c.animateOut();
                }
                slideUp_d.animateIn();
                veh_details.setVisibility(View.GONE);
            }
        }
        if(view.getId()==R.id.veh_control){
            if(slideUp_c.isVisible()){
                slideUp_c.animateOut();
                 veh_details.setVisibility(View.VISIBLE);
            }else {
                if(slideUp_d.isVisible()) {
                    slideUp_d.animateOut();
                    veh_details.setVisibility(View.GONE);
                }
                else{
                    slideUp_c.animateIn();
                    veh_control.setVisibility(View.GONE);
                     }
                 }
            }
        if(view.getId()==R.id.address_vehicles){
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/place/"+lat+","+lon+"/@"+lat+","+lon+",20z/data=!3m1!1e3");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
               startActivity(mapIntent);
            }
        }
        if(view.getId()==R.id.but_status){send("1","0");}
        if(view.getId()==R.id.but_control){
            int p=spinner.getSelectedItemPosition();
            if(p==0){dialog("3","0");}
            else if(p==1){ send("2","0");}
            else if(p==2){send("6","0");}
            else if(p==3){send("7","0");}
           }
        if(view.getId()==R.id.but_meter) {
            EditText edit_km =  findViewById(R.id.edit_km);
            String km_value = edit_km.getText().toString();
            if (km_value.length() == 0) {
                Toast.makeText(Car_Info.this, "Please enter value", Toast.LENGTH_SHORT).show();
            } else {
                send("8", km_value);
            }
        }
        if(view.getId()==R.id.veh_details2){
            slideUp_d.animateOut();
        }
        if(view.getId()==R.id.text_veh){
            slideUp_c.animateOut();
        }
     }
     public void send_command(final String type, final String value){
         AlertDialog.Builder alertDialog = new AlertDialog.Builder(Car_Info.this);
         alertDialog.setTitle(getResources().getString(R.string.str33));
         alertDialog.setCancelable(false);
         alertDialog.setMessage(getResources().getString(R.string.str34));
         alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 if(Utils.isOnline(getApplicationContext())) {
                     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                     String name = prefs.getString(Preferences.USER_NAME, "");
                     String pass = prefs.getString(Preferences.PASSWORD, "");
                     Vehicle_control vehicle_control = new Vehicle_control(name, pass, type, value,device_id);
                     vehicle_control.execute();
                 }else{
                     ALL.show_custom_msg(getApplicationContext(),Car_Info.this,getResources().getString(R.string.str12));

                 }
             }
         });

         alertDialog.setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {

             }
         });
         alertDialog.show();
     }
    @Override
    protected void onDestroy() {
        if (butt_clic.contains("1")) {
            Vehicles vehicles = Vehicles.getInstance();
            vehicles.show_map("V1");
        }
        super.onDestroy();
    }
    private class Car_Info_List extends AsyncTask<Object, Object, List<Vehicle_Details>> {
        private String _userName;
        private String _password,_vehicle_id;
        List<Vehicle_Details> list_info=new ArrayList<Vehicle_Details>();;
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        LinearLayout info_page=(LinearLayout) findViewById(R.id.main);
        public Car_Info_List(String userName, String password,String vehicle_id) {
            _userName = userName;
            _password = password;
            _vehicle_id=vehicle_id;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            info_page.setVisibility(View.GONE);
            veh_details.setEnabled(false);
            veh_control.setEnabled(false);
        }
        @Override
        protected List<Vehicle_Details> doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url+"/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "vehicle_info"));
                nameValuePairs.add(new BasicNameValuePair("vehicle_id", _vehicle_id));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("vehicle_info");
                        for(int i=0; i < v.length(); i++){
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            stop_time.setText(jsonObject2.optString("stop_time1"));
                            ide_time.setText(jsonObject2.optString("idelling_time"));
                            txt_car_name.setText(jsonObject2.optString("name_vehicle"));
                            text_car.setText(jsonObject2.optString("name_vehicle"));
                            text_car2.setText(jsonObject2.optString("name_vehicle"));
                            txt_driver_name.setText(jsonObject2.optString("driver_name"));
                            txt_reason.setText(jsonObject2.optString("reason"));
                            txt_phone.setText(jsonObject2.optString("driver_phone"));
                            txt_las_location.setText(jsonObject2.optString("last_location"));
                            txt_last_update.setText(jsonObject2.optString("last_update"));
                            txt_odometer.setText(jsonObject2.optString("odometer"));
                            txt_version.setText(jsonObject2.optString("version"));
                            address_vehicles.setText(jsonObject2.optString("address"));
                            vehicle_control=jsonObject2.optString("vehicle_control");
                            vehicle_status=jsonObject2.optString("vehicle_status");
                            vehicle_odometer=jsonObject2.optString("vehicle_odometer");
                            lon=jsonObject2.optString("lon");
                            lat=jsonObject2.optString("lat");
                            speed=jsonObject2.optString("speed");
                            if(jsonObject2.optString("idelling_time").equals("0") && jsonObject2.optString("stop_time1").equals("0")){
                                tab_ideling.setVisibility(View.GONE);
                            }
                            if(jsonObject2.optString("idelling_time").equals("0")){ide_time.setVisibility(View.GONE);img_idel.setVisibility(View.GONE);line99.setVisibility(View.GONE);}
                            if(jsonObject2.optString("stop_time1").equals("0")){stop_time.setVisibility(View.GONE);img_time.setVisibility(View.GONE);line99.setVisibility(View.GONE);}
                            JSONArray array2 = new JSONArray(v.getJSONObject(i).getString("vehicle_details"));
                            for(int j=0; j < array2.length(); j++) {
                                JSONObject jsonObject3 = array2.getJSONObject(j);
                                list_info.add(new Vehicle_Details(jsonObject3.optString("title"), jsonObject3.optString("value"), jsonObject3.optString("type")));
                            }
                        }
                        return list_info;
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
            return null;
        }
        @Override
        protected void onPostExecute(List<Vehicle_Details> result) {
          if(result!=null) {
              if (!result.isEmpty()) {
                  show_details_info(result);
                }
           }
            linlaHeaderProgress.setVisibility(View.GONE);
            info_page.setVisibility(View.VISIBLE);
            veh_details.setEnabled(true);
            show_hid_control();
        }
    }
   public void show_hid_control(){
       try{
       if(vehicle_control.equals("0") && vehicle_status.equals("0") && vehicle_odometer.equals("0") ) {
           veh_control.setEnabled(false);
       }else{
           veh_control.setEnabled(true);
           if(vehicle_control.equals("1")){cont.setVisibility(View.VISIBLE);}else{cont.setVisibility(View.GONE);}
           if(vehicle_status.equals("1")){stat.setVisibility(View.VISIBLE);}else{stat.setVisibility(View.GONE);}
           if(vehicle_odometer.equals("1")){odom.setVisibility(View.VISIBLE);}else{odom.setVisibility(View.GONE);}
       }}
       catch (Exception e){}
   }
    public void show_details_info(List<Vehicle_Details> result){
        adapter=new CustomAdapterDetails(this,result);
        listView.setAdapter(adapter);
    }
    private class Vehicle_control extends AsyncTask<Object, Object, String> {
        private String _userName;
        private String _password,_type,_value,_vehicle_id;
        private ProgressDialog _progress;
        public Vehicle_control(String userName, String password,String type,String value,String vehicle_id) {
            _userName = userName;
            _password = password;
            _vehicle_id=vehicle_id;
            _type=type;
            _value=value;
            veh_details.setEnabled(false);
            veh_control.setEnabled(false);
            _progress = new ProgressDialog(Car_Info.this);
            _progress.setMessage("Loading ...");
            _progress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            _progress.show();
            _progress.setCancelable(false);
        }
        @Override
        protected String doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url+"/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(6);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "control"));
                nameValuePairs.add(new BasicNameValuePair("control_type", _type));
                nameValuePairs.add(new BasicNameValuePair("control_value", _value));
                nameValuePairs.add(new BasicNameValuePair("vehicle_id",_vehicle_id ));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        String value;
                        if(resString.contains("0")){value="0";}
                        else{value="1";}
                        return value;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if(result!=null) {
                if (!result.isEmpty()) {
                     if(result.equals("1")){
                         Toast.makeText(Car_Info.this,"Transmission successful",Toast.LENGTH_SHORT).show();
                         Intent intent=new Intent(Car_Info.this,Car_Info.class);
                         intent.putExtra("device_id",_vehicle_id);
                         startActivity(intent);
                     }
                    else{Toast.makeText(Car_Info.this,"Transmission failed",Toast.LENGTH_SHORT).show();}
                }
            }
            else{Toast.makeText(Car_Info.this,"Transmission failed",Toast.LENGTH_SHORT).show(); }
            _progress.dismiss();
            veh_details.setEnabled(true);
            veh_control.setEnabled(true);

        }
    }


    public class WebAppInterface {


        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public double getValue() {
            return Double.parseDouble(speed);
        }
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // refresh your views here
//        Locale.setDefault(locale);
//
//
//        Configuration config = new Configuration();
//        config.locale = locale;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);
//        super.onConfigurationChanged(newConfig);
//    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void dialog(final String type, final String value){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("");
        alert.setMessage(getResources().getString(R.string.str_pass2));
        final EditText input = new EditText(this);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String srt = input.getEditableText().toString().trim();
                if(srt.equals(pass)){
                    send(type,value);
                }
                else {
                    dialog(type,value);
                    ALL.show_custom_msg(getApplicationContext(),Car_Info.this,getResources().getString(R.string.str13));

                }
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
    public void send(final String type, final String value){
        if(Utils.isOnline(getApplicationContext())) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String name = prefs.getString(Preferences.USER_NAME, "");
            String pass = prefs.getString(Preferences.PASSWORD, "");
            Vehicle_control vehicle_control = new Vehicle_control(name, pass, type, value,device_id);
            vehicle_control.execute();
        }else{
            ALL.show_custom_msg(getApplicationContext(),Car_Info.this,getResources().getString(R.string.str12));

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale=new Locale("en-US");
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang = prefs.getString(Preferences.language1, "");
        if(lang.trim().equals("1")){
            newLocale=new Locale("en-US");

        }else if(lang.trim().equals("0")){
            newLocale=new Locale("ar");
        }
        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

}
