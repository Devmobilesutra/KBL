package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.google.gson.annotations.SerializedName;

public class ChangePwdPOJO {

    private String data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

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

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", message = "+message+", status = "+status+"]";
    }
}

