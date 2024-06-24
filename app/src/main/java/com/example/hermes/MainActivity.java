package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent_toLoginn = new Intent(MainActivity.this, Login.class);
        startActivity(intent_toLoginn);
        authentication = FirebaseAuth.getInstance();

        if(authentication == null){
            Intent intent_toLogin = new Intent(MainActivity.this, Login.class);
            startActivity(intent_toLogin);
        }

    }
}