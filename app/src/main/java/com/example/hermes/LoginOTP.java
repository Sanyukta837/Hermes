package com.example.hermes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginOTP extends AppCompatActivity {

    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        phone_number = getIntent().getExtras().getString("phone_obtained");
        Toast.makeText(getApplicationContext(),phone_number, Toast.LENGTH_LONG).show();

    }
}