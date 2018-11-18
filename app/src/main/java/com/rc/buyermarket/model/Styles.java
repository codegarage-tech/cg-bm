package com.rc.buyermarket.model;

public class Styles {
    private String id = "";
    private String style_key = "";
    private String style_vaue = "";

    public Styles(String id, String style_key, String style_vaue) {
        this.id = id;
        this.style_key = style_key;
        this.style_vaue = style_vaue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle_key() {
        return style_key;
    }

    public void setStyle_key(String style_key) {
        this.style_key = style_key;
    }

    public String getStyle_vaue() {
        return style_vaue;
    }

    public void setStyle_vaue(String style_vaue) {
        this.style_vaue = style_vaue;
    }

    @Override
    public String toString() {
        return "Styles{" +
                "id='" + id + '\'' +
                ", style_key='" + style_key + '\'' +
                ", style_vaue='" + style_vaue + '\'' +
                '}';
    }
}
