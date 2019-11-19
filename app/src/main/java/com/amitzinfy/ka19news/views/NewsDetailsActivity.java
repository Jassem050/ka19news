package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.utils.NetworkUtils;
import com.amitzinfy.ka19news.viewmodels.FavouritesViewModel;
import com.amitzinfy.ka19news.viewmodels.NewsDetailsViewModel;
import com.chinalwb.are.glidesupport.GlideApp;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailsActivity";

    private AppCompatTextView newsTitle, newsTime, newsImageCaption, newsAuthor;
    private AppCompatImageView newsImage;
    private WebView newsDescription;
    private String newsTitleString, newsDescriptionString, newsImageString, newsCategoryString, newsImageCaptionString;
    private String newsWriterId, newsAdminId, newsWriterName, writerName;
    private int newsId;
    private ActionBar actionBar;
    private AppCompatToggleButton toggleButton;
    private FavouritesViewModel favouritesViewModel;
    private NewsDetailsViewModel newsDetailsViewModel;

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
        Bundle bundle = getIntent().getExtras();
        newsTitle = findViewById(R.id.news_title);
        newsImage = findViewById(R.id.news_image);
        newsDescription = findViewById(R.id.news_description);
        newsImageCaption = findViewById(R.id.news_image_caption);
        newsAuthor = findViewById(R.id.news_author);
        if (bundle != null) {
            newsTitleString = bundle.getString("news_title");
            newsDescriptionString = bundle.getString("news_description");
            newsImageString = bundle.getString("news_image");
            newsId = bundle.getInt("news_id");
            newsCategoryString = bundle.getString("news_category");
            newsImageCaptionString = bundle.getString("news_image_caption");
            newsWriterId = bundle.getString("writer_id", null);
            newsAdminId = bundle.getString("admin_id");
            writerName = bundle.getString("writer_name", null);
            Log.d(TAG, "init: writer_name: " + writerName + " : " + newsWriterId);
            Log.d(TAG, "init: newsID: " + newsId);
        }
        actionBar = getSupportActionBar();
        Log.d(TAG, "init: desc: " + newsDescriptionString);
        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        newsDetailsViewModel = ViewModelProviders.of(this).get(NewsDetailsViewModel.class);
    }

    private void setViews(){
        if (newsWriterId != null && writerName == null){
            getWriterDetails(newsWriterId);
        } else if (newsWriterId == null && writerName != null){
            newsAuthor.setText(writerName);
        } else {
            newsAuthor.setText(getString(R.string.app_name));
        }
        newsTitle.setText(newsTitleString);
        newsImageCaption.setText(newsImageCaptionString);
        newsDescription.loadDataWithBaseURL(null, newsDescriptionString, "text/html","UTF-8", null);
        newsDescription.getSettings().setAppCacheEnabled(true);
        newsDescription.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        GlideApp.with(this).load(NetworkUtils.IMAGE_URL + newsImageString).into(newsImage);
    }

    private void getWriterDetails(String newsWriterId){
        newsDetailsViewModel.getWriterDetails(newsWriterId).observe(this, user -> {
            newsAuthor.setText(user.getName());
            newsWriterName = user.getName();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_menu, menu);
        menu.findItem(R.id.favourite_item).setActionView(R.layout.toggle_layout);
        toggleButton = menu.findItem(R.id.favourite_item)
                .getActionView().findViewById(R.id.toolbar_toggle_favourite);
        favouritesViewModel.getFavouriteNews(newsId).observe(this, favouriteNews -> {
            if (favouriteNews.length > 0){
                if (!toggleButton.isChecked())
                    toggleButton.setChecked(true);
            } else {
                if (toggleButton.isChecked())
                    toggleButton.setChecked(false);
            }
        });

        FavouriteNews favouriteNews = new FavouriteNews(newsId, newsTitleString,
                newsDescriptionString, newsImageString, newsCategoryString, newsImageCaptionString, newsAuthor.getText().toString());
        toggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                favouritesViewModel.insertFavNews(favouriteNews);
            } else {
                favouritesViewModel.deleteFavNews(favouriteNews);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share_item){
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType("text/plain")
                    .setChooserTitle("Share News with: ")
                    .setText("https://sports.ndtv.com/west-indies-vs-india-2019/ms-dhoni-doubtful-for-west-indies-tour-will-participate-in-transitioning-phase-for-team-india-report-2070897?News_Trending")
                    .startChooser();
        }
        return super.onOptionsItemSelected(item);
    }
}
