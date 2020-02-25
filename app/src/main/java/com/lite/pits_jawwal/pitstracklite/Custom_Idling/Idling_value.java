package com.lite.pits_jawwal.pitstracklite.Custom_Idling;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Idling_value {
    private String trip, time,add,ignontime,ignoff,point,stoptotaltime;
    public Idling_value(String trip, String time, String add, String ignontime, String ignoff, String point, String stoptotaltime){
       this.time = time;
        this.trip = trip;
        this.add=add;
        this.ignontime=ignontime;
        this.ignoff=ignoff;
        this.point=point;
        this.stoptotaltime=stoptotaltime;
    }

    public String getTime() {
        return time;
    }
    public String getTrip() {
        return trip;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getAdd() {
        return add;
    }

    public String getIgnontime() {
        return ignontime;
    }

    public String getIgnoff() {
        return ignoff;
    }

    public String getPoint() {
        return point;
    }

    public String getStoptotaltime() {
        return stoptotaltime;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public void setIgnoff(String ignoff) {
        this.ignoff = ignoff;
    }

    public void setIgnontime(String ignontime) {
        this.ignontime = ignontime;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setStoptotaltime(String stoptotaltime) {
        this.stoptotaltime = stoptotaltime;
    }
}
