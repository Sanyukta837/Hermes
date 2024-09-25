package com.example.hermes.adapter;
import com.example.hermes.ChatActivity;
import com.example.hermes.R;
import com.example.hermes.model.ChatroomModel;
import com.example.hermes.model.UserModel;
import com.example.hermes.utils.AndroidUtils;
import com.example.hermes.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentChatsRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatsRecyclerAdapter.ChatroomModelViewHolder> {
    Context context;

    public RecentChatsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        FirebaseUtils.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean wasLastMessageSentByMe;
                        wasLastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtils.getCurrentUserID());
                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                        FirebaseUtils.getOtherProfilePicStorageRef(otherUserModel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(pic_task -> {
                                    if(pic_task.isSuccessful()){
                                        Uri uri = pic_task.getResult();
                                        AndroidUtils.setProfilePic(context,uri,holder.profilePicture);
                                    }
                                });

                        holder.usernameText.setText(otherUserModel.getName());
                        if(wasLastMessageSentByMe){
                            holder.lastMessageText.setText("You: " + model.getLastMessage());
                        }else{
                            holder.lastMessageText.setText(model.getLastMessage());
                        }

                        holder.lastMessageTime.setText(FirebaseUtils.timeStampToString(model.getLastMessageTimestamp()));

                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ChatActivity.class);
                            AndroidUtils.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });

                    }


                });
    }

    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterLog", "Creating new view holder");
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);

        return new ChatroomModelViewHolder(view);
    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePicture;


        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_recent_rr);
            lastMessageText = itemView.findViewById(R.id.last_message_recent_rr);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_recent_rr);
            profilePicture = itemView.findViewById(R.id.profile_picture_image_view);

        }
    }
}












