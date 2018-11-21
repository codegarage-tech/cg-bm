package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionBedroomList extends ResponseBase {

    private List<Bedroom> data = new ArrayList<>();

    public SessionBedroomList(List<Bedroom> data) {
        this.data = data;
    }

    public List<Bedroom> getData() {
        return data;
    }

    public void setData(List<Bedroom> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}