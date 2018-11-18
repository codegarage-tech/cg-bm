package com.rc.buyermarket.model;

public class States {

    private String id = "";
    private String name = "";
    private String country_id = "";

    public States(String id, String name, String country_id) {
        this.id = id;
        this.name = name;
        this.country_id = country_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    @Override
    public String toString() {
        return "States{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country_id='" + country_id + '\'' +
                '}';
    }
}
