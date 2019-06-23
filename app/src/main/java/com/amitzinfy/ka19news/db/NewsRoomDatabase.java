package com.amitzinfy.ka19news.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.amitzinfy.ka19news.dao.FavouriteNewsDao;
import com.amitzinfy.ka19news.models.room.FavouriteNews;

@Database(entities = {FavouriteNews.class}, version = 1, exportSchema = false)
public abstract class NewsRoomDatabase extends RoomDatabase {

    public abstract FavouriteNewsDao favouriteNewsDao();
    private static NewsRoomDatabase INSTANCE;

    public static NewsRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (NewsRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, NewsRoomDatabase.class, "ka19news_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
