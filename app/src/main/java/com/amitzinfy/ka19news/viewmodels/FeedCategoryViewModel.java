package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.room.FeedCategory;
import com.amitzinfy.ka19news.repositories.FeedCategoryRepository;

public class FeedCategoryViewModel extends AndroidViewModel {

    private FeedCategoryRepository feedCategoryRepository;

    public FeedCategoryViewModel(@NonNull Application application) {
        super(application);
        feedCategoryRepository = FeedCategoryRepository.getInstance(application);
    }

    public void insertFeedCategory(FeedCategory feedCategory){
        feedCategoryRepository.insertFavNews(feedCategory);
    }

    public void deleteFeedCategory(FeedCategory feedCategory){
        feedCategoryRepository.deleteFeedCategory(feedCategory);
    }

    public LiveData<FeedCategory[]> getFeedCategory(int id){
        return feedCategoryRepository.getFeedCategory(id);
    }

    public LiveData<FeedCategory[]> getFeedCategories(){
        return feedCategoryRepository.getFeedCategories();
    }

}
