package com.example.hermes.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.hermes.model.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username", model.getName());
        intent.putExtra("phone", model.getPhonenumber());
        intent.putExtra("userId", model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setName(intent.getStringExtra("username"));
        userModel.setPhonenumber(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }


}





