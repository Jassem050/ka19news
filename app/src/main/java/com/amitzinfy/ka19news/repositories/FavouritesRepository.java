package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.room.FavouriteNews;

import java.util.List;

public class FavouritesRepository {

    private NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;
    private LiveData<List<FavouriteNews>> favNewsList;

    public FavouritesRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
        favNewsList = favouriteNewsDao.getAllFavNews();
    }

    public LiveData<List<FavouriteNews>> getFavNewsList(){
        return favNewsList;
    }

    public void deleteFavNews(FavouriteNews favouriteNews){
        new DeleteFavNewsAsyncTask(favouriteNewsDao).execute(favouriteNews);
    }

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return favouriteNewsDao.getFavouriteNews(id);
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        new InsertFavouriteAsyncTask(favouriteNewsDao).execute(favouriteNews);
    }

    private static class DeleteFavNewsAsyncTask extends AsyncTask<FavouriteNews, Void, Void>{

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

    private static class InsertFavouriteAsyncTask extends AsyncTask<FavouriteNews, Void, Void>{

        private FavouriteNewsDao asyncTaskDao;

        InsertFavouriteAsyncTask(FavouriteNewsDao dao){
            this.asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(FavouriteNews... favouriteNews) {
            asyncTaskDao.insert(favouriteNews[0]);
            return null;
        }
    }
}
