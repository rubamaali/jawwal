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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.DatePicker;
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

import com.lite.pits_jawwal.pitstracklite.CustomStopHours.Customadapter_StopHours;
import com.lite.pits_jawwal.pitstracklite.CustomStopHours.StopHours_value;
import com.lite.pits_jawwal.pitstracklite.Custom_Event.Event_item;
import com.lite.pits_jawwal.pitstracklite.Custom_Idling.Customadapter_Idling;
import com.lite.pits_jawwal.pitstracklite.Custom_Idling.Idling_value;
import com.lite.pits_jawwal.pitstracklite.Custom_Visit.Customadapter_myvisit;
import com.lite.pits_jawwal.pitstracklite.Custom_Visit.Visit_value;
import com.lite.pits_jawwal.pitstracklite.Customer.ActivityCustomer;
import com.lite.pits_jawwal.pitstracklite.List.CustomListList;
import com.lite.pits_jawwal.pitstracklite.List.Vehicles_list;
import com.lite.pits_jawwal.pitstracklite.Order.CartValues;
import com.lite.pits_jawwal.pitstracklite.Order.ListCartAdapter;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.CustomReportValue;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Customadapter_report_trip;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Customadapter_report_workhours;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Customadapter_workhours;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.ReportList;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Report_Trip_value;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Report_work_hour_value;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;

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

import static com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout.TAG;


