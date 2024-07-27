package com.example.hermes.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {

    //store vayeko userID line
    public static String getCurrentUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    //tei uid ko through bata
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(getCurrentUserID());
    }

    public static boolean isLoggedIn(){
        String uid = getCurrentUserID();
        if( uid != null) {
            return true;
        } else {
            return false;
        }
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

}
