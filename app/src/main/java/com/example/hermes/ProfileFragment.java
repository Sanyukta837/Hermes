package com.example.hermes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hermes.model.UserModel;
import com.example.hermes.utils.AndroidUtils;
import com.example.hermes.utils.FirebaseUtils;


public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText usernameInput;
    EditText phoneInput;
    Button updateProfileButton;
    ProgressBar progressBar;
    UserModel currentUserModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);
        usernameInput = view.findViewById(R.id.edit_username);
        phoneInput = view.findViewById(R.id.edit_phone);
        updateProfileButton = view.findViewById(R.id.update_profile_button);
        progressBar = view.findViewById(R.id.update_profile_progressbar);

        getUserData();

        updateProfileButton.setOnClickListener(v -> {
            updateDetails();
        });
        return view;
    }

    void updateDetails(){

        String newUsername = usernameInput.getText().toString();
        if( newUsername.isEmpty() || newUsername.length() < 3){
            usernameInput.setError("Username cannot be less than 3 letters!");
            return;
        }
        currentUserModel.setName(newUsername);
        setInprogress(true);
        updateToFirebase();
    }

    void updateToFirebase(){
        FirebaseUtils.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                   setInprogress(false);
                   if(task.isSuccessful()){
                       AndroidUtils.showToast(getContext(),"Updated Successfully");
                   }else{
                       AndroidUtils.showToast(getContext(),"Update Failed");
                   }
                });
    }


    void getUserData(){
        setInprogress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
        setInprogress(false);
        currentUserModel = task.getResult().toObject(UserModel.class);
        usernameInput.setText(currentUserModel.getName());
        phoneInput.setText(currentUserModel.getPhonenumber());
        });
    }

    void setInprogress(boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileButton.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            updateProfileButton.setVisibility(View.VISIBLE);
        }
    }

}