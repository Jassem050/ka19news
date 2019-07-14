package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.repositories.FavouritesRepository;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private FavouritesRepository favouritesRepository;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favouritesRepository = new FavouritesRepository(application);
    }

    public LiveData<List<FavouriteNews>> getAllFavNews(){
        return favouritesRepository.getFavNewsList();
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        favouritesRepository.deleteFavNews(favouriteNews);
    }

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return favouritesRepository.getFavouriteNews(id);
    }
}
