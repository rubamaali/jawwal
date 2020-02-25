package com.lite.pits_jawwal.pitstracklite.Vehicle_Custom;

/**
 * Created by Ruba-PITS on 9/23/2017.
 */

public class  List_Car_Info {
    private String reason, driver_phone, last_update, last_location, address, odometer, version, driver_name, car_name,vehicle_control,speed;
    public List_Car_Info(String car_name, String driver_name, String reason, String driver_phone, String last_location, String last_update, String odometer, String version,String address,String vehicle_control,String speed) {
        this.car_name = car_name;
        this.driver_name = driver_name;
        this.reason = reason;
        this.driver_phone = driver_phone;
        this.last_location = last_location;
        this.last_update = last_update;
        this.odometer = odometer;
        this.version = version;
        this.address=address;
        this.vehicle_control=vehicle_control;
        this.speed=speed;

    }

    public String getcar_name() {
        return car_name;
    }
    public void setcar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getdriver_name() {
        return driver_name;
    }
    public void setdriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getreason() {
        return reason;
    }
    public void setreason(String reason) {
        this.reason = reason;
    }

    public String getdriver_phone() {
        return driver_phone;
    }
    public void setdriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getlast_location() {
        return last_location;
    }
    public void setlast_location(String last_location) {
        this.last_location = last_location;
    }

    public String getlast_update() {
        return last_update;
    }
    public void setlast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getodometer() {
        return odometer;
    }
    public void setodometer(String odometer) {
        this.odometer = odometer;
    }

    public String getversion() {
        return version;
    }
    public void setversion(String version) {
        this.version = version;
    }

    public String getvehicle_control() {
        return vehicle_control;
    }
    public void setvehicle_control(String vehicle_control) {this.vehicle_control = vehicle_control;}

    public String getspeed() {
        return speed;
    }
    public void setSpeed(String speed) {this.speed = speed;}

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {this.address = address;}
}