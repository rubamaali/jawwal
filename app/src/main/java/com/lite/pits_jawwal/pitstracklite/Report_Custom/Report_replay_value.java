package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import android.graphics.Bitmap;

/**
 * Created by Ruba-PITS on 2/27/2018.
 */

public class Report_replay_value {
    String msg,time,lastseen,img;
     String userid;
     String send;
    String bitmap;
    Bitmap bitmap_img;
    public  Report_replay_value(String msg,String time,String userid,String send,String lastseen,String img, Bitmap bitmap_img){
        this.msg=msg;
        this.time=time;
        this.userid=userid;
        this.send=send;
        this.lastseen=lastseen;
        this.img=img;
        this.bitmap_img=bitmap_img;
    }

    public Report_replay_value(String msg, String time, String userid, String send, String lastseen, String img, String bitmap) {
        this.msg=msg;
        this.time=time;
        this.userid=userid;
        this.send=send;
        this.lastseen=lastseen;
        this.img=img;
        this.bitmap=bitmap;
    }

    public String getMsg() {
        return msg;
    }
    public String getTime() {
        return time;
    }
    public String  getUserid() {
        return userid;
    }
    public void setSend(String  send) {
        this.send = send;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setUserid(String  userid) {
        this.userid = userid;
    }
    public String isSend() {
        return send;
    }

    public String  getBitmap() {
        return bitmap;
    }

    public String getImg() {
        return img;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public Bitmap getBitmap_img() {
        return bitmap_img;
    }

    public void setBitmap_img(Bitmap bitmap_img) {
        this.bitmap_img = bitmap_img;
    }
}
