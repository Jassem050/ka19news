package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.GlideApp;
import com.amitzinfy.ka19news.utils.NetworkUtils;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailsActivity";

    private AppCompatTextView newsTitle, newsTime;
    private AppCompatImageView newsImage;
    private WebView newsDescription;
    private String newsTitleString, newsDescriptionString, newsImageString;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        init();
        setViews();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void init(){
        newsTitle = findViewById(R.id.news_title);
        newsImage = findViewById(R.id.news_image);
        newsDescription = findViewById(R.id.news_description);
        newsTitleString = getIntent().getExtras().getString("news_title");
        newsDescriptionString = getIntent().getExtras().getString("news_description");
        newsImageString = getIntent().getExtras().getString("news_image");
        actionBar = getSupportActionBar();
        Log.d(TAG, "init: desc: " + newsDescriptionString);
    }

    private void setViews(){
        newsTitle.setText(newsTitleString);
        newsDescription.loadDataWithBaseURL(null, newsDescriptionString, "text/html","UTF-8", null);
        newsDescription.getSettings().setAppCacheEnabled(true);
        newsDescription.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        GlideApp.with(this).load(NetworkUtils.IMAGE_URL + newsImageString).into(newsImage);
    }
}
