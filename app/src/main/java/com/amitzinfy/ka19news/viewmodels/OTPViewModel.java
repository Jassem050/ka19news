package com.amitzinfy.ka19news.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amitzinfy.ka19news.models.retrofit.OTPResponse;
import com.amitzinfy.ka19news.repositories.OTPRepository;

public class OTPViewModel extends AndroidViewModel {

    private static final String TAG = "OTPViewModel";

    private OTPRepository otpRepository;

    public OTPViewModel(@NonNull Application application) {
        super(application);
        otpRepository = new OTPRepository(application);
    }

    public LiveData<OTPResponse> getOTP(String phoneNumber){
        Log.d(TAG, "getOTP: otpViewModel");
        return otpRepository.getOTP(phoneNumber);
    }


}
