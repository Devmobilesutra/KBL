package com.emsphere.commando4.kirloskarempowerapp.restforreg;




import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by commando4 on 1/2/2018.
 */

public class ApiClient {
     //seedmanagement.cloudapp.net //192.168.1.54 //125.99.42.40
    //public static final String BASE_URL = "http://52.66.44.191/CanteenMobileAppService/ValidateCanteenMobileUser.ashx";
    private static Retrofit retrofit = null;

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                   // .baseUrl("http://seedmanagement.cloudapp.net/")
                    .baseUrl("https://cloud.emsphere.com/")

                    .addConverterFactory(GsonConverterFactory.create())
                     .client(client)
                    .build();
        }
        return retrofit;
    }
}
