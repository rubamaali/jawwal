package com.lite.pits_jawwal.pitstracklite.Order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Utils;

import java.util.ArrayList;

public class ActivityOrderHistory extends AppCompatActivity implements AsyncResponseCart, View.OnClickListener {
    private View view,wait;
    private static TextView txt_customer;
    private SharedPreferences preference;
    private  String name,pass,customerId,customName,customId;
    private ConfirmCartPhp confirmCartPhp;
    private ListCartAdapter listCartAdapter;
    public int start_index = 0;
    private RelativeLayout loadmore;
    private ListView list_report;
    private ArrayList<CartValues> listhistory = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_order_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order);
        declartation();

    }
    private void declartation() {
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        name = preference.getString(Preferences.USER_NAME, "");
        pass = preference.getString(Preferences.PASSWORD, "");
        wait = findViewById(R.id.wait);
        setWaitValue(View.GONE);
        list_report =findViewById(R.id.list_report);
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        loadmore = footerView.findViewById(R.id.loadmore);
        loadmore.setOnClickListener(this);
        list_report.addFooterView(footerView);
        customName = getIntent().getStringExtra("geof_name");
        customId = getIntent().getStringExtra("geof_id");
        txt_customer=findViewById(R.id.txt_customer);
        txt_customer=findViewById(R.id.txt_customer);
        txt_customer.setText(customName);
        get_action();

    }
    public void setWaitValue(int waitValue){
        try {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
            animation.setDuration(100);
            wait.startAnimation(animation);
            wait.setVisibility(waitValue);
        }catch (Exception e){}
    }
    private void get_action() {
        if(Utils.isOnline(this)) {
            confirmCartPhp=new ConfirmCartPhp( name,  pass,  this,this,String.valueOf(start_index),customId,"customer");
            confirmCartPhp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            setWaitValue(View.VISIBLE);
        }else{
            ALL.show_custom_msg(this, ActivityOrderHistory.this, getResources().getString(R.string.str17));
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loadmore) {
            if (listhistory != null) {
                start_index = listhistory.size();
                get_action();
            }
        }
    }
    @Override
    public void processFinishCart(ArrayList<CartValues> cartValue) {
        setWaitValue(View.GONE);
        if(cartValue!=null){
            if (cartValue.size() > 0) {
                listhistory.addAll(cartValue);
            }
            setCartList(listhistory);
        }
    }
    private void setCartList(ArrayList<CartValues> cartValue) {
        listCartAdapter=new ListCartAdapter(this,cartValue,"show");
        list_report.setAdapter(listCartAdapter);
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
}
