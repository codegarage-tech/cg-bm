package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionPropertyTypeList extends ResponseBase {

    private List<PropertyType> data = new ArrayList<>();

    public SessionPropertyTypeList(List<PropertyType> data) {
        this.data = data;
    }

    public List<PropertyType> getData() {
        return data;
    }

    public void setData(List<PropertyType> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}