package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amitzinfy.ka19news.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PhoneNumberActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private MaterialButton continueButton;
    private TextInputEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
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


}