public class Reports extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    View view;
    private Spinner report_type;
    private DatePickerDialog.OnDateSetListener mydatesetlistener;
    private TextView txt_date, select_car, hint_msg, txt_units_name;
    private String datee;
    private Button generate;
    private ImageView imageView, txt_sort;
    private int flag_zoom = 0;
    Locale local = new Locale("en-US");
    LinearLayout mLinearLayout, mapLinearLayout;
    LinearLayout mLinearLayoutHeader;
    Customadapter_workhours customadapterWorkhours;
    Customadapter_report_workhours customadapterReportWorkhours;
    Customadapter_report_trip customadapterReportTrip;
    Customadapter_myvisit customadapter_myvisit;
    Customadapter_StopHours customadapterStopHours;
    Customadapter_Idling customadapterIdling;
    ListCartAdapter listCartAdapter;
    private ListView list_report_work;
    private ArrayList<Work_Report_Value> work_report_valueList;
    private ArrayList<Visit_value> visit_list_value;
    private ArrayList<CartValues> cartList;
    private ArrayList<StopHours_value> stophouts_list_value;
    private ArrayList<Idling_value> idling_values;
    private ArrayList<Report_work_hour_value> work_hour_valueList;
    private ArrayList<Report_Trip_value> report_trip_values;
    private ImageView next, previous;

    private LinearLayout no_reports;
    private String name, pass, url;
    private Button map_end, map_size;
    private List<Vehicles_list> vehicles_lists;
    private String dev_id = "";
    private String dev_name = "";
    private int type = 0;
    private WebView show_map;
    private static Reports reports;
    LinearLayout zoom;
    private int list_index = -1;
    private String device_id = "0";
    SwipeRefreshLayout refreshreport2;
    private CustomListList dataAdapter2;
    SearchView editsearch;
    private SharedPreferences prefs;
    private FloatingActionButton add_rep;
    private CustomReportValue dataAdapterReport;
    public Reports() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Config config= Config.getInstance(getContext());
        config.setAppLocale();
        view = inflater.inflate(R.layout.fragment_reports, container, false);
        add_rep =  view.findViewById(R.id.add_rep);
        add_rep.setOnClickListener(this);
        reports = this;
        zoom =  view.findViewById(R.id.zoom);
        report_type =  view.findViewById(R.id.report_type);
        report_type.setVisibility(View.VISIBLE);
        no_reports =  view.findViewById(R.id.no_reports);
        txt_units_name =  view.findViewById(R.id.txt_units_name);
        select_car =  view.findViewById(R.id.select_car);
        select_car.setOnClickListener(this);
        select_car.setVisibility(View.VISIBLE);
        generate =  view.findViewById(R.id.but_generate);
        generate.setOnClickListener(this);
        imageView =  view.findViewById(R.id.arrow);
        txt_sort =  view.findViewById(R.id.txt_sort);
        txt_sort.setOnClickListener(this);
        map_end =  view.findViewById(R.id.map_end);
        map_end.setOnClickListener(this);
        map_size =  view.findViewById(R.id.map_size);
        map_size.setOnClickListener(this);
        next =  view.findViewById(R.id.butt_next);
        previous =  view.findViewById(R.id.butt_previous);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        refreshreport2 =  view.findViewById(R.id.refreshreport2);
        refreshreport2.setColorSchemeResources(R.color.colorAccent);

        hint_msg =  view.findViewById(R.id.hint_msg);

        ArrayList<ReportList> lists=new ArrayList<>();
        String report=prefs.getString(Preferences.REPORTLIST,"0");
        Log.d(TAG, "onCreateView: "+report);
        String [] listValidReport=report.split(",");
         if(listValidReport!=null){
             for(int i=0;i<listValidReport.length;i++){
                 switch (listValidReport[i]){
                     case "1": case "01.0":
                     lists.add(new ReportList("1",getResources().getString(R.string.Vehicle_History)));
                     break;
                     case "8": case "08.0":
                         lists.add(new ReportList("8",getResources().getString(R.string.Work_Hours)));
                      break;
                     case "71": case "71.0":
                         lists.add(new ReportList("71",getResources().getString(R.string.Work_Reports)));
                     break;
                     case "80": case "80.0":
                     lists.add(new ReportList("80",getResources().getString(R.string.Work_hours_e)));
                     break;
                     case "98": case "98.0":
                         lists.add(new ReportList("98",getResources().getString(R.string.visitsreport)));
                      break;
                         case "19.0": case "19":
                         lists.add(new ReportList("19.0",getResources().getString(R.string.stops_hours)));
                      break;
                     case "61": case "61.0":
                         lists.add(new ReportList("61.0",getResources().getString(R.string.idlingrep)));
                      break;
                     case "97.3":
                         lists.add(new ReportList("97.3",getResources().getString(R.string.order_history)));
                     break;
                 }
             }
         }
        add_rep.setVisibility(View.VISIBLE);
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy", local);
        txt_date =  view.findViewById(R.id.txt_date);
        txt_date.setOnClickListener(this);
        dataAdapterReport = new CustomReportValue(getActivity(),lists);
        report_type.setAdapter(dataAdapterReport);

        try {
            Vehicles vehicles = Vehicles.getInstance();
            vehicles_lists = vehicles.get_List();
        } catch (Exception e) {

        }

        String strtext = getArguments() != null ? getArguments().getString("index") : null;
        String reportid="0";
        if (strtext!=null && strtext.length() > 2) {
            String[] words = strtext.split(",");
            dev_id = words[0];
            reportid =words[1];
        } else {
            String stype = prefs.getString(Preferences.LAST_REPORT, "");
            String sid = prefs.getString(Preferences.LAST_USER, "");
            if (!stype.equals("") && !sid.equals("")) {
                int Itype = Integer.parseInt(stype) + 1;
                int idev_id = Integer.parseInt(sid);
                if (Itype != -1 && idev_id != -1) {
                    type = Itype;
                    dev_id = get_user_index(idev_id);
                }
            }
        }

        if (!reportid.equals("0")) {
            int index= dataAdapterReport.getIndex(reportid);
            if(index!=-1){
                report_type.setSelection(index);
            }
        }


        String newDateStr = postFormater.format(new Date());
        if (dev_id.length() >= 0) {
            try {
                list_index = Integer.parseInt(dev_id);
                select_car.setText(vehicles_lists.get(list_index).getName());
                dev_name = vehicles_lists.get(list_index).getName();
                device_id = vehicles_lists.get(list_index).getDeviceid();
                SimpleDateFormat Formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);
                Date last_date = Formater.parse(vehicles_lists.get(list_index).getLast_update());
                newDateStr = postFormater.format(last_date);
            } catch (Exception e) {
            }
        }
        txt_date.setText(newDateStr);
        boolean boo_comper = comapare_date(newDateStr);
        if (!boo_comper) {
            hint_msg.setText(view.getResources().getString(R.string.hint_mag) + " " + newDateStr);
            hint_msg.setVisibility(View.VISIBLE);
        } else {
            hint_msg.setVisibility(View.GONE);
        }
        show_map =  view.findViewById(R.id.show_map);
        show_map.setWebViewClient(new WebViewClient());
        show_map.getSettings().setJavaScriptEnabled(true);
        show_map.setWebChromeClient(new WebChromeClient());
        show_map.setOnClickListener(this);
        name = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");
        url = prefs.getString(Preferences.URL, "");

        mydatesetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                Calendar cal = new GregorianCalendar(y, m, d);
                setdate(cal);
            }
        };
        mLinearLayout =  view.findViewById(R.id.layout_report);
        mapLinearLayout =  view.findViewById(R.id.layoutmap);
        mLinearLayoutHeader =  view.findViewById(R.id.header);
        mLinearLayout.setVisibility(View.VISIBLE);
        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility() == View.GONE) {
                    expand();
                } else {
                    collapse();
                }
            }
        });
        list_report_work =  view.findViewById(R.id.list_report_work);
        show_rep();

        refreshreport2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                show_rep();
                refreshreport2.setRefreshing(false);
            }
        });
        list_report_work.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                set_n_p(View.VISIBLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mLastFirstVisibleItem < firstVisibleItem) {
                    set_n_p(View.INVISIBLE);
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    set_n_p(View.INVISIBLE);
                }
                mLastFirstVisibleItem = firstVisibleItem;

            }
        });
        return view;
    }

    public String get_user_index(int unit_id) {
        if (vehicles_lists != null) {
            for (int i = 0; i < vehicles_lists.size(); i++) {
                if (vehicles_lists.get(i).getDeviceid().equals(String.valueOf(unit_id))) {
                    return String.valueOf(i);
                }
            }
        }
        return "-1";
    }

    public void set_n_p(int value) {
        next.setVisibility(value);
        previous.setVisibility(value);

    }

    private void next_previous_data(int value) {
        hint_msg.setVisibility(View.GONE);
        datee = txt_date.getText().toString();
        Calendar c = Calendar.getInstance();
        DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", local);
        Date d1 = null;
        try {
            d1 = sdf2.parse(datee.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(d1);
        c.add(Calendar.DATE, value);
        setdate(c);
        show_rep();
    }

    public boolean comapare_date(String date_pre) {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy", local);
        String newDateStr = postFormater.format(new Date());
        return date_pre.equals(newDateStr);
    }

    public void show_list() {
        try {
            android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(getContext());
            LayoutInflater factory = LayoutInflater.from(getContext());
            final View view2 = factory.inflate(R.layout.listvehicles, null);
            final ListView listvehi =  view2.findViewById(R.id.list_vehicles);

            editsearch =  view2.findViewById(R.id.searchView1);
            editsearch.setOnQueryTextListener(this);
            editsearch.setOnClickListener(this);
            dataAdapter2 = new CustomListList(getActivity(), vehicles_lists);
            listvehi.setAdapter(dataAdapter2);

            alertadd.setView(view2);
            alertadd.setNeutralButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    dataAdapter2.filter("");
                }
            });
            final android.support.v7.app.AlertDialog alertDialog = alertadd.create();
            alertDialog.show();
            listvehi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    list_index = position;
                    select_car.setText(vehicles_lists.get(position).getName());
                    device_id = vehicles_lists.get(position).getDeviceid();
                    dev_name = vehicles_lists.get(position).getName();
                    alertDialog.dismiss();
                    dataAdapter2.filter("");
                }
            });
            Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            nbutton.setTextColor(Color.parseColor("#7D8A92"));
            nbutton.setAllCaps(false);
            pbutton.setAllCaps(false);
        } catch (Exception e) {
        }
    }

    public void show_rep() {
        try {
            int report_position = report_type.getSelectedItemPosition();
            ReportList reportList = dataAdapterReport.getItem(report_position);
            set_last_select(report_position, device_id);
            if (Utils.isOnline(getContext())) {
                String key = reportList.getId();
                if (key.equals("1")) {
                    show_map.loadUrl(url + "/mobilelite.php?USER=" + name + "&PASS=" + pass + "&page=map");
                } else if (key.equals("8") && device_id.length() > 0) {
                    show_map.loadUrl(url + "/mobilelite.php?USER=" + name + "&PASS=" + pass + "&page=trip&vehicle_id=" + device_id + "&Time=" + get_time_today());
                } else if (key.equals("71") && device_id.length() > 0) {
                    show_map.loadUrl(url + "/mobilelite.php?USER=" + name + "&PASS=" + pass + "&page=map");
                } else if (key.equals("80") && device_id.length() > 0) {
                    show_map.loadUrl(url + "/mobilelite.php?USER=" + name + "&PASS=" + pass + "&page=trip_emp&vehicle_id=" + device_id + "&Time=" + get_time_today());
                }else{
                    collapse2();
                }
                int position = list_index;
                if (position >= 0) {
                    String ty = "1";
                    refreshreport2.setVisibility(View.VISIBLE);
                    ty = reportList.getId();
                    String id = device_id;
                    Report_List vehicles_list = new Report_List(name, pass, get_time_today(), ty, id);
                    vehicles_list.execute();
                    txt_units_name.setText(txt_date.getText());
                } else {
                    ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str16));
                    collapse2();
                }
            } else {
                ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str12));
                collapse2();

            }
        }catch (Exception e){}
    }

    public void set_last_select(int report_type, String id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Preferences.LAST_REPORT, String.valueOf(report_type));
        editor.putString(Preferences.LAST_USER, String.valueOf(id));
        editor.apply();
    }

    private String get_time_today() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", local);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date2 = null;
        try {
            date2 = (Date) formatter.parse(txt_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = (date2 != null ? date2.getTime() : 0) / 1000;
        return String.valueOf(time);
    }

    private void setdate(final Calendar calendar) {
        SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy", local);
        TextView txt_da =  view.findViewById(R.id.txt_date);
        txt_da.setText(postFormater.format(calendar.getTime()));
    }

    private void expand() {
        mLinearLayout.setVisibility(View.VISIBLE);
        imageView.setRotation((float) 180.0);
    }

    public void expand2(String lat, String lon) {
        collapse();
        mapLinearLayout.setVisibility(View.VISIBLE);
        show_map.loadUrl("javascript:moveto_loc('" + lon + "','" + lat + "')");
    }

    public void show_trip(String trip) {
        collapse();
        mapLinearLayout.setVisibility(View.VISIBLE);
        show_map.loadUrl("javascript:select_trip('" + trip + "')");
    }

    private void collapse() {
        mLinearLayout.setVisibility(View.GONE);
        imageView.setRotation((float) 0.0);
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
        datee = txt_date.getText().toString();
        if (view.getId() == R.id.add_rep) {
            Intent intent = new Intent(getContext(), Home.class);
            if (device_id != null && device_id.length() > 0) {
                intent.putExtra("id", device_id);
                intent.putExtra("user_name", dev_name);
                startActivity(intent);
            } else {
                ALL.show_custom_msg(getContext(), getActivity(), getResources().getString(R.string.str16));
            }
        } else if (view.getId() == R.id.txt_date) {
            Calendar c = Calendar.getInstance();
            if (datee.equals("")) {
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, mydatesetlistener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            } else {
                DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", local);
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
        } else if (view.getId() == R.id.but_generate) {
            hint_msg.setVisibility(View.GONE);
            collapse();
            collapse2();
            show_rep();
        } else if (view.getId() == R.id.map_end) {
            collapse2();
        } else if (view.getId() == R.id.map_size) {
            if (flag_zoom == 0) {
                flag_zoom = 1;
                ViewGroup.LayoutParams params = zoom.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                zoom.setLayoutParams(params);
                mapLinearLayout.setLayoutParams(params);
                show_map.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                map_size.setBackgroundResource(R.drawable.zoomout);
            } else {
                final float scale = getResources().getDisplayMetrics().density;
                flag_zoom = 0;
                ViewGroup.LayoutParams params = zoom.getLayoutParams();
                params.height = (int) (230 * scale);
                zoom.setLayoutParams(params);
                map_size.setBackgroundResource(R.drawable.size2);
            }
        } else if (view.getId() == R.id.select_car) {
            show_list();
        } else if (view.getId() == R.id.txt_sort) {
            sort_list();
        } else if (view.getId() == R.id.butt_previous) {
            next_previous_data(-1);
        } else if (view.getId() == R.id.butt_next) {
            next_previous_data(1);
        }
    }

    public void sort_list() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = prefs.getString(Preferences.SORT, "");
        if (sort.equals("1")) {
            sort = "0";
        } else {
            sort = "1";
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Preferences.SORT, sort);
        editor.apply();
        if (customadapterWorkhours != null) {
            customadapterWorkhours.sortlist();
        }
        if (customadapterReportWorkhours != null) {
            customadapterReportWorkhours.sortlist();
        }
        if (customadapterReportTrip != null) {
            customadapterReportTrip.sortlist();
        }
    }

    private class Report_List extends AsyncTask<Object, Object, String> {
        private String _userName;
        private String _password, _type, _iid;
        List<Event_item> list = new ArrayList<Event_item>();
        private String _date;
        private SharedPreferences prefs;

        LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

        public Report_List(String userName, String password, String date, String type, String iid) {
            _userName = userName;
            _password = password;
            _date = date;
            _type = type;
            _iid = iid;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            work_report_valueList = new ArrayList<>();
            visit_list_value = new ArrayList<>();
            work_hour_valueList = new ArrayList<>();
            report_trip_values = new ArrayList<>();
            stophouts_list_value = new ArrayList<>();
            idling_values = new ArrayList<>();
            cartList = new ArrayList<>();
            prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            next.setEnabled(false);
            previous.setEnabled(false);
        }

        @Override
        protected String doInBackground(Object... data) {
            try {
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "report"));
                nameValuePairs.add(new BasicNameValuePair("Time", _date));
                nameValuePairs.add(new BasicNameValuePair("report_type", _type));
                nameValuePairs.add(new BasicNameValuePair("vehicle_id", _iid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("report");
                        String flag = "0";
                        for (int i = 0; i < v.length(); i++) {
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            switch (_type) {
                                case "8":
                                case "80": {
                                    flag = "88";
                                    String driver = "error";
                                    if (jsonObject2.has("driver")) {
                                        driver = jsonObject2.optString("driver");
                                    }
                                    String from1 = jsonObject2.optString("from").replace("\\n", " \n ");
                                    String lines[] = from1.split("\\n");
                                    String from = lines[0];
                                    String odo_start = lines[1] + " " + getResources().getString(R.string.km);
                                    String time = jsonObject2.optString("time");
                                    String to1 = jsonObject2.optString("to").replace("\\n", " \n ");
                                    String lines2[] = to1.split("\\n");
                                    String to = lines2[0];
                                    String odo_end = lines2[1] + " " + getResources().getString(R.string.km);
                                    String trip = jsonObject2.optString("trip");
                                    String odometer = jsonObject2.optString("odometer") + getResources().getString(R.string.km);
                                    String maxspeed = jsonObject2.optString("maxspeed") + getResources().getString(R.string.km_h);
                                    String afffrom = jsonObject2.optString("afffrom");
                                    String addto = jsonObject2.optString("addto");
                                    report_trip_values.add(new Report_Trip_value(from, time, to, trip, odometer, maxspeed, afffrom, addto, driver, odo_start, odo_end));
                                    break;
                                }
                                case "1": {
                                    flag = "1";
                                    String driver = "error";
                                    if (jsonObject2.has("driver")) {
                                        driver = jsonObject2.optString("driver");
                                    }
                                    work_hour_valueList.add(new Report_work_hour_value(jsonObject2.optString("time"), jsonObject2.optString("igntion"), jsonObject2.optString("speed") + " " + getResources().getString(R.string.km_h), jsonObject2.optString("reason"), jsonObject2.optString("lon"), jsonObject2.optString("lat"), jsonObject2.optString("address"), (jsonObject2.optString("odometer") + " " + getResources().getString(R.string.km)), jsonObject2.optString("battary"), driver));
                                    break;
                                }
                                case "71":
                                    flag = "71";
                                    work_report_valueList.add(new Work_Report_Value(jsonObject2.optString("userid"), jsonObject2.optString("per_id"), jsonObject2.optString("report"), jsonObject2.optString("createdTime"), jsonObject2.optString("lon"), jsonObject2.optString("lat"), jsonObject2.optString("address"), jsonObject2.optString("msg_id"),
                                            jsonObject2.optString("userid"), jsonObject2.optString("type"), jsonObject2.optString("name"), jsonObject2.optString("done"), jsonObject2.optString("total"),
                                            jsonObject2.optString("notseen"), jsonObject2.optString("report_type"),
                                            jsonObject2.optString("customer"), jsonObject2.optString("all_replies"),
                                            jsonObject2.optString("skip"),jsonObject2.optString("geozoneid"),
                                            jsonObject2.optString("offlineID"),jsonObject2.optString("offlineloc_id"),
                                            jsonObject2.optString("doneaddress"),jsonObject2.optString("distance_write"),
                                            jsonObject2.optString("distance_done"),jsonObject2.optString("done_timestamp")));
                                    break;
                                case "98":
                                    flag = "98";
                                    visit_list_value.add(new Visit_value(jsonObject2.optString("time_in"), jsonObject2.optString("time_out"),
                                            jsonObject2.optString("address"), jsonObject2.optString("type"),
                                            jsonObject2.optString("lon"), jsonObject2.optString("lat"),
                                            jsonObject2.optString("id"), jsonObject2.optString("totaltime")));
                                    break;
                                case "19.0":
                                    flag = "19.0";
                                    stophouts_list_value.add(new StopHours_value(jsonObject2.optString("from"),
                                            jsonObject2.optString("to"), jsonObject2.optString("time"),
                                            jsonObject2.optString("afffrom"), jsonObject2.optString("trip")));
                                    break;
                                case "61.0": {
                                    flag = "61.0";
                                    idling_values.add(new Idling_value( jsonObject2.optString("trip"),  jsonObject2.optString("time"),  jsonObject2.optString("add"),  jsonObject2.optString("ignontime"),  jsonObject2.optString("ignoff"),
                                            jsonObject2.optString("point"),  jsonObject2.optString("stoptotaltime")));

                                    break;

                                }
                                case "97.3":
                                    flag = "97.3";
                                    cartList.add(new CartValues(jsonObject2.optString("RId"),
                                            jsonObject2.optString("type"),jsonObject2.optString("createtime"),
                                            jsonObject2.optString("totalPrice"),jsonObject2.optString("updatetime")
                                            ,jsonObject2.optString("geozoneID"),jsonObject2.optString("stockId")
                                            ,jsonObject2.optString("quantity") ,jsonObject2.optString("customerId"),
                                            jsonObject2.optString("stockeffecy_id"),jsonObject2.optString("AName"),
                                            jsonObject2.optString("EName"),jsonObject2.optString("createdby"))
                                    );
                                    break;
                            }
                        }
                        return flag;
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
        protected void onPostExecute(String result) {
            String sort = prefs.getString(Preferences.SORT, "");
            try {
                if (result != null) {
                    switch (result) {
                        case "88":
                            if (report_trip_values.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(report_trip_values);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                customadapterReportTrip = new Customadapter_report_trip(getContext(), report_trip_values);
                                list_report_work.setAdapter(customadapterReportTrip);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        case "71":
                            ALL.set_number_notification(getContext());
                            if (work_report_valueList != null && work_report_valueList.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(work_report_valueList);
                                }
                                list_report_work.setVisibility(View.VISIBLE);

                                customadapterWorkhours = new Customadapter_workhours(getContext(), work_report_valueList, false);
                                list_report_work.setAdapter(customadapterWorkhours);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        case "98":
                            ALL.set_number_notification(getContext());
                            if (visit_list_value.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(visit_list_value);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                customadapter_myvisit = new Customadapter_myvisit(getContext(), visit_list_value);
                                list_report_work.setAdapter(customadapter_myvisit);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        case "1":
                            if (work_hour_valueList.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(work_hour_valueList);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                customadapterReportWorkhours = new Customadapter_report_workhours(getContext(), work_hour_valueList);
                                list_report_work.setAdapter(customadapterReportWorkhours);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        case "19.0":
                            if (stophouts_list_value.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(work_hour_valueList);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                if (stophouts_list_value != null) {
                                    customadapterStopHours = new Customadapter_StopHours(getContext(), stophouts_list_value);
                                    list_report_work.setAdapter(customadapterStopHours);
                                    list_report_work.refreshDrawableState();
                                    no_reports.setVisibility(View.GONE);
                                }
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        case "61.0":
                            if (idling_values.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(work_hour_valueList);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                customadapterIdling = new Customadapter_Idling(getContext(), idling_values);
                                list_report_work.setAdapter(customadapterIdling);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;

                        case "97.3":
                            if (cartList.size() > 0) {
                                if (sort.equals("1")) {
                                    Collections.reverse(cartList);
                                }
                                list_report_work.setVisibility(View.VISIBLE);
                                listCartAdapter=new ListCartAdapter(getContext(),cartList,"show");
                                list_report_work.setAdapter(listCartAdapter);
                                list_report_work.refreshDrawableState();
                                no_reports.setVisibility(View.GONE);
                            } else {
                                no_reports.setVisibility(View.VISIBLE);
                                list_report_work.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            no_reports.setVisibility(View.VISIBLE);
                            list_report_work.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    no_reports.setVisibility(View.VISIBLE);
                    list_report_work.setVisibility(View.GONE);
                    collapse2();
                }
                linlaHeaderProgress.setVisibility(View.GONE);
                next.setEnabled(true);
                previous.setEnabled(true);
            }catch (Exception e){
                linlaHeaderProgress.setVisibility(View.GONE);
                next.setEnabled(true);
                previous.setEnabled(true);
            }
        }
    }


    public static Reports getIns() {
        return reports;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_customer, menu);
        return ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_customer) {
           Intent intent=new Intent(getContext(), ActivityCustomer.class);
           startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_customer);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String pitsponal = prefs.getString(Preferences.PITSONAL, "0");
        if(prefs.getString(Preferences.URL, "").equals("https://mpitsonal.pitstrack.com")|| pitsponal.equals("1")){
            settingsItem.setVisible(true);
        }else{
            settingsItem.setVisible(false);
        }
    }
}
