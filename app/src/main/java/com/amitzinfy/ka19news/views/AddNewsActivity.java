package com.amitzinfy.ka19news.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;

public class AddNewsActivity extends AppCompatActivity implements AddNewsDetailsFragment.OnFragmentInteractionListener,
        AddNewsContentFragment.OnFragmentInteractionListener {

    private ActionBar actionBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        preferenceManager = PreferenceManager.getInstance(this);
        loadFragment(AddNewsDetailsFragment.newInstance("news_details", "news_details"));
        setActionBar();
    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void clearAllDataFromPref() {
        preferenceManager.setLanguageId("");
        preferenceManager.setLanguageNameNews("");
        preferenceManager.setCategoryIdNews("");
        preferenceManager.setNewsTitle("");
        preferenceManager.setNewsContent("");
        preferenceManager.setNewsImageUrl("");
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            clearAllDataFromPref();
            finish();
        }
        super.onBackPressed();
    }
}
