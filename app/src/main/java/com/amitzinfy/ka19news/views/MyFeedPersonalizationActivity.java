package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed_personalization);
        init();
        handler.postDelayed(runnable = () -> {

        }, 1500);

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

        saveButton.setOnClickListener(view -> startActivity(new Intent(MyFeedPersonalizationActivity.this, MainActivity.class)));
    }

    public void init(){
        bgLayout = findViewById(R.id.bg_layout);
        progressBar = findViewById(R.id.progress_bar);
        saveButton = findViewById(R.id.save_btn);
        bgLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        feedCategoryViewModel = ViewModelProviders.of(this).get(FeedCategoryViewModel.class);
        preferenceManager =  PreferenceManager.getInstance(this);
        handler = new Handler();
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
                    feedCategoryViewModel.getFeedCategory(categoryList.get(i).getId()).observe(this, feedCategories -> {
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
                            } else {
                                String text = "," + id;
                                String appnd = preferenceManager.getCategory();
                                String txt = appnd.concat(text);
                                preferenceManager.setIds(txt);
                            }

                            preferenceManager.setCategory(preferenceManager.getIds());
                        } else {
                            feedCategoryViewModel.deleteFeedCategory(feedCategory);
                            if (preferenceManager.getIds() != null) {
                                List<String> c_list = new ArrayList<>(Arrays.asList(preferenceManager.getIds().split(",")));
                                c_list.remove(String.valueOf(id));
                                List<String> cat_list = c_list;
                                preferenceManager.setIds(TextUtils.join(",", cat_list));
                                preferenceManager.setCategory(preferenceManager.getIds());
                            }
                        }
                    });

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
