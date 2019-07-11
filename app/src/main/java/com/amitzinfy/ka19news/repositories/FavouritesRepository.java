package com.amitzinfy.ka19news.repositories;

import android.app.Application;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;

public class FavouritesRepository {

    private NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;

    public FavouritesRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
    }
}
