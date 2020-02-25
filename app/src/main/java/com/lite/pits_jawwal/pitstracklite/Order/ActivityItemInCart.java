package com.lite.pits_jawwal.pitstracklite.Order;

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
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Config;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Utils;

import java.util.ArrayList;


public class ActivityItemInCart extends AppCompatActivity implements View.OnClickListener ,AsyncResponseItemOrder{
    private SharedPreferences pref;
    private String name,password;
    private ListView listview_item;
    private TextView custom_name,txt_date,total_price;
    private CartValues cart;
    private View wait;
    private listviewItemAdapter listItem;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        setContentView(R.layout.activity_show_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order);
        declartation();

    }
    private void declartation() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        name=pref.getString(Preferences.USER_NAME,"");
        password=pref.getString(Preferences.PASSWORD,"");
        listview_item=findViewById(R.id.listview_item);
        View footerView =  ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.order_footer, null, false);
        listview_item.addFooterView(footerView);

        custom_name=findViewById(R.id.custom_name);
        total_price=footerView.findViewById(R.id.total_price);
        txt_date=findViewById(R.id.txt_date);
        cart= (CartValues) getIntent().getSerializableExtra("cart");
        custom_name.setText(cart.getGeozoneID());
        txt_date.setText(cart.getCreatetime());
        wait = findViewById(R.id.wait);
        setWaitValue(View.GONE);
        getItemorder(cart.getRId());
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
    public void onClick(View v) {

    }
    private  void getItemorder(String rfid){
        if(Utils.isOnline(this)) {
            ItemConfirmCartPhp item = new ItemConfirmCartPhp(name,password, this,this,rfid);
            item.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            setWaitValue(View.VISIBLE);
        }else{
            ALL.show_custom_msg(this, ActivityItemInCart.this, getResources().getString(R.string.str17));
        }

    }

    @Override
    public void processFinishCart(ArrayList<ItemOrderValue> listValues) {
        setWaitValue(View.GONE);
        if(listValues!=null){
            listItem=new listviewItemAdapter(ActivityItemInCart.this,listValues,this);
            listview_item.setAdapter(listItem);
            total_price.setText(cart.getTotalPrice());
        }

    }
    public void setWaitValue(int waitValue){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
        animation.setDuration(100);
        wait.startAnimation(animation);
        wait.setVisibility(waitValue);
    }
}
