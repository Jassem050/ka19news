package com.amitzinfy.ka19news.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.amitzinfy.ka19news.models.room.NewsCategory;

import java.util.List;

@Dao
public interface NewsCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(NewsCategory newsCategory);

    @Query("SELECT * FROM news_categories")
    public LiveData<List<NewsCategory>> getAllCategories();
}
