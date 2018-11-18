package com.rc.buyermarket.model;

public class ParamsSellerLogin {

    private String email = "";
    private String password = "";


    public ParamsSellerLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "ParamsBuyerLogin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
