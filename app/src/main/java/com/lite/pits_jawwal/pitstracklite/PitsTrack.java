package com.lite.pits_jawwal.pitstracklite;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Customers.FragmentCustomer;
import com.lite.pits_jawwal.pitstracklite.DB.Insert_Vehicles;
import com.lite.pits_jawwal.pitstracklite.Firebase.PitsRegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ruba-PITS on 6/3/2017.
 */

public class PitsTrack extends AppCompatActivity {
    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private static PitsTrack instance;
    Locale locale;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    public static String IntentExtra = " intentExtra";
    private int _selectedItem = 0;
    private FrameLayout show_h;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView username;
    String car_info, profile, vehicles, map, evevt, report, feedback, Logout,customer = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        show_h = findViewById(R.id.fragmaent);
        username = findViewById(R.id.username);
        instance = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = prefs.getString(Preferences.language1, "");
        String nn = prefs.getString(Preferences.USER_NAME, "");
        username.setText(nn);
        switch (lang.trim()) {
            case "1":
                vehicles = "Units";
                map = "Map";
                profile = "Profile";
                evevt = "Events";
                feedback = "Feedback";
                Logout = "Logout";
                report = "Reports";
                car_info = "Car Information";
                customer="Customer";
                locale = new Locale("en-US");
                break;
            case "0":
                vehicles = "وحدات التتبع";
                map = "الخريطة";
                profile = "الصفحة الشخصية";
                evevt = "التنبيهات";
                feedback = "التقييم";
                Logout = "تسجيل خروج";
                report = "التقارير";
                car_info = "معلومات المركبة";
                customer="الزبائن";
                locale = new Locale("ar");
                break;
            default:
                locale = new Locale("en-US");
                show_dialog();
                break;
        }
        Locale.setDefault(locale);
        //Configuration config = new Configuration();
       // config.locale = locale;
        String pitsponal = prefs.getString(Preferences.PITSONAL, "0");
       // this.getApplicationContext().getResources().updateConfiguration(config, null);
        mNavItems.add(new NavItem(vehicles, R.drawable.cara_menu,0));
        mNavItems.add(new NavItem(map, R.drawable.map_menu,1));
        mNavItems.add(new NavItem(report, R.drawable.report_menu2,2));
        mNavItems.add(new NavItem(evevt, R.drawable.event_menu,3));
        mNavItems.add(new NavItem(profile, R.drawable.profile_menu,4));
        mNavItems.add(new NavItem(feedback, R.drawable.rating_menu,5));
        if (prefs.getString(Preferences.URL, "").equals("https://mpitsonal.pitstrack.com") || pitsponal.equals("1")) {
            mNavItems.add(new NavItem(customer, R.drawable.menu_customer, 7));
        }
        mNavItems.add(new NavItem(Logout, R.drawable.logout_menu,6));
        mDrawerLayout =  findViewById(R.id.drawerLayout);
        mDrawerPane =  findViewById(R.id.drawerPane);
        mDrawerList =  findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String lan = prefs.getString(Preferences.language1, "");
                if (!lan.trim().equals("1") && !lan.trim().equals("0")) {
                    show_dialog();
                }
                selectItemFromDrawer(mNavItems.get(position)._id, "");
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("DR", "onDrawerClosed: " + getTitle());
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (checkPlayServices()) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,Manifest.permission.CALL_PHONE
            ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 18888);
