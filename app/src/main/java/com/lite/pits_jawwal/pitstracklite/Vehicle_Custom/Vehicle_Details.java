package com.lite.pits_jawwal.pitstracklite.Vehicle_Custom;

/**
 * Created by Ruba-PITS on 9/17/2017.
 */

public class Vehicle_Details {
    private String details_name;
    private String details_id;
    private String details_value;
    public Vehicle_Details(String details_name, String details_id, String details_value) {
        this.details_name = details_name;
        this.details_id = details_id;
        this.details_value = details_value;
    }

    public String getdetails_name() {
        return details_name;
    }

    public void setdetails_name(String details_name) {
        this.details_name = details_name;
    }

    public String getdetails_id() {
        return details_id;
    }

    public void setdetails_id(String details_id) {
        this.details_id = details_id;
    }

    public String getdetails_value() {
        return details_value;
    }

    public void setdetails_value(String details_value) {
        this.details_value = details_value;
    }
}
