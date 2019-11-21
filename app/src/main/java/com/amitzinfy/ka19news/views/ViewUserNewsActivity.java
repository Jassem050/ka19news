package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.UserNewsAdapter;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.UserViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewUserNewsActivity extends AppCompatActivity implements UserNewsAdapter.UserNewsClickListener {

    private static final String TAG = "ViewUserNewsActivity";

    private RecyclerView recyclerView;
    private UserNewsAdapter userNewsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private UserViewModel userViewModel;
    private PreferenceManager preferenceManager;
    private ActionBar actionBar;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_news);
        setUpActionBar();
        bindViews();
        getUserAddedNews(preferenceManager.getAccessToken());
    }

    private void bindViews(){
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        preferenceManager = PreferenceManager.getInstance(this);
        recyclerView = findViewById(R.id.user_news_recycler_view);
        userNewsAdapter = new UserNewsAdapter(this, this);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userNewsAdapter);
    }

    private void setUpActionBar(){
        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void getUserAddedNews(String accessToken){
        userViewModel.getUserNews(accessToken).observe(this, news -> {
            newsList = news;
            userNewsAdapter.setNewsList(news);
            recyclerView.setAdapter(userNewsAdapter);
        });
    }

    @Override
    public void onNewsItemClick(int position) {
        News news = newsList.get(position);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf.parse(news.getDate() + " " + news.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMillis = cal.getTimeInMillis();
        String time = DateUtils.getRelativeTimeSpanString(timeInMillis,  System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_MONTH).toString();

        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("news_id", news.getId());
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_description", news.getDescription());
        intent.putExtra("news_image", news.getImage());
        intent.putExtra("news_image_caption", news.getImageCaption());
        intent.putExtra("news_category", news.getCategoryName());
        intent.putExtra("news_time", time);
        intent.putExtra("news_date", formatDate("yyyy-MM-dd HH:mm:ss", "MMMM dd, yyyy HH:mm",
                news.getDate()+ " " + news.getTime()));
        intent.putExtra("writer_id", news.getWriterId());
        intent.putExtra("admin_id", news.getAdmin_id());
        startActivity(intent);
    }

    public static String formatDate(String fromFormat, String toFormat, String dateToFormat) {
        SimpleDateFormat inFormat = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = inFormat.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat(toFormat);

        return outFormat.format(date);
    }

}
