package com.amitzinfy.ka19news.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_categories")
public class NewsCategory {

    @PrimaryKey
    @ColumnInfo
    private int id;

    @ColumnInfo(name = "category_name")
    private String name;

    @ColumnInfo(name = "language_name")
    private String language_name;

    public NewsCategory(int id, String name, String language_name) {
        this.id = id;
        this.name = name;
        this.language_name = language_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }
}
