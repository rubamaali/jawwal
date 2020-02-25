package com.lite.pits_jawwal.pitstracklite;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.widget.ImageView;

import com.lite.pits_jawwal.pitstracklite.Custom_Event.CustomList_event;
import com.lite.pits_jawwal.pitstracklite.Custom_Event.Event_item;
import com.lite.pits_jawwal.pitstracklite.DB.Insert_Vehicles;
import com.lite.pits_jawwal.pitstracklite.DB.Insert_events;
import com.lite.pits_jawwal.pitstracklite.DB.Insert_notifications;
import com.lite.pits_jawwal.pitstracklite.List.CustomListList;
import com.lite.pits_jawwal.pitstracklite.List.Vehicles_list;
import com.lite.pits_jawwal.pitstracklite.NotiReplay.ActivityReplayNoti;

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

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.ContentValues.TAG;


public class Event extends Fragment implements View.OnClickListener,SearchView.OnQueryTextListener  {
    View view;
    private Spinner event_type;
    private DatePickerDialog.OnDateSetListener mydatesetlistener;
    private TextView txt,select_car,txt_select_time;
    private String str_filter1="";
    private String str_filter2="";
    private String datee,username;
    private Button generate;
    private ImageView imageView,txt_sort;
    private Button map_end;
    private ImageView next,previous;
    Locale local=new Locale("en-US");
    LinearLayout mLinearLayout,mapLinearLayout;
    LinearLayout mLinearLayoutHeader;
    ListView list_event;
    CustomList_event adapter_event;
    TextView text1,text2;
    private WebView show_map;
    private static Event event;
    private String name,pass,ptime,strtext,date;
    private CustomListList dataAdapter2;
    private LinearLayout no_events;
    SearchView editsearch;
    private List<Vehicles_list> vehicles_lists;
    private List<Vehicles_list> list_all;
    private LinearLayout linlaHeaderProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int list_index;
    private String device_id="0";
    private int res=0;
    static TextView notifCount;
    static int mNotifCount = 0;
    public static Event instance;
    private SharedPreferences prefs;
    private String url;
    public Event() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view= inflater.inflate(R.layout.fragment_event, container, false);
        event_type =  view.findViewById(R.id.event_type);
        strtext = getArguments().getString("index");
        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        event_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos!=0) {
                    str_filter1 = (String) parent.getItemAtPosition(pos);
                }else{
                    str_filter1="";
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        show_map= view.findViewById(R.id.show_map);
        show_map.setWebViewClient(new WebViewClient());
        show_map.getSettings().setJavaScriptEnabled(true);
        show_map.setWebChromeClient(new WebChromeClient());
        show_map.setOnClickListener(this);
        mSwipeRefreshLayout =  view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        event_type.setVisibility(View.VISIBLE);
        select_car =  view.findViewById(R.id.select_car);
        select_car.setOnClickListener(this);
        no_events=view.findViewById(R.id.no_events);
        select_car.setVisibility(View.VISIBLE);
        next=view.findViewById(R.id.butt_next);
        previous=view.findViewById(R.id.butt_previous);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        txt_sort=view.findViewById(R.id.txt_sort) ;
        txt_sort.setOnClickListener(this);
        if(!strtext.equals("F") && strtext.length()>0 && !strtext.equals("")) {
           try{ Vehicles vehicles=Vehicles.getInstance();
            vehicles_lists= vehicles.get_List();
            list_index=Integer.parseInt(strtext);
            select_car.setText(vehicles_lists.get(list_index).getName());
            device_id=vehicles_lists.get(list_index).getDeviceid();
            str_filter2=vehicles_lists.get(list_index).getName();}
           catch (Exception e){}
        }
        else if(strtext.equals("F")){
            new CountDownTimer(4000, 1000) {
                public void onTick(long millisUntilFinished) {
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                }
                public void onFinish() {
                    linlaHeaderProgress.setVisibility(View.GONE);
                    get_Event();
                }
            }.start();
        }
        generate=view.findViewById(R.id.but_generate) ;
        generate.setOnClickListener(this);
        imageView=view.findViewById(R.id.arrow) ;

        map_end=view.findViewById(R.id.map_end);
        map_end.setOnClickListener(this);
        event=this;
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name=prefs.getString(Preferences.USER_NAME,"");
        username=name;
        pass=prefs.getString(Preferences.PASSWORD,"");
        url = prefs.getString(Preferences.URL, "");
        SharedPreferences.Editor editor=prefs.edit();
        editor.apply();
        ALL.set_number_notification(getContext());
        ShortcutBadger.applyCount(getContext(), 0);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        dataAdapter.add(getResources().getString(R.string.str14));
        dataAdapter.add(getResources().getString(R.string.Ignition_On));
        dataAdapter.add(getResources().getString(R.string.Speed_Violation));
        dataAdapter.add(getResources().getString(R.string.Main_Power_Low));
        dataAdapter.add(getResources().getString(R.string.Accident));
        dataAdapter.add(getResources().getString(R.string.Entering_perimeter));
        dataAdapter.add(getResources().getString(R.string.Leaving_perimeter));
        dataAdapter.add(getResources().getString(R.string.Harsh_Braking));
        dataAdapter.add(getResources().getString(R.string.Odometer));
        dataAdapter.add(getResources().getString(R.string.Door));
        dataAdapter.add(getResources().getString(R.string.Theft));
        dataAdapter.add(getResources().getString(R.string.Temperature));
        dataAdapter.add(getResources().getString(R.string.sms));

        event_type.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy",local);
        String newDateStr = postFormater.format(new Date());
        txt=view.findViewById(R.id.txt_date);
        txt_select_time=view.findViewById(R.id.txt_select_time);
        txt.setText(newDateStr);
        txt_select_time.setText(txt.getText());
        txt.setOnClickListener(this);
        mydatesetlistener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                Calendar cal=new GregorianCalendar(y,m,d);
                setdate(cal);
            }
        };
        mLinearLayout =  view.findViewById(R.id.layout_report);
        mapLinearLayout =  view.findViewById(R.id.layoutmap);
        mLinearLayoutHeader =  view.findViewById(R.id.header);
        mLinearLayout.setVisibility(View.GONE);
        //mapLinearLayout.setVisibility(View.GONE);
        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility() == View.GONE) {
                    collapse2();
                    expand();
                } else {
                    collapse();
                }
            }
        });
        list_event=view.findViewById(R.id.list_event);
        list_event .setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                set_n_p(View.VISIBLE);
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    set_n_p(View.INVISIBLE);
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    set_n_p(View.INVISIBLE);
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });
        ptime=get_time_today();
        Locale local=new Locale("en-US");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy",local);
        date = mdformat.format(calendar.getTime());
        mdformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date2 = null;
        try {
            date2 =  mdformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = (date2 != null ? date2.getTime() : 0) / 1000;
        String _date= String.valueOf(time);
        if(Utils.isOnline(getContext())) {
            get_Event();
        }else{
            Insert_notifications notifications=new Insert_notifications(getContext());
            Insert_events events=new Insert_events(getContext());
            List<Event_item> list_ev=events.All_events(_date);
            List<Event_item> sms=notifications.All_Notifications(date);
            list_ev.addAll(sms);
            if(list_ev.size()>0){res=1;}
            adapter_event=new CustomList_event(getActivity(),list_ev,getContext());
            list_event.setAdapter(adapter_event);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_Event();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        if (Utils.isOnline(getContext())) {
            show_map.loadUrl(url+"/mobilelite.php?USER="+name +"&PASS="+pass+"&page=map");
        }
        instance=this;
        return view;
    }
    public void show_online_map(){
        show_map.setVisibility(View.VISIBLE);
    }
    public void show_list(){
        android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view2 = factory.inflate(R.layout.listvehicles, null);
        final ListView listvehi=view2.findViewById(R.id.list_vehicles);
        editsearch = view2.findViewById(R.id.searchView1);
        editsearch.setOnQueryTextListener(this);
        editsearch.setOnClickListener(this);
       final Insert_Vehicles db = new Insert_Vehicles(getContext());
       if(!strtext.equals("0") && strtext.length()>=0) {
           Vehicles vehicles = Vehicles.getInstance();
           vehicles_lists = vehicles.get_List();
       }
       if(vehicles_lists==null || vehicles_lists.size()==0){
           vehicles_lists =db.All_Vehicles();
       }
       list_all=new ArrayList<>();
        if(vehicles_lists!=null && vehicles_lists.size()>0){
             list_all.add(new Vehicles_list("",getResources().getString(R.string.str15),"","","","","","",-1,"","",""));
             list_all.addAll(vehicles_lists);
             dataAdapter2 = new CustomListList (getActivity(),list_all);
             listvehi.setAdapter(dataAdapter2);
      }
        alertadd.setView(view2);
        alertadd.setNeutralButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                list_index=-1;
                dialog.cancel();
            }
        });
        final android.support.v7.app.AlertDialog alertDialog = alertadd.create();
        alertDialog.show();
        listvehi.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                list_index=position;
                if(list_all.get(position).getIndex()==-1){
                    select_car.setText(getResources().getString(R.string.str15));
                    str_filter2="";
                }else {
                    select_car.setText( list_all.get(list_index).getName());
                    device_id = list_all.get(list_index).getDeviceid();
                    str_filter2 = list_all.get(list_index).getName();
                }
                alertDialog.dismiss();
            }
        });
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#7D8A92"));
        nbutton.setAllCaps(false);
        pbutton.setAllCaps(false);
    }
    public void get_Event(){
        try {
        if(Utils.isOnline(getContext())) {
            Event_List vehicles_list = new Event_List(name, pass, get_time_today());
            vehicles_list.execute();
        }else{
            ALL.show_custom_msg(getContext(),getActivity(),getResources().getString(R.string.str12));
        }
        }catch (Exception e){}
    }
    private String get_time_today(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", local);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date2 = null;
        try {
            date2 =  formatter.parse(txt.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time=0;
        if (date2 != null) {
            date = formatter.format(date2.getTime());
            time = date2.getTime() / 1000;
        }

        return String.valueOf(time);
    }
    private void setdate(final Calendar calendar){
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy",local);
        if(calendar.getTime().after(new Date())){
            next.setEnabled(false);
            return;
        }
        else{
            next.setEnabled(true);
        }
        TextView txt_da= view.findViewById(R.id.txt_date);
        txt_da.setText(postFormater.format(calendar.getTime()));
    }
    public void expand() {
        mLinearLayout.setVisibility(View.VISIBLE);
        imageView.setRotation((float) 180.0);
    }
    public void expand2(String lat,String lon,String name,String address) {
        collapse();
        mapLinearLayout.setVisibility(View.VISIBLE);
        show_online_map();
        show_map.loadUrl("javascript:moveto_loc(" + lon + "," + lat + ")");
    }

    private void collapse() {
        mLinearLayout.setVisibility(View.GONE);
        imageView.setRotation((float)0.0);
    }
    private void collapse2() {
        mapLinearLayout.setVisibility(View.GONE);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        dataAdapter2.filter(s);
        return false;
    }
    @Override
    public void onClick(View view) {
        datee=txt.getText().toString();
        if(view.getId()==R.id.txt_date){
            Calendar c=Calendar.getInstance();
            if(datee.equals("")) {
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, mydatesetlistener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
            else{
                DateFormat  sdf2 = new SimpleDateFormat("dd-MM-yyyy",local);
                Date d1 = null;
                try {
                    d1 = sdf2.parse(datee);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.setTime(d1);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, mydatesetlistener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        }
        if(view.getId()==R.id.but_generate){
            show_event();

        }
        if(view.getId()==R.id.map_end){
            collapse2();
        }
        if(view.getId()==R.id.select_car){
            try {
                show_list();
            }catch (Exception e){}
        }
        else if(view.getId()==R.id.butt_previous){
            next_previous_data(-1);
        }
        else if(view.getId()==R.id.butt_next){
            next_previous_data(1);
        }
        else if(view.getId()==R.id.txt_sort){
            sort_list();
        }
        else if(view.getId()==R.id.noticlick){
            openNotiReplay();
        }
    }
    public void sort_list(){
        SharedPreferences  prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort=prefs.getString(Preferences.SORT,"");
        if(sort.equals("1")){sort="0";}
        else {sort="1";}
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(Preferences.SORT,sort);
        editor.apply();
        if(adapter_event!=null) {
            adapter_event.sortlist();
        }
    }
    public void show_event(){
        if(!ptime.equals(get_time_today())) {
            ptime=get_time_today();
            Event_List vehicles_list = new Event_List(name, pass, get_time_today());
            vehicles_list.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            if(res==1) {
                boolean filter=adapter_event.filter(str_filter1 + "," + str_filter2);
                if(!filter){
                    list_event.setVisibility(View.GONE);
                    no_events.setVisibility(View.VISIBLE);
                }else{
                    list_event.setVisibility(View.VISIBLE);
                    no_events.setVisibility(View.GONE);
                }
            }
        }
        txt_select_time.setText(txt.getText());
        collapse();
    }
    public void set_n_p(int value){
        next.setVisibility(value);
        previous.setVisibility(value);

    }
    private void next_previous_data(int value){
        datee=txt.getText().toString();
        Calendar c=Calendar.getInstance();
        DateFormat  sdf2 = new SimpleDateFormat("dd-MM-yyyy",local);
        Date d1 = null;
        try {
            d1 = sdf2.parse(datee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(d1);
        c.add(Calendar.DATE, value);
        setdate(c);
        show_event();
    }
    public static Event getIns(){
        return event;
    }

    private class Event_List extends AsyncTask<Object, Object, List<Event_item>> {
        private String _userName;
        private String _password;
        List<Event_item> list=new ArrayList<>();
        private String _date;

        public Event_List(String userName, String password,String date) {
            _userName = userName;
            _password = password;
            _date=date;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }
        @Override
        protected List<Event_item> doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url+"/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "event"));
                nameValuePairs.add(new BasicNameValuePair("Time", _date));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("event");
                        for(int i=v.length()-1; i >= 0; i--){
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            Event_item item = new Event_item(jsonObject2.optString("event"),jsonObject2.optString("vehicle"),jsonObject2.optString("address"),jsonObject2.optString("time"),jsonObject2.optString("speed"),jsonObject2.optString("lon"),jsonObject2.optString("lat"));
                            list.add(item);
                        }
                        try {

                            mNotifCount = Integer.parseInt(jsonObject.optString("total"));
                        }catch (Exception e){
                            mNotifCount=0;
                        }
                        return list;
                    }
                }
                int i;
            } catch (UnsupportedEncodingException e) {
                return list;
            } catch (ClientProtocolException e) {
                return list;
            } catch (IOException e) {
                e.printStackTrace();
                return list;
            } catch (JSONException e) {
                return list;
            }catch (Exception e){}
            return list;
        }
        @Override
        protected void onPostExecute(List<Event_item> result) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String sort = prefs.getString(Preferences.SORT, "");
                boolean filtr = false;
                if (result != null) {
                    setupBadge(mNotifCount);
                    if (result.size() > 0) {
                        list_event.setVisibility(View.VISIBLE);
                        no_events.setVisibility(View.GONE);
                        try {
                            Insert_events events = new Insert_events(getContext());
                            events.insert(result, _date);
                        } catch (Exception e) {
                        }


                    } else {
                        list_event.setVisibility(View.GONE);
                        no_events.setVisibility(View.VISIBLE);
                        collapse2();
                        res = 0;
                        collapse2();
                    }
                } else {
                    list_event.setVisibility(View.GONE);
                    no_events.setVisibility(View.VISIBLE);
                    collapse2();
                    res = 0;
                }
                try {
                    Insert_notifications notifications = new Insert_notifications(getContext());
                    List<Event_item> sms = notifications.All_Notifications(date);
                    if (result != null) {
                        result.addAll(sms);
                    }

                    if (result != null && result.size() > 0) {
                        if (sort.equals("1")) {
                            Collections.reverse(result);
                        }
                        adapter_event = new CustomList_event(getActivity(), result, getContext());
                        list_event.setAdapter(adapter_event);
                        filtr = adapter_event.filter(str_filter1 + "," + str_filter2);
                        res = 1;
                    }
                    if (!filtr) {
                        list_event.setVisibility(View.GONE);
                        no_events.setVisibility(View.VISIBLE);
                    } else {
                        list_event.setVisibility(View.VISIBLE);
                        no_events.setVisibility(View.GONE);
                    }
                    linlaHeaderProgress.setVisibility(View.GONE);
                } catch (Exception e) {
                }
            }catch (Exception e){  Log.e(TAG, "onPostExecute: ",e );}
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menunoti, menu);
        MenuItem item = menu.findItem(R.id.action_replay);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View actionView = MenuItemCompat.getActionView(item);
        notifCount =  actionView.findViewById(R.id.cart_badge);
        notifCount.setText(String.valueOf(mNotifCount));
        FrameLayout noticlick=actionView.findViewById(R.id.noticlick);
        noticlick.setOnClickListener(this);
        setupBadge(mNotifCount);
        return ;
    }
    public void setupBadge(int count) {
        mNotifCount=count;
        if (notifCount != null) {
            if (mNotifCount == 0) {
                if (notifCount.getVisibility() != View.GONE) {
                    notifCount.setVisibility(View.GONE);
                }
            } else {
                notifCount.setText(String.valueOf((mNotifCount)));
                if (notifCount.getVisibility() != View.VISIBLE) {
                    notifCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public void removeone(){
        mNotifCount=mNotifCount-1;
        setupBadge(mNotifCount);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_replay) {
            openNotiReplay();
        }
        return super.onOptionsItemSelected(item);
    }
    private void openNotiReplay() {
        Intent intent=new Intent(getContext(), ActivityReplayNoti.class);
        startActivity(intent);
    }
    public static Event getInstance(){
        return instance;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_replay);
        String pitsponal = prefs.getString(Preferences.PITSONAL, "0");
        if (prefs.getString(Preferences.URL, "").equals("https://mpitsonal.pitstrack.com")|| pitsponal.equals("1")) {
            settingsItem.setVisible(true);
        }else{
            settingsItem.setVisible(false);
        }
    }
}