//            if (Utils.isOnline(this)) {
//                Intent intent = new Intent(this, PitsRegistrationIntentService.class);
//                startService(intent);
//            }
        }
        Bundle extras = getIntent().getExtras();
        String data = extras != null ? extras.getString(PitsTrack.IntentExtra) : null;
        if (data!=null && data.equals("Login")) {
            selectItemFromDrawer(0, "");
        } else if (data!=null && data.contains("report")) {
            selectItemFromDrawer(0, "");
            Intent intent = new Intent(this, Replay_Report.class);
            String msgid = extras.getString("msgid");
            String offlineid = extras.getString("offlineid");
            String userid = extras.getString("userid");
            Work_Report_Value value = new Work_Report_Value("", "", "", "",
                    "", "", "", msgid, userid, "", "", "",
                    "", "", "", "", "","","",offlineid,"","","","","");
            intent.putExtra("report", value);
            intent.putExtra("msgid", msgid);
            intent.putExtra("msgid", userid);
            intent.putExtra("offlineid", offlineid);
            startActivity(intent);
        } else {
            selectItemFromDrawer(3, "F");
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
            }
        });
        try {
        } catch (Exception e) {
        }
    }
    public void show_dialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PitsTrack.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Authentication Failed");
        alertDialog.setMessage("It seems like your password has been change. If you didn't change your password, please contact PITS!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectItemFromDrawer(6, "");
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public void selectItemFromDrawer(int position, String veh) {
        String title="";
        try {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (position == 0) {
                title=vehicles;
                show_h.setVisibility(View.VISIBLE);
                Vehicles vehicles = new Vehicles();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, vehicles, vehicles.getClass().getName()).commit();
            } else if (position == 1) {
                title=map;
                show_h.setVisibility(View.VISIBLE);
                Map map = new Map();
                Bundle bundle = new Bundle();
                bundle.putString("index", veh);
                map.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, map, map.getClass().getName()).addToBackStack(map.getClass().getName()).commit();
            } else if (position == 2) {
                title=report;
                show_h.setVisibility(View.VISIBLE);
                Reports reports = new Reports();
                Bundle bundle = new Bundle();
                bundle.putString("index", veh);
                reports.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, reports, reports.getClass().getName()).addToBackStack(reports.getClass().getName()).commit();
            } else if (position == 3) {
                title=evevt;
                show_h.setVisibility(View.VISIBLE);
                Event event = new Event();
                Bundle bundle = new Bundle();
                bundle.putString("index", veh);
                event.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, event, event.getClass().getName()).addToBackStack(event.getClass().getName()).commit();
            } else if (position == 4) {
                title=profile;
                show_h.setVisibility(View.VISIBLE);
                Profile profile = new Profile();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, profile, profile.getClass().getName()).addToBackStack(profile.getClass().getName()).commit();
            } else if (position == 5) {
                title=feedback;
                show_h.setVisibility(View.VISIBLE);
                Feedback fed = new Feedback();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, fed, fed.getClass().getName()).addToBackStack(fed.getClass().getName()).commit();
            }else if (position == 7) {
                title=customer;
                show_h.setVisibility(View.VISIBLE);
                FragmentCustomer customer = new FragmentCustomer();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmaent, customer, customer.getClass().getName()).addToBackStack(customer.getClass().getName()).commit();
            }
            else if (position == 6) {
                title=Logout;
                if (Utils.isOnline(this)) {
                    Intent startServiceIntent = new Intent(PitsTrack.this, PitsRegistrationIntentService.class);
                    stopService(startServiceIntent);
                    startService(startServiceIntent);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Preferences.URL, "url");
                    editor.putString(Preferences.USER_NAME, "");
                    editor.putString(Preferences.PASSWORD, "");
                    editor.putBoolean(Preferences.HAS_LOGIN, false);
                    editor.putString(Preferences.FULL_NAME, "");
                    editor.putString(Preferences.ADDREES, "");
                    editor.putString(Preferences.PHONE, "");
                    editor.putString(Preferences.MOBILE, "");
                    editor.putString(Preferences.EMAIL, "");
                    editor.putString(Preferences.language, "");
                    editor.putString(Preferences.GMT, "");
                    editor.apply();
                    Insert_Vehicles vehicles = new Insert_Vehicles(getApplicationContext());
                    vehicles.Delete_all();
                    Intent intent = new Intent(PitsTrack.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    cancel_notification();
                    return;
                } else {
                    ALL.show_custom_msg(getApplicationContext(), PitsTrack.this, getResources().getString(R.string.str12));
                }
            }else if(position==8){
                try {
                try {
                    startActivity(getPackageManager().getLaunchIntentForPackage("com.pitsonal.pits_jawwal.pitslocation"));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.pitsonal.pits_jawwal.pitslocation")));
                }

                }catch (Exception e){}
            }
            DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
            mDrawerList.setAdapter(adapter);
            mDrawerList.setItemChecked(position, true);
            setTitle(title);
            mDrawerLayout.closeDrawer(mDrawerPane);
            _selectedItem = position;
        } catch (Exception e) {
        }
    }

    private class NavItem {
        private String _title;
        private int _imageSrc,_id;

        public NavItem(String title, int imageSrc,int id) {
            _title = title;
            _imageSrc = imageSrc;
            _id=id;
        }
    }

    private class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            } else {
                view = convertView;
            }
            TextView titleView =  view.findViewById(R.id.title);
            ImageView imageView =  view.findViewById(R.id.icon);
            imageView.setImageResource(mNavItems.get(position)._imageSrc);
            titleView.setText(mNavItems.get(position)._title);

            if (mNavItems.get(position)._id == _selectedItem) {
                titleView.setTextColor(Color.parseColor("#000000"));
                view.setBackgroundColor(Color.parseColor("#E6E6E6"));

            } else {
                titleView.setTextColor(Color.parseColor("#000000"));
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            return view;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public static PitsTrack getInstance() {
        return instance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 18888) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    Intent intent = new Intent(this, PitsRegistrationIntentService.class);
                    startService(intent);
                    // ContextCompat.startForegroundService(this,intent);
                }catch (Exception e){}
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        Lang lang = new Lang();
        lang.lan();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount() - 1; ++i) {
                fm.popBackStack();
            }
            selectItemFromDrawer(0, "");
            mDrawerList.setItemChecked(0, true);
            setTitle(mNavItems.get(0)._title);
            super.onBackPressed();
        }
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
    public void cancel_notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

}
