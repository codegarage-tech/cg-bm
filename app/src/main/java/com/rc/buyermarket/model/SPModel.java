package com.rc.buyermarket.model;

/**
 * Created by MSHL on 3/19/2018.
 */

public class SPModel {

    private String id = "";
    private String sp_title = "";

    public SPModel() {
    }

    public SPModel(String id, String sp_title) {
        this.id = id;
        this.sp_title = sp_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSp_title() {
        return sp_title;
    }

    public void setSp_title(String sp_title) {
        this.sp_title = sp_title;
    }
}
