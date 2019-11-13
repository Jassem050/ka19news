package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.AddNewsResponse;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.Language;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewsRepository {
    private static final String TAG = "AddNewsRepository";
    private MutableLiveData<List<Language>> languageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categoryMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<AddNewsResponse> addNewsResponseMutableLiveData = new MutableLiveData<>();

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
                call.clone().enqueue(this);
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
                call.clone().enqueue(this);
            }
        });
    }

    public LiveData<List<Category>> getCategories(String accessToken, String languageId){
        categories(accessToken, languageId);
        return categoryMutableLiveData;
    }

    private void addNews(String accessToken, File file, String languageId, String languageName,
                         String categoryId, String newsTitle, String newsContent, String imgCaption, String place){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);

        RequestBody fileReqBody = RequestBody.create(MediaType.get("image/*"), file);
        Log.d(TAG, "updateProfImage: fileName: " + file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        RequestBody language_id = RequestBody.create(MediaType.parse("text/plain"), languageId);
        RequestBody language_name = RequestBody.create(MediaType.parse("text/plain"), languageName);
        RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), categoryId);
        RequestBody news_title = RequestBody.create(MediaType.parse("text/plain"), newsTitle);
        RequestBody news_content = RequestBody.create(MediaType.parse("text/plain"), newsContent);
        RequestBody img_caption = RequestBody.create(MediaType.parse("text/plain"), imgCaption);
        RequestBody place_loc = RequestBody.create(MediaType.parse("text/plain"), place);

        Call<AddNewsResponse> call = apiInterface.postNews("Bearer " + accessToken, part, language_id,
                language_name, category_id, news_title, news_content, img_caption, place_loc);
        call.enqueue(new Callback<AddNewsResponse>() {
            @Override
            public void onResponse(Call<AddNewsResponse> call, Response<AddNewsResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "onResponse: addnews" + response.body().getSuccess());
                } else {
                    Log.d(TAG, "onResponse: addnewsfail: " + response);
                }
            }

            @Override
            public void onFailure(Call<AddNewsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: addnews: ", t);
            }
        });
    }

    public LiveData<AddNewsResponse> postNews(String accessToken, File file, String languageId,
                                              String languageName, String categoryId, String newsTitle,
                                              String newsContent, String imgCaption, String place){
        addNews(accessToken, file, languageId, languageName, categoryId, newsTitle, newsContent, imgCaption, place);
        return addNewsResponseMutableLiveData;
    }
}
