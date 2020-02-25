package com.lite.pits_jawwal.pitstracklite.List;

/**
 * Created by Ruba-PITS on 6/22/2017.
 */

public class Vehicles_list {
    private String last_update;
    private String name;
    private String key;
    private String address;
    private String deviceid;
    private String lat;
    private String lon;
    private String icon;
    private String speed,phone;
    private int index_index;
    private String  driver_name;

    public Vehicles_list(String last_update, String name, String key, String address, String deviceid, String lat, String lon,String icon,int index_index,String speed,String driver_name,String phone){
        this.last_update=last_update;
        this.name=name;
        this.key=key;
        this.address=address;
        this.deviceid=deviceid;
        this.lat=lat;
        this.lon=lon;
        this.icon=icon;
        this.index_index=index_index;
        this.speed=speed;
        this.driver_name=driver_name;
        this.phone=phone;

    }

    public String getLast_update() {
        return last_update;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getAddress() {
        return address;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getIcon() {return icon;}

    public int getIndex() {return index_index;}

    public String getSpeed() {return speed;}
    public String getDriver_name() {return driver_name;}

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setIndex(int index_index) {
        this.index_index = index_index;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public void setDriver_name(String driver_name) {this.driver_name = driver_name;}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return deviceid ;
    }
}
