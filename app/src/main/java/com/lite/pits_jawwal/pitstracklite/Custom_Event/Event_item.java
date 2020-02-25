package com.lite.pits_jawwal.pitstracklite.Custom_Event;

/**
 * Created by Ruba-PITS on 8/6/2017.
 */

public class Event_item {
    private String event_name;
    private String vehicle_name;
    private String address;
    private String time;
    private String speed;
    private String lon;
    private String lat;

    public Event_item(String event_name, String vehicle_name, String address,String time,String speed ,String lon ,String lat ){
        this.event_name=event_name;
        this.vehicle_name=vehicle_name;
        this.address=address;
        this.time=time;
        this.speed=speed;
        this.lon=lon;
        this.lat=lat;
    }
    public String getevent_name() {
        return event_name;
    }
    public void setevent_name(String event_name) {this.event_name= event_name;}

    public String getVehicle_name() {
        return vehicle_name;
    }
    public void setVehicle_name(String vehicle_name) {this.vehicle_name= vehicle_name;}

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {this.address= address;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time= time;}

    public String getSpeed() {return speed;}
    public void setSpeed(String speed) {this.speed= speed;}

    public String getlon() {return lon;}
    public void setlon(String lon) {this.lon= lon;}

    public String getlat() {return lat;}
    public void lat(String lat) {this.lat= lat;}
}


