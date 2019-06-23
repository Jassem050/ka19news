package com.amitzinfy.ka19news.repositories;

import android.app.Application;

import com.amitzinfy.ka19news.models.News;

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
}
