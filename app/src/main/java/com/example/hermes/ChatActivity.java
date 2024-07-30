package com.example.hermes;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hermes.model.UserModel;
import com.example.hermes.utils.AndroidUtils;

public class ChatActivity extends AppCompatActivity {
    UserModel otheruser;
    EditText messageInput;
    ImageButton backButton;
    ImageButton sendMessageButton;
    TextView otherUsername;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otheruser = AndroidUtils.getUserModelFromIntent(getIntent());
        messageInput = findViewById(R.id.chat_message_input);
        backButton = findViewById(R.id.chat_backbutton);
        sendMessageButton = findViewById(R.id.chat_send_button);
        otherUsername = findViewById(R.id.chat_otheruser);
        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener(v->{
            finish();
        });

        otherUsername.setText(otheruser.getName());

    }
}