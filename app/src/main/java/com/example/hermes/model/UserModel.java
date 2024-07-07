package com.example.hermes.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String name;
    private String phonenumber;
    private Timestamp timestamp;

    public UserModel() {
    }

    public UserModel(String name, String phonenumber, Timestamp timestamp) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
