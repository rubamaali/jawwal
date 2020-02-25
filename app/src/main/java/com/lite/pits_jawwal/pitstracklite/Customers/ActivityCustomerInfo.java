package com.lite.pits_jawwal.pitstracklite.Customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Config;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.HorizontalFeilds;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsValue;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityCustomerInfo  extends Activity implements View.OnClickListener {
    private TextView txt_type,tags;
    private ListView listt;
    private LinearLayout select_type;
    private EditText msg_txt,phone,website,email,fax,customer_name,customer_mobile,account_number;
    private List<Location_type> type_loc;
    private Locale locale;
    private SharedPreferences prefs;
    private ImageView call,callcustomer,img_back,send_location;
    private PlaceData placeData_value;
    private HorizontalFeilds horizontalFeilds;
    private RecyclerView FieldsRecyclerView;
    ArrayList<FieldsValue> tagsList;
    private CardView without_loc;
    private String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.loction_info);
        declaration();
//        String lang = prefs.getString(Preferences.language1, "");
//        if(lang.trim().equals("1")) {
//            locale=new Locale("en-US");
//        }
//        else{locale=new Locale("ar");}
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }
    public void declaration(){
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        name=prefs.getString(Preferences.USER_NAME,"");
        send_location = findViewById(R.id.send_location);
        send_location.setOnClickListener(this);
        txt_type = (TextView) findViewById(R.id.txt_type);
        FieldsRecyclerView =  findViewById(R.id.FieldsRecyclerView);
        FieldsRecyclerView.setEnabled(false);
        FieldsRecyclerView.addItemDecoration(new DividerItemDecoration(ActivityCustomerInfo.this, LinearLayoutManager.HORIZONTAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            }
        });
        tagsList=new ArrayList<>();
        txt_type.setOnClickListener(this);
        tags = findViewById(R.id.tags);
        tags.setOnClickListener(this);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        ListView listt=(ListView)findViewById(R.id.list_type);
        listt.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        select_type = (LinearLayout) findViewById(R.id.select_type);
        select_type.setVisibility(View.GONE);
        msg_txt=(EditText)findViewById(R.id.msg_txt);
        phone=(EditText)findViewById(R.id.phone);
        call = (ImageView) findViewById(R.id.call);
        call.setOnClickListener(this);
        callcustomer = (ImageView) findViewById(R.id.callcustomer);
        callcustomer.setOnClickListener(this);
        website=(EditText)findViewById(R.id.website);
        email=(EditText)findViewById(R.id.email);
        fax=(EditText)findViewById(R.id.fax);
        customer_mobile=(EditText)findViewById(R.id.customer_mobile);
        account_number=(EditText)findViewById(R.id.account_number);
        customer_name=(EditText)findViewById(R.id.customer_name);
        without_loc = findViewById(R.id.without_loc);
        listt.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txt_type.setText(type_loc.get(position).getName());
                txt_type.setError(null);
                select_type.setVisibility(View.GONE);
            }
        });
        show_info();
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txt_type){
            select_type.setVisibility(View.VISIBLE);
        }
        else if(view.getId()==R.id.call){
            String ph=phone.getText().toString();
            if(!ph.equals("null")&&ph.length()>1) {
                show_call(ph);
            }
        }else if(view.getId()==R.id.callcustomer){
            String ph=customer_mobile.getText().toString();
            if(!ph.equals("null")&&ph.length()>1) {
                show_call(ph);
            }
        }else if(view.getId()==R.id.img_back){
            finish();
        }
    }
    private void show_info() {
        without_loc.setVisibility(View.GONE);
        placeData_value = (PlaceData) getIntent().getSerializableExtra("placeInformation");
        boolean allowAupdate=getIntent().getBooleanExtra("update",false);
        if(placeData_value!=null) {
            ArrayList<TagsValue> getTagsValues=placeData_value.getTagsValues();
            if(getTagsValues!=null) {
                for (int i = 0; i < getTagsValues.size(); i++) {
                    tagsList.addAll(getTagsValues.get(i).getTagsValues());
                }
                setTagsFields();
            }
        }
            send_location.setEnabled(false);
            setLocatinInfo(false);

        if(placeData_value!=null) {
            msg_txt.setText(placeData_value.getPlaceName());
            txt_type.setText(get_loc_name(placeData_value.getPlaceId()));
            if (!placeData_value.getPhone().equals("null"))
                phone.setText(placeData_value.getPhone());
            if (!placeData_value.getWebsite().equals("null"))
                website.setText(placeData_value.getWebsite());
            if (!placeData_value.getEmail().equals("null"))
                email.setText(placeData_value.getEmail());
            if (!placeData_value.getFax().equals("null"))
                fax.setText(placeData_value.getFax());
            if (!placeData_value.getCustomerName().equals("null"))
                customer_name.setText(placeData_value.getCustomerName());
            if (!placeData_value.getCustomerPhone().equals("null"))
                customer_mobile.setText(placeData_value.getCustomerPhone());
            if (!placeData_value.getAccount_number().equals("null"))
                account_number.setText(placeData_value.getAccount_number());


        }
    }
    private void setLocatinInfo(Boolean visible) {
        txt_type.setEnabled(visible);
        msg_txt.setEnabled(visible);
        phone.setEnabled(visible);
        website.setEnabled(visible);
        email.setEnabled(visible);
        fax.setEnabled(visible);
        tags.setEnabled(visible);
        customer_mobile.setEnabled(visible);
        account_number.setEnabled(visible);
        customer_name.setEnabled(visible);
    }
    public String get_loc_name(String type) {
        ArrayList<com.lite.pits_jawwal.pitstracklite.Type.Location_type> type_loc=ALL.get_location_type(this);
        try {
            int tt = Integer.parseInt(type);
            if(tt>100){tt=tt-101;}
            else{tt=tt-1;}
            if (tt > 0) {
                return type_loc.get(tt).getName();
            }
        }catch (Exception e){type_loc.get(0).getName();}
        return type_loc.get(0).getName();
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);
//        super.onConfigurationChanged(newConfig);
//    }
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Locale newLocale=new Locale("en-US");
//        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(newBase);
//        String lang = prefs.getString(Preferences.language1, "");
//        if(lang.trim().equals("1")){
//            newLocale=new Locale("en-US");
//
//        }else if(lang.trim().equals("0")){
//            newLocale=new Locale("ar");
//        }
//        Context context = ContextWrapper.wrap(newBase, newLocale);
//        super.attachBaseContext(context);
//    }
    public  void show_call(final String num){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("الرجاء الاختيار...");
        alertDialog.setPositiveButton("اتصال", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+num));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });
        alertDialog.setNegativeButton("رسالة", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+num));
                startActivity(sendIntent);
                dialog.dismiss();
            }
        });
        alertDialog.setNeutralButton("الغاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
    public void setTagsFields(){
        horizontalFeilds = new HorizontalFeilds(tagsList, getApplicationContext(),true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ActivityCustomerInfo.this, LinearLayoutManager.HORIZONTAL, false);
        FieldsRecyclerView.setLayoutManager(horizontalLayoutManager);
        FieldsRecyclerView.setAdapter(horizontalFeilds);
        horizontalFeilds.notifyDataSetChanged();

    }

}
