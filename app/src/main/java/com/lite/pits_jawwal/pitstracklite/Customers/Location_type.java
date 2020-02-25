package com.lite.pits_jawwal.pitstracklite.Customers;

import android.support.annotation.NonNull;

public class Location_type implements Comparable<Location_type>  {
    private String name;
    private String key;
    private int image,count;

    public Location_type(String name, String key,int img,int count){
        this.name=name;
        this.key=key;
        this.image=img;
        this.count=count;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    @Override
    public int compareTo(@NonNull Location_type location_type) {
        if (count > location_type.count) {
            return -1;
        }
        else if (count <  location_type.count) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
