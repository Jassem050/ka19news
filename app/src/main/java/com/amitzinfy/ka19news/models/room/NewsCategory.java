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

    public NewsCategory(int id, String name) {
        this.id = id;
        this.name = name;
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
}
