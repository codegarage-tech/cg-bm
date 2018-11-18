package com.rc.buyermarket.model;

public class ParamsAddProperty {

    private String id = "";
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String contact = "";
    private String purchase_type = "";
    private String prc_approved = "";
    private String credit_score = "";
    private String property_type = "";
    private String buyer_id = "";
    private String country = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private String bedroom = "";
    private String bathroom = "";
    private String basement = "";
    private String garage = "";
    private String style = "";
    private String exterior = "";
    private String price_min = "";
    private String price_max = "";

    public ParamsAddProperty(String id, String first_name, String last_name, String email, String contact, String purchase_type, String prc_approved, String credit_score, String property_type, String buyer_id, String country, String city, String state, String zipcode, String bedroom, String bathroom, String basement, String garage, String style, String exterior, String price_min, String price_max) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.contact = contact;
        this.purchase_type = purchase_type;
        this.prc_approved = prc_approved;
        this.credit_score = credit_score;
        this.property_type = property_type;
        this.buyer_id = buyer_id;
        this.country = country;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.basement = basement;
        this.garage = garage;
        this.style = style;
        this.exterior = exterior;
        this.price_min = price_min;
        this.price_max = price_max;
    }

    @Override
    public String toString() {
        return "ParamsAddProperty{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", purchase_type='" + purchase_type + '\'' +
                ", prc_approved='" + prc_approved + '\'' +
                ", credit_score='" + credit_score + '\'' +
                ", property_type='" + property_type + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", bedroom='" + bedroom + '\'' +
                ", bathroom='" + bathroom + '\'' +
                ", basement='" + basement + '\'' +
                ", garage='" + garage + '\'' +
                ", style='" + style + '\'' +
                ", exterior='" + exterior + '\'' +
                ", price_min='" + price_min + '\'' +
                ", price_max='" + price_max + '\'' +
                '}';
    }
}
