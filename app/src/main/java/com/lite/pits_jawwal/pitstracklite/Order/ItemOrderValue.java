package com.lite.pits_jawwal.pitstracklite.Order;

public class ItemOrderValue {
    String product,ptice,quantity,productE;

    public ItemOrderValue(String productA, String quantity, String ptice, String productE) {
        this.product = productA;
        this.ptice = ptice;
        this.quantity = quantity;
        this.productE=productE;
    }

    public String getProduct() {
        return product;
    }

    public String getPtice() {
        return ptice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setPtice(String ptice) {
        this.ptice = ptice;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductE() {
        return productE;
    }

    public void setProductE(String productE) {
        this.productE = productE;
    }
}
