package com.lite.pits_jawwal.pitstracklite.Custom_Visit;

import java.io.Serializable;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Visit_value implements Serializable {
    private String time_in,time_out,address,type,lon,lat,id,totaltime;
    public Visit_value(String time_in, String time_out, String address, String type, String lon, String lat, String id, String totaltime) {
        this.time_in=time_in;
        this.time_out=time_out;
        this.address=address;
        this.type=type;
        this.lat=lat;
        this.lon=lon;
        this.id=id;
        this.totaltime=totaltime;
    }

    public String getId() {
        return id;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getTime_in() {
        return time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }
}
