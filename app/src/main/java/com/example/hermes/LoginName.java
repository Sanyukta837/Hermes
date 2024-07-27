package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hermes.model.UserModel;
import com.example.hermes.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.time.Instant;

public class LoginName extends AppCompatActivity {

    EditText username_input;
    Button username_enter_button;
    ProgressBar username_progressBar;
    String phone_number;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);

        username_input = findViewById(R.id.login_username);
        username_enter_button = findViewById(R.id.button_username);
        username_progressBar = findViewById(R.id.username_progressbar);
        phone_number = getIntent().getExtras().getString("phone");

        getUsername();

        username_enter_button.setOnClickListener(v -> {
            setUsername();
        });


    }


    void getUsername(){
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    userModel = task.getResult().toObject(UserModel.class);
                    if(userModel != null)
                        username_input.setText(userModel.getName());
                }
            }
        });
    }

    void setUsername(){
        setInprogress(true);
        String username = username_input.getText().toString();
        if( username.isEmpty() || username.length() < 3){
            username_input.setError("Username cannot be less than 3 letters!");
            setInprogress(false);
            return;
        }
        if (userModel != null){
            //user exist garxa vane setter matra chalauxa
            userModel.setName(username);
        } else{
            //user exist gardaina vane constructor le instantiate garxa
            userModel = new UserModel(username, phone_number, Timestamp.now(), FirebaseUtils.getCurrentUserID());
        }

        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginName.this, MainActivity.class);
                    //flags combine garda | (bitwise OR )chalune rahexa
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }


    void setInprogress(boolean state){
        if(state){
            username_progressBar.setVisibility(View.VISIBLE);
            username_enter_button.setVisibility(View.GONE);
        } else{
            username_progressBar.setVisibility(View.GONE);
            username_enter_button.setVisibility(View.VISIBLE);
        }
    }
}