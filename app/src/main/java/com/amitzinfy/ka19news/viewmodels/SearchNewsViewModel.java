package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.repositories.SearchNewsRepository;

import java.util.List;

public class SearchNewsViewModel extends AndroidViewModel {

    private SearchNewsRepository searchNewsRepository;
    private MutableLiveData<String> searchQueryText = new MutableLiveData<>();

    public SearchNewsViewModel(@NonNull Application application) {
        super(application);
        searchNewsRepository = SearchNewsRepository.getInstance(application);
    }

    public LiveData<List<News>> getSearchNews(){
        return Transformations.switchMap(searchQueryText, query -> searchNewsRepository.getSearchNews(query));
    }

    public void setSearchQueryText(String searchQueryText){
        this.searchQueryText.setValue(searchQueryText);
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        searchNewsRepository.insertFavNews(favouriteNews);
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        searchNewsRepository.deleteFavNews(favouriteNews);
    }

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return searchNewsRepository.getFavouriteNews(id);
    }
}
