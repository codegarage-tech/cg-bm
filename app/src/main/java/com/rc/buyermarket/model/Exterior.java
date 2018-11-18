package com.rc.buyermarket.model;

public class Exterior {
    private String id = "";
    private String exterior_key = "";
    private String exterior_value = "";

    public Exterior(String id, String exterior_key, String exterior_value) {
        this.id = id;
        this.exterior_key = exterior_key;
        this.exterior_value = exterior_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExterior_key() {
        return exterior_key;
    }

    public void setExterior_key(String exterior_key) {
        this.exterior_key = exterior_key;
    }

    public String getExterior_value() {
        return exterior_value;
    }

    public void setExterior_value(String exterior_value) {
        this.exterior_value = exterior_value;
    }

    @Override
    public String toString() {
        return "Exterior{" +
                "id='" + id + '\'' +
                ", exterior_key='" + exterior_key + '\'' +
                ", exterior_value='" + exterior_value + '\'' +
                '}';
    }
}
