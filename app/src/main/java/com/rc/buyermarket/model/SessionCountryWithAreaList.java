package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionCountryWithAreaList extends ResponseBase {

    private List<Country> data = new ArrayList<>();

    public SessionCountryWithAreaList(List<Country> data) {
        this.data = data;
    }

    public List<Country> getData() {
        return data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}