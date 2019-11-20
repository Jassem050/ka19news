package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.UserResponse;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private TextInputEditText nameEditText, emailEditText, addressEditText;
    private RadioGroup genderRadioGroup;
    private DatePicker datePicker;
    private MaterialButton registerButton;
    private UserViewModel userViewModel;
    private UserResponse userResp;
    private String gender, phoneNumber;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();

    }

    private void bindViews(){
        nameEditText = findViewById(R.id.name_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        addressEditText = findViewById(R.id.address_edittext);
        genderRadioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.date_picker);
        registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(this);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        preferenceManager = PreferenceManager.getInstance(this);

        genderRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.male){
                gender = getString(R.string.male);
            } else {
                gender = getString(R.string.female);
            }
        });

        if (getIntent().hasExtra("phone_number")) {
            Bundle extras = getIntent().getExtras();

            phoneNumber = extras != null ? extras.getString("phone_number") : null;
            Log.d(TAG, "bindViews: phone Number");
        } else {
            Log.d(TAG, "bindViews: no number");
        }

    }

    /*
    *   observing user LiveData
    *   @param name, email, address, gender, dateOfBirth, phoneNumber
    * */
    private void registerUser(String name, String email, String address, String gender,
                              String dateOfBirth, String phoneNumber){
        userViewModel.registerUser(name, email, address, gender, dateOfBirth, phoneNumber)
                .observe(this, userResponse -> {
            this.userResp = userResponse;
            Log.d(TAG, "registerUser: activity " + this.userResp.getAccessToken());
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            preferenceManager.setAppStatus(getString(R.string.writer_status));
            preferenceManager.setUserStatus(getString(R.string.logged_in_status));
            preferenceManager.setAccessToken(this.userResp.getAccessToken());
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_btn){
            Log.d(TAG, "onClick: in");
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String dateOfBirth = year + "-" + month + "-" + day;
            Log.d(TAG, "onClick: phone: " + phoneNumber);
            Log.d(TAG, "onClick: name: " + name);
            Log.d(TAG, "onClick: email: " + email);
            Log.d(TAG, "onClick: address: " + address);
            Log.d(TAG, "onClick: dateOfBirth : " + dateOfBirth);
            
            if (!name.equals("") && gender!= null && !gender.equals("") && !address.equals("") && !dateOfBirth.equals("")) {
                Log.d(TAG, "onClick: inif");
                registerUser(name, email, address, gender, dateOfBirth, phoneNumber);
            } else {
                Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
