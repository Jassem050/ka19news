package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.dao.FeedCategoryDao;
import com.amitzinfy.ka19news.db.NewsRoomDatabase;
import com.amitzinfy.ka19news.models.room.FeedCategory;

public class FeedCategoryRepository {

    private static FeedCategoryRepository feedCategoryRepository;
    private NewsRoomDatabase newsRoomDatabase;
    private FeedCategoryDao feedCategoryDao;

    public static FeedCategoryRepository getInstance(Application application){
        if (feedCategoryRepository == null){
            feedCategoryRepository = new FeedCategoryRepository(application);
        }
        return feedCategoryRepository;
    }

    public FeedCategoryRepository(Application application) {
        newsRoomDatabase = NewsRoomDatabase.getDatabase(application);
        feedCategoryDao = newsRoomDatabase.feedCategoryDao();
    }

    public void insertFavNews(FeedCategory feedCategory){
        new InsertFeedCategoryAsyncTask(feedCategoryDao).execute(feedCategory);
    }

    private static class InsertFeedCategoryAsyncTask extends AsyncTask<FeedCategory, Void, Void> {

        private FeedCategoryDao asyncTaskDao;

        InsertFeedCategoryAsyncTask(FeedCategoryDao dao){
            this.asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(FeedCategory... feedCategories) {
            asyncTaskDao.insert(feedCategories[0]);
            return null;
        }
    }

    public void deleteFeedCategory(FeedCategory feedCategory){
        new DeleteFeedCategoryAsyncTask(feedCategoryDao).execute(feedCategory);
    }

    private static class DeleteFeedCategoryAsyncTask extends AsyncTask<FeedCategory, Void, Void>{

        private FeedCategoryDao asyncTaskDao;

        DeleteFeedCategoryAsyncTask(FeedCategoryDao dao){
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FeedCategory... feedCategories) {
            asyncTaskDao.delete(feedCategories[0]);
            return null;
        }
    }

    public LiveData<FeedCategory[]> getFeedCategory(int id){
        return feedCategoryDao.getFeedCategory(id);
    }
}
