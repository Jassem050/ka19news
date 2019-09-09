package com.amitzinfy.ka19news.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.amitzinfy.ka19news.models.room.FeedCategory;


@Dao
public interface FeedCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FeedCategory feedCategory);

    @Delete
    void delete(FeedCategory feedCategory);

    @Query("SELECT * from feed_categories WHERE id= :id")
    LiveData<FeedCategory[]> getFeedCategory(int id);

    @Query("SELECT * from feed_categories")
    LiveData<FeedCategory[]> getFeedCategories();
}
