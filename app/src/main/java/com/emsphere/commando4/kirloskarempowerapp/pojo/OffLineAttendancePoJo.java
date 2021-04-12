package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/23/2018.
 */

public class OffLineAttendancePoJo {
    @SerializedName("swipeTime")
   private String swipeTime;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("door")
    private String door;
    @SerializedName("locationAddress")
    private String locationAddress;
    @SerializedName("swipeImageFileName")
    private String swipeImageFileName;
    @SerializedName("isOnlineSwipe")
    private String isOnlineSwipe;
    @SerializedName("remark")
    private String remark;
    @SerializedName("id_mark")
    private String id_mark;//
    @SerializedName("locationProvider")
    private String locationProvider;

    public String getSwipeTime() {
        return swipeTime;
    }

    public void setSwipeTime(String swipeTime) {
        this.swipeTime = swipeTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getSwipeImageFileName() {
        return swipeImageFileName;
    }

    public void setSwipeImageFileName(String swipeImageFileName) {
        this.swipeImageFileName = swipeImageFileName;
    }

    public String getIsOnlineSwipe() {
        return isOnlineSwipe;
    }

    public void setIsOnlineSwipe(String isOnlineSwipe) {
        this.isOnlineSwipe = isOnlineSwipe;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId_mark() {
        return id_mark;
    }

    public void setId_mark(String id_mark) {
        this.id_mark = id_mark;
    }

    public String getLocationProvider() {
        return locationProvider;
    }

    public void setLocationProvider(String locationProvider) {
        this.locationProvider = locationProvider;
    }
}
