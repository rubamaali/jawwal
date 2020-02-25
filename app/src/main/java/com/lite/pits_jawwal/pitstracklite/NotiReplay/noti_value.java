package com.lite.pits_jawwal.pitstracklite.NotiReplay;

public class noti_value {
    private String reply,createdby,creationtime,reportid,commandtype,replayid,seen,report_type,offlineID,type;
    public noti_value(String reply,String createdby,String creationtime,String reportid,String commandtype,
                      String replayid,String seen,String report_type,String offlineID,String type){
        this.replayid=replayid;
        this.reply=reply;
        this.createdby=createdby;
        this.creationtime=creationtime;
        this.reportid=reportid;
        this.commandtype=commandtype;
        this.seen=seen;
        this.report_type=report_type;
        this.offlineID=offlineID;
        this.type=type;
    }

    public String getCommandtype() {
        return commandtype;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public String getReplayid() {
        return replayid;
    }

    public String getReply() {
        return reply;
    }

    public String getReportid() {
        return reportid;
    }

    public String getSeen() {
        return seen;
    }

    public void setCommandtype(String commandtype) {
        this.commandtype = commandtype;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public void setReplayid(String replayid) {
        this.replayid = replayid;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getOfflineID() {
        return offlineID;
    }

    public void setOfflineID(String offlineID) {
        this.offlineID = offlineID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
