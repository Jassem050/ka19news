package com.amitzinfy.ka19news.models.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsAdded {

    @Expose
    @SerializedName("news_count")
    private int newsCount;
    @Expose
    @SerializedName("news_accepted")
    private int newsAcceptedCount;

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public int getNewsAcceptedCount() {
        return newsAcceptedCount;
    }

    public void setNewsAcceptedCount(int newsAcceptedCount) {
        this.newsAcceptedCount = newsAcceptedCount;
    }
}
