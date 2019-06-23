package com.amitzinfy.ka19news.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.amitzinfy.ka19news.models.room.FavouriteNews;

@Dao
public interface FavouriteNewsDao {

    @Insert
    void insert(FavouriteNews favouriteNews);

    @Delete
    void delete(FavouriteNews favouriteNews);

    @Update
    void update(FavouriteNews favouriteNews);

    @Query("SELECT * FROM favourite_table WHERE id = :id")
    FavouriteNews[] getFavouriteNews(int id);
}