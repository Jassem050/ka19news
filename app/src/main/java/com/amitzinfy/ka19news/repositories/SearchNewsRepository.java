package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchNewsRepository {
    private static final String TAG = "SearchNewsRepository";

    private static SearchNewsRepository searchNewsRepository;
    private MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;

    public static SearchNewsRepository getInstance(Application application){
        if (searchNewsRepository == null){
            synchronized (SearchNewsRepository.class){
                if (searchNewsRepository == null){
                    searchNewsRepository = new SearchNewsRepository(application);
                }
            }
        }
        return searchNewsRepository;
    }

    public SearchNewsRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
    }

    private void loadSearchNewsList(String searchQuery){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getSearchNewsList(searchQuery);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Collections.reverse(response.body());
                    newsList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: search: " + t);
            }
        });
    }

    public MutableLiveData<List<News>> getSearchNews(String searchQuery){
        loadSearchNewsList(searchQuery);
        return newsList;
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        new InsertFavouriteAsyncTask(favouriteNewsDao).execute(favouriteNews);
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

    public void deleteFavNews(FavouriteNews favouriteNews){
        new DeleteFavNewsAsyncTask(favouriteNewsDao).execute(favouriteNews);
    }

    private static class DeleteFavNewsAsyncTask extends AsyncTask<FavouriteNews, Void, Void> {
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

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return favouriteNewsDao.getFavouriteNews(id);
    }
}
