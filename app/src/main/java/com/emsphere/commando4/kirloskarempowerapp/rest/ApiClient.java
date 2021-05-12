package com.emsphere.commando4.kirloskarempowerapp.rest;




import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by commando4 on 1/2/2018.
 */

public class ApiClient {
     //seedmanagement.cloudapp.net //192.168.1.54 //125.99.42.40
    public static final String BASE_URL = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl1"));
    private static Retrofit retrofit = null;

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();


    public static Retrofit getClient() {
        if (retrofit==null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)

                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return retrofit;
    }
}
