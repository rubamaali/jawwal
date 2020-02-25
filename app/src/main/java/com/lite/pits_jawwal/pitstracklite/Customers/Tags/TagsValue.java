package com.lite.pits_jawwal.pitstracklite.Customers.Tags;

import com.lite.pits_jawwal.pitstracklite.Customers.FieldsValue;

import java.io.Serializable;
import java.util.ArrayList;

public class TagsValue implements Serializable {
    private String tagid,tagname,tagcolor,tagtype,tagmulti;
    private ArrayList<FieldsValue> tagsValues;
    private boolean select;
    public TagsValue(String tagid, String tagname, String tagcolor, String tagtype, String tagmulti, ArrayList<FieldsValue> tagsValues, boolean select){
        this.tagid=tagid;
        this.tagname=tagname;
        this.tagcolor=tagcolor;
        this.tagtype=tagtype;
        this.tagmulti=tagmulti;
        this.tagsValues=tagsValues;
        this.select=select;
    }

    public String getTagcolor() {
        return tagcolor;
    }

    public String getTagid() {
        return tagid;
    }

    public String getTagmulti() {
        return tagmulti;
    }

    public String getTagname() {
        return tagname;
    }

    public String getTagtype() {
        return tagtype;
    }

    public void setTagcolor(String tagcolor) {
        this.tagcolor = tagcolor;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public void setTagmulti(String tagmulti) {
        this.tagmulti = tagmulti;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public void setTagtype(String tagtype) {
        this.tagtype = tagtype;
    }

    public ArrayList<FieldsValue> getTagsValues() {
        return tagsValues;
    }

    public void setTagsValues(ArrayList<FieldsValue> tagsValues) {
        this.tagsValues = tagsValues;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
