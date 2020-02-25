package com.lite.pits_jawwal.pitstracklite.Report_Custom;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Report_work_hour_value {
    private String time, igntion,speed,reason,odometer,battary,address,driver,lon,lat;
    public Report_work_hour_value(String time, String igntion, String speed, String reason, String lon, String lat, String address,String odometer,String battary,String driver) {
        this.time = time;
        this.igntion = igntion;
        this.speed = speed;
        this.reason = reason;
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.odometer = odometer;
        this.battary = battary;
        this.driver = driver;
    }

    public String getTime() {
        return time;
    }
    public String getAddress() {
        return address;
    }
    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }
    public String getIgntion() {
        return igntion;
    }
    public String getSpeed() {
        return speed;
    }
    public String getReason() {
        return reason;
    }
    public String getOdometer() {
        return odometer;
    }
    public String getBattary() {
        return battary;
    }
    public String getDriver() {
        return driver;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public void setIgntion(String igntion) {
        this.igntion = igntion;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setOdometer(String odometer) {
        this.reason = odometer;
    }
    public void setBattary(String battary) {
        this.battary = battary;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }

}
