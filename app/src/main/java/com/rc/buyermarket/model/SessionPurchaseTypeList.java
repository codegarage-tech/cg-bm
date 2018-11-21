package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionPurchaseTypeList extends ResponseBase {

    private List<PurchaseType> data = new ArrayList<>();

    public SessionPurchaseTypeList(List<PurchaseType> data) {
        this.data = data;
    }

    public List<PurchaseType> getData() {
        return data;
    }

    public void setData(List<PurchaseType> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}