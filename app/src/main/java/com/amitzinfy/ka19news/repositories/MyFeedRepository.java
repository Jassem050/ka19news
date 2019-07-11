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

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFeedRepository {
    private static final String TAG = "MyFeedRepository";

    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private static MyFeedRepository myFeedRepository;
    private  NewsRoomDatabase newsRoomDatabase;
    private FavouriteNewsDao favouriteNewsDao;

    public MyFeedRepository(Application application){
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();
    }

    private void loadNewsList(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getCategoryNewsList(1,1);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Collections.reverse(response.body());
                        newsList.postValue(response.body());
                        Log.d(TAG, "onResponse: news: " + response.body().get(0).getTitle());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: news: " + t);

            }
        });
    }

    private void loadCategories(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategoryList(1);
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
            }
        });

    }



    public LiveData<List<Category>> getCategories(){
        loadCategories();
        return categoryList;
    }

    public LiveData<List<News>> getNewsList(){
        loadNewsList();
        return newsList;
    }

    public void insertFavNews(FavouriteNews favouriteNews){
        new InsertFavouriteAsyncTask(favouriteNewsDao).execute(favouriteNews);
    }

    private class InsertFavouriteAsyncTask extends AsyncTask<FavouriteNews, Void, Void>{

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
