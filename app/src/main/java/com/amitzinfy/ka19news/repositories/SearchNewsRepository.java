package com.amitzinfy.ka19news.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchNewsRepository {
    private static final String TAG = "SearchNewsRepository";

    private static SearchNewsRepository searchNewsRepository;
    private MutableLiveData<List<News>> newsList;

    public static SearchNewsRepository getInstance(){
        if (searchNewsRepository == null){
            synchronized (SearchNewsRepository.class){
                if (searchNewsRepository == null){
                    searchNewsRepository = new SearchNewsRepository();
                }
            }
        }
        return searchNewsRepository;
    }

    private void loadSearchNewsList(String searchQuery){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getSearchNewsList(searchQuery);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                Collections.reverse(response.body());
                newsList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: search: " + t);
            }
        });
    }

    public LiveData<List<News>> getSearchNews(String searchQuery){
        loadSearchNewsList(searchQuery);
        return newsList;
    }
}
