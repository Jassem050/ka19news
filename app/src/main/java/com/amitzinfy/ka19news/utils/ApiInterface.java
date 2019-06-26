package com.amitzinfy.ka19news.utils;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("category_news/{language_id}/{category_id}")
    Call<List<News>> getCategoryNewsList(@Path("language_id") int languageId, @Path("category_id") int categoryId);

    @GET("categories/{language_id}")
    Call<List<Category>> getCategoryList(@Path("language_id") int languageId);
}