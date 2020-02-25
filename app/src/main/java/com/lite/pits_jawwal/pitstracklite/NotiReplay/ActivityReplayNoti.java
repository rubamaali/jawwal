package com.lite.pits_jawwal.pitstracklite.NotiReplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lite.pits_jawwal.pitstracklite.Config;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.ContextWrapper;
import com.lite.pits_jawwal.pitstracklite.Event;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Utils;

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



public class ActivityReplayNoti extends AppCompatActivity implements View.OnClickListener  {
    String totalRelay;
    private ReplayNoti replayNoti = null;
    private SharedPreferences preference;
    public int start_index = 0;
    private CustomadapterNotiReplay customadapterMyreplay;
    private RelativeLayout loadmore;
    private ListView list_replay;
    private Locale locale;
    private String lastStringSer;
    private int lastPlaceFilterIndex;
    ArrayList<noti_value> notiValues = new ArrayList<>();
    private LinearLayout task,report,replay,attachment;
    private boolean Stask,Sreport,Sreplay,Sattachment;
    private static ActivityReplayNoti instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.allreplies));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.replay_noti);
        declaration();
    }
    public static ActivityReplayNoti getInstance(){
        return instance;
    }
    public void minEventCount(){
        Event event=Event.getInstance();
        event.removeone();
    }
    private void declaration(){
        instance=this;
        lastStringSer="";
        Stask=false;
        Sreport=false;
        Sreplay=false;
        Sattachment=false;
        list_replay =findViewById(R.id.list_replay);
        report =findViewById(R.id.report);
        report.setOnClickListener(this);
        replay =findViewById(R.id.replay);
        replay.setOnClickListener(this);
        task =findViewById(R.id.task);
        task.setOnClickListener(this);
        attachment =findViewById(R.id.attachment);
        attachment.setOnClickListener(this);
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        loadmore = footerView.findViewById(R.id.loadmore);
        loadmore.setOnClickListener(this);
        list_replay.addFooterView(footerView);
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        get_replay();
//        final String lang = preference.getString(Preferences.language1, "");
//        if (lang.trim().equals("1")) {
//            locale = new Locale("en-US");
//        } else {
//            locale = new Locale("ar");
//        }
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }
    public void get_replay() {
        if (Utils.isOnline(this)) {
            String name = preference.getString(Preferences.USER_NAME, "");
            String pass = preference.getString(Preferences.PASSWORD, "");
            replayNoti = new ReplayNoti(name, pass,String.valueOf(start_index));
            replayNoti.execute();
        } else {
            ALL.show_custom_msg(this, this, getResources().getString(R.string.str17));

        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loadmore) {
            if (notiValues != null) {
                start_index = lastPlaceFilterIndex;
                get_replay();
            }
        }
        else if (view.getId() == R.id.task) {
           if(Stask){
               Stask=false;
               task.setBackgroundResource(R.drawable.line);
           }else{
               Stask=true;
               task.setBackgroundColor(Color.parseColor("#D5D5D5"));
           }
            setfilter2();
        } else if (view.getId() == R.id.report) {
            if(Sreport){
                Sreport=false;
                report.setBackgroundResource(R.drawable.line);
            }else{
                Sreport=true;
                report.setBackgroundColor(Color.parseColor("#D5D5D5"));
            }
            setfilter2();
        }else if (view.getId() == R.id.replay) {
            if(Sreplay){
                Sreplay=false;
                replay.setBackgroundResource(R.drawable.line);
            }else{
                Sreplay=true;
                replay.setBackgroundColor(Color.parseColor("#D5D5D5"));
            }
            setfilter2();
        }else if (view.getId() == R.id.attachment) {
            if(Sattachment){
                Sattachment=false;
                attachment.setBackgroundResource(R.drawable.line);
            }else{
                Sattachment=true;
                attachment.setBackgroundColor(Color.parseColor("#D5D5D5"));
            }
            setfilter2();
        }
    }
    private void setfilter2(){
    if(customadapterMyreplay!=null){
        customadapterMyreplay.filter(lastStringSer,Stask,Sreport,Sreplay,Sattachment);
    }
    }
    private class ReplayNoti extends AsyncTask<Object, Object, ArrayList<noti_value> > {
        private String _userName,_password,start;
        ArrayList<noti_value> list = new ArrayList<noti_value>();
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        public ReplayNoti(String userName, String password,String start) {
            _userName = userName;
            _password = password;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            this.start=start;
        }

        @Override
        protected ArrayList<noti_value>  doInBackground(Object... data) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = preference.getString(Preferences.URL, "");
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "noti_report"));
                nameValuePairs.add(new BasicNameValuePair("start", start));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                list.clear();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("noti_report");
                        for (int i = 0; i < v.length(); i++) {
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            list.add(new noti_value(jsonObject2.optString("reply"),
                                    jsonObject2.optString("createdby"),
                                    jsonObject2.optString("creationtime"), jsonObject2.optString("reportid"),
                                    jsonObject2.optString("commandtype"), jsonObject2.optString("replayid"),
                                    jsonObject2.optString("seen"),
                                    jsonObject2.optString("report_type"),
                                    jsonObject2.optString("offlineID"), jsonObject2.optString("type")));
                        }
                        totalRelay = jsonObject.optString("total");
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
        protected void onPostExecute(ArrayList<noti_value>  result) {
            try {
                if (result != null) {
                    if (list.size() > 0) {
                        int pos= notiValues.size();
                        notiValues.addAll(result);
                        customadapterMyreplay = new CustomadapterNotiReplay(ActivityReplayNoti.this, notiValues);
                        lastPlaceFilterIndex=notiValues.size();
                        list_replay.setVisibility(View.VISIBLE);
                        list_replay.setAdapter(customadapterMyreplay);
                        list_replay.refreshDrawableState();
                        scrollMyListViewToBottom(pos);
                        customadapterMyreplay.filter(lastStringSer,Stask,Sreport,Sreplay,Sattachment);
                    } else {
                        ALL.show_custom_msg(getApplicationContext(), ActivityReplayNoti.this, getResources().getString(R.string.morereplay));

                    }
                } else {
                    ALL.show_custom_msg(getApplicationContext(), ActivityReplayNoti.this, getResources().getString(R.string.morereplay));
                    list_replay.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
            }
            linlaHeaderProgress.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStop() {
        if (replayNoti != null && replayNoti.getStatus() == AsyncTask.Status.RUNNING) {
            replayNoti.cancel(true);
        }
        super.onStop();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
        super.onConfigurationChanged(newConfig);
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
    private void scrollMyListViewToBottom(int position) {
        list_replay.setSelectionFromTop(position - 1, 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (customadapterMyreplay != null) {
                    lastStringSer = s;
                    if (TextUtils.isEmpty(s)) {
                        customadapterMyreplay.filter("",Stask,Sreport,Sreplay,Sattachment);
                    }
                    else {
                    customadapterMyreplay.filter(s,Stask,Sreport,Sreplay,Sattachment);
                     }
                }
                return true;
            }
        });
        return true;
    }
}
