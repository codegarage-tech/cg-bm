package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionStyleList extends ResponseBase {

    private List<Styles> data = new ArrayList<>();

    public SessionStyleList(List<Styles> data) {
        this.data = data;
    }

    public List<Styles> getData() {
        return data;
    }

    public void setData(List<Styles> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}