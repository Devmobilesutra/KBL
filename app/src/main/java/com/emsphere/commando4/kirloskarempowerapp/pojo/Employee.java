package com.emsphere.commando4.kirloskarempowerapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/5/2018.
 */

public class Employee {

    @SerializedName("EmployeeID")
    int EmployeeID;
    @SerializedName("FirstName")
    String FirstName;
    @SerializedName("LastName")
    String LastName;
    @SerializedName("Gender")
    String Gender;
    @SerializedName("EmployeeImage")
    String EmployeeImage;
    @SerializedName("OfficalEmailID")
    String OfficalEmailID;
    @SerializedName("BirthDate")
    String BirthDate;

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmployeeImage() {
        return EmployeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        EmployeeImage = employeeImage;
    }

    public String getOfficalEmailID() {
        return OfficalEmailID;
    }

    public void setOfficalEmailID(String officalEmailID) {
        OfficalEmailID = officalEmailID;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }
}
