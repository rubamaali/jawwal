package com.lite.pits_jawwal.pitstracklite.Report_Custom;

public class ReportList {
    private String id,name;
    public ReportList(String id,String name){
        this.id=id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
