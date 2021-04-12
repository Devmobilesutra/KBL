package com.emsphere.commando4.kirloskarempowerapp.pojo.area;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AreaPojo {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    private ArrayList<Data> data;


    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
