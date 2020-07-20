package com.dolphin.zanders.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientcusome {
    //live URL = https://shop2.gzanders.com/mobileapi/
    //test URL = http://testgz.shop2.gzanders.com/mobileapi/
    public static final String MAIN_URLL = "http://dkbraende.demoproject.info/rest/V1/";
    public static final String api_token = "123!@#ABCabc";


    public static int VIEWTYPE=0;

    private static Retrofit retrofit = null;
  public static   OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MAIN_URLL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
