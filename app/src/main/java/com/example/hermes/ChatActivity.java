package com.example.hermes;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hermes.adapter.ChatRecyclerAdapter;
import com.example.hermes.adapter.SearchUserRecyclerAdapter;
import com.example.hermes.model.ChatMessageModel;
import com.example.hermes.model.ChatroomModel;
import com.example.hermes.model.UserModel;
import com.example.hermes.utils.AndroidUtils;
import com.example.hermes.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otheruser;
    EditText messageInput;
    ImageButton backButton;
    ImageButton sendMessageButton;
    TextView otherUsername;
    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;

    String chatroomId;
    ChatroomModel chatroomModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otheruser = AndroidUtils.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtils.getChatroomId(FirebaseUtils.getCurrentUserID(), otheruser.getUserId());
        messageInput = findViewById(R.id.chat_message_input);
        backButton = findViewById(R.id.chat_backbutton);
        sendMessageButton = findViewById(R.id.chat_send_button);
        otherUsername = findViewById(R.id.chat_otheruser);
        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener(v->{
            finish();
        });

        otherUsername.setText(otheruser.getName());
        getOrCreateChatroom();

        sendMessageButton.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessage(message);
        }));

        setupChatRecyclerview();
    }

    void setupChatRecyclerview(){
        Query query = FirebaseUtils.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);



        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();


        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void sendMessage(String message){
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtils.getCurrentUserID());
        FirebaseUtils.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.getCurrentUserID(), Timestamp.now());
        FirebaseUtils.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");


                        }
                    }
                });
    }

    void getOrCreateChatroom(){
        FirebaseUtils.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               chatroomModel = task.getResult().toObject(ChatroomModel.class);
               if(chatroomModel == null){

                   chatroomModel = new ChatroomModel(chatroomId,
                           Arrays.asList(FirebaseUtils.getCurrentUserID(), otheruser.getUserId()),
                           Timestamp.now(),
                           "");

                   FirebaseUtils.getChatroomReference(chatroomId).set(chatroomModel);
               }
           }
        });
    }
}