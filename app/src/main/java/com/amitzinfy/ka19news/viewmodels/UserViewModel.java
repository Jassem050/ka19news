package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.NewsAdded;
import com.amitzinfy.ka19news.models.retrofit.User;
import com.amitzinfy.ka19news.models.retrofit.UserResponse;
import com.amitzinfy.ka19news.repositories.UserRepository;

import java.io.File;

public class UserViewModel extends AndroidViewModel {
    private static final String TAG = "UserViewModel";

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<UserResponse> registerUser(String name, String email, String address, String gender,
                                               String dateOfBirth, String phoneNumber){
        Log.d(TAG, "registerUser: viewModel");
        return userRepository.registerUser(name, email, address, gender, dateOfBirth, phoneNumber);
    }
    public LiveData<UserResponse> loginUser(String phoneNumber){
        Log.d(TAG, "loginUser: viewModel");
        return userRepository.loginUser(phoneNumber);
    }

    public LiveData<UserResponse> getUserDetails(String access_token){
        Log.d(TAG, "getUserDetails: viewModel");
        return userRepository.getUserDetails(access_token);
    }

    public LiveData<NewsAdded> getAddedNewsCount(String access_token){
        return userRepository.getAddedNewsCount(access_token);
    }

    public LiveData<User> updateProfImage(String access_token, File file){
        Log.d(TAG, "updateProfImage: viewModel");
        return userRepository.updateProfilePhoto(access_token, file);
    }
}
