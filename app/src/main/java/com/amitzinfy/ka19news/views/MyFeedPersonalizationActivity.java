package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.room.FeedCategory;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.FeedCategoryViewModel;
import com.amitzinfy.ka19news.viewmodels.MyFeedViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFeedPersonalizationActivity extends AppCompatActivity {

    private static final String TAG = "MyFeedPersonalizationAc";

    private MyFeedViewModel myFeedViewModel;
    private ChipGroup feedChipGroup;
    private Chip[] chip;
    private RelativeLayout bgLayout;
    private ProgressBar progressBar;
    private MaterialButton saveButton;
    private FeedCategoryViewModel feedCategoryViewModel;
    private PreferenceManager preferenceManager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed_personalization);
        init();

        myFeedViewModel = ViewModelProviders.of(this).get(MyFeedViewModel.class);

        feedChipGroup = findViewById(R.id.feed_chip_group);
        feedChipGroup.setChipSpacingVertical(20);

        myFeedViewModel.getCategories().observe(this, categories -> {
            if (categories !=  null && categories.size() > 0) {
                loadChips(categories);
                bgLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

        if (preferenceManager.getIsFeedFirstTimeLaunch() == 2){
            saveButton.setVisibility(View.GONE);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        saveButton.setOnClickListener(view -> {
            preferenceManager.setIsFeedFirstTimeLaunch(2);
            startActivity(new Intent(MyFeedPersonalizationActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void init(){
        bgLayout = findViewById(R.id.bg_layout);
        progressBar = findViewById(R.id.progress_bar);
        saveButton = findViewById(R.id.save_btn);
        bgLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        feedCategoryViewModel = ViewModelProviders.of(this).get(FeedCategoryViewModel.class);
        preferenceManager =  PreferenceManager.getInstance(this);
        actionBar = getSupportActionBar();
    }

    public void loadChips(List<Category> categoryList){
        feedChipGroup.removeAllViews();
        float eightDp = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 8,
                this.getResources().getDisplayMetrics() );
        if (categoryList != null) {
            if (categoryList.size() > 0) {
                chip = new Chip[categoryList.size()];
                for (int i = 0; i < categoryList.size(); i++) {
                    chip[i] = new Chip(MyFeedPersonalizationActivity.this);
                    chip[i].setCheckable(true);
                    chip[i].setText(categoryList.get(i).getName());
                    chip[i].setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text_color));
                    chip[i].setChipStrokeColorResource(R.color.colorPrimary);
                    chip[i].setChipStrokeWidth(4);
                    chip[i].setCheckedIconVisible(false);
                    chip[i].setChipBackgroundColorResource(R.color.chip_bg_color);
                    chip[i].setChipStartPadding(eightDp);
                    chip[i].setChipEndPadding(eightDp);
                    feedChipGroup.addView(chip[i], i);

                    int id = categoryList.get(i).getId();
                    int finalI = i;
                    feedCategoryViewModel.getFeedCategory(id).observe(this, feedCategories -> {
                        if (feedCategories.length > 0){
                            chip[finalI].setChecked(true);
                        } else {
                            chip[finalI].setChecked(false);
                        }
                    });

                    FeedCategory feedCategory = new FeedCategory(categoryList.get(i).getId());
                    chip[i].setOnCheckedChangeListener((compoundButton, b) -> {
                        if (b){
                            feedCategoryViewModel.insertFeedCategory(feedCategory);
                            if (preferenceManager.getIds().equals("id")){
                                preferenceManager.setIds(String.valueOf(id));
                            } else if (!preferenceManager.getIds().contains(String.valueOf(id))){
                                String text = "," + id;
                                String appnd = preferenceManager.getCategory();
                                String txt = appnd.concat(text);
                                preferenceManager.setIds(txt);
                            }

                            preferenceManager.setCategory(preferenceManager.getIds());
                            Log.d(TAG, "loadChips: check: feed_categories: " + preferenceManager.getCategory());
                        } else {
                            feedCategoryViewModel.deleteFeedCategory(feedCategory);
                            if (preferenceManager.getIds() != null) {
                                List<String> c_list = new ArrayList<>(Arrays.asList(preferenceManager.getIds().split(",")));
                                c_list.remove(String.valueOf(id));
                                List<String> cat_list = c_list;
                                preferenceManager.setIds(TextUtils.join(",", cat_list));
                                Log.d(TAG, "loadChips: ids: " + preferenceManager.getIds());
                                preferenceManager.clearCategory();
                                preferenceManager.setCategory(preferenceManager.getIds());
                                Log.d(TAG, "loadChips: uncheck: feed_categories: " + preferenceManager.getCategory());
                            }
                        }
                    });

                }
            }
        }
    }

}
