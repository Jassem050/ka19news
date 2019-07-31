package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.repositories.MyFeedRepository;

import java.util.List;

public class MyFeedViewModel extends AndroidViewModel {

    private MyFeedRepository myFeedRepository;
    private MutableLiveData<Integer> languageId = new MutableLiveData<>();

    public MyFeedViewModel(@NonNull Application application) {
        super(application);
        myFeedRepository = new MyFeedRepository(application);
    }

    public LiveData<List<News>> getNewsList(String category_id){

        return Transformations.switchMap(languageId, input -> myFeedRepository.getNewsList(input, category_id));
    }

    public LiveData<List<News>> getLanguageNews(int languageId){
        return myFeedRepository.getLanguageNews(languageId);
    }

    public LiveData<List<Category>> getCategories(){
        return myFeedRepository.getCategories();
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        myFeedRepository.insertFavNews(favouriteNews);
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        myFeedRepository.deleteFavNews(favouriteNews);
    }

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return myFeedRepository.getFavouriteNews(id);
    }

    public void setLanguageId(int languageId) {
        this.languageId.setValue(languageId);
    }
}
