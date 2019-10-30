package com.amitzinfy.ka19news.views;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amitzinfy.ka19news.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText nameEditText, emailEditText, addressEditText;
    private RadioGroup genderRadioGroup;
    private DatePicker datePicker;
    private MaterialButton registerButton;

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

        genderRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.male){
                Toast.makeText(this, "male", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_btn){
            int month = datePicker.getMonth() + 1;
            Toast.makeText(this, datePicker.getDayOfMonth() + "/" + month + "/" +
                    datePicker.getYear(), Toast.LENGTH_SHORT).show();
        }
    }
}
