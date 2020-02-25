package com.lite.pits_jawwal.pitstracklite;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.lite.pits_jawwal.pitstracklite.DB.Insert_Vehicles;
import com.lite.pits_jawwal.pitstracklite.List.CustomArrayAdapter;
import com.lite.pits_jawwal.pitstracklite.List.SwipeListViewTouchListener;
import com.lite.pits_jawwal.pitstracklite.List.Vehicles_list;

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


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Vehicles extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {
    View view;
    ListView listView;
    SearchView editsearch;
    CustomArrayAdapter adapter;
    Button but_on, but_off, but_ref;
    private static Vehicles instance;
    private String name, pass;
    private String device_id = "0";
    List<Vehicles_list> list;
    List<Vehicles_list> list_all;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Timer timer;

    public Vehicles() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vehicles, container, false);
        listView = view.findViewById(R.id.listView);
        editsearch = view.findViewById(R.id.searchView);
        but_off = view.findViewById(R.id.but_off);
        but_off.setOnClickListener(this);
        but_ref = view.findViewById(R.id.but_ref);
        but_ref.setOnClickListener(this);
        but_on = view.findViewById(R.id.but_on);
        but_on.setOnClickListener(this);
        editsearch.setOnQueryTextListener(this);
        editsearch.setOnClickListener(this);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        instance = this;
        editsearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_on.setVisibility(View.VISIBLE);
                but_off.setVisibility(View.VISIBLE);
                but_ref.setVisibility(View.GONE);
            }
        });
        editsearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                but_on.setVisibility(View.GONE);
                but_off.setVisibility(View.GONE);
                but_ref.setVisibility(View.VISIBLE);
                try {
                    adapter.filter_key("");
                } catch (Exception e) {
                }
                return false;
            }
        });
        timer = new Timer();
        list = new ArrayList<>();
        list_all = new ArrayList<>();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");
        refresh();
        final PitsTrack main = PitsTrack.getInstance();
        SwipeListViewTouchListener touchListener =
                new SwipeListViewTouchListener(
                        listView,
                        new SwipeListViewTouchListener.OnSwipeCallback() {
                            @Override
                            public void onSwipeLeft(ListView listView, int[] reverseSortedPositions) {
                                int index = 0;
                                for (int i : reverseSortedPositions) {
                                    index = adapter.getItem(i).getIndex();
                                }
                                try {
                                    main.selectItemFromDrawer(1, String.valueOf(index));
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onSwipeRight(ListView listView, int[] reverseSortedPositions) {
                                try {
                                    for (int i : reverseSortedPositions) {
                                        device_id = adapter.get_id(i);
                                    }
                                    Intent intent = new Intent(getActivity(), Car_Info.class);
                                    intent.putExtra("device_id", device_id);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                            }

                        },
                        false, false);
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        String hint = prefs.getString(Preferences.SHOW_HINT, "");
        if (hint.equals("0")) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
            LayoutInflater factory = LayoutInflater.from(getContext());
            final View view2 = factory.inflate(R.layout.dialog_image, null);
            alertadd.setView(view2);
            alertadd.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertadd.setNeutralButton(getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Preferences.SHOW_HINT, "1");
                    editor.apply();
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertadd.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.show();
            Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            nbutton.setTextColor(Color.parseColor("#63D643"));
            pbutton.setTextColor(Color.parseColor("#ffffff"));
            nbutton.setAllCaps(false);
            pbutton.setAllCaps(false);
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                device_id = adapter.get_id(i);
                final int iindex = adapter.get_index(i);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());
                if (prefs.getString(Preferences.URL, "").equals("https://mpitsonal.pitstrack.com")) {
                    popup.getMenu().findItem(R.id.report3).setVisible(true);
                } else {
                    popup.getMenu().findItem(R.id.report3).setVisible(false);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        if (title.equals(getResources().getString(R.string.map))) {
                            main.selectItemFromDrawer(1, String.valueOf(iindex));
                        } else if (title.equals(getResources().getString(R.string.info))) {
                            Intent intent = new Intent(getActivity(), Car_Info.class);
                            intent.putExtra("device_id", device_id);
                            startActivity(intent);
                        } else if (title.equals(getResources().getString(R.string.report1))) {
                            main.selectItemFromDrawer(2, iindex + "," + "8");//2
                        } else if (title.equals(getResources().getString(R.string.report2))) {
                            main.selectItemFromDrawer(2, iindex + "," + "1");//1
                        } else if (title.equals(getResources().getString(R.string.Work_Reports))) {
                            main.selectItemFromDrawer(2, iindex + "," + "71");//3
                        } else if (title.equals(getResources().getString(R.string.event))) {
                            main.selectItemFromDrawer(3, String.valueOf(iindex));
                        }
                        return true;
                    }
                });
                popup.show();
                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        setRepeatingAsyncTask();
        return view;
    }

    public void refresh() {
        if (Utils.isOnline(getActivity())) {
            try {
                Vehicles_List vehicles_list = new Vehicles_List(name, pass, "0");
                vehicles_list.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
            }
        } else {
            final Insert_Vehicles db = new Insert_Vehicles(getContext());
            List<Vehicles_list> listlist = db.All_Vehicles();
            if (listlist != null) {
                show_vehicle_info(listlist);
            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        try {
            adapter.filter(text);
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.but_off) {
            editsearch.setQuery("Off", true);
        }
        if (view.getId() == R.id.but_on) {
            editsearch.setQuery("On", true);
        }
        if (view.getId() == R.id.searchView) {
            editsearch.setIconified(false);
        }
        if (view.getId() == R.id.but_ref) {
            refresh();
        }
    }

    public void show_map(String v_name) {
        final PitsTrack main = PitsTrack.getInstance();
        main.selectItemFromDrawer(1, v_name);
    }

    public static Vehicles getInstance() {
        return instance;
    }

    private class Vehicles_List extends AsyncTask<Object, Object, List<Vehicles_list>> {
        private String _userName;
        private String _password;
        private String _flag;
        List<Vehicles_list> rowItems = new ArrayList<>();
        LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);

        public Vehicles_List(String userName, String password, String flag) {
            _userName = userName;
            _password = password;
            _flag = flag;
            if (_flag.equals("0")) {
                linlaHeaderProgress.setVisibility(View.VISIBLE);
            }
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected List<Vehicles_list> doInBackground(Object... data) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                ///https://mjo.pitstrack.com/mobilelite.php?USER=alwaly&PASS=alwaly123&page=vehicles
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "vehicles"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("vehicles");
                        for (int i = 0; i < v.length(); i++) {
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            String name = "";
                            if (jsonObject2.has("driver")) {
                                name += jsonObject2.optString("driver");
                            }
                            Vehicles_list item = new Vehicles_list(jsonObject2.optString("last_update"), jsonObject2.optString("name_vehicle"), jsonObject2.optString("active"), jsonObject2.optString("address_vehicle"), jsonObject2.optString("id_vehicle"), jsonObject2.optString("lat"), jsonObject2.optString("lon"), jsonObject2.optString("icon"), i, jsonObject2.optString("speed"), name,jsonObject2.optString("phone"));
                            rowItems.add(item);
                        }
                        return rowItems;
                    }
                }
                int i;
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
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Vehicles_list> result) {
            try {
                show_vehicle_info(result);
                linlaHeaderProgress.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } catch (Exception e) {
            }
            show_vehicle_info(result);
        }
    }

    public void show_vehicle_info(List<Vehicles_list> result) {
        if (result != null) {
            if (!result.isEmpty()) {
                try {
                    list = result;
                    list_all.clear();
                    list_all.addAll(list);
                    adapter = new CustomArrayAdapter(getActivity(), result,getContext());
                    listView.setAdapter(adapter);
                    final Insert_Vehicles db = new Insert_Vehicles(getContext());
                    db.insert(result);
                } catch (Exception e) {
                    return;
                }
            }
        } else {
        }
    }

    public List<Vehicles_list> get_List() {
        return list_all;
    }
    private void setRepeatingAsyncTask() {
        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            String name = prefs.getString(Preferences.USER_NAME, "");
                            String pass = prefs.getString(Preferences.PASSWORD, "");
                            Vehicles_List vehicles_list = new Vehicles_List(name, pass, "1");
                            vehicles_list.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 90 * 1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }
}
