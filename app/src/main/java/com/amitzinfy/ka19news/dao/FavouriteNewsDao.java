package com.amitzinfy.ka19news.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.amitzinfy.ka19news.models.room.FavouriteNews;

import java.util.List;

@Dao
public interface FavouriteNewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavouriteNews favouriteNews);

    @Delete
    void delete(FavouriteNews favouriteNews);

    @Update
    void update(FavouriteNews favouriteNews);

    @Query("SELECT * from favourite_table")
    LiveData<List<FavouriteNews>> getAllFavNews();

    @Query("SELECT * FROM favourite_table WHERE id = :id")
    LiveData<FavouriteNews[]> getFavouriteNews(int id);
}
