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

    @ColumnInfo(name = "language_id")
    private int language_id;

    public NewsCategory(int id, String name, int language_id) {
        this.id = id;
        this.name = name;
        this.language_id = language_id;
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

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }
}
