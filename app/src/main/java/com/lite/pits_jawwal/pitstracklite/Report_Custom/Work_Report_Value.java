package com.lite.pits_jawwal.pitstracklite.Report_Custom;

import java.io.Serializable;

/**
 * Created by Ruba-PITS on 2/12/2018.
 */

public class Work_Report_Value implements Serializable {
    private String per_name, per_id,report,createdTime,lon,lat,address,msgid,userid,type,name,done,total,seen,report_type,
            customer,all_replies,skip,geozoneid,offlineID,offlinLoc_id,doneaddress,distance_write,distance_done,done_timestamp;
    public Work_Report_Value(String per_name, String per_id, String report, String createdTime, String lon, String lat,String address,String msgid,String userid,String type,String name,
                             String done,String total,String seen,String report_type,String customer,String all_replies,
                             String skip,String geozoneid,String offlineID,String offlinLoc_id,
                             String doneaddress, String distance_write,String distance_done,String done_timestamp) {
        this.per_name = per_name;
        this.per_id = per_id;
        this.report = report;
        this.createdTime = createdTime;
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.msgid=msgid;
        this.userid=userid;
        this.type=type;
        this.name=name;
        this.done=done;
        this.total=total;
        this.seen=seen;
        this.report_type=report_type;
        this.customer=customer;
        this.all_replies=all_replies;
        this.skip=skip;
        this.geozoneid=geozoneid;
        this.offlineID=offlineID;
        this.offlinLoc_id=offlinLoc_id;
        this.distance_done=distance_done;
        this.done_timestamp=done_timestamp;
        this.distance_write=distance_write;
        this.doneaddress=doneaddress;
    }

    public String getDone() {
        return done;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedTime() {
        return createdTime;
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

    public String getPer_id() {
        return per_id;
    }

    public String getPer_name() {
        return per_name;
    }

    public String getReport() {
        return report;
    }

    public String getMsgid() {
        return msgid;
    }

    public String getUserid() {
        return userid;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public void setPer_name(String per_name) {
        this.per_name = per_name;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAll_replies() {
        return all_replies;
    }

    public void setAll_replies(String all_replies) {
        this.all_replies = all_replies;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public String getGeozoneid() {
        return geozoneid;
    }

    public void setGeozoneid(String geozoneid) {
        this.geozoneid = geozoneid;
    }

    public String getOfflineID() {
        return offlineID;
    }

    public void setOfflineID(String offlineID) {
        this.offlineID = offlineID;
    }

    public String getOfflinLoc_id() {
        return offlinLoc_id;
    }

    public void setOfflinLoc_id(String offlinLoc_id) {
        this.offlinLoc_id = offlinLoc_id;
    }

    public String getDoneaddress() {
        return doneaddress;
    }

    public String getDistance_write() {
        return distance_write;
    }

    public String getDistance_done() {
        return distance_done;
    }

    public String getDone_timestamp() {
        return done_timestamp;
    }

    public void setDoneaddress(String doneaddress) {
        this.doneaddress = doneaddress;
    }

    public void setDistance_write(String distance_write) {
        this.distance_write = distance_write;
    }

    public void setDistance_done(String distance_done) {
        this.distance_done = distance_done;
    }

    public void setDone_timestamp(String done_timestamp) {
        this.done_timestamp = done_timestamp;
    }
}
