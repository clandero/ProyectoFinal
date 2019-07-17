package com.example.proyectofinal;

public class UserItem {
    private String user;
    private String fb_token;
    private String phone;
    private String status;
    private int company;
    public UserItem(String us,String fbt,String ph,String st,int comp){
        user = us;
        fb_token = fbt;
        phone = ph;
        status = st;
        company = comp;
    }

    public String getUser() {
        return user;
    }

    public String getFb_token() {
        return fb_token;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public int getCompany() {
        return company;
    }
}
