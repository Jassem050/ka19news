package com.amitzinfy.ka19news.models.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouriteNews {

    @PrimaryKey
    @ColumnInfo
    private int id;

    @ColumnInfo(name = "news_title")
    private String title;

    @ColumnInfo(name = "news_description")
    private String description;

    @ColumnInfo(name = "news_image")
    private String image;

    @ColumnInfo(name = "news_image_caption")
    private String imageCaption;

    @ColumnInfo(name = "news_category")
    private String category;

    @ColumnInfo(name = "writer_name")
    private String writerName;

    @ColumnInfo(name = "news_time")
    private String time;

    public FavouriteNews(int id, @NonNull String title, @NonNull String description, String image,
                         String category, String imageCaption, String writerName, String time){
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.category = category;
        this.imageCaption = imageCaption;
        this.writerName = writerName;
        this.time = time;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
