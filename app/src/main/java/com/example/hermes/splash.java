package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hermes.utils.FirebaseUtils;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseUtils.isLoggedIn()){
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(splash.this, Login.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 2000);

    }
}