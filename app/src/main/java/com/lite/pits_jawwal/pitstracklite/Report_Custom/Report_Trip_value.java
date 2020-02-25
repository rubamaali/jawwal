package com.lite.pits_jawwal.pitstracklite.Report_Custom;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Report_Trip_value {
    private String from, time,to,trip,odometer,maxspeed,afffrom,addto,driver,odo_start,odo_end;
    public Report_Trip_value(String from, String time, String to, String trip, String odometer, String maxspeed, String afffrom, String addto, String driver,String odo_start,String odo_end){
        this.from = from;
        this.time = time;
        this.to = to;
        this.trip = trip;
        this.maxspeed = maxspeed;
        this.afffrom = afffrom;
        this.odometer = odometer;
        this.addto = addto;
        this.driver = driver;
        this.odo_start = odo_start;
        this.odo_end = odo_end;
    }

    public String getTime() {
        return time;
    }
    public String getTo() {
        return to;
    }
    public String getTrip() {
        return trip;
    }
    public String getOdometer() {
        return odometer;
    }
    public String getMaxspeed() {
        return maxspeed;
    }
    public String getAfffrom() {
        return afffrom;
    }
    public String getAddto() {
        return addto;
    }
    public String getFrom() {
        return from;
    }
    public String getDriver() {
        return driver;
    }
    public String getOdo_end() {
        return odo_end;
    }
    public String getOdo_start() {
        return odo_start;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public void setAddto(String addto) {
        this.addto = addto;
    }
    public void setAfffrom(String afffrom) {
        this.afffrom = afffrom;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }
    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }
    public void setTrip(String trip) {
        this.trip = trip;
    }
    public void setOdo_end(String odo_end) {
        this.odo_end = odo_end;
    }
    public void setOdo_start(String odo_start) {
        this.odo_start = odo_start;
    }
}
