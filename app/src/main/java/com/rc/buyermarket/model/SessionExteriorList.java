package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionExteriorList extends ResponseBase {

    private List<Exterior> data = new ArrayList<>();

    public SessionExteriorList(List<Exterior> data) {
        this.data = data;
    }

    public List<Exterior> getData() {
        return data;
    }

    public void setData(List<Exterior> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}