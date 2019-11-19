package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.User;
import com.amitzinfy.ka19news.repositories.NewsDetailsRepository;

public class NewsDetailsViewModel extends AndroidViewModel {

    private NewsDetailsRepository newsDetailsRepository;
    public NewsDetailsViewModel(@NonNull Application application) {
        super(application);
        newsDetailsRepository = new NewsDetailsRepository(application);
    }

    public LiveData<User> getWriterDetails(String writer_id){
        return newsDetailsRepository.getWriterDetails(writer_id);
    }
}
