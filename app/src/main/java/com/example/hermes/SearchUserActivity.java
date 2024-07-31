//mine
package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hermes.model.UserModel;
import com.example.hermes.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.Query;
import com.example.hermes.adapter.SearchUserRecyclerAdapter;

public class SearchUserActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageButton searchButton;
    EditText searchUsers;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        backButton = findViewById(R.id.backbutton);
        searchUsers = findViewById(R.id.search_user_input);
        searchButton = findViewById(R.id.search_button_icon);
        recyclerView = findViewById(R.id.search_user_recycler_view);
        searchButton.requestFocus();
        backButton.setOnClickListener(v -> {
            finish();
        });

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchUsers.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length() < 3){
                searchUsers.setError("Invalid Username");
                return;
            }
            setupSearchRecyclerView(searchTerm);
        });


    }

    void setupSearchRecyclerView(String searchTerm) {
        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("name", searchTerm)
                .whereLessThanOrEqualTo("name",searchTerm+'\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();


        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}





