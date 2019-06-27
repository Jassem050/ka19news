package com.amitzinfy.ka19news.models.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouriteNews {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "news_title")
    private String title;

    @ColumnInfo(name = "news_description")
    private String description;

    @ColumnInfo(name = "news_image")
    private String image;

    public FavouriteNews(@NonNull int id, @NonNull String title, @NonNull String description, String image){
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
