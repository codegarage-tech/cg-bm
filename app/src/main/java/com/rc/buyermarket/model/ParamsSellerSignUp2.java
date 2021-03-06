package com.rc.buyermarket.model;

public class ParamsSellerSignUp2 {

    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String phone = "";
    private String password = "";
    private Integer  id ;
    private String signup_day = "";

    public ParamsSellerSignUp2(String first_name, String last_name, String email, String phone, String password, Integer id, String signup_day) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.id = id;
        this.signup_day = signup_day;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignup_day() {
        return signup_day;
    }

    public void setSignup_day(String signup_day) {
        this.signup_day = signup_day;
    }

    @Override
    public String toString() {
        return "ParamsSellerSignUp{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", signup_day='" + signup_day + '\'' +
                '}';
    }
}
