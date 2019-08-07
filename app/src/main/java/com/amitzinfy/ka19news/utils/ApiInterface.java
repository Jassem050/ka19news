package com.amitzinfy.ka19news.utils;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("category_news/{language_name}/{category_id}")
    Call<List<News>> getCategoryNewsList(@Path("language_name") String languageName, @Path("category_id") int categoryId);

    @GET("categories/{language_name}")
    Call<List<Category>> getCategoryList(@Path("language_name") String languageName);

    @GET("searchnews")
    Call<List<News>> getSearchNewsList(@Query("data") String searchQuery);

    @GET("feednews/{language_name}")
    Call<List<News>> getFeedNews(@Path("language_name") String languageName, @Query("category_ids") String categoryIds);

    @GET("language_news/{language_name}")
    Call<List<News>> getLanguageNews(@Path("language_name") String language_name);
}