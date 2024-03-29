package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.dao.NewsCategoryDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.models.room.NewsCategory;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.PreferenceManager;
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
    private FavouriteNewsDao favouriteNewsDao;
    private PreferenceManager preferenceManager;

    public HeadLinesRepository(Application application){
        preferenceManager = PreferenceManager.getInstance(application);
        NewsRoomDatabase newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        categoryDao = newsRoomDatabase.newsCategoryDao();
        categoryList = categoryDao.getAllCategories(preferenceManager.getLanguageName());
        favouriteNewsDao = newsRoomDatabase.favouriteNewsDao();

    }

    // load categories from server
    private void loadCategories(String languageName){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategoryList(languageName);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body();
                    if (categoryList != null) {
                        for (Category category : categoryList) {
                            NewsCategory newsCategory = new NewsCategory(category.getId(), category.getName(), category.getLanguageName());
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

    // load news from server
    private void loadNewsList(String languageName, int categoryId){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<News>> call = apiInterface.getCategoryNewsList(languageName, categoryId);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        Collections.reverse(response.body());
                        newsList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, "onFailure: headlines news: " + t);
            }
        });
    }

    // insertFavNews NewsCategory to room in background
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
    public LiveData<List<NewsCategory>> getNewsCategories(String languageName){
        loadCategories(languageName);
        return categoryList;
    }

    // insertFavNews to news_categories room table
    private void insert(NewsCategory newsCategory){
        new InsertAsyncTask(categoryDao).execute(newsCategory);
    }

    public LiveData<List<News>> getNewsList(String languageName, int categoryId){
        loadNewsList(languageName, categoryId);
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

    public void deleteAllCategories(){
        new DeleteAllCategories(categoryDao).execute();
    }

    private static class DeleteAllCategories extends AsyncTask<Void, Void, Void>{
        private NewsCategoryDao asyncTaskDao;

        DeleteAllCategories(NewsCategoryDao dao){
            this.asyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }
}
