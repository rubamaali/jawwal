package com.lite.pits_jawwal.pitstracklite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.Customer.AsyncResponseCustomer;
import com.lite.pits_jawwal.pitstracklite.Customer.GeofenceList;
import com.lite.pits_jawwal.pitstracklite.Customer.PlaceListAdapter;
import com.lite.pits_jawwal.pitstracklite.Type.CustomLocationType;
import com.lite.pits_jawwal.pitstracklite.Type.Location_type;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;


/**
 * Created by Ruba-PITS on 10/23/2017.
 */

public class Home extends AppCompatActivity implements View.OnClickListener,
        SearchView.OnQueryTextListener , AsyncResponseCustomer {
    private Button send_msg;
    private EditText msg_txt;
    private String  datee;
    private RelativeLayout container_;
    private String name, pass;
    private Locale locale;
    private SharedPreferences prefs;
    private View view;
    private SearchView editsearch;
    private String devise_id;
    public String geoid = "1";
    public String lon = "1";
    public String lat = "1";
    public String t_name = "";
    public String reportid = "";
    public CustomLocationType adapter;
    private PlaceListAdapter adapter2;
    private TextView txt_date, txt, user_name;
    long tasck_time = 0L;
    private Spinner type_report;
    private LinearLayout linlaHeaderProgress;
    ArrayList<PlaceData> result2 = new ArrayList<>();

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        setContentView(R.layout.home_report);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.str35));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declaration();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (Utils.isOnline(this)) {
            GeofenceList geo = new GeofenceList(name, pass,this,this,"0","1");
            geo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            List_report list = new List_report();
            list.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            ALL.show_custom_msg(getApplicationContext(), Home.this, getResources().getString(R.string.str12));
        }

    }
    Locale local = new Locale("en-US");
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
    private void declaration() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");
        linlaHeaderProgress = findViewById(R.id.linlaHeaderProgress2);
        container_ = findViewById(R.id.container);
        container_.setOnClickListener(this);
        send_msg = findViewById(R.id.send);
        send_msg.setOnClickListener(this);
        msg_txt = findViewById(R.id.message_txt);
        devise_id = getIntent().getStringExtra("id");
        String _name = getIntent().getStringExtra("user_name");
        Log.d(TAG, "declaration: " + devise_id);
        user_name = findViewById(R.id.user_name);
        user_name.setText(getResources().getString(R.string.str38) + " " + _name);
        txt_date = findViewById(R.id.txt_date);
        txt_date.setOnClickListener(this);
        txt = findViewById(R.id.txt);
        txt.setOnClickListener(this);
        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);
        String newDateStr = postFormater.format(new Date());
        txt_date.setText(newDateStr);
        tasck_time = (Calendar.getInstance().getTimeInMillis() / 1000);
        type_report =  findViewById(R.id.type_report);
        type_report.setAdapter(adapter);
        type_report.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reportid = adapter.getid(position);
                t_name = adapter.getname(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String lang = prefs.getString(Preferences.language1, "");
        Locale locale;
        if (lang.trim().equals("1")) {
            locale = new Locale("en-US");
        } else {
            locale = new Locale("ar");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    private void setdate(final Calendar calendar) {
        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);

        txt_date = findViewById(R.id.txt_date);
        txt_date.setText(postFormater.format(calendar.getTime()));
        tasck_time = (calendar.getTimeInMillis() / 1000);
    }

    private void Show_celender() {
        android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view2 = factory.inflate(R.layout.activity_date_picker_time_picker, null);
        final DatePicker datePicker =  view2.findViewById(R.id.datePickerExample);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        final TimePicker timePicker =  view2.findViewById(R.id.timePickerExample);
        alertadd.setCancelable(false);
        alertadd.setView(view2);
        alertadd.setNeutralButton("الغاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertadd.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                setdate(calendar);

            }
        });
        final android.support.v7.app.AlertDialog alertDialog = alertadd.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        datee = txt_date.getText().toString();
        if (view.getId() == R.id.txt_date) {
            Calendar c = Calendar.getInstance();
            if (datee == "") {
                Show_celender();
            } else {
                Show_celender();
            }
        }
        if (view.getId() == R.id.send) {
            if (!geoid.equals("1")) {
                String msg = msg_txt.getText().toString();
                if (msg.length() > 0) {
                    datee = txt_date.getText().toString();
                    Locale local = new Locale("en-US");

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date2 = null;
                    try {
                        date2 = formatter.parse(datee);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long time = date2.getTime() / 1000;
                    String tt = String.valueOf(time);
                    if (Utils.isOnline(getApplicationContext())) {
                        try {
                            send_task send_t = new send_task(msg, String.valueOf(tasck_time));
                            send_t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (Exception e) {
                        }
                    } else {
                        ALL.show_custom_msg(getApplicationContext(), Home.this, getResources().getString(R.string.str12));
                    }
                }
            }else{
                ALL.show_custom_msg(getApplicationContext(), Home.this, getResources().getString(R.string.customernameerror));
            }
        }
        else if (view.getId() == R.id.txt) {
            Show_();

        }
    }

    public void Show_() {
        final android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view2 = factory.inflate(R.layout.geof, null);
        final ListView listvehi =  view2.findViewById(R.id.list_type);

        editsearch =  view2.findViewById(R.id.searchView1);
        editsearch.setOnQueryTextListener(this);
        editsearch.setOnClickListener(this);
        if(result2!=null) {
            adapter2 = new PlaceListAdapter(this, result2, 0);
            listvehi.setAdapter(adapter2);
        }
        alertadd.setView(view2);
        alertadd.setNeutralButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if(adapter2!=null) {
                    adapter2.filter("");
                }

            }
        });
        final android.support.v7.app.AlertDialog alertDialog = alertadd.create();
        alertDialog.show();
        listvehi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(adapter2!=null) {
                    geoid = adapter2.getiid(position);
                    lon = adapter2.getlon(position);
                    lat = adapter2.getlat(position);
                    txt.setText(adapter2.getname(position));
                    adapter2.filter("");
                    alertDialog.dismiss();
                }
            }
        });
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#7D8A92"));
        nbutton.setAllCaps(false);
        pbutton.setAllCaps(false);

    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter2.filter(s);
        return false;
    }

    @Override
    public void processFinish(ArrayList<PlaceData> result) {
        result2 = result;
        linlaHeaderProgress.setVisibility(View.GONE);
    }

    private class List_report extends AsyncTask<Object, Object, List<Location_type>> {
        ArrayList<PlaceData> list = new ArrayList<PlaceData>();
        private SharedPreferences prefs;
        LinearLayout linlaHeaderProgress =  findViewById(R.id.linlaHeaderProgress);
        List<Location_type> type_loc;

        public List_report() {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            type_loc = new ArrayList<>();

        }

        @Override
        protected List<Location_type> doInBackground(Object... data) {
            try {
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("USER", prefs.getString(Preferences.USER_NAME, "")));
                nameValuePairs.add(new BasicNameValuePair("PASS", prefs.getString(Preferences.PASSWORD, "")));
                nameValuePairs.add(new BasicNameValuePair("page", "task_type"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                list.clear();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("task_type");

                        for (int i = 0; i < v.length(); i++) {
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            type_loc.add(new Location_type(jsonObject2.optString("name"), jsonObject2.optString("id")));
                        }
                        return type_loc;
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
        protected void onPostExecute(List<Location_type> result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if(result!=null) {
                show_report_type(result);
            }
        }
    }

    public void show_report_type(List<Location_type> result) {
        type_report =  findViewById(R.id.type_report);
        adapter = new CustomLocationType(getBaseContext(), result);
        type_report.setAdapter(adapter);
    }



    private class send_task extends AsyncTask<Object, Object, String> {
        ArrayList<PlaceData> list = new ArrayList<PlaceData>();
        private SharedPreferences prefs;
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        List<Location_type> type_loc;
        String _msg, _time;

        public send_task(String msg, String time) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            type_loc = new ArrayList<>();
            _msg = msg;
            _time = time;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... data) {
            try {
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(11);
                nameValuePairs.add(new BasicNameValuePair("USER", prefs.getString(Preferences.USER_NAME, "")));
                nameValuePairs.add(new BasicNameValuePair("PASS", prefs.getString(Preferences.PASSWORD, "")));
                nameValuePairs.add(new BasicNameValuePair("page", "task_send"));
                nameValuePairs.add(new BasicNameValuePair("note", _msg));
                nameValuePairs.add(new BasicNameValuePair("iid", devise_id));
                nameValuePairs.add(new BasicNameValuePair("task_id", reportid));
                nameValuePairs.add(new BasicNameValuePair("geoid", geoid));
                nameValuePairs.add(new BasicNameValuePair("timestamp_task", _time));
                nameValuePairs.add(new BasicNameValuePair("task_name", t_name));
                nameValuePairs.add(new BasicNameValuePair("lon", lon));
                nameValuePairs.add(new BasicNameValuePair("lat", lat));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                list.clear();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    return resString;
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result != null && result.contains("1")) {
                if (result.contains("1")) {
                    ALL.show_custom_msg(getApplicationContext(), Home.this, getResources().getString(R.string.str27));
                    msg_txt.setText("");
                } else {
                    ALL.show_custom_msg(getApplicationContext(), Home.this, getResources().getString(R.string.str36));
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale = new Locale("en-US");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang = prefs.getString(Preferences.language1, "");
        if (lang.trim().equals("1")) {
            newLocale = new Locale("en-US");

        } else if (lang.trim().equals("0")) {
            newLocale = new Locale("ar");
        }
        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

}
