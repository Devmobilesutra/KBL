package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.emsphere.commando4.kirloskarempowerapp.pojo.loginpojo.Data;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 1/25/2018.
 */

public class LoginPOJO {

    @SerializedName("User_name")
    String User_name;
    @SerializedName("Password")
    String Password;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    private ArrayList<com.emsphere.commando4.kirloskarempowerapp.pojo.loginpojo.Data> data;


    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
