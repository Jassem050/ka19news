package com.amitzinfy.ka19news.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    private void subscribe(String searchQuery){
        newsObserver = news -> {
            if (news != null && news.size() > 0) {
                Log.d(TAG, "onChanged: searchresultsactivity: " + news.get(0).getTitle());
                searchNewsAdapter.setNewsList(news);
                searchNewsAdapter.notifyDataSetChanged();
            }
        };
//        searchNewsAdapter.clearAllNews();
//        searchNewsAdapter.setNewsList(searchNewsViewModel.getSearchNews(searchQuery));
//        searchNewsAdapter.notifyDataSetChanged();
        searchNewsViewModel.setSearchQueryText(searchQuery);
        searchNewsViewModel.getSearchNews().observe(this, newsObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_results_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search_menu).getActionView();
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && newText.length() > 3) {
                    Toast.makeText(SearchResultsActivity.this, newText, Toast.LENGTH_SHORT).show();
                    subscribe(newText);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
}
