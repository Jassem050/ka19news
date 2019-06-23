package com.amitzinfy.ka19news.utils;

import com.amitzinfy.ka19news.models.retrofit.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("getNews")
    Call<List<News>> getNewsList();

    @GET("categories")
    Call<List<News>> getCategoryList();
}