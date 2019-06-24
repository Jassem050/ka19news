package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.viewmodels.MyFeedViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MyFeedPersonalizationActivity extends AppCompatActivity {

    private MyFeedViewModel myFeedViewModel;
    private List<Category> categoryList;
    private Chip[] chip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed_personalization);

        myFeedViewModel = ViewModelProviders.of(this).get(MyFeedViewModel.class);

        ChipGroup feedChipGroup = findViewById(R.id.feed_chip_group);
        feedChipGroup.setChipSpacingVertical(20);
        float eightDp = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 8,
                this.getResources().getDisplayMetrics() );

        categoryList = myFeedViewModel.getCategories();
        if (categoryList.size() > 0){
            chip = new Chip[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++){
                chip[i] = new Chip(MyFeedPersonalizationActivity.this);
                chip[i].setCheckable(true);
                chip[i].setText(categoryList.get(i).getName());
                chip[i].setTextColor(getResources().getColorStateList(R.color.chip_text_color));
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