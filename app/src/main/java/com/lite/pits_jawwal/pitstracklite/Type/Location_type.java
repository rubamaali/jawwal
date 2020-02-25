package com.lite.pits_jawwal.pitstracklite.Type;

/**
 * Created by Ruba-PITS on 10/31/2017.
 */

public class Location_type {
    private String name;
    private String key;
    public Location_type(String name, String key){
        this.name=name;
        this.key=key;
        }
    public String getName() {
        return name;
    }
    public String getKey() {
        return key;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
