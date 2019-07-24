package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.viewmodels.MyFeedViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MyFeedPersonalizationActivity extends AppCompatActivity {

    private static final String TAG = "MyFeedPersonalizationAc";

    private MyFeedViewModel myFeedViewModel;
    private ChipGroup feedChipGroup;
    private Chip[] chip;
    private RelativeLayout bgLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed_personalization);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bgLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1500);

        myFeedViewModel = ViewModelProviders.of(this).get(MyFeedViewModel.class);

        feedChipGroup = findViewById(R.id.feed_chip_group);
        feedChipGroup.setChipSpacingVertical(20);

        myFeedViewModel.getCategories().observe(this, categories -> {
            Log.d(TAG, "onChanged: categories: " + categories.get(0).getName());
            loadChips(categories);
        });

    }

    public void init(){
        bgLayout = findViewById(R.id.bg_layout);
        progressBar = findViewById(R.id.progress_bar);
        bgLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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
                }
            }
        }
    }
}
