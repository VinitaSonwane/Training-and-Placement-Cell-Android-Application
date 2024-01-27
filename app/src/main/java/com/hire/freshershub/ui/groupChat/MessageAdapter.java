package com.hire.freshershub.ui.groupChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hire.freshershub.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    ArrayList<Message> messageArrayList;
    Context context;

    static class MessageViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImageView;
        TextView messageTextView;
        TextView authorTextView;

        MessageViewHolder(View view)
        {
            super(view);
            photoImageView = view.findViewById(R.id.photoImageView);
            messageTextView = view.findViewById(R.id.messageTextView);
            authorTextView = view.findViewById(R.id.nameTextView);
        }
    }

    public MessageAdapter(@NonNull Context context, ArrayList<Message> messageArrayList)
    {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    public MessageViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View MessageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new MessageViewHolder(MessageView);
    }

    public void onBindViewHolder(@NonNull MessageViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        Message message = messageArrayList.get(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            holder.messageTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(holder.photoImageView);
            holder.photoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent groupChatPhotoIntent = new Intent(context, GroupChatPhoto.class);
                    groupChatPhotoIntent.putExtra("authorName", message.getName());
                    groupChatPhotoIntent.putExtra("imageUrl", message.getPhotoUrl());
                    context.startActivity(groupChatPhotoIntent);
                }
            });
        } else {
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.messageTextView.setText(message.getText());
        }

        holder.authorTextView.setText(message.getName());


    }
    public int getItemCount()
    {
        return messageArrayList.size();

    }

}