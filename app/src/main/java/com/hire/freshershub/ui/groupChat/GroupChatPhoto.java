package com.hire.freshershub.ui.groupChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hire.freshershub.R;

import java.util.Objects;

public class GroupChatPhoto extends AppCompatActivity {

    String authorName;
    String imageUrl;
    ImageView groupChatPhotoFullScreen;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_photo);
        groupChatPhotoFullScreen = findViewById(R.id.group_chat_photo_full_screen);

        Intent intent = this.getIntent();
        if(intent!=null){
            authorName = intent.getStringExtra("authorName");
            imageUrl = intent.getStringExtra("imageUrl");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(authorName);

        Glide.with(this)
                .load(imageUrl)
                .into(groupChatPhotoFullScreen);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}