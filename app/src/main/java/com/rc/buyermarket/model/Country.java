package com.rc.buyermarket.model;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private String id = "";
    private String country_name = "";

    private List<States> states = new ArrayList<>();

    public Country(String id, String country_name, List<States> states) {
        this.id = id;
        this.country_name = country_name;
        this.states = states;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public List<States> getStates() {
        return states;
    }

    public void setStates(List<States> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", country_name='" + country_name + '\'' +
                ", states=" + states +
                '}';
    }
}
