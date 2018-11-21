package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionBathroomList extends ResponseBase {

    private List<Bathroom> data = new ArrayList<>();

    public SessionBathroomList(List<Bathroom> data) {
        this.data = data;
    }

    public List<Bathroom> getData() {
        return data;
    }

    public void setData(List<Bathroom> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}