package com.emsphere.commando4.kirloskarempowerapp.markhistory;

/**
 * Created by commando1 on 8/8/2017.
 */

public class HeaderData {

    String date;
    String punch;

   /* public HeaderData(String date, String punch) {
        this.date = date;
        this.punch = punch;
    }*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPunch() {
        return punch;
    }

    public void setPunch(String punch) {
        this.punch = punch;
    }

    @Override
    public String toString() {
        return "HeaderData{" +
                "date='" + date + '\'' +
                ", punch='" + punch + '\'' +
                '}';
    }
}
