package com.rc.buyermarket.model;

public class Bathroom {

    private String id = "";
    private String bathroom_key = "";
    private String bathroom_value = "";

    public Bathroom(String id, String bathroom_key, String bathroom_value) {
        this.id = id;
        this.bathroom_key = bathroom_key;
        this.bathroom_value = bathroom_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBathroom_key() {
        return bathroom_key;
    }

    public void setBathroom_key(String bathroom_key) {
        this.bathroom_key = bathroom_key;
    }

    public String getBathroom_value() {
        return bathroom_value;
    }

    public void setBathroom_value(String bathroom_value) {
        this.bathroom_value = bathroom_value;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", bathroom_key='" + bathroom_key + '\'' +
                ", bathroom_value='" + bathroom_value + '\'' +
                '}';
    }
}