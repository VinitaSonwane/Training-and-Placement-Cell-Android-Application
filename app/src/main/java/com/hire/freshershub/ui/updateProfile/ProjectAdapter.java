package com.hire.freshershub.ui.updateProfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{
    Context context ;
    ArrayList<ProjectItem> projectList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;

    static class ProjectViewHolder extends RecyclerView.ViewHolder{
        TextView projectTitle;
        TextView projectDomain;
        TextView projectDescription;
        EditText editProjectTitle;
        EditText editProjectDomain;
        EditText editProjectDescription;
        Button editCancelBtn;
        Button editSaveBtn;
        ImageView editProjectBtn;
        LinearLayout projectLayout;
        LinearLayout editProjectLayout;
        ProjectViewHolder(View view)
        {
            super(view);
            projectTitle= view.findViewById(R.id.project_title);
            projectDomain= view.findViewById(R.id.project_domain);
            projectDescription= view.findViewById(R.id.project_description);
            editProjectTitle= view.findViewById(R.id.edit_project_title);
            editProjectDomain= view.findViewById(R.id.edit_project_domain);
            editProjectDescription= view.findViewById(R.id.edit_project_description);
            editCancelBtn= view.findViewById(R.id.edit_project_cancel_button);
            editSaveBtn= view.findViewById(R.id.edit_project_save_button);
            editProjectBtn= view.findViewById(R.id.edit_project_detail);
            projectLayout= view.findViewById(R.id.project_layout);
            editProjectLayout= view.findViewById(R.id.edit_project_layout);
        }
    }
    public ProjectAdapter(@NonNull Context context,ArrayList<ProjectItem> projectList)
    {
        this.context = context;
        this.projectList = projectList;
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfileDatabaseReference = mFirebaseDatabase.getReference().child("users");
    }
    @NonNull
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View projectView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item,parent,false);
        return new ProjectViewHolder(projectView);
    }
    public void onBindViewHolder(ProjectViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        ProjectItem projectItem =  projectList.get(position);
        holder.projectTitle.setText(projectItem.getProjectTitle());
        holder.projectDomain.setText(projectItem.getProjectDomain());
        holder.projectDescription.setText(projectItem.getProjectDescription());

        if(projectItem.getProjectTitle().length()==0 && projectItem.getProjectDomain().length() == 0 && projectItem.getProjectDescription().length()==0 ){
            holder.editProjectTitle.setText(holder.projectTitle.getText().toString());
            holder.editProjectDescription.setText(holder.projectDescription.getText().toString());
            holder.editProjectDomain.setText(holder.projectDomain.getText().toString());
            holder.projectLayout.setVisibility(View.GONE);
            holder.editProjectLayout.setVisibility(View.VISIBLE);
        }

        holder.editProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editProjectTitle.setText(holder.projectTitle.getText().toString());
                holder.editProjectDescription.setText(holder.projectDescription.getText().toString());
                holder.editProjectDomain.setText(holder.projectDomain.getText().toString());
                holder.projectLayout.setVisibility(View.GONE);
                holder.editProjectLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.editCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.editProjectTitle.length()==0 ||
                        holder.editProjectDescription.length()==0
                        || holder.editProjectDomain.length()==0)
                {
                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("projectDetails").child(String.valueOf(position)).removeValue();
                    projectList.remove(position);
                    notifyItemRemoved(position);
                }
                holder.editProjectTitle.setText("");
                holder.editProjectDescription.setText("");
                holder.editProjectDomain.setText("");
                holder.projectLayout.setVisibility(View.VISIBLE);
                holder.editProjectLayout.setVisibility(View.GONE);
            }
        });

        holder.editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.editProjectTitle.length()<1 ||
                        holder.editProjectDescription.length()<1
                        || holder.editProjectDomain.length()<1)
                {
                    Toast.makeText(context,"Enter All Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    String title = holder.editProjectTitle.getText().toString();
                    String domain = holder.editProjectDomain.getText().toString();
                    String description = holder.editProjectDescription.getText().toString();

                    ProjectItem currProjectItem = new ProjectItem(title, domain, description);

                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("projectDetails").child(String.valueOf(position)).setValue(currProjectItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Your data has been submitted successfully", Toast.LENGTH_SHORT).show();
                                    projectList.remove(position);
                                    projectList.add(currProjectItem);
                                    notifyItemChanged(position);
                                    holder.projectLayout.setVisibility(View.VISIBLE);
                                    holder.editProjectLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(holder.editProjectLayout.getWindowToken(), 0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Problem while uploading the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    public int getItemCount()
    {
        return projectList.size();
    }

}
