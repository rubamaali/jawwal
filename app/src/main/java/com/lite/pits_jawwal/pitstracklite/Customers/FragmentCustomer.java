package com.lite.pits_jawwal.pitstracklite.Customers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.PersonalHistoryReport.HisroryReport;
import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;
import com.lite.pits_jawwal.pitstracklite.Customer.AsyncResponseCustomer;
import com.lite.pits_jawwal.pitstracklite.Customer.GeofenceList;
import com.lite.pits_jawwal.pitstracklite.Customer.PlaceListAdapter;
import com.lite.pits_jawwal.pitstracklite.Customers.Filter.FilterActivity;
import com.lite.pits_jawwal.pitstracklite.Customers.SearchGeo.AsyncResponseSearchGeof;
import com.lite.pits_jawwal.pitstracklite.Customers.SearchGeo.SearchGeozone;
import com.lite.pits_jawwal.pitstracklite.Customers.SearchTags.SearchTags;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.AsyncResponseTags;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.HorizontalFeilds;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.HorizontalTags;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsPhp;
import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsValue;
import com.lite.pits_jawwal.pitstracklite.Order.ActivityOrderHistory;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.lite.pits_jawwal.pitstracklite.Utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;



public class FragmentCustomer extends Fragment implements  View.OnClickListener, SearchView.OnQueryTextListener,
        AsyncResponseCustomer, AsyncResponseSearchGeof, AsyncResponseTags {
    private View view;
    private static TextView loc_type, get_all;
    private RelativeLayout loadmore;
    private static ListView listView, list_geoz;
    private static LinearLayout type, linlaHeaderProgress,tags;
    private static SwipeRefreshLayout refreshreport2;
    private CategoryListAdapter mAdapter;
    public static ArrayList<PlaceData> placesGotten = new ArrayList<>();
    private static PlaceListAdapter adapter;
    private Locale locale;
    private SharedPreferences prefs;
    private static FragmentCustomer geof_list;
    private String lastStringSer = "";
    public int start_index = 0;
    public int quantity = 100;
    public String getAll = "0";
    private int selectTypeIndex = 29;
    private int lastPlaceFilterIndex = 0;
    private GeofenceList geo;
    private CardView checkoutclick,filter;
    private HorizontalTags horizontaltags;
    private HorizontalFeilds horizontalFeilds;
    private ArrayList<TagsValue> tagsValues;
    private ArrayList<FieldsValue> fieldsValues;
    private RecyclerView TagsRecyclerView,FieldsRecyclerView;
    private String filterlast;
    private View show_menu;
    private String name,pass;
    private LinearLayout history,show_info,Navigate,order_history;
    private TextView cancel,geof_name;
    private  PlaceData placeSelect;
    private Double lon = 0.0;
    private Double lat = 0.0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.geof_list, container, false);
        declaration();
        //get_noti();
        return  view;
    }

    private void declaration() {
        geof_list=this;
        TagsRecyclerView =  view.findViewById(R.id.TagsRecyclerView);
        TagsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        FieldsRecyclerView =  view.findViewById(R.id.FieldsRecyclerView);
        FieldsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });


        geof_name = view.findViewById(R.id.geof_name);
        Navigate = view.findViewById(R.id.Navigate);
        Navigate.setOnClickListener(this);
        order_history = view.findViewById(R.id.order_history);
        order_history.setOnClickListener(this);
        history = view.findViewById(R.id.history);
        history.setOnClickListener(this);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        show_info = view.findViewById(R.id.show_info);
        show_info.setOnClickListener(this);
        show_menu = view.findViewById(R.id.show_menu);
        listView = view.findViewById(R.id.list_type);
        tagsValues=new ArrayList<>();
        fieldsValues=new ArrayList<>();
        listView.setVisibility(View.VISIBLE);
        checkoutclick= view.findViewById(R.id.checkoutclick);
        checkoutclick.setOnClickListener(this);
        filter= view.findViewById(R.id.filter);
        filter.setOnClickListener(this);
        list_geoz = view.findViewById(R.id.list_geoz);
        View footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        loadmore = footerView.findViewById(R.id.loadmore);
        loadmore.setOnClickListener(this);
        list_geoz.addFooterView(footerView);
        type = view.findViewById(R.id.type);
        loc_type = view.findViewById(R.id.loc_type);
        get_all = view.findViewById(R.id.get_all);
        get_all.setOnClickListener(this);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        type.setOnClickListener(this);
        list_geoz.setVisibility(View.GONE);
        tags =view.findViewById(R.id.tags);
        filterlast="";
        refreshreport2 =view.findViewById(R.id.refreshreport2);
        refreshreport2.setColorSchemeResources(R.color.colorAccent);
        refreshreport2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                show_geo(true);
                refreshreport2.setRefreshing(false);
            }
        });
        placesGotten.clear();
        refreshreport2.setVisibility(View.GONE);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String lang = prefs.getString(Preferences.language1, "");
        name=prefs.getString(Preferences.USER_NAME, "");
        pass=prefs.getString(Preferences.PASSWORD, "");
        if (lang.trim().equals("1")) {
            locale = new Locale("en-US");
        } else {
            locale = new Locale("ar");
        }
        setList();
        show_geo(true);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config, null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type.setVisibility(View.VISIBLE);
                lastPlaceFilterIndex = 0;
                selectTypeIndex = position;
                clearArray(mAdapter.getImage(position), mAdapter.get_id(position));
                loc_type.setText(mAdapter.get_name(position));
            }
        });
        list_geoz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                placeSelect = adapter.getItem(position);
                lon=placeSelect.getPlaceLon();
                lat=placeSelect.getPlaceLat();
                show_menu.setVisibility(View.VISIBLE);
                geof_name.setText(placeSelect.getPlaceName());
            }
        });

        get_tags();
    }
    public void show_geo(boolean is_ref) {
        filter.setVisibility(View.GONE);
        filterlast="";
        if (Utils.isOnline(getContext())) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            if (is_ref) {
                placesGotten.clear();
                lastPlaceFilterIndex = 0;
                start_index = 0;
                quantity = 100;
                getAll = "0";
            }
            geo = new GeofenceList(name,pass,getContext(), this, String.valueOf(start_index), getAll);
            geo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            set_all();
            ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str12));
        }


    }

    public void set_all() {
        type.setVisibility(View.VISIBLE);
        clearArray(R.drawable.ic_all, mAdapter.get_id(selectTypeIndex));
        loc_type.setText(mAdapter.get_name(selectTypeIndex));
    }

    public void setList() {
        mAdapter = new CategoryListAdapter(getContext(), LocationData.getCategories(getContext()));
        listView.setAdapter(mAdapter);
        listView.setVisibility(View.GONE);
        list_geoz.setVisibility(View.VISIBLE);
        refreshreport2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search2, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter != null) {
                    if (TextUtils.isEmpty(s)) {
                        adapter.filter("");
                        listView.clearTextFilter();
                        lastStringSer = s;
                    } else {
                        Character lastChar = s.charAt(s.length() - 1);
                        if (lastChar == ' ') {
                            searchGeozone(s);
                            getAll = "1";
                        }else {
                            adapter.filter(s);
                            lastStringSer = s;
                        }

                    }

                }
                return true;
            }
        });
        return ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            Intent intent=new Intent(getContext(), FilterActivity.class);
            intent.putExtra("filterlast",filterlast);
            startActivityForResult(intent,10);
        }
        return super.onOptionsItemSelected(item);
    }
    private void searchGeozone(String s) {
        if (Utils.isOnline(getContext())) {
            SearchGeozone searchGeozone = new SearchGeozone(this, getContext(), s);
            searchGeozone.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void processFinish(ArrayList<PlaceData> result) {
        linlaHeaderProgress.setVisibility(View.GONE);
        int in = 0;
        if (result != null) {
            in = result.size();
                if (getAll == "1") {
                    placesGotten = result;
                    in = placesGotten.size();
                } else {
                    placesGotten.addAll(result);
                    in = placesGotten.size();

                }

            set_all();
        }
    }
    private void scrollMyListViewToBottom(int position) {
        list_geoz.setSelectionFromTop(position - 1, 0);
    }
    public static String round_(double gps_speed) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return String.valueOf(round(gps_speed, 2));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void clearArray(int newImg, String id) {
        ArrayList<PlaceData> places = new ArrayList<>();

        if (id.equals("0")) {
          places.addAll(placesGotten);
        } else {
            for (int i = 0; i < placesGotten.size(); i++) {
                PlaceData place = placesGotten.get(i);
                if (place.getPlaceId().equals(id)) {
                    places.add(place);
                }
            }
        }
        Collections.sort(places);
        refreshreport2.setVisibility(View.VISIBLE);
        list_geoz.setVisibility(View.VISIBLE);
        try {
            adapter = new PlaceListAdapter(getContext(), places, newImg);
        }catch (Exception ignored){}
        list_geoz.setAdapter(adapter);
        listView.setVisibility(View.GONE);
        if (getAll.equals("0")) {
            scrollMyListViewToBottom(lastPlaceFilterIndex);
        }
        lastPlaceFilterIndex = places.size();
        if (lastStringSer.length() > 0) {
            adapter.filter(lastStringSer);
        }
    }
    public static FragmentCustomer getinstance() {
        return geof_list;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.type) {
            listView.setVisibility(View.VISIBLE);
            list_geoz.setVisibility(View.GONE);
            refreshreport2.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
        } else if (view.getId() == R.id.get_all) {
            getAll = "1";
            show_geo(false);
        } else if (view.getId() == R.id.loadmore) {
            if (placesGotten != null) {
                getAll = "0";
                start_index = placesGotten.size();
                quantity = 100;
                show_geo(false);
            }
        }else if(view.getId()==R.id.filter){
            show_geo(false);
            getAll = "0";
            start_index=0;
            quantity = 100;
            filter.setVisibility(View.GONE);
        }else if(view.getId()==R.id.cancel){
            show_menu.setVisibility(View.GONE);
        }else if(view.getId()==R.id.history){
            Intent intent = new Intent(getContext(), HisroryReport.class);
            intent.putExtra("geof_name", placeSelect.getPlaceName());
            intent.putExtra("geof_id", placeSelect.getIid());
            intent.putExtra("offlinLoc_id", placeSelect.getOfflinid());
            startActivity(intent);

        }else if(view.getId()==R.id.show_info){
            Intent intent = new Intent(getContext(), ActivityCustomerInfo.class);
            intent.putExtra("placeInformation", placeSelect);
            startActivity(intent);

        }else if (view.getId() == R.id.Navigate) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f ", lat, lon) + "?q=" + lat + "," + lon;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
        else if(view.getId()==R.id.order_history){
            Intent intent=new Intent(getContext(), ActivityOrderHistory.class);
            intent.putExtra("geof_name",placeSelect.getPlaceName());
            intent.putExtra("geof_id",placeSelect.getIid());
            startActivity(intent);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(geo != null && geo.getStatus() == AsyncTask.Status.RUNNING)
            geo.cancel(true);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(adapter!=null){
            adapter.filter("");
        }
        super.onHiddenChanged(hidden);
    }
    private void get_tags() {
        if(Utils.isOnline(getContext())) {
            TagsPhp tagsPhp = new TagsPhp(this, getContext());
            tagsPhp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str12));
        }
    }
    @Override
    public void processFinishTag(ArrayList<TagsValue> _tagsValues) {
        tagsValues=_tagsValues;
        if(_tagsValues!=null) {
            setTags();
        }
    }
    public void setTags(){
        horizontaltags = new HorizontalTags(tagsValues, getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        TagsRecyclerView.setLayoutManager(horizontalLayoutManager);
        TagsRecyclerView.setAdapter(horizontaltags);
        horizontaltags.notifyDataSetChanged();

    }
    public void setFields(){
        fieldsValues=horizontaltags.getSelectTag();
        horizontalFeilds = new HorizontalFeilds(fieldsValues, getContext(),true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        FieldsRecyclerView.setLayoutManager(horizontalLayoutManager);
        FieldsRecyclerView.setAdapter(horizontalFeilds);
        horizontalFeilds.notifyDataSetChanged();

    }
    public void searchTags(String fieldsid) {
        getAll = "1";
        if (fieldsid.equals("0")) {
            show_geo(false);
        } else {
            if (Utils.isOnline(getContext())) {
                SearchTags searchtags = new SearchTags(this, getContext(), fieldsid);
                searchtags.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
            }
        }
    }
    public void removeSelectFields(String tagid){
        horizontalFeilds.removeSelect(tagid,false) ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode ==10 && data!=null) {
            Bundle bundle = data.getExtras();
            String tagsList = bundle.getString("tagsList");
            searchTags(tagsList);
            filter.setVisibility(View.VISIBLE);
            filterlast=tagsList;
        }

    }

}
