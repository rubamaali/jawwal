package com.lite.pits_jawwal.pitstracklite.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lite.pits_jawwal.pitstracklite.Alarm;
import com.lite.pits_jawwal.pitstracklite.Custom_Event.Event_item;
import com.lite.pits_jawwal.pitstracklite.DB.Insert_events;
import com.lite.pits_jawwal.pitstracklite.MainActivity;
import com.lite.pits_jawwal.pitstracklite.PitsTrack;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by WIN on 9/17/2016.
 */
public class PitsGcmListenerService  extends FirebaseMessagingService {
    private static final String TAG = "MyGcmListenerService";
    private int count=0;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean a=prefs.getBoolean(Preferences.HAS_LOGIN,false);
        int num=0;
       try {
        num= Integer.parseInt(prefs.getString(Preferences.NUMBER_NOTIFICATION,""));
       }catch (Exception e){
           num=0;
       }
        num=num+1;
        ShortcutBadger.applyCount(getApplicationContext(), num);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString(Preferences.NUMBER_NOTIFICATION, String.valueOf(num));
        editor.apply();
        if(a==false) {
            return;
        }

        Map<String, String> data=remoteMessage.getData();
        String message = data.get("message");
        String type="event";
        String msg="0";
        String msgid=data.get("msgid");
        String offlineid=data.get("offlineID");
        String userid=data.get("userid");
        String sound=data.get("sound");
        Log.d(TAG, "Message: " + message);
        if(message!=null) {
            sendGeneralNotification(message, msgid, userid,sound,offlineid );
        }
    }


    /**
     * Create and show a simple notification containing the received GCM message, and splash intent.
     *
     * @param message GCM message received.
     */
    private void sendGeneralNotification(String message,String msgid,String userid,String type,String offlineid ) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(msgid!=null && userid!=null && msgid.length()>0 && userid.length()>0){
            intent.putExtra(PitsTrack.IntentExtra, "report");
            intent.putExtra("msgid",msgid);
            intent.putExtra("userid",userid);
            intent.putExtra("offlineid",offlineid);
        }
        else if(type!=null && !type.equals("default")) {
            Intent intent1 = new Intent(this, Alarm.class);
            intent1.putExtra("msg", String.valueOf(message));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(!Alarm.active) {
                startActivity(intent1);
                intent.putExtra(PitsTrack.IntentExtra, "notification");
            }else{
                Intent intent_chat=new Intent(getPackageName());
                intent_chat.putExtra("message", message);
                LocalBroadcastManager local=LocalBroadcastManager.getInstance(this);
                local.sendBroadcast(intent_chat);
            }
        }
        else {
            intent.putExtra(PitsTrack.IntentExtra, "notification");
        }

        Random generator = new Random();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, generator.nextInt(), intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel(defaultSoundUri);
        else {
            channel = "";
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Sound=prefs.getString(Preferences.SOUND,"");
        if (Sound.equals("0")){
            defaultSoundUri=null;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pits_jawwal))
                .setSmallIcon(R.drawable.not_icon)
                .setColor(Color.parseColor("#2D5B75"))
                .setContentTitle("PITS Track")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle("PITS Track");
        bigText.setSummaryText("PITS Track");
        notificationBuilder.setStyle(bigText);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = message.hashCode();
        Calendar c = Calendar.getInstance();
        long s = c.getTimeInMillis();
        notificationManager.notify(Long.toString(s), id, notificationBuilder.build());
        count=count+1;
        Event_List event_list=new Event_List();
        event_list.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    private synchronized String createChannel( Uri defaultSoundUri) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String name = "channelpits";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel("channelpits", name, importance);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        mChannel.setSound(defaultSoundUri, audioAttributes);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "channelpits";
    }
    private class Event_List extends AsyncTask<Object, Object, List<Event_item>> {
        private String _userName;
        private String _password;
        List<Event_item> list=new ArrayList<Event_item>();
        private String _date;
        public Event_List() {
        }
        @Override
        protected List<Event_item> doInBackground(Object... data) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String url = prefs.getString(Preferences.URL, "");
            _userName = prefs.getString(Preferences.USER_NAME, "");
            _password = prefs.getString(Preferences.PASSWORD, "");
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String newDateStr = formatter.format(new Date());
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date2 = null;
                try {
                    date2 = (Date) formatter.parse(newDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time = date2.getTime() / 1000;
                _date= String.valueOf(time);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url+"/mobilelite.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
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
                        for(int i=0; i < v.length(); i++){
                            JSONObject jsonObject2 = v.getJSONObject(i);
                            Event_item item = new Event_item(jsonObject2.optString("event").toString(),jsonObject2.optString("vehicle").toString(),jsonObject2.optString("address").toString(),jsonObject2.optString("time").toString(),jsonObject2.optString("speed").toString(),jsonObject2.optString("lon").toString(),jsonObject2.optString("lat").toString());
                            list.add(item);
                        }
                        return list;
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
            }catch (Exception e){
                return null;
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Event_item> result) {
            if(result!=null) {
                if (result.size()>0) {
                    list=result;
                    try {
                        Insert_events events = new Insert_events(getApplicationContext());
                        events.insert(result,_date);
                    }catch (Exception e){}
                }

            }


        }
}

}
