package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lite.pits_jawwal.pitstracklite.ALL;
import com.lite.pits_jawwal.pitstracklite.Preferences;
import com.lite.pits_jawwal.pitstracklite.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by Ruba-PITS on 2/27/2018.
 */

public class Custom_report_replay extends ArrayAdapter<Report_replay_value> {
    private ArrayList<Report_replay_value> list_all=new ArrayList<>();;
    private Context activity;
    protected LayoutInflater inflater;
    private TextView time,msg,lastseen,txt_error,user_txt;
    private SharedPreferences prefs;
    private String user;
    private ProgressBar pbHeaderProgress;
    private Activity activity1;
    public Custom_report_replay(Context activity, int textViewResourceId, Activity activity1){
        super(activity, textViewResourceId);
        this.activity=activity;
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        user=prefs.getString(Preferences.USER_NAME,"");
        this.activity1=activity1;
    }
    @Override
    public int getCount() {
        return this.list_all.size();
    }

    @Override
    public Report_replay_value getItem(int i) {
        return this.list_all.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Report_replay_value customReportReplay = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String left_right;
        if((customReportReplay.getUserid().toLowerCase()).equals(user.toLowerCase())){
            left_right="right";
        }else{
            left_right="left";
        }
        if((customReportReplay.getImg()).equals("1")){
            if (left_right.equals("right")) {
                row = inflater.inflate(R.layout.replay_img, parent, false);
                pbHeaderProgress =  row.findViewById(R.id.pbHeaderProgress);
                txt_error =  row.findViewById(R.id.txt_error);
                if (customReportReplay.isSend().contains("1")) {
                    pbHeaderProgress.setVisibility(View.GONE);
                } else if (customReportReplay.isSend().contains("2")) {
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                    txt_error.setVisibility(View.GONE);
                }else {
                    pbHeaderProgress.setVisibility(View.GONE);
                    txt_error.setVisibility(View.VISIBLE);
                }
            }else{
                row = inflater.inflate(R.layout.replay_img_left, parent, false);
                user_txt= row.findViewById(R.id.user);
                user_txt.setText(customReportReplay.getUserid());

            }
            final GifImageView msg=row.findViewById(R.id.msg);
            String url=customReportReplay.getBitmap();
            if(url!=null && !url.isEmpty()) {
                Picasso.with(activity).load(url).into(msg, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {

                    }
                });
            }else if(customReportReplay.getBitmap_img()!=null){
                msg.setImageBitmap(customReportReplay.getBitmap_img());
            }
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Report_replay_value customReportReplay = getItem(position);
                   show_image(customReportReplay.getBitmap());
                }
            });
        }

        else {
            if (left_right.equals("right")) {
                row = inflater.inflate(R.layout.replay_right, parent, false);
                pbHeaderProgress =  row.findViewById(R.id.pbHeaderProgress);
                msg =  row.findViewById(R.id.msg);
                if (customReportReplay.isSend().contains("1")) {
                    pbHeaderProgress.setVisibility(View.GONE);
                } else if (customReportReplay.isSend().contains("2")) {
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                } else {
                    pbHeaderProgress.setVisibility(View.GONE);
                    msg.setError("");
                }
            } else {
                row = inflater.inflate(R.layout.replay_left, parent, false);
                msg =  row.findViewById(R.id.msg);
                user_txt= row.findViewById(R.id.user);
                user_txt.setText(customReportReplay.getUserid());
            }
            msg.setText(customReportReplay.getMsg());
        }
        time =  row.findViewById(R.id.time);
        time.setText(customReportReplay.getTime());
        if(left_right.equals("right")){
            lastseen =  row.findViewById(R.id.lastseen);
            String las=customReportReplay.getLastseen();
            if(las.equals("0") || las.contains("01/01/70")){
                lastseen.setVisibility(View.GONE);
            }
            else{
                lastseen.setVisibility(View.VISIBLE);
                lastseen.setText("  "+las+"  ");
            }
        }
        return row;
    }
    @Override
    public void add(Report_replay_value object) {
        list_all.add(object);
        super.add(object);
    }
    public void set_send(String val){
        Report_replay_value value=getItem((list_all.size()-1));
        value.setSend(val);
        notifyDataSetChanged();

     }
     public void clear_all(){
        list_all.clear();
        notifyDataSetChanged();
     }

    private void show_image(String url){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity1);
        LayoutInflater factory = LayoutInflater.from(activity);
        final View view = factory.inflate(R.layout.show_image, null);
        final ImageView image_dialog=view.findViewById(R.id.img_dialog);
        if(url!=null && !url.isEmpty()) {
            Picasso.with(activity).load(url).into(image_dialog, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                }
            });
        }

        alert.setPositiveButton(activity.getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.setNeutralButton(activity.getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                BitmapDrawable draw = (BitmapDrawable) image_dialog.getDrawable();
                Bitmap bitmap = draw.getBitmap();
                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/pitstrack");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    activity.sendBroadcast(intent);
                    ALL.show_custom_msg(activity,activity1,activity.getString(R.string.saveph));
                } catch (FileNotFoundException e) {
                    ALL.show_custom_msg(activity,activity1,activity.getString(R.string.str36));
                    e.printStackTrace();
                } catch (IOException e) {
                    ALL.show_custom_msg(activity,activity1,activity.getString(R.string.str36));
                    e.printStackTrace();
                }
            }
        });
        alert.setView(view);
        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#60000000")));
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#ffffff"));
        nbutton.setTextSize(20f);
        pbutton.setTextColor(Color.parseColor("#ffffff"));
        pbutton.setTextSize(20f);
        nbutton.setAllCaps(false);
        pbutton.setAllCaps(false);
    }


}
