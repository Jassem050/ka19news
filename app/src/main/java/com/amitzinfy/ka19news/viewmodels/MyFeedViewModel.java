package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.repositories.MyFeedRepository;

import java.util.List;

public class MyFeedViewModel extends AndroidViewModel {

    private MyFeedRepository myFeedRepository;

    public MyFeedViewModel(@NonNull Application application) {
        super(application);
        myFeedRepository = MyFeedRepository.getInstance();
    }

    public LiveData<List<News>> getNewsList(){
       return myFeedRepository.getNewsList();
    }

    public LiveData<List<Category>> getCategories(){
        return myFeedRepository.getCategories();
    }
}
