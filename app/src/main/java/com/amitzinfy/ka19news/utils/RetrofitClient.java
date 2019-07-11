package com.amitzinfy.ka19news.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();


    public static Retrofit getRetrofitClient(){
        if (retrofit == null){
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(15, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .client(builder.build())
                    .build();
        }
        return retrofit;
    }
}
