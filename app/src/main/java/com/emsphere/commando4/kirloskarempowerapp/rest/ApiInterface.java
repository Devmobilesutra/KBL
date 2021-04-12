package com.emsphere.commando4.kirloskarempowerapp.rest;

import com.emsphere.commando4.kirloskarempowerapp.pojo.ChangePwdPOJO;
import com.emsphere.commando4.kirloskarempowerapp.pojo.CommanResponsePojo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.LoginPOJO;
import com.emsphere.commando4.kirloskarempowerapp.pojo.area.AreaPojo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.config.EmployeeConfig;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by commando4 on 1/2/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("EmployeeApi/UserLogin")
    Call<LoginPOJO> SendLoginData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetMobileApplicationConfiguration")
    Call<EmployeeConfig> getConfigData(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApi/AddMobileSwipes")
    Call<CommanResponsePojo> AddMobileSwipesData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApi/AddEmployeeMobileLocationTrackingDetails")
    Call<CommanResponsePojo> LocationTrackData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApi/AddEmployeeDeviceFutureStatus")
    Call<CommanResponsePojo> EmployeeDeviceStatus(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetEmployeeAreaCoordinates")
    Call<AreaPojo> getEmployeeAreaConfig(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApi/ChangePassword")
    Call<ChangePwdPOJO> UpdatePassword(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApi/ForgotPassword")
    Call<ChangePwdPOJO> ForgotPassword(@FieldMap Map<String, String> fields);



}

