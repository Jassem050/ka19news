package com.amitzinfy.ka19news.models.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {

    @Expose
    @SerializedName("language_name")
    private String languageName;
    @Expose
    @SerializedName("language")
    private String language;

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
