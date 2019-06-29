package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.dao.NewsCategoryDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.NewsCategory;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLinesRepository {

    private static final String TAG = "HeadLinesRepository";

    private LiveData<List<NewsCategory>> categoryList;
    private MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private NewsCategoryDao categoryDao;

    public HeadLinesRepository(Application application){
        NewsRoomDatabase newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        categoryDao = newsRoomDatabase.newsCategoryDao();
        categoryList = categoryDao.getAllCategories();

    }

    private void loadCategories(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategoryList(1);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body();
                    if (categoryList != null) {
                        for (Category category : categoryList) {
                            NewsCategory newsCategory = new NewsCategory(category.getId(), category.getName());
                            insert(newsCategory);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d(TAG, "onFailure: headlines: " + t);
            }
        });

    }

    private void loadNewsList(int categoryId){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getCategoryNewsList(1, categoryId);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()){
                    Collections.reverse(response.body());
                    newsList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: headlines news: " + t);
            }
        });
    }

    // insert NewsCategory to room in background
    private static class InsertAsyncTask extends AsyncTask<NewsCategory, Void, Void>{

        private NewsCategoryDao mAsyncTaskDao;

        InsertAsyncTask(NewsCategoryDao dao){
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(NewsCategory... newsCategories) {
            mAsyncTaskDao.insert(newsCategories[0]);
            return null;
        }
    }

    // load categories from network and return categorylist from room
    public LiveData<List<NewsCategory>> getNewsCategories(){
        loadCategories();
        return categoryList;
    }

    // insert to news_categories room table
    private void insert(NewsCategory newsCategory){
        new InsertAsyncTask(categoryDao).execute(newsCategory);
    }

    public LiveData<List<News>> getNewsList(int categoryId){
        loadNewsList(categoryId);
        return newsList;
    }
}
