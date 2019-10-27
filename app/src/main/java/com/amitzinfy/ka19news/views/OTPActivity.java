package com.amitzinfy.ka19news.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amitzinfy.ka19news.R;
import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class OTPActivity extends AppCompatActivity {

    private static final String TAG = "OTPActivity";

    private ActionBar actionBar;
    private MaterialButton doneButton;
    private OtpView otpView;
    private String phoneNumber;

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
        otpView = findViewById(R.id.otp_view);
        doneButton = findViewById(R.id.done_btn);
        if (getIntent().hasExtra("phone_number")) {
            Bundle extras = getIntent().getExtras();

            phoneNumber = extras != null ? extras.getString("phone_number") : null;
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
}
