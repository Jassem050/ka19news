package com.amitzinfy.ka19news.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.SearchNewsAdapter;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.viewmodels.SearchNewsViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";

    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private SearchNewsViewModel searchNewsViewModel;
    private Observer<List<News>> newsObserver;
    private SearchNewsAdapter searchNewsAdapter;
    private RecyclerView recyclerView;
    private LottieAnimationView searchAnimation;
    private Handler handler;
    private Runnable mRunnable;
    private AppCompatTextView searchQueryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        init();
    }

    private void init(){
        materialToolbar = (MaterialToolbar) findViewById(R.id.search_results_toolbar);
        setSupportActionBar(materialToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        searchNewsAdapter = new SearchNewsAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(searchNewsAdapter);
        searchNewsViewModel = ViewModelProviders.of(this).get(SearchNewsViewModel.class);
        searchAnimation = (LottieAnimationView) findViewById(R.id.search_progress);
        handler = new Handler();
        searchQueryText = (AppCompatTextView) findViewById(R.id.search_query);

    }

    private void subscribe(String searchQuery){
        newsObserver = news -> {
            if (news != null && news.size() > 0) {
                Log.d(TAG, "onChanged: searchresultsactivity: " + news.get(0).getTitle());
                searchNewsAdapter.setNewsList(news);
                searchNewsAdapter.notifyDataSetChanged();
            }
        };
        searchNewsViewModel.setSearchQueryText(searchQuery);
        searchNewsViewModel.getSearchNews().observe(this, newsObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_results_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search_menu).getActionView();
        setSearchView(searchView, searchManager);

        return super.onCreateOptionsMenu(menu);
    }

    // set searchView in toolbar
    private void setSearchView(SearchView searchView, SearchManager searchManager){
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.onActionViewExpanded();
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked");
            searchView.clearFocus();
            searchView.setQuery("", true);
            closeButton.setVisibility(View.GONE);
            searchAnimation.setVisibility(View.GONE);
            searchQueryText.setVisibility(View.GONE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQueryText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchAnimation.setVisibility(View.VISIBLE);
                searchQueryText.setText(String.format("%s%s", getString(R.string.search_results_text) + ": ", newText));
                if (!TextUtils.isEmpty(newText) && newText.length() > 3) {
                    handler.postDelayed(mRunnable = () -> {
                        Toast.makeText(SearchResultsActivity.this, newText, Toast.LENGTH_SHORT).show();
                        subscribe(newText);
                        recyclerView.setVisibility(View.VISIBLE);
                        searchAnimation.setVisibility(View.GONE);
                    }, 1000);

                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            subscribe(query);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
