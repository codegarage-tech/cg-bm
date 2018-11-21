package com.rc.buyermarket.model;

public class Bedroom {

    private String id = "";
    private String bedroom_key = "";
    private String bedroom_value = "";

    public Bedroom(String id, String bedroom_key, String bedroom_value) {
        this.id = id;
        this.bedroom_key = bedroom_key;
        this.bedroom_value = bedroom_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBedroom_key() {
        return bedroom_key;
    }

    public void setBedroom_key(String bedroom_key) {
        this.bedroom_key = bedroom_key;
    }

    public String getBedroom_value() {
        return bedroom_value;
    }

    public void setBedroom_value(String bedroom_value) {
        this.bedroom_value = bedroom_value;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", bedroom_key='" + bedroom_key + '\'' +
                ", bedroom_value='" + bedroom_value + '\'' +
                '}';
    }
}