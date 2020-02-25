package com.lite.pits_jawwal.pitstracklite.Order;

import java.io.Serializable;

public class CartValues implements Serializable {
    private String RId,type,createtime,totalPrice,updatetime,geozoneID,stockId,quantity,customerId,stockeffecy_id,AName,EName,createdby;

    public CartValues(String RId, String type, String createtime, String totalPrice, String updatetime,
     String geozoneID, String stockId, String quantity, String customerId, String stockeffecy_id
            , String AName, String EName, String createdby) {
        this.RId = RId;
        this.type = type;
        this.createtime = createtime;
        this.totalPrice = totalPrice;
        this.updatetime = updatetime;
        this.geozoneID = geozoneID;
        this.stockId=stockId;
        this.quantity=quantity;
        this.customerId=customerId;
        this.stockeffecy_id=stockeffecy_id;
        this.EName=EName;
        this.AName=AName;
        this.createdby=createdby;
    }

    public String getRId() {
        return RId;
    }

    public String getType() {
        return type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getGeozoneID() {
        return geozoneID;
    }

    public void setRId(String RId) {
        this.RId = RId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setGeozoneID(String geozoneID) {
        this.geozoneID = geozoneID;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStockeffecy_id() {
        return stockeffecy_id;
    }

    public void setStockeffecy_id(String stockeffecy_id) {
        this.stockeffecy_id = stockeffecy_id;
    }

    public String getAName() {
        return AName;
    }

    public String getEName() {
        return EName;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public void setEName(String EName) {
        this.EName = EName;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
}
