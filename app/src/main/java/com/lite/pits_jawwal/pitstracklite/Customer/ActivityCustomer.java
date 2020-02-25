package com.lite.pits_jawwal.pitstracklite.Customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Config;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.ContextWrapper;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.RelatedReport.AsyncResponseRelated;
import com.lite.pits_jawwal.pitstracklite.RelatedReport.Report_List;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Customadapter_workhours;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;
import com.lite.pits_jawwal.pitstracklite.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityCustomer extends AppCompatActivity implements AsyncResponseCustomer, View.OnClickListener
, SearchView.OnQueryTextListener, AsyncResponseRelated {
    private SharedPreferences preference;
    private PlaceListAdapter adapter2;
    private String name,pass;
    private ListView list_report;
    private LinearLayout linlaHeaderProgress;
    private TextView txt;
    private SearchView editsearch;
    public String geoid = "1";
    public String lon = "1";
    public String lat = "1";
    private RelativeLayout loadmore;
    private Report_List report_list = null;
    private ArrayList<PlaceData> result2 = new ArrayList<>();
    public ArrayList<Work_Report_Value> listhistory = new ArrayList<>();
    public int start_index = 0;
    public int quantity = 10;
    private static Customadapter_workhours customadapterMyreport;
    private String customId,offlineloc_id;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setTitle(getResources().getString(R.string.Relatedeport));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declaration();
    }
    private void declaration() {
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        name=preference.getString(Preferences.USER_NAME,"");
        pass=preference.getString(Preferences.PASSWORD,"");
        list_report=findViewById(R.id.list_report);
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        loadmore = footerView.findViewById(R.id.loadmore);
        loadmore.setOnClickListener(this);
        list_report.addFooterView(footerView);
        linlaHeaderProgress=findViewById(R.id.linlaHeaderProgress);


        txt = findViewById(R.id.txt);
        txt.setOnClickListener(this);
//        String lang = preference.getString(Preferences.language1, "");
//        Locale locale;
//        if (lang != null) {
//            if (lang.trim().equals("1")) {
//                locale = new Locale("en-US");
//            } else {
//                locale = new Locale("ar");
//            }
//
//            Locale.setDefault(locale);
//            Configuration config = new Configuration();
//            config.locale = locale;
//            this.getApplicationContext().getResources().updateConfiguration(config, null);
//        }
        getCustomer();
    }
    private void getCustomer(){
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        if(Utils.isOnline(this)){
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            GeofenceList geofenceList=new GeofenceList(name,pass,this,this,"0","1");
            geofenceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }else{
            ALL.show_custom_msg(getApplicationContext(), ActivityCustomer.this, getResources().getString(R.string.str12));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt) {
            Show_();

        }
        else if (view.getId() == R.id.loadmore) {
            if (listhistory != null) {
                start_index = listhistory.size();
                quantity = 100;
                get_report();
            }
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
                geoid = adapter2.getiid(position);
                customId=geoid;
                offlineloc_id=adapter2.getofflineloc_id(position);
                listhistory=new ArrayList<>();
                lon = adapter2.getlon(position);
                lat = adapter2.getlat(position);
                txt.setText(adapter2.getname(position));
                adapter2.filter("");
                alertDialog.dismiss();
                get_report();
            }
        });
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#7D8A92"));
        nbutton.setAllCaps(false);
        pbutton.setAllCaps(false);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter2.filter(newText);return false;
    }
    public void get_report() {
        if (Utils.isOnline(this)) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            String name = preference.getString(Preferences.USER_NAME, "");
            String pass = preference.getString(Preferences.PASSWORD, "");
            report_list = new Report_List(name, pass, customId,String.valueOf(start_index),String.valueOf(quantity),this,this,offlineloc_id);
            report_list.execute();
        } else {
            ALL.show_custom_msg(this, ActivityCustomer.this, getResources().getString(R.string.str17));

        }
    }
    @Override
    public void onStop() {
        if (report_list != null && report_list.getStatus() == AsyncTask.Status.RUNNING) {
            report_list.cancel(true);
        }
        super.onStop();
    }

    @Override
    public void processFinish2(ArrayList<Work_Report_Value> result) {
        try {
            if (result != null) {
                if (result.size() > 0) {
                } else {
                    ALL.show_custom_msg(getApplicationContext(), ActivityCustomer.this, getResources().getString(R.string.morereport));
                }
                listhistory.addAll(result);
                customadapterMyreport = new Customadapter_workhours(getApplicationContext(), listhistory,true);
                list_report.setVisibility(View.VISIBLE);
                list_report.setAdapter(customadapterMyreport);
                list_report.refreshDrawableState();
            } else {
                ALL.show_custom_msg(getApplicationContext(), ActivityCustomer.this, getResources().getString(R.string.morereport));
            }
        } catch (Exception e) {
        }
        linlaHeaderProgress.setVisibility(View.GONE);
    }

    @Override
    public void processFinish(ArrayList<PlaceData> result) {
        result2 = result;
        linlaHeaderProgress.setVisibility(View.GONE);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale = new Locale("en-US");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang = prefs.getString(Preferences.language1, "");
        if (lang != null) {
            if (lang.trim().equals("1")) {
                newLocale = new Locale("en-US");

            } else if (lang.trim().equals("0")) {
                newLocale = new Locale("ar");
            }
        }
        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }
}
