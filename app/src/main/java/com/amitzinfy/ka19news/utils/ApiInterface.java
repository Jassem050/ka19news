package com.amitzinfy.ka19news.utils;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.Language;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.retrofit.NewsAdded;
import com.amitzinfy.ka19news.models.retrofit.OTPResponse;
import com.amitzinfy.ka19news.models.retrofit.User;
import com.amitzinfy.ka19news.models.retrofit.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("otp_generate")
    Call<OTPResponse> getOTPInfo(@Field("mobile") String phoneNumber);

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> registerUser(@Field("name") String name, @Field("email") String email,
                                    @Field("location") String address, @Field("gender") String gender,
                                    @Field("dob") String dateOfBirth, @Field("mobile") String phoneNumber);

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> loginUser(@Field("mobile") String phoneNumber);

    @Headers("Accept: application/json")
    @GET("user")
    Call<UserResponse> getUserDetails(@Header("Authorization") String accessToken);

    @Headers("Accept: application/json")
    @POST("news_count")
    Call<NewsAdded> getNewsCount(@Header("Authorization") String accessToken);

    @Headers("Accept: application/json")
    @Multipart
    @POST("updateimage")
    Call<User> updateProfImage(@Header("Authorization") String accessToken, @Part MultipartBody.Part file);

    @Headers("Accept: application/json")
    @Multipart
    @POST("addnews")
    Call<News> postNews(@Header("Authorization") String accessToken, @Part MultipartBody.Part file,
                        @Part("language_id") String languageId, @Part("language_name") String languageName,
                        @Part("cat_id") String categoryId, @Part("news_title") String newsTitle,
                        @Part("news_content") String newsContent, @Part("place") String place);

    @Headers("Accept: application/json")
    @POST("languages")
    Call<List<Language>> getLanguages(@Header("Authorization") String accessToken);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("find_cat")
    Call<List<Category>> getCategories(@Header("Authorization") String accessToken, @Field("language_id") String languageId);
}