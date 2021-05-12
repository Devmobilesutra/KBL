package com.emsphere.commando4.kirloskarempowerapp.markhistory;

/**
 * Created by commando1 on 8/8/2017.
 */

public class EmployeeMarkHistory {

    String Date;
    String data;
    String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EmployeeMarkHistory{" +
                "Date='" + Date + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
