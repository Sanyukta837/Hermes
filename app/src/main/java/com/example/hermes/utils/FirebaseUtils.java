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

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String user1Id, String user2Id){
        if(user1Id.hashCode() < user2Id.hashCode()){
            return user1Id + "_" + user2Id;
        } else {
            return user2Id + "_" + user1Id;
        }
    }

}
