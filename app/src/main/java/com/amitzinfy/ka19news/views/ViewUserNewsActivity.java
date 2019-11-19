package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.UserNewsAdapter;

public class ViewUserNewsActivity extends AppCompatActivity implements UserNewsAdapter.UserNewsClickListener {

    private RecyclerView recyclerView;
    private UserNewsAdapter userNewsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_news);

        bindViews();
    }

    private void bindViews(){
        recyclerView = findViewById(R.id.user_news_recycler_view);
        userNewsAdapter = new UserNewsAdapter(this, this);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userNewsAdapter);
    }

    @Override
    public void onNewsItemClick(int position) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        startActivity(intent);
    }
}
