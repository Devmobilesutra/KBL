package com.emsphere.commando4.kirloskarempowerapp.restforreg;

import com.emsphere.commando4.kirloskarempowerapp.pojo.LicensePOJO;
import com.emsphere.commando4.kirloskarempowerapp.pojo.RegistrationDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by commando4 on 1/2/2018.
 */

public interface ApiInterface {
    //Enterprise_MobileAppService //Empower_License //enterprise_mobileappservice

    @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/TrackerMobUser/Registration")
    Call<RegistrationDetails> SendRegistrationData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/TrackerMobUser/VerifyLicenseNumber")
    Call<LicensePOJO> SendLicenseRegData(@FieldMap Map<String, String> fields);

}

