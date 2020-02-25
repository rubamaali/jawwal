package com.lite.pits_jawwal.PersonalHistoryReport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.ALL;
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


public class HisroryReport extends AppCompatActivity implements View.OnClickListener, AsyncResponseRelated {
    private String customName, customId, offlinLoc_id;
    private TextView txt_custom_name;
    private static Customadapter_workhours customadapterMyreport;
    private ListView list_report;
    private SharedPreferences preference;
    private Report_List report_list = null;
    private RelativeLayout loadmore;
    public int start_index = 0;
    public int quantity = 10;
    public ArrayList<Work_Report_Value> listhistory = new ArrayList<>();
    private Locale locale;
    private LinearLayout linlaHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.Relatedeport));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.historyreport);
        declaration();
    }

    private void declaration() {
        customName = getIntent().getStringExtra("geof_name");
        customId = getIntent().getStringExtra("geof_id");
        offlinLoc_id = getIntent().getStringExtra("offlinLoc_id");
        txt_custom_name = findViewById(R.id.txt_custom_name);
        txt_custom_name.setText(customName);
        list_report = findViewById(R.id.list_report);
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        loadmore = footerView.findViewById(R.id.loadmore);
        loadmore.setOnClickListener(this);
        list_report.addFooterView(footerView);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        get_report();
        final String lang = preference.getString(Preferences.language1, "");
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

    public void get_report() {
        if (Utils.isOnline(this)) {
            String name = preference.getString(Preferences.USER_NAME, "");
            String pass = preference.getString(Preferences.PASSWORD, "");
            report_list = new Report_List(name, pass, customId, String.valueOf(start_index), String.valueOf(quantity), this, this, offlinLoc_id);
            report_list.execute();
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        } else {
            ALL.show_custom_msg(this, HisroryReport.this, getResources().getString(R.string.str17));

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loadmore) {
            if (listhistory != null) {
                start_index = listhistory.size();
                quantity = 100;
                get_report();
            }
        }
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
    @Override
    public void processFinish2(ArrayList<Work_Report_Value> result) {
        try {
            if (result != null) {
                if (result.size() > 0) {
                    listhistory.addAll(result);
                    customadapterMyreport = new Customadapter_workhours(getApplicationContext(), listhistory, true);
                    list_report.setVisibility(View.VISIBLE);
                    list_report.setAdapter(customadapterMyreport);
                    list_report.refreshDrawableState();
                } else {
                    ALL.show_custom_msg(getApplicationContext(), HisroryReport.this, getResources().getString(R.string.morereport));

                }
            } else {
                ALL.show_custom_msg(getApplicationContext(), HisroryReport.this, getResources().getString(R.string.morereport));
            }
        } catch (Exception e) {
        }
        linlaHeaderProgress.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        if (report_list != null && report_list.getStatus() == AsyncTask.Status.RUNNING) {
            report_list.cancel(true);
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
}
