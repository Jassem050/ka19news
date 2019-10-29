package com.amitzinfy.ka19news.views;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.OTPResponse;
import com.amitzinfy.ka19news.viewmodels.OTPViewModel;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OTPActivity";
    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code

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
        Task<Void> task = SmsRetriever.getClient(this).startSmsUserConsent(null);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
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


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Toast.makeText(OTPActivity.this, "Otp Entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     *  Retrieve OTP
     */
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


    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    otpView.setText(oneTimeCode);

                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }

    private String parseOneTimeCode(String message) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(message);
        String otp = null;
        while (matcher.find()){
            otp = matcher.group();
        }
        return otp;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsVerificationReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done_btn:
        }
    }
}
