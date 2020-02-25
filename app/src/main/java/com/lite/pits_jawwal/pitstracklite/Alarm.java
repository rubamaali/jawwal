package com.lite.pits_jawwal.pitstracklite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.Fount.CustomTextViewL;

public class Alarm extends AppCompatActivity implements View.OnClickListener{
    private Button remove_alarm;
    private MediaPlayer mp;
    private TextView txt_msg;
    private SharedPreferences prefs;
    private LinearLayout linearLayout;
    public static boolean active = false;
    int number=1;
    private BroadcastReceiver mhander = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String message = intent.getStringExtra("message");
                showalarm(message);
            } catch (Exception ignored) {
            }
        }
    };

    private void showalarm(String message) {
        CustomTextViewL tv = new CustomTextViewL(this);
        tv.setTextSize(17.0f);
        tv.setText(message);
        tv.setTextColor(Color.parseColor("#000000"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        if(number%2==0) {
            tv.setBackgroundColor(Color.parseColor("#E7E6E6"));
        }else{
            tv.setBackgroundColor(Color.parseColor("#E3EFFF"));
        }
        number++;
        tv.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(tv);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);
        LocalBroadcastManager.getInstance(this).registerReceiver(mhander, new IntentFilter(getPackageName()));
        this.setFinishOnTouchOutside(false);
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        declaration();
        mp = MediaPlayer.create(getBaseContext(), R.raw.alarm_sound);
        mp.start();
        mp.setLooping(true);
    }

    private void declaration(){
        remove_alarm=findViewById(R.id.remove_alarm);
        remove_alarm.setOnClickListener(this);
        txt_msg=findViewById(R.id.txt_msg);
        txt_msg.setOnClickListener(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String msg=getIntent().getStringExtra("msg");
        txt_msg.setText(msg);
        linearLayout=findViewById(R.id.linearLayout);
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.remove_alarm){
            if(mp!=null){
                mp.stop();
                finish();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    protected void onDestroy() {
        if(mp!=null){
            mp.stop();
            finish();
        }
        super.onDestroy();
        active = false;

    }
}
