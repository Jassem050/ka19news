package com.amitzinfy.ka19news.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.amitzinfy.ka19news.models.room.NewsCategory;
import com.amitzinfy.ka19news.views.DynamicTabFragment;

import java.util.List;

public class CategoryAdapter extends FragmentStatePagerAdapter {

    private List<NewsCategory> categoryList;

    public CategoryAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DynamicTabFragment.newInstance(position + 1, categoryList.get(position).getId());
    }


    public void setCategoryList(List<NewsCategory> categoryList){
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        if (categoryList != null) {
            return categoryList.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getName();
    }
}
