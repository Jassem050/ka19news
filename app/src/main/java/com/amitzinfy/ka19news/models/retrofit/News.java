package com.amitzinfy.ka19news.models.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @Expose
    @SerializedName("news_id")
    private int id;
    @Expose
    @SerializedName("news_title")
    private String title;
    @Expose
    @SerializedName("news_description")
    private String description;
    @Expose
    @SerializedName("news_image")
    private String image;
    @Expose
    @SerializedName("news_time")
    private String time;
    @Expose
    @SerializedName("category_name")
    private String categoryName;

    public News(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
