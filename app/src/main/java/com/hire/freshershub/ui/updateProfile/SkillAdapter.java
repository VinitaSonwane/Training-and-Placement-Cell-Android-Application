package com.hire.freshershub.ui.updateProfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {
    Context context;
    ArrayList<String> itemList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;

    static class SkillViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        ImageView deleteImageView;
        SkillViewHolder(View view){
            super(view);
            nameTextView = view.findViewById(R.id.list_item_name);
            deleteImageView = view.findViewById(R.id.list_item_delete);
        }
    }

    public SkillAdapter(@NonNull Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfileDatabaseReference = mFirebaseDatabase.getReference().child("users");
    }

    @NonNull
    public SkillViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_item, parent, false);
        return new SkillViewHolder(itemView);
    }

    public void onBindViewHolder(SkillViewHolder holder, @SuppressLint("RecyclerView") int position){
        String item = itemList.get(position);
        holder.nameTextView.setText(item);

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String currSkill = itemList.get(position);
                mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("skills").child(currSkill).removeValue();
                itemList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public int getItemCount(){
        return itemList.size();
    }

}
