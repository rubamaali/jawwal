package com.lite.pits_jawwal.pitstracklite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.List.CustomListList;
import com.lite.pits_jawwal.pitstracklite.List.Vehicles_list;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    View view;
    WebView webview;
    TextView select_vehicle;
    private Button go_info;
    SearchView editsearch;
    private int list_index;
    private String device_id = "0";
    private static Map instance;
    private String name, pass, url;
    private CustomListList dataAdapter;
    private List<Vehicles_list> vehicles_lists;
    private Vehicles vehicles;

    public Map() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_map, container, false);
        String strtext = getArguments().getString("index");
        WebClient webClient = new WebClient(getActivity());
        webview = view.findViewById(R.id.map);
        go_info = view.findViewById(R.id.map_info);
        go_info.setOnClickListener(this);
        go_info.setVisibility(View.GONE);
        webview.setWebViewClient(webClient);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setVisibility(View.GONE);
        select_vehicle = view.findViewById(R.id.vehicle);
        instance = this;
        select_vehicle.setOnClickListener(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        url = prefs.getString(Preferences.URL, "");
        name = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");

        try {
            vehicles = Vehicles.getInstance();
            vehicles_lists = vehicles.get_List();
        } catch (Exception e) {
        }
        if (strtext != null && strtext.length() >= 1) {
            try {
                list_index = Integer.parseInt(strtext);
                select_vehicle.setText(vehicles_lists.get(list_index).getName());
                device_id = vehicles_lists.get(list_index).getDeviceid();
                go_info.setVisibility(View.VISIBLE);
            } catch (Exception e) {
            }
        }
        if (Utils.isOnline(getContext())) {
            show_online_map();
        }
        return view;

    }

    public void show_online_map() {
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl(url + "/mobilelite.php?USER=" + name + "&PASS=" + pass + "&page=map");
    }

    public void show_list() {
        try {
            final AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
            LayoutInflater factory = LayoutInflater.from(getContext());
            final View view2 = factory.inflate(R.layout.listvehicles, null);
            final ListView listvehi = view2.findViewById(R.id.list_vehicles);
            editsearch = view2.findViewById(R.id.searchView1);
            editsearch.setOnQueryTextListener(this);
            editsearch.setOnClickListener(this);
            if (vehicles_lists != null) {
                dataAdapter = new CustomListList(getActivity(), vehicles_lists);
            }
            listvehi.setAdapter(dataAdapter);
            alertadd.setView(view2);
            alertadd.setNeutralButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if(dataAdapter!=null)
                    dataAdapter.filter("");

                }
            });
            final AlertDialog alertDialog = alertadd.create();
            alertDialog.show();
            listvehi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    list_index = vehicles_lists.get(position).getIndex();
                    select_vehicle.setText(vehicles_lists.get(position).getName());
                    device_id = vehicles_lists.get(position).getDeviceid();
                    go_info.setVisibility(View.VISIBLE);
                    show_location("");
                    dataAdapter.filter("");
                    alertDialog.dismiss();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vehicle) {
            show_list();
        } else if (view.getId() == R.id.map_info) {
            Intent intent = new Intent(getActivity(), Car_Info.class);
            intent.putExtra("device_id", vehicles_lists.get(list_index).getDeviceid());
            startActivity(intent);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(dataAdapter!=null) {
            dataAdapter.filter(s);
        }
        return false;
    }

    public void show_location(String flag) {
        if (!device_id.equals("0")) {
            webview.loadUrl("javascript:car_moveto('" + device_id + "','" + flag + "')");

        }
    }

    public static Map getInstance() {
        return instance;
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
}
