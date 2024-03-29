package com.amitzinfy.ka19news.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ShareCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchResultsActivity extends AppCompatActivity implements SearchNewsAdapter.NewsItemClickListener {

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
    private AppCompatTextView searchQueryText, noResultsText;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        init();
    }

    private void init(){
        materialToolbar = findViewById(R.id.search_results_toolbar);
        setSupportActionBar(materialToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        searchNewsAdapter = new SearchNewsAdapter(this, this);
        recyclerView = findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        searchNewsViewModel = ViewModelProviders.of(this).get(SearchNewsViewModel.class);
        searchAnimation = findViewById(R.id.search_progress);
        handler = new Handler();
        searchQueryText = findViewById(R.id.search_query);
        noResultsText = findViewById(R.id.no_results);

    }

    private void subscribe(String searchQuery){
        noResultsText.setVisibility(View.GONE);
        newsObserver = news -> {
            newsList = news;
            if (newsList != null && newsList.size() > 0) {
                searchAnimation.setVisibility(View.GONE);
                noResultsText.setVisibility(View.GONE);
                searchNewsAdapter.setNewsList(newsList);
                searchNewsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(searchNewsAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
                handler.postDelayed(mRunnable = () ->  {
                    if (newsList != null && newsList.size() == 0){
                        searchAnimation.setVisibility(View.GONE);
                        noResultsText.setVisibility(View.VISIBLE);
                    }
                }, 1500);
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
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked");
            searchView.clearFocus();
            searchView.setQuery("", true);
            closeButton.setVisibility(View.GONE);
            newsList.clear();
            searchAnimation.setVisibility(View.GONE);
            searchQueryText.setVisibility(View.GONE);
            recyclerView.invalidate();
            recyclerView.setVisibility(View.GONE);
            noResultsText.setVisibility(View.GONE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noResultsText.setVisibility(View.GONE);
                searchQueryText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                if (newsList != null) newsList.clear();
                searchQueryText.setText(String.format("%s%s", getString(R.string.search_results_text) + ": ", query));
                if (!TextUtils.isEmpty(query) && query.length() > 0) {
                    searchAnimation.setVisibility(View.VISIBLE);
                    searchAnimation.playAnimation();
                    handler.postDelayed(mRunnable = () -> subscribe(query), 1500);


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noResultsText.setVisibility(View.GONE);
//                searchQueryText.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//                searchAnimation.setVisibility(View.VISIBLE);
//                searchQueryText.setText(String.format("%s%s", getString(R.string.search_results_text) + ": ", newText));
//                if (!TextUtils.isEmpty(newText) && newText.length() > 3) {
//                    handler.postDelayed(mRunnable = () -> {
//                        Toast.makeText(SearchResultsActivity.this, newText, Toast.LENGTH_SHORT).show();
//                        subscribe(newText);
//                        recyclerView.setVisibility(View.VISIBLE);
//                        searchAnimation.setVisibility(View.GONE);
//                    }, 1500);
//
//                }
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
            noResultsText.setVisibility(View.GONE);
//            subscribe(query);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    public void onItemToggleButtonChecked(int position) {
        News news = newsList.get(position);
        Log.d(TAG, "onItemToggleButtonChecked: id: " + news.getId());
//        searchNewsViewModel.insertFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
//                news.getImage(), news.getCategoryName(), news.getImageCaption(), news.getWriterId()));
    }

    @Override
    public void onItemToggleButtonUnChecked(int position) {
        News news = newsList.get(position);
        Log.d(TAG, "onItemToggleButtonUnChecked: id: " + news.getId());
//        searchNewsViewModel.deleteFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
//                news.getImage(), news.getCategoryName(), news.getImageCaption(), news.getWriterId()));
    }

    @Override
    public void setItemToggleButton(ToggleButton toggleButton, int position) {
        News news = newsList.get(position);
        searchNewsViewModel.getFavouriteNews(news.getId()).observe(this, favouriteNews -> {
            if (favouriteNews.length > 0){
                if (!toggleButton.isChecked()) {
                    toggleButton.setChecked(true);
                }
            } else {
                if (toggleButton.isChecked())
                    toggleButton.setChecked(false);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        News news = newsList.get(position);
        Intent intent = new Intent(this, NewsDetailsActivity.class);
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

        return outFormat.format(Objects.requireNonNull(date));
    }

    @Override
    public void onShareButtonClicked(int position) {
        News news = newsList.get(position);
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType("text/plain")
                    .setChooserTitle("Share News with: ")
                    .setText("https://sports.ndtv.com/west-indies-vs-india-2019/ms-dhoni-doubtful-for-west-indies-tour-will-participate-in-transitioning-phase-for-team-india-report-2070897?News_Trending")
                    .startChooser();
    }


}
