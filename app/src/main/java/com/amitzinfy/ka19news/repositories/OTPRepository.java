package com.amitzinfy.ka19news.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amitzinfy.ka19news.models.retrofit.OTPResponse;
import com.amitzinfy.ka19news.utils.ApiInterface;
import com.amitzinfy.ka19news.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPRepository {

    private static final String TAG = "OTPRepository";
    private MutableLiveData<OTPResponse> otpResponseLiveData = new MutableLiveData<>();

    public OTPRepository(Application application) {
    }

    public void getOTPInfo(String phoneNumber){
        Log.d(TAG, "getOTPInfo: networkCall");
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient().create(ApiInterface.class);
        Call<OTPResponse> call = apiInterface.getOTPInfo(phoneNumber);
        call.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "onResponse: otp entered");
                    otpResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: otpInfo", t);
            }
        });
    }

    public LiveData<OTPResponse> getOTP(String phoneNumber){
        Log.d(TAG, "getOTP: otpRepo");
        getOTPInfo(phoneNumber);
        return otpResponseLiveData;
    }
}
