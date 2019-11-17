package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.NewsAdded;
import com.amitzinfy.ka19news.models.retrofit.User;
import com.amitzinfy.ka19news.models.retrofit.UserResponse;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";

    private MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();
    private MutableLiveData<UserResponse> userResponseLiveData = new MutableLiveData<>() ;
    private MutableLiveData<NewsAdded> newsAddedMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> imageResponseMutableLiveData = new MutableLiveData<>();

    public UserRepository(Application application) {}

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

    private void userDetails(String access_token){
        Log.d(TAG, "userDetails: serverResp");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<UserResponse> call = apiInterface.getUserDetails("Bearer " + access_token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    userResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: userDetails: ", t);
            }
        });
    }

    public LiveData<UserResponse> getUserDetails(String access_token){
        userDetails(access_token);
        return userResponseLiveData;
    }

    private void newsCount(String access_token){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<NewsAdded> call = apiInterface.getNewsCount("Bearer " + access_token);
        call.enqueue(new Callback<NewsAdded>() {
            @Override
            public void onResponse(Call<NewsAdded> call, Response<NewsAdded> response) {
                if (response.isSuccessful() && response.body() != null){
                    newsAddedMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NewsAdded> call, Throwable t) {
                Log.d(TAG, "onFailure: newsAdded ", t);
            }
        });
    }

    public LiveData<NewsAdded> getAddedNewsCount(String access_token){
        newsCount(access_token);
        return newsAddedMutableLiveData;
    }

    private void updateProfImage(String accessToken, File file){
        Log.d(TAG, "updateProfImage: serverRepo");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);

        RequestBody fileReqBody = RequestBody.create(MediaType.get("image/*"), file);
        Log.d(TAG, "updateProfImage: fileName: " + file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        Call<User> call = apiInterface.updateProfImage("Bearer " + accessToken, part);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null){
                    imageResponseMutableLiveData.postValue(response.body());
                    Log.d(TAG, "onResponse: updateImage: " + response.body().getImage() + "\n message: "
                            + response.body().getMessage());
                } else {
                    Log.d(TAG, "onResponse: updateImage:else " + response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: updateImage: ", t);
                call.clone().enqueue(this);
            }
        });
    }


    public LiveData<User> updateProfilePhoto(String access_token, File file){
        updateProfImage(access_token, file);
        return imageResponseMutableLiveData;
    }

    private void logout(String accessToken){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<UserResponse> call = apiInterface.logoutUser("Bearer " + accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    userResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: logout ", t);
                call.clone().enqueue(this);
            }
        });
    }

    public LiveData<UserResponse> logoutUser(String accessToken){
        logout(accessToken);
        return userResponseLiveData;
    }
}
