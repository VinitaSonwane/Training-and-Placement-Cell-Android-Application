package com.hire.freshershub.ui.jobsHome;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hire.freshershub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class JobDetails extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mJobsDatabaseReference;

    TextView jobDetailsName;
    TextView jobDetailsPosition;
    TextView jobDetailsExperience;
    TextView jobDetailsOpenings;
    TextView jobDetailsLocation;
    TextView jobDetailsSalary;
    TextView jobDetailsSkills;
    TextView jobDetailsDescription;
    Button jobDetailsApplyButton;
    Button jobDetailsCancelButton;

    String type;
    int id;
    String redirectUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mJobsDatabaseReference = mFirebaseDatabase.getReference().child("jobs");

        jobDetailsName = findViewById(R.id.job_details_name);
        jobDetailsPosition = findViewById(R.id.job_details_position);
        jobDetailsExperience = findViewById(R.id.job_details_experience);
        jobDetailsOpenings = findViewById(R.id.job_details_openings);
        jobDetailsLocation = findViewById(R.id.job_details_location);
        jobDetailsSalary = findViewById(R.id.job_details_salary);
        jobDetailsSkills = findViewById(R.id.job_details_skills);
        jobDetailsDescription = findViewById(R.id.job_details_description);
        jobDetailsApplyButton = findViewById(R.id.job_details_apply_button);
        jobDetailsCancelButton = findViewById(R.id.job_details_cancel_button);

        Intent intent = this.getIntent();
        if(intent!=null){
            type = intent.getStringExtra("type");
            if(type.equals("college")){
                id = intent.getIntExtra("id", 0);
                getJobDetailsFromDatabase();
            }else{
                jobDetailsName.setText(intent.getStringExtra("name"));
                jobDetailsPosition.setText(intent.getStringExtra("position"));
                jobDetailsExperience.setText("Not Specified");
                jobDetailsOpenings.setText("Not Specified");
                jobDetailsLocation.setText(intent.getStringExtra("location"));
                jobDetailsSalary.setText(intent.getStringExtra("salary"));
                jobDetailsSkills.setText("Not Specified");
                jobDetailsDescription.setText(intent.getStringExtra("description"));
                redirectUrl = intent.getStringExtra("redirect_url");
            }
        }

        jobDetailsApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(redirectUrl==null){
                    if(type.equals("college")){
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
                                            Toast.makeText(JobDetails.this, "Application submitted Successfully!!!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(JobDetails.this, "Please Complete the profile First", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }
                }else{
                    if(redirectUrl.equals("Not Specified")){
                        if(type.equals("college")){
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
                                                Toast.makeText(JobDetails.this, "Application submitted Successfully!!!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(JobDetails.this, "Please Complete the profile First", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });
                        }
                    }else{
                        Uri uri = Uri.parse(redirectUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            }
        });

        jobDetailsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getJobDetailsFromDatabase(){
        mJobsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    if(Objects.equals(postSnapshot.getKey(), String.valueOf(id))){
                        Job job = postSnapshot.getValue(Job.class);
                        jobDetailsName.setText(job.getCompanyName());
                        jobDetailsPosition.setText(job.getPosition());
                        jobDetailsExperience.setText(job.getExperience());
                        jobDetailsOpenings.setText(String.valueOf(job.getOpenings()));
                        jobDetailsLocation.setText(job.getLocation());
                        jobDetailsSalary.setText(job.getSalary());
                        jobDetailsSkills.setText(job.getSkills());
                        redirectUrl = job.getUrl();
                        jobDetailsDescription.setText(job.getJobDescription());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}