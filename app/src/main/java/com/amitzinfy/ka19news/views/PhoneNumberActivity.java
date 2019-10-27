package com.amitzinfy.ka19news.views;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amitzinfy.ka19news.R;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PhoneNumberActivity extends AppCompatActivity {

    private static final int CREDENTIAL_PICKER_REQUEST = 1;  // Set to an unused request code

    private ActionBar actionBar;
    private MaterialButton continueButton;
    private TextInputEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        setUpActionBar();
        bindViews();
        try {
            requestHint();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
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
        continueButton = findViewById(R.id.continue_btn);
        phoneNumberEditText = findViewById(R.id.phone_edittext);
        continueButton.setOnClickListener(view -> {
            if (phoneNumberEditText.getText() != null && phoneNumberEditText.getText().length() == 10) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                intent.putExtra("phone_number", phoneNumber);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    // Construct a request for phone numbers and show the picker
    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREDENTIAL_PICKER_REQUEST:
                // Obtain the phone number from the result
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string

                    String phoneNumber = null;
                    if (credential != null) {
                        phoneNumber = parsePhoneNumber(credential.getId());
                    }

                    phoneNumberEditText.setText(phoneNumber);

                }
                break;

        }
    }

    private String parsePhoneNumber(String phoneNumber){
        return phoneNumber.replaceFirst("[+]91","");
    }

}
