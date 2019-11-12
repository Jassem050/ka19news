package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.Language;
import com.amitzinfy.ka19news.repositories.AddNewsRepository;

import java.util.List;

public class AddNewsViewModel extends AndroidViewModel {

    private AddNewsRepository addNewsRepository;
    public AddNewsViewModel(@NonNull Application application) {
        super(application);
        addNewsRepository = new AddNewsRepository(application);
    }

    public LiveData<List<Language>> getLanguages(String accessToken){
        return addNewsRepository.getLanguages(accessToken);
    }

    public LiveData<List<Category>> getCategories(String accessToken, String languageId){
        return addNewsRepository.getCategories(accessToken, languageId);
    }
}
