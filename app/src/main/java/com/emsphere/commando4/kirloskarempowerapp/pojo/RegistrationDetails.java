package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 1/25/2018.
 */

public class RegistrationDetails {
    @SerializedName("employeeCode")
    String employeeCode;
    @SerializedName("mobileNumber")
    String mobileNumber;
    @SerializedName("companyCode")
    String companyCode;
    @SerializedName("registrationType")
    String registrationType;
    @SerializedName("message")
    String message;
    @SerializedName("status")
    String status;
    @SerializedName("data")
    String data;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
