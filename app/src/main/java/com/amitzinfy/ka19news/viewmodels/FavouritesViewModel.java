package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.repositories.FavouritesRepository;

public class FavouritesViewModel extends AndroidViewModel {

    private FavouritesRepository favouritesRepository;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favouritesRepository = new FavouritesRepository(application);
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        favouritesRepository.deleteFavNews(favouriteNews);
    }
}
