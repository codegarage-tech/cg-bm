package com.rc.buyermarket.model;

public class PropertyType {
    private String id = "";
    private String property_key = "";
    private String property_value = "";

    public PropertyType(String id, String property_key, String property_value) {
        this.id = id;
        this.property_key = property_key;
        this.property_value = property_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty_key() {
        return property_key;
    }

    public void setProperty_key(String property_key) {
        this.property_key = property_key;
    }

    public String getProperty_value() {
        return property_value;
    }

    public void setProperty_value(String property_value) {
        this.property_value = property_value;
    }

    @Override
    public String toString() {
        return "PropertyEnum{" +
                "id='" + id + '\'' +
                ", property_key='" + property_key + '\'' +
                ", property_value='" + property_value + '\'' +
                '}';
    }
}
