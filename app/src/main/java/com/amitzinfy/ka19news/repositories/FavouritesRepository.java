package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.room.FavouriteNews;

public class FavouritesRepository {

    private NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;

    public FavouritesRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        new DeleteFavNewsAsyncTask(favouriteNewsDao).execute(favouriteNews);
    }

    private class DeleteFavNewsAsyncTask extends AsyncTask<FavouriteNews, Void, Void>{

        private FavouriteNewsDao asyncTaskDao;

        DeleteFavNewsAsyncTask(FavouriteNewsDao dao){
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavouriteNews... favouriteNews) {
            asyncTaskDao.delete(favouriteNews[0]);
            return null;
        }
    }
}
