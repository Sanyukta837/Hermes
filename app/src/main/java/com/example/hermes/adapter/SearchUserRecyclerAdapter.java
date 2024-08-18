package com.example.hermes.adapter;
import com.example.hermes.ChatActivity;
import com.example.hermes.R;
import com.example.hermes.model.UserModel;
import com.example.hermes.utils.AndroidUtils;
import com.example.hermes.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {
    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        Log.d("AdapterLog", "Binding view holder at position: " + position);
        holder.usernameText.setText(model.getName());
        holder.phoneText.setText(model.getPhonenumber());  
        if(model.getUserId().equals(FirebaseUtils.getCurrentUserID())){
            holder.usernameText.setText(model.getName() + " (Me)");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtils.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterLog", "Creating new view holder");
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);

        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView phoneText;
        ImageView profilePicture;


        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text_rr);
            phoneText = itemView.findViewById(R.id.phone_number_text_rr);
            profilePicture = itemView.findViewById(R.id.profile_picture_image_view);

        }
    }
}












