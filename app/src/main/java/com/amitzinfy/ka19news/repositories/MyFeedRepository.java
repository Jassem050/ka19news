package com.amitzinfy.ka19news.repositories;

import android.app.Application;

import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;


public class MyFeedRepository {

    public MyFeedRepository(Application application) {
    }

    public List<News> loadNewsList(){
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            newsList.add(new News("Mangalore News Portal"));
        }
        return newsList;
    }

    public void getCat(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
    }
}
