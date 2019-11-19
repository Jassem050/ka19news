package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.User;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailsRepository {

    private static final String TAG = "NewsDetailsRepository";
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public NewsDetailsRepository(Application application){

    }

    private void getWriter(String writer_id){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getWriterDetails(writer_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null){
                    userMutableLiveData.postValue(response.body());
                } else {
                    Log.d(TAG, "onResponse: writer_fail: " + response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: writer: ", t);
                call.clone().enqueue(this);
            }
        });
    }

    public LiveData<User> getWriterDetails(String writer_id){
        getWriter(writer_id);
        return userMutableLiveData;
    }
}
