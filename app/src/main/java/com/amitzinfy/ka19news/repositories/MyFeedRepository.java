package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFeedRepository {
    private static final String TAG = "MyFeedRepository";
    private static final int TOTAL_RETRY = 3;

    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private static MyFeedRepository myFeedRepository;
    private  NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;
    private int favNewsLength;
    private List<News> serverError;

    public MyFeedRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
    }

    private void loadNewsList(String languageName, String ids){
        final int[] retry = {0};
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getFeedNews(languageName, ids);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Collections.reverse(response.body());
                        newsList.postValue(response.body());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: news: " + t);
                serverError = new ArrayList<>();
                serverError.add(0, new News("error", "Could not connect to server"));
                newsList.postValue(serverError);
                if (retry[0] < TOTAL_RETRY) {
                    call.clone().enqueue(this);
                    retry[0]++;
                }
                serverError = null;
            }
        });
    }

    private void loadCategories(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategoryList("English");
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        categoryList.postValue(response.body());
                        List<Category> categoryList = response.body();
                        Log.d(TAG, "onResponse: categories" + categoryList.get(0).getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                call.clone().enqueue(this);
            }
        });

    }


    private void loadLanguageNews(String languageName){
        final int[] retry = {0};
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getLanguageNews(languageName);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        newsList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: languageNews: " + t);
                serverError = new ArrayList<>();
                serverError.add(0, new News("error", "could not connect to server"));
                newsList.postValue(serverError);
                if (retry[0] < TOTAL_RETRY) {
                    call.clone().enqueue(this);
                    retry[0]++;
                }
                serverError = null;
            }
        });
    }

    public LiveData<List<News>> getLanguageNews(String languageName){
        loadLanguageNews(languageName);
        return newsList;
    }

    public LiveData<List<Category>> getCategories(){
        loadCategories();
        return categoryList;
    }

    public LiveData<List<News>> getNewsList(String languageName, String ids){
        loadNewsList(languageName, ids);
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

    public LiveData<FavouriteNews[]> getFavouriteNews(int id){
        return favouriteNewsDao.getFavouriteNews(id);
    }

}
