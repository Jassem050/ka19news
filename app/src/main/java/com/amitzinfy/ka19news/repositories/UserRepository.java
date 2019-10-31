package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.UserResponse;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";

    private MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();

    public UserRepository(Application application) {
    }

    private void register(String name, String email, String address, String gender,
                         String dateOfBirth, String phoneNumber) {
        Log.d(TAG, "register: serverRep");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<UserResponse> call = apiInterface.registerUser(name, email, address, gender, dateOfBirth, phoneNumber);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    userLiveData.postValue(response.body());
                } else {
                    Log.d(TAG, "onResponse: "+ response);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });
    }

    public LiveData<UserResponse> registerUser(String name, String email, String address, String gender,
                                               String dateOfBirth, String phoneNumber){
        Log.d(TAG, "registerUser: repo");
        register(name, email, address, gender, dateOfBirth, phoneNumber);
        return userLiveData;
    }

    private void login(String phoneNumber){
        Log.d(TAG, "login: serverRepo");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<UserResponse> call = apiInterface.loginUser(phoneNumber);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    userLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });
    }
    
    public LiveData<UserResponse> loginUser(String phoneNumber){
        Log.d(TAG, "loginUser: repo");
        login(phoneNumber);
        return userLiveData;
    }
}
