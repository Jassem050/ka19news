package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;

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

import java.util.List;

public class ViewUserNewsActivity extends AppCompatActivity implements UserNewsAdapter.UserNewsClickListener {

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
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("news_id", news.getId());
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_description", news.getDescription());
        intent.putExtra("news_image", news.getImage());
        intent.putExtra("news_image_caption", news.getImageCaption());
        intent.putExtra("news_category", news.getCategoryName());
        intent.putExtra("news_time", news.getTime());
        intent.putExtra("writer_id", news.getWriterId());
        intent.putExtra("admin_id", news.getAdmin_id());
        startActivity(intent);
    }
}
