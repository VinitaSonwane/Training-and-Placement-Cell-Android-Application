package com.hire.freshershub.ui.jobsHome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hire.freshershub.MainActivity;
import com.hire.freshershub.R;
import com.hire.freshershub.ui.groupChat.FcmNotificationsSender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddJob extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mJobsDatabaseReference;

    EditText nameEditText;
    EditText positionEditText;
    EditText experienceEditText;
    EditText openingsEditText;
    EditText locationEditText;
    EditText salaryEditText;
    EditText skillsEditText;
    EditText urlEditText;
    EditText jobDescriptionEditText;
    Button cancelButton;
    Button submitButton;

    int totalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mJobsDatabaseReference = mFirebaseDatabase.getReference().child("jobs");

        nameEditText = findViewById(R.id.add_job_name);
        positionEditText = findViewById(R.id.add_job_position);
        experienceEditText = findViewById(R.id.add_job_experience);
        openingsEditText = findViewById(R.id.add_job_openings);
        locationEditText = findViewById(R.id.add_job_location);
        salaryEditText = findViewById(R.id.add_job_salary);
        skillsEditText = findViewById(R.id.add_job_skills);
        urlEditText = findViewById(R.id.add_job_url);
        jobDescriptionEditText = findViewById(R.id.add_job_description);
        cancelButton = findViewById(R.id.add_job_cancel_button);
        submitButton = findViewById(R.id.add_job_submit_button);

        setRequired(nameEditText);
        setRequired(positionEditText);
        setRequired(jobDescriptionEditText);

        mJobsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalSize = (int) dataSnapshot.getChildrenCount();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateJob()){
                    int id = totalSize+1;
                    String position = positionEditText.getText().toString();
                    String companyName = nameEditText.getText().toString();
                    String experience = experienceEditText.getText().toString();
                    if(experience.length()==0){
                        experience = "Not Specified";
                    }

                    String openings = openingsEditText.getText().toString();
                    if(openings.length()==0){
                        openings = "Not Specified";
                    }

                    String location = locationEditText.getText().toString();
                    if(location.length()==0){
                        location = "Not Specified";
                    }

                    String salary = salaryEditText.getText().toString();
                    if(salary.length()==0){
                        salary = "Not Specified";
                    }

                    String skills = skillsEditText.getText().toString();
                    if(skills.length()==0){
                        skills = "Not Specified";
                    }

                    String url = urlEditText.getText().toString();
                    if(url.length()==0){
                        url = "Not Specified";
                    }

                    String jobDescription = jobDescriptionEditText.getText().toString();

                    Job job = new Job(id, position, companyName, experience, openings, location, salary, skills, url, jobDescription);
                    mJobsDatabaseReference.child(String.valueOf(id)).setValue(job);
                    Intent intent = new Intent(AddJob.this, MainActivity.class);
                    startActivity(intent);
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "Job Alert", companyName+" is Hiring for "+position, "new_job", getApplicationContext(), AddJob.this);
                    notificationsSender.SendNotifications();
                }
            }
        });
    }

    private boolean validateJob(){
        String companyName = nameEditText.getText().toString();
        if(companyName.length()==0){
            nameEditText.setError("Company Name should not be empty");
            return false;
        }

        String position = positionEditText.getText().toString();
        if(position.length()==0){
            positionEditText.setError("Position should not be empty");
            return false;
        }

        String jobDescription = jobDescriptionEditText.getText().toString();
        if(jobDescription.length()==0){
            jobDescriptionEditText.setError("Job Description should not be empty");
            return false;
        }

        return true;
    }

    private void setRequired(EditText editText){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(editText.getHint());
        spannableStringBuilder.append(" ");
        spannableStringBuilder.append("*");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spannableStringBuilder.length()-1, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(spannableStringBuilder);
    }

}