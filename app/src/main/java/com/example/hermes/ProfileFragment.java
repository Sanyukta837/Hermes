package com.example.hermes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText usernameInput;
    EditText phoneInput;
    Button updateProfileButton;
    ProgressBar progressBar;
    UserModel currentUserModel;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if( result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            selectedImageUri = data.getData();
                            AndroidUtils.setProfilePic(getContext(), selectedImageUri, profilePic);
                        }
                    }
                }
                );
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

        profilePic.setOnClickListener( (v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
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

        if(selectedImageUri != null){
            FirebaseUtils.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirebase();
                    });
        }else{
            updateToFirebase();
        }


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
        FirebaseUtils.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                           if(task.isSuccessful()){
                               Uri uri = task.getResult();
                               AndroidUtils.setProfilePic(getContext(),uri,profilePic);
                           }
                        });
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