package com.lite.pits_jawwal.pitstracklite.Categories;


import android.support.annotation.NonNull;


import com.lite.pits_jawwal.pitstracklite.Customers.Tags.TagsValue;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceData implements Comparable<PlaceData>,Serializable {

    private String placeName,iid,phone,email,website,fax,distance,classificationId,customerPhone,customerName,offlinid,account_number;
    private String placeId,in;
    private double placeLon;
    private double placeLat;
    private double placeDistance;
    private boolean isOffline;
    private ArrayList<TagsValue> tagsValues;
    public PlaceData(String placeName, String placeId, double placeLon, double placeLat,
                     double placeDistance, String in, String iid, String phone,
                     String email, String website, String fax, String distance, String classificationId, String customerName,
                     String customerPhone, ArrayList<TagsValue> tagsValues, String offlinid, boolean isOffline,String account_number ) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.placeLon = placeLon;
        this.placeLat = placeLat;
        this.placeDistance = placeDistance;
        this.in=in;
        this.iid=iid;
        this.phone=phone;
        this.email=email;
        this.website=website;
        this.fax=fax;
        this.distance=distance;
        this.classificationId=classificationId;
        this.customerName=customerName;
        this.customerPhone=customerPhone;
        this.tagsValues=tagsValues;
        this.offlinid=offlinid;
        this.isOffline=isOffline;
        this.account_number=account_number;
    }

    public PlaceData(String placeName, String placeId, Double placeLon, Double placeLat) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.placeLon = placeLon;
        this.placeLat = placeLat;
        this.placeDistance = 0.0;
        this.in="";
        this.iid="";
        this.phone="";
        this.email="";
        this.website="";
        this.fax="";
        this.distance="";
        this.classificationId="";
        this.customerName="";
        this.customerPhone="";
        this.offlinid="";
        this.isOffline=false;
        this.account_number="";
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getPlaceLon() {
        return placeLon;
    }

    public void setPlaceLon(double placeLon) {
        this.placeLon = placeLon;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceDistance() {
        return placeDistance;
    }

    public void setPlaceDistance(double placeDistance) {
        this.placeDistance = placeDistance;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getIid() {
        return iid;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    @Override
    public int compareTo(@NonNull PlaceData placeData) {
        if (placeDistance > placeData.placeDistance) {
            return 1;
        }
        else if (placeDistance <  placeData.placeDistance) {
            return -1;
        }
        else {
            return 0;
        }
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public ArrayList<TagsValue> getTagsValues() {
        return tagsValues;
    }

    public void setTagsValues(ArrayList<TagsValue> tagsValues) {
        this.tagsValues = tagsValues;
    }

    public String getOfflinid() {
        return offlinid;
    }

    public void setOfflinid(String offlinid) {
        this.offlinid = offlinid;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
}