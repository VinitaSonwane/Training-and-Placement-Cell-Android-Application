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

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.InternshipViewHolder>{
    Context context ;
    ArrayList<InternshipItem> internshipList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;

    static class InternshipViewHolder extends RecyclerView.ViewHolder{
        TextView posName;
        TextView mode;
        TextView orgName;
        TextView startMonth;
        TextView startYear;
        TextView endMonth;
        TextView endYear;
        TextView description;
        EditText editPosName;
        EditText editMode;
        EditText editOrgName;
        EditText editStartMonth;
        EditText editStartYear;
        EditText editEndMonth;
        EditText editEndYear;
        EditText editDescription;
        LinearLayout internshipLayout;
        LinearLayout editInternshipLayout;
        ImageView editInternshipBtn;
        Button editCancelBtn;
        Button editSaveBtn;
        InternshipViewHolder(View view)
        {
            super(view);
            posName = view.findViewById(R.id.internship_pos);
            mode = view.findViewById(R.id.internship_mode);
            orgName = view.findViewById(R.id.internship_org_name);
            startMonth = view.findViewById(R.id.internship_start_month);
            startYear = view.findViewById(R.id.internship_start_year);
            endMonth = view.findViewById(R.id.internship_end_month);
            endYear = view.findViewById(R.id.internship_end_year);
            description = view.findViewById(R.id.internship_description);
            editPosName = view.findViewById(R.id.edit_internship_pos);
            editOrgName = view.findViewById(R.id.edit_internship_org_name);
            editMode = view.findViewById(R.id.edit_internship_mode);
            editStartMonth = view.findViewById(R.id.edit_start_month);
            editStartYear = view.findViewById(R.id.edit_start_year);
            editEndMonth = view.findViewById(R.id.edit_end_month);
            editEndYear = view.findViewById(R.id.edit_end_year);
            editDescription = view.findViewById(R.id.edit_internship_description);
            editInternshipBtn = (ImageView) view.findViewById(R.id.edit_internship_details);
            editCancelBtn = view.findViewById(R.id.edit_internship_cancel_button);
            editSaveBtn = view.findViewById(R.id.edit_internship_save_button);
            internshipLayout = view.findViewById(R.id.internship_layout);
            editInternshipLayout = view.findViewById(R.id.edit_internship_layout);
        }
    }
    public InternshipAdapter(@NonNull Context context,ArrayList<InternshipItem> internshipList)
    {
        this.context = context;
        this.internshipList = internshipList;
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfileDatabaseReference = mFirebaseDatabase.getReference().child("users");
    }

    @NonNull
    public InternshipViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View internView = LayoutInflater.from(parent.getContext()).inflate(R.layout.internship_item,parent,false);
        return new InternshipViewHolder(internView);
    }
    public void onBindViewHolder(InternshipViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        InternshipItem internshipItem =  internshipList.get(position);
        holder.posName.setText(internshipItem.getInternshipPos());
        holder.mode.setText(internshipItem.getInternshipMode());
        holder.orgName.setText(internshipItem.getInternshipOrgName());
        holder.startYear.setText(internshipItem.getInternshipStartYear());
        holder.startMonth.setText(internshipItem.getInternshipStartMonth());
        holder.endMonth.setText(internshipItem.getInternshipEndMonth());
        holder.endYear.setText(internshipItem.getInternshipEndYear());
        holder.description.setText(internshipItem.getInternshipDescription());

        if(internshipItem.getInternshipPos().length()==0 &&
                internshipItem.getInternshipMode().length()==0 &&
                internshipItem.getInternshipOrgName().length()==0 &&
                internshipItem.getInternshipStartMonth().length()==0 &&
                internshipItem.getInternshipStartYear().length()==0 &&
                internshipItem.getInternshipEndMonth().length()==0 &&
                internshipItem.getInternshipStartYear().length()==0 &&
                internshipItem.getInternshipDescription().length()==0  )
        {
            holder.editPosName.setText(holder.posName.getText().toString());
            holder.editOrgName.setText(holder.orgName.getText().toString());
            holder.editDescription.setText(holder.description.getText().toString());
            holder.editMode.setText(holder.mode.getText().toString());
            holder.editStartYear.setText(holder.startYear.getText().toString());
            holder.editStartMonth.setText(holder.startMonth.getText().toString());
            holder.editEndMonth.setText(holder.endMonth.getText().toString());
            holder.editEndYear.setText(holder.endYear.getText().toString());
            holder.internshipLayout.setVisibility(View.GONE);
            holder.editInternshipLayout.setVisibility(View.VISIBLE);
        }
        holder.editInternshipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editPosName.setText(holder.posName.getText().toString());
                holder.editOrgName.setText(holder.orgName.getText().toString());
                holder.editDescription.setText(holder.description.getText().toString());
                holder.editMode.setText(holder.mode.getText().toString());
                holder.editStartYear.setText(holder.startYear.getText().toString());
                holder.editStartMonth.setText(holder.startMonth.getText().toString());
                holder.editEndMonth.setText(holder.endMonth.getText().toString());
                holder.editEndYear.setText(holder.endYear.getText().toString());
                holder.internshipLayout.setVisibility(View.GONE);
                holder.editInternshipLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.editCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.editPosName.length()==0 ||
                        holder.editOrgName.length()==0 ||
                        holder.editDescription.length()==0 ||
                        holder.editMode.length()==0 ||
                        holder.editStartYear.length()==0 ||
                        holder.editStartMonth.length()==0 ||
                        holder.editEndMonth.length()==0 ||
                        holder.editEndYear.length()==0)
                {
                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("internshipDetails").child(String.valueOf(position)).removeValue();
                    internshipList.remove(position);
                    notifyItemRemoved(position);
                }

                holder.editPosName.setText("");
                holder.editOrgName.setText("");
                holder.editDescription.setText("");
                holder.editMode.setText("");
                holder.editStartYear.setText("");
                holder.editStartMonth.setText("");
                holder.editEndMonth.setText("");
                holder.editEndYear.setText("");
                holder.internshipLayout.setVisibility(View.VISIBLE);
                holder.editInternshipLayout.setVisibility(View.GONE);
            }
        });

        holder.editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(     holder.editPosName.length()<1 ||
                        holder.editOrgName.length()<1 ||
                        holder.editMode.length()<1 ||
                        holder.editStartMonth.length()<1 ||
                        holder.editStartYear.length()<1 ||
                        holder.editEndMonth.length()<1 ||
                        holder.editEndYear.length()<1 ||
                        holder.editDescription.length()<1
                )
                {
                    Toast.makeText(context,"Please enter all fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    String internshipPos = holder.editPosName.getText().toString();
                    String internshipMode = holder.editMode.getText().toString();
                    String internshipOrgName = holder.editOrgName.getText().toString();
                    String internshipStartMonth = holder.editStartMonth.getText().toString();
                    String internshipStartYear = holder.editStartYear.getText().toString();
                    String internshipEndMonth = holder.editEndMonth.getText().toString();
                    String internshipEndYear = holder.editEndYear.getText().toString();
                    String internshipDescription = holder.editDescription.getText().toString();

                    InternshipItem currInternshipItem = new InternshipItem(internshipPos, internshipMode, internshipOrgName, internshipStartMonth, internshipStartYear, internshipEndMonth, internshipEndYear, internshipDescription);

                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("internshipDetails").child(String.valueOf(position)).setValue(currInternshipItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Your data has been submitted successfully", Toast.LENGTH_SHORT).show();
                                    internshipList.remove(position);
                                    internshipList.add(currInternshipItem);
                                    notifyItemChanged(position);
                                    holder.internshipLayout.setVisibility(View.VISIBLE);
                                    holder.editInternshipLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(holder.editInternshipLayout.getWindowToken(), 0);
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
        return internshipList.size();

    }

}