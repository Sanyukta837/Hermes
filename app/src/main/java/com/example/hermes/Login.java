package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText phoneNumber;
    Button send_otp;
    ProgressBar login_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country);
        phoneNumber = findViewById(R.id.login_number);
        send_otp = findViewById(R.id.button_send_otp);
        login_progressBar = findViewById(R.id.login_progressbar);

        //country code picker le line phone number source
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
        login_progressBar.setVisibility(View.GONE);

        send_otp.setOnClickListener((v) ->{
            if(!countryCodePicker.isValidFullNumber()){
                phoneNumber.setError("Phone number is not valid");
                return;
            }
            Intent intent = new Intent(Login.this, LoginOTP.class);
            intent.putExtra("phone_obtained", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });



    }
}














