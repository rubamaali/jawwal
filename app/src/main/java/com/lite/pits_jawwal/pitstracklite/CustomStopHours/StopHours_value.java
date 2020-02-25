package com.lite.pits_jawwal.pitstracklite.CustomStopHours;

import java.io.Serializable;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class StopHours_value implements Serializable {
    private String from, to, time, address, trip;

    public StopHours_value(String from, String to, String time, String address, String trip) {
        this.address = address;
        this.from = from;
        this.time = time;
        this.to = to;
        this.trip = trip;
    }

    public String getAddress() {
        return address;
    }

    public String getFrom() {
        return from;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }
}


