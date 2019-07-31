package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.models.room.NewsCategory;
import com.amitzinfy.ka19news.repositories.HeadLinesRepository;

import java.util.List;

public class HeadLinesViewModel extends AndroidViewModel {
    private HeadLinesRepository headLinesRepository;

    public HeadLinesViewModel(@NonNull Application application) {
        super(application);
        headLinesRepository = new HeadLinesRepository(application);
    }

    public LiveData<List<NewsCategory>> getNewsCategories(int languageId){
        return headLinesRepository.getNewsCategories(languageId);
    }

    public LiveData<List<News>> getNewsList(int languageId, int categoryId){
        return headLinesRepository.getNewsList(languageId, categoryId);
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        headLinesRepository.insertFavNews(favouriteNews);
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        headLinesRepository.deleteFavNews(favouriteNews);
    }

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return headLinesRepository.getFavouriteNews(id);
    }
}
