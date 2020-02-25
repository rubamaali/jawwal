package com.lite.pits_jawwal.pitstracklite;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    private TextView url;
    private AutoCompleteTextView _userText;
    private EditText _passText;
    private int position = 0;
    private Button login;
    private String mUrl, mUserName, mPassword, USER_NAME2;
    private Callback _callback;
    private String isNotificaiton = "0";
    private String[] rem_name = new String[1];
    private String msgid = "0";
    private String offlineid = "0";
    private String userid = "0";
    private ProgressDialog _progress;
    private RelativeLayout pagehome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        String notification = getIntent().getStringExtra(PitsTrack.IntentExtra);
        if (notification != null && notification.equalsIgnoreCase("notification")) {
            isNotificaiton = "1";
        } else if (notification != null && notification.equalsIgnoreCase("report")) {
            isNotificaiton = "2";
            msgid = getIntent().getStringExtra("msgid");
            offlineid = getIntent().getStringExtra("offlineid");
            userid = getIntent().getStringExtra("userid");
        }


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login =  findViewById(R.id.but_login);
        pagehome =  findViewById(R.id.pagehome2);
        pagehome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        login.setOnClickListener(this);
        _userText = findViewById(R.id.txt_username);
        _passText =  findViewById(R.id.txt_pass);
        if (hasCredentials()) {
            loginSuccessful();
            return;
        }
        url =  findViewById(R.id.url);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        _callback = new Callback() {
            @Override
            public void onDone(int response) {
                _progress.dismiss();
                if (response == 1) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Preferences.URL, mUrl);
                    editor.putString(Preferences.USER_NAME, mUserName);
                    editor.putString(Preferences.USER_NAME2, mUserName);
                    editor.putString(Preferences.PASSWORD, mPassword);
                    editor.putString(Preferences.SHOW_HINT, "0");
                    editor.putBoolean(Preferences.HAS_LOGIN, true);
                    editor.putString(Preferences.SORT, "0");
                    editor.putString(Preferences.MAP_TYPE, "on");
                    editor.putString(Preferences.LAST_REPORT, "-1");
                    editor.putString(Preferences.LAST_USER, "-1");
                    editor.putString(Preferences.SOUND, "1");
                    editor.apply();
                    loginSuccessful();
                    ALL.set_number_notification(getApplicationContext());
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ALL.show_custom_msg(getApplicationContext(), MainActivity.this, "Wrong user name or password.");
                        _userText.requestFocus();
                    }
                });
            }
        };

    }

    private boolean hasCredentials() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER_NAME2 = sharedPreferences.getString(Preferences.USER_NAME2, "");
        if (USER_NAME2.length() > 2) {
            rem_name[0] = USER_NAME2;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rem_name);
            _userText.setThreshold(1);
            _userText.setAdapter(adapter);
        }
        return sharedPreferences.getBoolean(Preferences.HAS_LOGIN, false);
    }

    private void loginSuccessful() {
        Intent intent = new Intent(MainActivity.this, PitsTrack.class);
        if (isNotificaiton.contains("1")) {
            intent.putExtra(PitsTrack.IntentExtra, "notification");
        } else if (isNotificaiton.contains("2")) {
            intent.putExtra(PitsTrack.IntentExtra, "report");
            intent.putExtra("msgid", msgid);
            intent.putExtra("userid", userid);
            intent.putExtra("offlineid", offlineid);

        } else {
            intent.putExtra(PitsTrack.IntentExtra, "Login");
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        position = newVal;
    }

    public void show() {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("");
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        final String[] data = new String[]{"p4.pitstrack.com", "p6.pitstrack.com", "jo.pitstrack.com", "car.pitstrack.com", "pitstrack.com", "school.pitstrack.com", "schools.pitstrack.com", "pitsonal.pitstrack.com", "portal.pitstrack.com"};
        np.setMinValue(0);
        np.setMaxValue(data.length - 1);
        np.setDisplayedValues(data);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        np.setValue(position);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url.setText(String.valueOf(data[np.getValue()]));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onClick(View view) {
         final String[] data2 = new String[]{"https://m.pitstrack.com", "https://mp6.pitstrack.com", "https://mjo.pitstrack.com", "https://mcar.pitstrack.com", "https://mp5.pitstrack.com", "https://mschool.pitstrack.com", "https://mschooltest.pitstrack.com", "https://mpitsonal.pitstrack.com", "https://mportal.pitstrack.com"};
        if (view.getId() == R.id.but_login) {
            if (Utils.isOnline(getApplicationContext())) {
                mPassword = _passText.getText().toString();
                mUserName = _userText.getText().toString();
                //mUrl = url.getText().toString();
                mUrl="https://mpitstrack.jawwal.ps";
              // mUrl="https://mpitsonal.pitstrack.com";
//                if (mUrl.isEmpty()) {
//                    ALL.show_custom_msg(getApplicationContext(), MainActivity.this, "Server URL can't be empty");
//                    return;
//                }
                if (mUserName.isEmpty()) {
                    _userText.setError("User name can't be empty");
                    _userText.requestFocus();
                    return;
                }

                if (mPassword.isEmpty()) {
                    _passText.setError("Password can't be empty");
                    _passText.requestFocus();
                    return;
                }
                if (_progress != null) {
                    if (_progress.isShowing()) {
                        _progress.dismiss();
                    }
                    _progress = null;
                }
                _progress = new ProgressDialog(MainActivity.this);
                _progress.setMessage("Loading ...");
                _progress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
                _progress.show();

               // mUrl = data2[position];

                LogTask task = new LogTask(mUserName, mPassword, mUrl, _callback);
                task.execute();
            } else {
                ALL.show_custom_msg(getApplicationContext(), MainActivity.this, getResources().getString(R.string.str12));
            }
        }
    }

    private interface Callback {
        void onDone(int response);
    }

    public class LogTask extends AsyncTask<Void, Void, Void> {
        private String _userName;
        private String _password;
        private String _url;
        private Callback _callback;

        public LogTask(String userName, String password, String url, Callback callback) {
            _userName = userName;
            _password = password;
            _url = url;
            _callback = callback;
        }

        @Override
        protected Void doInBackground(Void... data) {
            String urlFormat = _url + "/loginclean.php?USER=" + _userName + "&PASS=" + _password;
            //https://mpitstrack.jawwal.ps/loginclean.php?USER=&PASS=
            String urlFormat2 = _url + "/lan.php?USER=" + _userName + "&PASS=" + _password;
            try {
                URL url = new URL(urlFormat);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("");
                writer.flush();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                String line = reader.readLine();
                writer.close();
                reader.close();
                if (line.contains("1")) {
                    line = "1";
                } else {
                    line = "0";
                }
                if (line.equals("1")) {
                    URL url2 = new URL(urlFormat2);
                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    conn2.setRequestMethod("POST");
                    conn2.setDoOutput(true);
                    OutputStreamWriter writer2 = new OutputStreamWriter(conn2.getOutputStream());
                    writer2.write("");
                    writer2.flush();
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                    String line2 = reader2.readLine();
                    if (line2.contains("1")) {
                        line2 = "1";
                    } else {
                        line2 = "0";
                    }
                    writer2.close();
                    reader2.close();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Preferences.language1, line2);
                    editor.apply();
                }
                _callback.onDone(Integer.parseInt(line));

            } catch (java.net.ProtocolException e) {
                _callback.onDone(0);
            } catch (MalformedURLException e) {
                _callback.onDone(0);
            } catch (IOException e) {
                _callback.onDone(0);
            } catch (Exception e) {
                _callback.onDone(0);
            }
            return null;
        }
    }
    public void setArabicLocale() {
        Locale arabicLocale = new Locale("ar");
        Locale.setDefault(arabicLocale);
        Configuration anConfiguration = new Configuration();
        anConfiguration.locale = arabicLocale;
        getResources().updateConfiguration(anConfiguration, getResources().getDisplayMetrics());
    }



    public void setEnglishLocale() {
        Locale arabicLocale = new Locale("en");
        Locale.setDefault(arabicLocale);
        Configuration anConfiguration = new Configuration();
        anConfiguration.locale = arabicLocale;
        getResources().updateConfiguration(anConfiguration, getResources().getDisplayMetrics());

    }
}
