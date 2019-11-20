package com.amitzinfy.ka19news.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.dao.FeedCategoryDao;
import com.amitzinfy.ka19news.dao.NewsCategoryDao;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.models.room.FeedCategory;
import com.amitzinfy.ka19news.models.room.NewsCategory;

@Database(entities = {FavouriteNews.class, NewsCategory.class, FeedCategory.class}, version = 7 , exportSchema = false)
public abstract class NewsRoomDatabase extends RoomDatabase {

    public abstract FavouriteNewsDao favouriteNewsDao();
    public abstract NewsCategoryDao newsCategoryDao();
    public abstract FeedCategoryDao feedCategoryDao();
    private static NewsRoomDatabase INSTANCE;

    public static NewsRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (NewsRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, NewsRoomDatabase.class, "ka19news_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
