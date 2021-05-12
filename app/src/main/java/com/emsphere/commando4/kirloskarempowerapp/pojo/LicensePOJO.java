package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 1/25/2018.
 */

public class LicensePOJO {
    @SerializedName("Licensecode")
    String Licensecode;
    @SerializedName("EmpId")
    String EmpId;
    @SerializedName("DeviceId")
    String DeviceId;

    @SerializedName("companycode")
    String companycode;

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;
    @SerializedName("serviceUrl")
    String serviceUrl;
    //serviceUrl

    private Data data;



    public String getLicensecode() {
        return Licensecode;
    }

    public void setLicensecode(String licensecode) {
        Licensecode = licensecode;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
