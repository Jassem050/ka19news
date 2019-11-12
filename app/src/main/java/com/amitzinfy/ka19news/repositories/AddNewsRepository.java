package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.Language;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewsRepository {
    private static final String TAG = "AddNewsRepository";
    private MutableLiveData<List<Language>> languageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categoryMutableLiveData = new MutableLiveData<>();

    public AddNewsRepository(Application application){}

    private void languages(String accessToken){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Language>> call = apiInterface.getLanguages("Bearer " + accessToken);
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                if (response.isSuccessful() && response.body() != null){
                    languageMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                Log.d(TAG, "onFailure: languages: ", t);
            }
        });
    }

    public LiveData<List<Language>> getLanguages(String accessToken){
        languages(accessToken);
        return languageMutableLiveData;
    }

    private void categories(String accessToken, String languageId){
        Log.d(TAG, "categories: server");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<List<Category>> call = apiInterface.getCategories("Bearer " + accessToken, languageId);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null){
                    categoryMutableLiveData.postValue(response.body());
                } else {
                    Log.d(TAG, "onResponse: fail: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d(TAG, "onFailure: categories: ", t);
            }
        });
    }

    public LiveData<List<Category>> getCategories(String accessToken, String languageId){
        categories(accessToken, languageId);
        return categoryMutableLiveData;
    }
}
