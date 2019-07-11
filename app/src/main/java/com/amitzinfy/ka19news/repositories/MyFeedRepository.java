package com.amitzinfy.ka19news.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFeedRepository {
    private static final String TAG = "MyFeedRepository";

    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private static MyFeedRepository myFeedRepository;

    public static MyFeedRepository getInstance(){
        if (myFeedRepository == null){
            myFeedRepository = new MyFeedRepository();
        }
        return myFeedRepository;
    }

    private void loadNewsList(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getCategoryNewsList(1,1);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Collections.reverse(response.body());
                        newsList.postValue(response.body());
                        Log.d(TAG, "onResponse: news: " + response.body().get(0).getTitle());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: news: " + t);

            }
        });
    }

    private void loadCategories(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategoryList(1);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        categoryList.postValue(response.body());
                        List<Category> categoryList = response.body();
                        Log.d(TAG, "onResponse: categories" + categoryList.get(0).getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }

    public LiveData<List<Category>> getCategories(){
        loadCategories();
        return categoryList;
    }

    public LiveData<List<News>> getNewsList(){
        loadNewsList();
        return newsList;
    }
}
