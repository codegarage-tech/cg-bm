package com.rc.buyermarket.model;

public class SellerSearchBuyer {
    private String id = "";
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String contact = "";
    private String purchase_type = "";
    private String prc_approved = "";
    private String credit_score = "";
    private String max_price = "";
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
    private String phone = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPurchase_type() {
        return purchase_type;
    }

    public void setPurchase_type(String purchase_type) {
        this.purchase_type = purchase_type;
    }

    public String getPrc_approved() {
        return prc_approved;
    }

    public void setPrc_approved(String prc_approved) {
        this.prc_approved = prc_approved;
    }

    public String getCredit_score() {
        return credit_score;
    }

    public void setCredit_score(String credit_score) {
        this.credit_score = credit_score;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getBasement() {
        return basement;
    }

    public void setBasement(String basement) {
        this.basement = basement;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_max() {
        return price_max;
    }

    public void setPrice_max(String price_max) {
        this.price_max = price_max;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", purchase_type='" + purchase_type + '\'' +
                ", prc_approved='" + prc_approved + '\'' +
                ", credit_score='" + credit_score + '\'' +
                ", max_price='" + max_price + '\'' +
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
                ", phone='" + phone + '\'' +
                '}';
    }
}
