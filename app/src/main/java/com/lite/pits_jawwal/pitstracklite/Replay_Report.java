package com.lite.pits_jawwal.pitstracklite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Report_Custom.Custom_report_replay;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Report_replay_value;
import com.lite.pits_jawwal.pitstracklite.Report_Custom.Work_Report_Value;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by Ruba-PITS on 2/27/2018.
 */

public class Replay_Report extends AppCompatActivity implements View.OnClickListener {
    TextView txt_report, address, txt_time, name, txt_sig, txt_camera, report_type,customer;
    private CheckBox checkbox;
    Button button_send;
    private Custom_report_replay customReportReplay;
    private EditText chatbox;
    private ListView list_msg;
    private SharedPreferences prefs;
    private String user, pass, msgid, userid,offlineID;
    private Send_Reply report_reply_send = null;
    private Locale local = new Locale("en-US");
    private SwipyRefreshLayout swipyrefreshlayout;
    private static Replay_Report replay_report;
    private CardView report;
    public static Replay_Report getReplay_report() {
        return replay_report;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config config= Config.getInstance(this);
        config.setAppLocale();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_replay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.report_replay));
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user = prefs.getString(Preferences.USER_NAME, "");
        pass = prefs.getString(Preferences.PASSWORD, "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.apply();
        ALL.set_number_notification(this);
        ShortcutBadger.applyCount(this, 0);
        Work_Report_Value workReportValue = (Work_Report_Value) getIntent().getSerializableExtra("report");
        declaration();
        hid();
        set_msg_info(workReportValue);
        show_report_replies();
        replay_report = this;

    }

    public void set_msg_info(Work_Report_Value workReportValue) {
        if (workReportValue != null) {
            msgid = workReportValue.getMsgid();
            offlineID=workReportValue.getOfflineID();
            user = prefs.getString(Preferences.USER_NAME, "");
            pass = prefs.getString(Preferences.PASSWORD, "");
            userid = workReportValue.getUserid();
        }
    }

    public void set_msg_info2(Work_Report_Value workReportValue) {
        if (workReportValue != null) {
            if(workReportValue.getAddress().length()>1) {
                address.setText(workReportValue.getAddress());
                address.setVisibility(View.VISIBLE);
            }else{
                address.setVisibility(View.GONE);
            }
           txt_time.setText(workReportValue.getCreatedTime());
            name.setText(workReportValue.getPer_name());
            String type_name = workReportValue.getName();
            String txt = workReportValue.getReport();
            txt_report.setText(txt);
            report_type.setText(type_name);
            if (workReportValue.getCustomer() != null && workReportValue.getCustomer().length() > 0 && !workReportValue.getCustomer().equals("null"))  {
                customer.setText(workReportValue.getCustomer());
                customer.setVisibility(View.VISIBLE);
            }else{
                customer.setVisibility(View.GONE);
            }
            if (!workReportValue.getReport_type().equals("1")) {
                checkbox.setVisibility(View.VISIBLE);
                if (workReportValue.getDone().equals("1")) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            } else {
                checkbox.setVisibility(View.GONE);
            }
        }
        report.setVisibility(View.VISIBLE);
    }

    public void declaration() {
        txt_report =  findViewById(R.id.txt_report);
        report_type =  findViewById(R.id.report_type);
        customer =  findViewById(R.id.customer);
        address =  findViewById(R.id.address);
        txt_time =  findViewById(R.id.txt_time);
        name =  findViewById(R.id.name);
        txt_camera =  findViewById(R.id.txt_camera);
        txt_camera.setOnClickListener(this);
        txt_sig =  findViewById(R.id.txt_sig);
        txt_sig.setOnClickListener(this);
        checkbox =  findViewById(R.id.checkbox);
        report =  findViewById(R.id.report);
        report.setVisibility(View.INVISIBLE);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end,
                                       Spanned d, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(src.charAt(i)) && !Character.isDigit(src.charAt(i)) && !Character.isSpaceChar(src.charAt(i))) {
                        return src.subSequence(start, i);
                    }
                }
                return null;
            }
        };
        chatbox =  findViewById(R.id.chatbox);
        chatbox.setFilters(new InputFilter[]{filter});
        button_send =  findViewById(R.id.button_send);
        button_send.setOnClickListener(this);
        customReportReplay = new Custom_report_replay(getApplicationContext(), R.layout.replay_right, Replay_Report.this);
        list_msg =  findViewById(R.id.list_msg);
        list_msg.setAdapter(customReportReplay);
        swipyrefreshlayout =  findViewById(R.id.swipyrefreshlayout);
        swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                show_report_replies();
            }
        });
    }

    public void hid() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (in != null) {
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_send) {
            sendChatMessage();
        } else if (view.getId() == R.id.txt_camera) {
            dispatchTakePictureIntent();
        } else if (view.getId() == R.id.txt_sig) {
            Intent intent = new Intent(this, Signature.class);
            startActivity(intent);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.pitstracklite_jawwal.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("en-US")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean sendChatMessage() {
        String ms = chatbox.getText().toString();
        if (ms.length() > 0) {
            user = prefs.getString(Preferences.USER_NAME, "");
            pass = prefs.getString(Preferences.PASSWORD, "");
            customReportReplay.add(new Report_replay_value(ms, get_time(), user, "2", "0", "0", ""));
            send_msg_img(ms, "");
            chatbox.setText("");
            return true;
        } else {
            return false;
        }
    }

    public void setImage(Bitmap s) {
        customReportReplay.add(new Report_replay_value("", get_time(), user, "2", "1", "1", s));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        s.compress(Bitmap.CompressFormat.PNG, 0, stream);

        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        if (encodedImage != null && encodedImage.length() > 0) send_msg_img(null, encodedImage);

    }

    private void send_msg_img(String msg, String img) {
        if (Utils.isOnline(getApplicationContext())) {
            report_reply_send = new Send_Reply(user, pass, msgid, msg, get_timestamp(), userid, img);
            report_reply_send.execute();
        } else {
            ALL.show_custom_msg(this, Replay_Report.this, getResources().getString(R.string.str17));
        }
    }

    private String get_timestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date2 = null;
        try {
            date2 =  formatter.parse(get_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = (date2 != null ? date2.getTime() : 0) / 1000;
        return String.valueOf(time);
    }

    public String get_time() {
        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", local);
        return postFormater.format(new Date());
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

    public void show_report_replies() {
        if (Utils.isOnline(getApplicationContext())) {
            Report_Reply_List report_reply_list = new Report_Reply_List(user, pass, msgid,offlineID);
            report_reply_list.execute();
        } else {
            ALL.show_custom_msg(getApplicationContext(), Replay_Report.this, getResources().getString(R.string.str12));
        }
    }

    private class Report_Reply_List extends AsyncTask<Object, Object, String> {
        private String _userName;
        private String _password, _msg,_offlineID;
        private Work_Report_Value workReportValue2;
        List<Report_replay_value> list = new ArrayList<Report_replay_value>();
        private SharedPreferences prefs;
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        public Report_Reply_List(String userName, String password, String msg,String offlineID) {
            _userName = userName;
            _password = password;
            _msg = msg;
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            customReportReplay.clear_all();
            _offlineID=offlineID;
        }

        @Override
        protected String doInBackground(Object... data) {
            try {
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "report_replies"));
                nameValuePairs.add(new BasicNameValuePair("msg_id", _msg));
                nameValuePairs.add(new BasicNameValuePair("offlineID", _offlineID));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String resString = EntityUtils.toString(resEntity);
                    if (resString != null) {
                        JSONObject jsonObject = new JSONObject(resString);
                        JSONArray v = jsonObject.getJSONArray("report_replies");
                        String flag = "0";
                        for (int i = 0; i < v.length(); i++) {
                            flag = "1";
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            workReportValue2 = new Work_Report_Value(jsonObject2.optString("userid"), jsonObject2.optString("per_id"), jsonObject2.optString("report"), jsonObject2.optString("createdTime"), jsonObject2.optString("lon"), jsonObject2.optString("lat"), jsonObject2.optString("address"), jsonObject2.optString("msgid"),
                                    jsonObject2.optString("userid"), jsonObject2.optString("type"),
                                    jsonObject2.optString("name"), jsonObject2.optString("done"), "", "",
                                    jsonObject2.optString("report_type"), jsonObject2.optString("customer"),
                                    "",jsonObject2.optString("skip"),jsonObject2.optString("geozoneid"),
                                    jsonObject2.optString("offlineID"),jsonObject2.optString("offlineloc_id"),
                                    jsonObject2.optString("doneaddress"),jsonObject2.optString("distance_write"),
                                    jsonObject2.optString("distance_done"),jsonObject2.optString("done_timestamp"));
                            JSONArray array2 = new JSONArray(v.getJSONObject(i).getString("msg_replies"));
                            for (int j = 0; j < array2.length(); j++) {
                                JSONObject jsonObject3 = array2.getJSONObject(j);
                                String img_string = jsonObject3.optString("image");
                                String have_image = "0";
                                String url_ = url + "/" + img_string;
                                if (url_.contains("jpg")) {
                                    have_image = "1";
                                }
                                Report_replay_value value = new Report_replay_value(jsonObject3.optString("msg"), jsonObject3.optString("time"), jsonObject3.optString("userid"), "1", jsonObject3.optString("lastseen"), have_image, url_);
                                list.add(value);
                            }
                        }
                        return flag;
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            swipyrefreshlayout.setRefreshing(false);
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result != null) {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        customReportReplay.add(list.get(i));
                    }
                    scrollMyListViewToBottom();
                }
            }
            if (workReportValue2 != null) {
                set_msg_info2(workReportValue2);
            }
        }
    }
    private class Send_Reply extends AsyncTask<Object, Object, String> {
        private String _userName;
        private String _password, _msgid, _msg, _time, _userid, _img;
        private SharedPreferences prefs;

        public Send_Reply(String userName, String password, String msgid, String msg, String time, String userid, String img) {
            _userName = userName;
            _password = password;
            _msgid = msgid;
            _msg = msg;
            _time = time;
            _userid = userid;
            _img = img;
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }

        @Override
        protected String doInBackground(Object... data) {
            try {
                String url = prefs.getString(Preferences.URL, "");
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
                nameValuePairs.add(new BasicNameValuePair("USER", _userName));
                nameValuePairs.add(new BasicNameValuePair("PASS", _password));
                nameValuePairs.add(new BasicNameValuePair("page", "send_replies"));
                nameValuePairs.add(new BasicNameValuePair("msg_id", _msgid));
                nameValuePairs.add(new BasicNameValuePair("msg", _msg));
                nameValuePairs.add(new BasicNameValuePair("image", _img));
                nameValuePairs.add(new BasicNameValuePair("time", _time));
                nameValuePairs.add(new BasicNameValuePair("userid", _userid));
                nameValuePairs.add(new BasicNameValuePair("imei", Utils.getIMEI(getApplicationContext())));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity);
                    if (result != null) {
                        String value;
                        if (result.contains("0")) {
                            value = "0";
                        } else {
                            value = "1";
                        }
                        return value;
                    }
                    return null;
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            } catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.contains("1")) {
                    customReportReplay.set_send("1");
                } else {
                    customReportReplay.set_send("3");
                }
                scrollMyListViewToBottom();
            } else {
                customReportReplay.set_send("3");
            }
        }
    }

    private void scrollMyListViewToBottom() {
        list_msg.post(new Runnable() {
            @Override
            public void run() {
                if (customReportReplay.getCount() > 0) {
                    list_msg.setSelection(customReportReplay.getCount() - 1);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri mUri = Uri.fromFile(new File(mCurrentPhotoPath));
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(Replay_Report.this.getContentResolver().openInputStream(mUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        customReportReplay.add(new Report_replay_value("", get_time(), user, "2", "1", String.valueOf(mUri), "0"));
                        bitmap = getResizedBitmap(bitmap, 800);
                        byte[] byteArray = bitmap_to_bytearray(bitmap);
                        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        if (encodedImage != null && encodedImage.length() > 0)
                            send_msg_img(null, encodedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }


    }

    public byte[] bitmap_to_bytearray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
