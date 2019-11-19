package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditProfileActivity";

    private TextInputLayout nameInputLayout, emailInputLayout, addressInputLayout, mobileInputLayout;
    private TextInputEditText nameEditText, emailEditText, addressEditText, mobileEditText;
    private DatePicker datePicker;
    private RadioGroup genderRadioGroup;
    private RadioButton maleBtn, femaleBtn;
    private PreferenceManager preferenceManager;
    private ActionBar actionBar;
    private MaterialButton updateProfileBtn;
    private String gender;
    private UserViewModel userViewModel;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setUpActionBar();
        bindViews();
        genderRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.male){
                gender = "Male";
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.female){
                gender = "Female";
            }
        });
    }

    private void setUpActionBar(){
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Edit Profile");
        }
    }

    private void bindViews(){
        nameInputLayout = findViewById(R.id.name_layout);
        emailInputLayout = findViewById(R.id.email_layout);
        addressInputLayout = findViewById(R.id.address_layout);
        mobileInputLayout = findViewById(R.id.phone_layout);
        nameEditText = findViewById(R.id.name_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        addressEditText = findViewById(R.id.address_edittext);
        mobileEditText = findViewById(R.id.phone_edittext);
        datePicker = findViewById(R.id.date_picker);
        genderRadioGroup = findViewById(R.id.radio_group);
        maleBtn = findViewById(R.id.male);
        femaleBtn = findViewById(R.id.female);
        updateProfileBtn = findViewById(R.id.update_btn);
        progressBarLayout = findViewById(R.id.progress_bar_layout);

        preferenceManager = PreferenceManager.getInstance(this);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        nameEditText.setText(preferenceManager.getUserName());
        nameEditText.requestFocus(nameEditText.getText().length());
        emailEditText.setText(preferenceManager.getUserEmail());
        addressEditText.setText(preferenceManager.getUserAddress());
        mobileEditText.setText(preferenceManager.getUserPhoneNo());
        if (preferenceManager.getUserGender().equals("Male")){
            gender = "Male";
            maleBtn.setChecked(true);
        } else {
            gender = "Female";
            femaleBtn.setChecked(true);
        }
        String[] date = preferenceManager.getUserDob().split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]) - 1;
        int day = Integer.parseInt(date[2]);
        datePicker.updateDate(year, month, day);
        updateProfileBtn.setOnClickListener(this);
    }

    private void updateProfileInfo(String accessToken, String name, String email, String phoneNumber, String gender,
                                   String address, String dateOfBirth){
        userViewModel.updateProfile(accessToken, name, email, phoneNumber, gender, address, dateOfBirth)
                .observe(this, userResponse -> {
                    if (userResponse != null && userResponse.getMessage().equals("Profile Updated")){
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        preferenceManager.setUserName(name);
                        preferenceManager.setUserEmail(email);
                        preferenceManager.setUserPhoneNo(phoneNumber);
                        preferenceManager.setUserGender(gender);
                        preferenceManager.setUserDob(dateOfBirth);
                        preferenceManager.setUserAddress(address);
                        progressBarLayout.setVisibility(View.GONE);
                        finish();
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_btn){
            progressBarLayout.setVisibility(View.VISIBLE);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String mobile = mobileEditText.getText().toString();
            String dateOfBirth = year + "-" + month + "-" + day;
            String accessToken = preferenceManager.getAccessToken();

            if (!name.equals("") && !email.equals("") && !mobile.equals("") && gender!= null &&
                    !gender.equals("") && !address.equals("") && !dateOfBirth.equals("")) {
                Log.d(TAG, "onClick: inif");
                updateProfileInfo(accessToken, name, email, mobile, gender, address, dateOfBirth);

            } else {
                progressBarLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
