package com.hire.freshershub.ui.jobsHome;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    Context context;
    ArrayList<Job> jobArrayList;
    private customButtonListener customListener;

    public interface customButtonListener {
        void onShowMoreButtonClickListener(int position);
        void onSetBookmarkClickListener(int position);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView;
        TextView companyNameTextView;
        Button jobViewDetails;
        Button jobApplyNow;
        ImageView jobBookmark;

        JobViewHolder(View view) {
            super(view);
            positionTextView = view.findViewById(R.id.job_item_position);
            companyNameTextView = view.findViewById(R.id.job_item_company_name);
            jobViewDetails = view.findViewById(R.id.job_view_details);
            jobApplyNow = view.findViewById(R.id.job_apply_now);
            jobBookmark = view.findViewById(R.id.job_item_bookmark);
        }
    }

    public JobAdapter(Context context, ArrayList<Job> jobArrayList) {
        this.context = context;
        this.jobArrayList = jobArrayList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Job job = jobArrayList.get(position);
        holder.positionTextView.setText(job.getPosition());
        holder.companyNameTextView.setText(job.getCompanyName());

        holder.jobViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customListener != null){
                    customListener.onShowMoreButtonClickListener(position);
                }
            }
        });

        holder.jobApplyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(job.getUrl()==null){
                    DatabaseReference mProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    mProfileDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isPrimary = false;
                            boolean isContact = false;
                            boolean isResume = false;
                            for(DataSnapshot innerDataSnapshot: snapshot.getChildren()){
                                if(Objects.equals(innerDataSnapshot.getKey(), "canApplyJobs")){
                                    if(Boolean.TRUE.equals(innerDataSnapshot.getValue(Boolean.class))){
                                        Toast.makeText(context, "Application submitted Successfully!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    if(Objects.equals(innerDataSnapshot.getKey(), "primaryDetails")){
                                        isPrimary = true;
                                    }else if(Objects.equals(innerDataSnapshot.getKey(), "contactDetails")){
                                        isContact = true;
                                    }else if(Objects.equals(innerDataSnapshot.getKey(), "isResumeAdded")){
                                        isResume = true;
                                    }
                                }
                            }
                            if(isPrimary&&isContact&&isResume){
                                mProfileDatabaseReference.child("canApplyJobs").setValue(true);
                            }else{
                                Toast.makeText(context, "Please Complete the profile First", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }else{
                    if(job.getUrl().equals("Not Specified")){
                        DatabaseReference mProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        mProfileDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean isPrimary = false;
                                boolean isContact = false;
                                boolean isResume = false;
                                for(DataSnapshot innerDataSnapshot: snapshot.getChildren()){
                                    if(Objects.equals(innerDataSnapshot.getKey(), "canApplyJobs")){
                                        if(Boolean.TRUE.equals(innerDataSnapshot.getValue(Boolean.class))){
                                            Toast.makeText(context, "Application submitted Successfully!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        if(Objects.equals(innerDataSnapshot.getKey(), "primaryDetails")){
                                            isPrimary = true;
                                        }else if(Objects.equals(innerDataSnapshot.getKey(), "contactDetails")){
                                            isContact = true;
                                        }else if(Objects.equals(innerDataSnapshot.getKey(), "isResumeAdded")){
                                            isResume = true;
                                        }
                                    }
                                }
                                if(isPrimary&&isContact&&isResume){
                                    mProfileDatabaseReference.child("canApplyJobs").setValue(true);
                                }else{
                                    Toast.makeText(context, "Please Complete the profile First", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }else{
                        Uri uri = Uri.parse(job.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                }
            }
        });

        holder.jobBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "This functionality is not available for now", Toast.LENGTH_SHORT).show();
            }
        });

//        if(job.getBookmark()){
//            holder.jobBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
//        }else{
//            holder.jobBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
//        }

//        holder.jobBookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(customListener != null){
//                    customListener.onSetBookmarkClickListener(position);
//                }
//                if(job.getBookmark()){
//                    holder.jobBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
//                }else{
//                    holder.jobBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
//                }
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }
}
