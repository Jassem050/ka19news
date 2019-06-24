package com.amitzinfy.ka19news.repositories;

import android.app.Application;

import com.amitzinfy.ka19news.models.retrofit.Category;
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

    public void loadCategories(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
    }

    public List<Category> getCategories(){
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            categoryList.add(new Category(i, "cateogory " + i));
        }
        return categoryList;
    }
}
