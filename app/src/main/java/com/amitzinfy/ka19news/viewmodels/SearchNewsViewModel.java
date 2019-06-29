package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.repositories.SearchNewsRepository;

import java.util.List;

public class SearchNewsViewModel extends AndroidViewModel {

    private SearchNewsRepository searchNewsRepository;

    public SearchNewsViewModel(@NonNull Application application) {
        super(application);
        searchNewsRepository = SearchNewsRepository.getInstance();
    }

    public LiveData<List<News>> getSearchNews(String searchQuery){
        return searchNewsRepository.getSearchNews(searchQuery);
    }
}
