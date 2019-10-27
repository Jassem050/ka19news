package com.amitzinfy.ka19news.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.OTPResponse;
import com.amitzinfy.ka19news.viewmodels.OTPViewModel;
import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class OTPActivity extends AppCompatActivity {

    private static final String TAG = "OTPActivity";
    private static final int RESOLVE_HINT = 1;

    private ActionBar actionBar;
    private MaterialButton doneButton;
    private OtpView otpView;
    private String phoneNumber;
    private OTPViewModel otpViewModel;
    private String otpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setUpActionBar();
        bindViews();

    }

    private void setUpActionBar(){
        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("");
            actionBar.setElevation(0.0f);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void bindViews(){
        otpViewModel = ViewModelProviders.of(this).get(OTPViewModel.class);
        otpView = findViewById(R.id.otp_view);
        doneButton = findViewById(R.id.done_btn);
        if (getIntent().hasExtra("phone_number")) {
            Bundle extras = getIntent().getExtras();

            phoneNumber = extras != null ? extras.getString("phone_number") : null;
        } else {
            Log.d(TAG, "bindViews: no number");
        }

        if (phoneNumber != null){
            Log.d(TAG, "bindViews: phoneNumber: " + phoneNumber);
            getOtpInfo(phoneNumber);
        }

        doneButton.setOnClickListener(view -> {
            Log.d(TAG, "onClick: doneButton");
            otpView.setText("4564");
        });
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Toast.makeText(OTPActivity.this, "Otp Entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOtpInfo(String phoneNumber){
        Log.d(TAG, "getOtpInfo: entered");
        otpViewModel.getOTP(phoneNumber).observe(this, new Observer<OTPResponse>() {
            @Override
            public void onChanged(OTPResponse otpResponse) {
                otpText = otpResponse.getOtp();
                Log.d(TAG, "onChanged: otp: " + otpText);
            }
        });
    }


}
