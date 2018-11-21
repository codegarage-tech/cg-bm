package com.rc.buyermarket.model;

public class PurchaseType {

    private String id = "";
    private String purchase_key = "";
    private String purchase_value = "";

    public PurchaseType(String id, String purchase_key, String purchase_value) {
        this.id = id;
        this.purchase_key = purchase_key;
        this.purchase_value = purchase_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchase_key() {
        return purchase_key;
    }

    public void setPurchase_key(String purchase_key) {
        this.purchase_key = purchase_key;
    }

    public String getPurchase_value() {
        return purchase_value;
    }

    public void setPurchase_value(String purchase_value) {
        this.purchase_value = purchase_value;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", purchase_key='" + purchase_key + '\'' +
                ", purchase_value='" + purchase_value + '\'' +
                '}';
    }
}