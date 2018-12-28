package com.rc.buyermarket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamsSellerSearchBuyer implements Parcelable{

    private String purchase_type = "";
    private String property_type = "";
    private String country = "";
    private String city = "";
    private String state = "";
    private String bedroom = "";
    private String bathroom = "";
    private String basement = "";
    private String garage = "";
    private String style = "";
    private String exterior = "";
    private int price_min = 10000;
    private int price_max = 10000;

    public ParamsSellerSearchBuyer(String purchase_type, String property_type, String country, String city, String state, String bedroom, String bathroom, String basement, String garage, String style, String exterior, int priceMin, int priceMax) {
        this.purchase_type = purchase_type;
        this.property_type = property_type;
        this.country = country;
        this.city = city;
        this.state = state;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.basement = basement;
        this.garage = garage;
        this.style = style;
        this.exterior = exterior;
        this.price_min = priceMin;
        this.price_max = priceMax;
    }

    @Override
    public String toString() {
        return "{" +
                "purchase_type='" + purchase_type + '\'' +
                ", property_type='" + property_type + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
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

    /**************************
     * Methods for parcelable *
     **************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(purchase_type);
        dest.writeString(property_type);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(bedroom);
        dest.writeString(bathroom);
        dest.writeString(basement);
        dest.writeString(garage);
        dest.writeString(style);
        dest.writeString(exterior);
        dest.writeInt(price_min);
        dest.writeInt(price_max);
    }

    protected ParamsSellerSearchBuyer(Parcel in) {
        purchase_type = in.readString();
        property_type = in.readString();
        country = in.readString();
        city = in.readString();
        state = in.readString();
        bedroom = in.readString();
        bathroom = in.readString();
        basement = in.readString();
        garage = in.readString();
        style = in.readString();
        exterior = in.readString();
        price_min = in.readInt();
        price_max = in.readInt();
    }

    public static final Creator<ParamsSellerSearchBuyer> CREATOR = new Creator<ParamsSellerSearchBuyer>() {
        @Override
        public ParamsSellerSearchBuyer createFromParcel(Parcel in) {
            return new ParamsSellerSearchBuyer(in);
        }

        @Override
        public ParamsSellerSearchBuyer[] newArray(int size) {
            return new ParamsSellerSearchBuyer[size];
        }
    };

}
