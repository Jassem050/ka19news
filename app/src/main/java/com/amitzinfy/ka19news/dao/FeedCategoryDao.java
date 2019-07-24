package com.amitzinfy.ka19news.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.amitzinfy.ka19news.models.room.FeedCategory;


@Dao
public interface FeedCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FeedCategory feedCategory);

    @Delete
    void delete(FeedCategory feedCategory);
}
