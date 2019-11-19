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
    @SerializedName("news_content")
    private String description;
    @Expose
    @SerializedName("news_image")
    private String image;
    @Expose
    @SerializedName("news_image_caption")
    private String imageCaption;
    @Expose
    @SerializedName("news_time")
    private String time;
    @Expose
    @SerializedName("category_name")
    private String categoryName;
    @Expose
    @SerializedName("writer_id")
    private String writerId;
    @Expose
    @SerializedName("admin_id")
    private String admin_id;
    @Expose
    @SerializedName("news_status")
    private String newsStatus;

    private String messageType;
    private String errorMessage;

    public News(String title) {
        this.title = title;
    }

    public News(String messageType, String errorMessage){
        this.messageType = messageType;
        this.errorMessage = errorMessage;
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

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(String newsStatus) {
        this.newsStatus = newsStatus;
    }
}
