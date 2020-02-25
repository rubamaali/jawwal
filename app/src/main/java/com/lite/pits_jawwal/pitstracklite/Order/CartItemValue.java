package com.lite.pits_jawwal.pitstracklite.Order;

import java.io.Serializable;

public class CartItemValue implements Serializable {
    String itemid,AName,EName,vendore,quantity,price,image,itemtype,details,SelectQ,SelecstP;
    boolean select;
    public CartItemValue(String itemid, String AName, String EName, String vendore, String quantity,
                         String price, String image, String itemtype, String details, String SelectQ,
                         String SelecstP, boolean select){
        this.itemid=itemid;
        this.AName=AName;
        this.EName=EName;
        this.vendore=vendore;
        this.quantity=quantity;
        this.price=price;
        this.image=image;
        this.itemtype=itemtype;
        this.details=details;
        this.SelecstP=SelecstP;
        this.SelectQ=SelectQ;
        this.select=select;
    }

    public String getItemid() {
        return itemid;
    }

    public String getAName() {
        return AName;
    }

    public String getEName() {
        return EName;
    }

    public String getVendore() {
        return vendore;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getItemtype() {
        return itemtype;
    }

    public String getDetails() {
        return details;
    }

    public String getSelecstP() {
        return SelecstP;
    }

    public String getSelectQ() {
        return SelectQ;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public void setAName(String AName) {
        this.AName = AName;
    }

    public void setEName(String EName) {
        this.EName = EName;
    }


    public void setVendore(String vendore) {
        this.vendore = vendore;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setSelecstP(String selecstP) {
        SelecstP = selecstP;
    }

    public void setSelectQ(String selectQ) {
        SelectQ = selectQ;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }
}
