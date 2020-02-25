package com.lite.pits_jawwal.pitstracklite.Customers.Filter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;


import com.lite.pits_jawwal.pitstracklite.Config;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.AsyncResponseTags;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.HorizontalTags;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsPhp;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsValue;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends Activity implements View.OnClickListener ,
        AsyncResponseTags,SearchView.OnQueryTextListener {
    private ImageView img_back,send_filter;
    private RecyclerView TagsRecyclerView;
    private HorizontalTags horizontaltags;
    private ArrayList<TagsValue> tagsValues;
    private ListView list_fields;
    private CustomAdapterFields2 customAdapterFields;
    private static FilterActivity filterActivity;
    private String[] listfields;
    private SearchView searchView;
    private SharedPreferences prefs;
    private Locale local=new Locale("en-US");
    private LinearLayout linlaHeaderProgress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
//        String lang = prefs.getString(Preferences.language1, "");
//        if(lang.trim().equals("1")) {
//            local=new Locale("en-US");
//        }
//        else if(lang.trim().equals("0")){
//            local=new Locale("ar");
//        }
//        Locale.setDefault(local);
//        Configuration config = new Configuration();
//        config.locale = local;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);
        declaration();
    }
    @Override
    public void onClick(View v) {
      if(v.getId()==R.id.img_back){
         finish();
      }else if(v.getId()==R.id.send_filter){
          String selectFields="";
          if(customAdapterFields!=null)
              selectFields=customAdapterFields.getSearch();
          Intent intent=getIntent();
          Bundle bundle=new Bundle();
          bundle.putSerializable("tagsList",selectFields);
          intent.putExtras(bundle);
          setResult(10, intent);
          finish();
      }
    }
    private void declaration() {
        linlaHeaderProgress=findViewById(R.id.linlaHeaderProgress);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        send_filter=findViewById(R.id.send_filter);
        send_filter.setOnClickListener(this);
        tagsValues=new ArrayList<>();
        list_fields=findViewById(R.id.list_fields);
        TagsRecyclerView =  findViewById(R.id.TagsRecyclerView);
        filterActivity=this;
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        TagsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        String filterlast=getIntent().getStringExtra("filterlast");
        if(filterlast.length()>0){
            listfields=filterlast.split(",");
        }

        get_tags();
    }
    public static FilterActivity getInstance(){
        return filterActivity;
    }
    public void setTags(){
        horizontaltags = new HorizontalTags(tagsValues, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        TagsRecyclerView.setLayoutManager(horizontalLayoutManager);
        TagsRecyclerView.setAdapter(horizontaltags);
        horizontaltags.notifyDataSetChanged();
        FieldsTags(true);
    }
    private void get_tags() {
        if(Utils.isOnline(this)) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            TagsPhp tagsPhp = new TagsPhp(this, this);
            tagsPhp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            ALL.show_custom_msg(this, FilterActivity.this, getResources().getString(R.string.str12));
        }
    }

    @Override
    public void processFinishTag(ArrayList<TagsValue> _tagsValues) {
        linlaHeaderProgress.setVisibility(View.GONE);
        tagsValues=_tagsValues;
        if(_tagsValues!=null) {
            setTags();
        }
    }

    private void FieldsTags(boolean first) {
        if(horizontaltags!=null){
            List<FieldsValue> tagsList=horizontaltags.getFields();
            int size=tagsList.size();
            if(size>0){
                showFields(tagsList,first);
            }
        }
    }
    private void showFields(List<FieldsValue> tagsList,boolean first) {
        if(tagsList!=null) {
            customAdapterFields = new CustomAdapterFields2(this, tagsList);
            list_fields.setAdapter(customAdapterFields);
            searchView.setQuery("",true);
            if(first)customAdapterFields.setSelect(listfields);
        }
    }
    public void setFields(){

        List<FieldsValue> tagsList=horizontaltags.getSelectTag();
        if(tagsList.size()>0)
            showFields(tagsList,false);
        else
            FieldsTags(false);
    }
    public void removeSelectFields(String tagid){
        customAdapterFields.removeSelect(tagid,false) ;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if ((customAdapterFields != null)) {
            customAdapterFields.filter(newText);
        }
        return false;
    }
//    protected void attachBaseContext(Context newBase) {
//        Locale newLocale=new Locale("en-US");;
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
}
