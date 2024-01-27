package com.hire.freshershub.ui.jobsHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CollegeJobs extends Fragment implements JobAdapter.customButtonListener{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mJobsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;

    JobAdapter jobAdapter;
    ArrayList<Job> jobs;
    RecyclerView jobsRecyclerView;
    ImageView jobItemBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jobs_list, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mJobsDatabaseReference = mFirebaseDatabase.getReference().child("jobs");
        mFirebaseAuth = FirebaseAuth.getInstance();

        jobs = new ArrayList<>();

        jobsRecyclerView = rootView.findViewById(R.id.jobs_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        jobsRecyclerView.setLayoutManager(linearLayoutManager);
        jobAdapter = new JobAdapter(getContext(), jobs);
        jobAdapter.setCustomButtonListener(this);
        jobsRecyclerView.setAdapter(jobAdapter);

        getJobData();

        return rootView;
    }

    @Override
    public void onShowMoreButtonClickListener(int position) {
//        Toast.makeText(getContext(), "Position "+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), JobDetails.class);
        intent.putExtra("type", "college");
        intent.putExtra("id", jobs.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onSetBookmarkClickListener(int position) {

    }

    private void getJobData(){
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String position = snapshot.child("position").getValue(String.class);
                    int id = snapshot.child("id").getValue(int.class);
                    String companyName = snapshot.child("companyName").getValue(String.class);
                    String url = snapshot.child("url").getValue(String.class);
                    Job job = new Job(id, position, companyName, url);
                    jobs.add(0, job);
                    jobAdapter.notifyItemInserted(0);
//                    jobs.add(job);
//                    jobAdapter.notifyItemInserted(jobs.size()-1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            };
            mJobsDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    private void detachJobsDatabaseReadListener(){
        if(mChildEventListener != null){
            mJobsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachJobsDatabaseReadListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(jobs.isEmpty()){
            getJobData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
