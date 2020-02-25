package com.lite.pits_jawwal.pitstracklite.Customers;

import java.io.Serializable;

public class FieldsValue implements Serializable {
    String fieldid,tagid,fieldname,color,tagmulti;
    boolean select;
    public FieldsValue(String fieldid, String tagid, String fieldname, String color, boolean isselect, String tagmulti){
        this.fieldid=fieldid;
        this.tagid=tagid;
        this.fieldname=fieldname;
        this.color=color;
        this.select=isselect;
        this.tagmulti=tagmulti;
    }

    public String getTagid() {
        return tagid;
    }

    public String getFieldid() {
        return fieldid;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public void setFieldid(String fieldid) {
        this.fieldid = fieldid;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTagmulti() {
        return tagmulti;
    }

    public void setTagmulti(String tagmulti) {
        this.tagmulti = tagmulti;
    }
}
