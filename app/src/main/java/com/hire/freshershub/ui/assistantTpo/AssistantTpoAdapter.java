package com.hire.freshershub.ui.assistantTpo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hire.freshershub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hire.freshershub.ui.updateProfile.ImageProvider;

import java.util.ArrayList;

public class AssistantTpoAdapter extends RecyclerView.Adapter<AssistantTpoAdapter.AssistantTpoViewHolder>{
    static Context context ;
    ArrayList<AssistantTpoItem> assistantTpoItemList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mAssistantTpoDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;
    private StorageReference mProfilePhotoStorageReference;

    static class AssistantTpoViewHolder extends RecyclerView.ViewHolder{
        TextView assistantTpoDeptName;
        TextView assistantTpoName;
        TextView assistantTpoDesignation;
        TextView assistantTpoWorkMail;
        TextView assistantTpoPersonalMail;
        TextView assistantTpoContactNo;
        EditText editAssistantTpoDeptName;
        EditText editAssistantTpoName;
        EditText editAssistantTpoDesignation;
        EditText editAssistantTpoWorkMail;
        EditText editAssistantTpoPersonalMail;
        EditText editAssistantTpoContactNo;
        Button assistantTpoCancelButton;
        Button assistantTpoSaveButton;
        ImageView assistantTpoEditButton;
        LinearLayout assistantTpoLayout;
        LinearLayout editAssistantTpoLayout;
        ImageView assistantTpoPhoto;

        AssistantTpoViewHolder(View view)
        {
            super(view);
            assistantTpoDeptName = view.findViewById(R.id.assistant_tpo_dept_name);
            assistantTpoName= view.findViewById(R.id.assistant_tpo_name);
            assistantTpoDesignation= view.findViewById(R.id.assistant_tpo_designation);
            assistantTpoWorkMail= view.findViewById(R.id.assistant_tpo_work_mail);
            assistantTpoPersonalMail= view.findViewById(R.id.assistant_tpo_personal_mail);
            assistantTpoContactNo= view.findViewById(R.id.assistant_tpo_contact_no);
            editAssistantTpoDeptName = view.findViewById(R.id.edit_assistant_tpo_dept_name);
            editAssistantTpoName = view.findViewById(R.id.edit_assistant_tpo_name);
            editAssistantTpoDesignation = view.findViewById(R.id.edit_assistant_tpo_designation);
            editAssistantTpoWorkMail = view.findViewById(R.id.edit_assistant_tpo_work_mail);
            editAssistantTpoPersonalMail = view.findViewById(R.id.edit_assistant_tpo_personal_mail);
            editAssistantTpoContactNo = view.findViewById(R.id.edit_assistant_tpo_contact_no);
            assistantTpoCancelButton = view.findViewById(R.id.assistant_tpo_cancel_button);
            assistantTpoSaveButton = view.findViewById(R.id.assistant_tpo_save_button);
            assistantTpoEditButton = view.findViewById(R.id.assistant_tpo_edit_button);
            assistantTpoLayout = view.findViewById(R.id.assistant_tpo_layout);
            editAssistantTpoLayout = view.findViewById(R.id.edit_assistant_tpo_layout);
            assistantTpoPhoto = view.findViewById(R.id.assistant_tpo_photo);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                String [] special = context.getResources().getStringArray(R.array.special_access);
                for(int i = 0; i< special.length; i++){
                    if(user.getUid().equals(special[i])){
                        assistantTpoEditButton.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
    }

    public AssistantTpoAdapter(@NonNull Context context,ArrayList<AssistantTpoItem> assistantTpoItemList)
    {
        this.context = context;
        this.assistantTpoItemList = assistantTpoItemList;
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAssistantTpoDatabaseReference = mFirebaseDatabase.getReference().child("assistantTPOs");
        mProfilePhotoStorageReference = FirebaseStorage.getInstance().getReference().child("assistant_tpo_photos");
    }

    @NonNull
    public AssistantTpoViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View assistantTpoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assistant_tpo_item,parent,false);
        return new AssistantTpoViewHolder(assistantTpoView);
    }

    public void onBindViewHolder(AssistantTpoViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        AssistantTpoItem assistantTpoItem = assistantTpoItemList.get(position);
        holder.assistantTpoDeptName.setText(assistantTpoItem.getAssistantDeptName());
        holder.assistantTpoName.setText(assistantTpoItem.getAssistantTpoName());
        holder.assistantTpoDesignation.setText(assistantTpoItem.getAssistantTpoDesignation());
        holder.assistantTpoWorkMail.setText(assistantTpoItem.getAssistantTpoWorkMail());
        holder.assistantTpoPersonalMail.setText(assistantTpoItem.getAssistantTpoPersonalMail());
        holder.assistantTpoContactNo.setText(assistantTpoItem.getAssistantTpoContactNo());

        getProfileImage(holder, assistantTpoItem.getAssistantDeptName());

        if(assistantTpoItem.getAssistantDeptName().length()==0 && assistantTpoItem.getAssistantTpoDesignation().length() == 0 && assistantTpoItem.getAssistantTpoWorkMail().length()==0 && assistantTpoItem.getAssistantTpoPersonalMail().length()==0 && assistantTpoItem.getAssistantTpoContactNo().length()==0 ){
            holder.editAssistantTpoDeptName.setText(holder.assistantTpoDeptName.getText().toString());
            holder.editAssistantTpoName.setText(holder.assistantTpoName.getText().toString());
            holder.editAssistantTpoDesignation.setText(holder.assistantTpoDesignation.getText().toString());
            holder.editAssistantTpoWorkMail.setText(holder.assistantTpoWorkMail.getText().toString());
            holder.editAssistantTpoPersonalMail.setText(holder.assistantTpoPersonalMail.getText().toString());
            holder.editAssistantTpoContactNo.setText(holder.assistantTpoContactNo.getText().toString());
            holder.assistantTpoLayout.setVisibility(View.GONE);
            holder.editAssistantTpoLayout.setVisibility(View.VISIBLE);
        }

        holder.assistantTpoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editAssistantTpoDeptName.setText(holder.assistantTpoDeptName.getText().toString());
                holder.editAssistantTpoName.setText(holder.assistantTpoName.getText().toString());
                holder.editAssistantTpoDesignation.setText(holder.assistantTpoDesignation.getText().toString());
                holder.editAssistantTpoWorkMail.setText(holder.assistantTpoWorkMail.getText().toString());
                holder.editAssistantTpoPersonalMail.setText(holder.assistantTpoPersonalMail.getText().toString());
                holder.editAssistantTpoContactNo.setText(holder.assistantTpoContactNo.getText().toString());
                holder.assistantTpoLayout.setVisibility(View.GONE);
                holder.editAssistantTpoLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.assistantTpoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(
                        holder.editAssistantTpoDeptName.length()==0 ||
                                holder.editAssistantTpoName.length()==0 ||
                                holder.editAssistantTpoDesignation.length()==0
                                || holder.editAssistantTpoWorkMail.length()==0
                                || holder.editAssistantTpoPersonalMail.length()==0
                                || holder.editAssistantTpoContactNo.length()==0
                )
                {
                    mAssistantTpoDatabaseReference.child(String.valueOf(position)).removeValue();
                    assistantTpoItemList.remove(position);
                    notifyItemRemoved(position);
                }
                holder.editAssistantTpoDeptName.setText("");
                holder.editAssistantTpoName.setText("");
                holder.editAssistantTpoDesignation.setText("");
                holder.editAssistantTpoWorkMail.setText("");
                holder.editAssistantTpoPersonalMail.setText("");
                holder.editAssistantTpoContactNo.setText("");
                holder.assistantTpoLayout.setVisibility(View.VISIBLE);
                holder.editAssistantTpoLayout.setVisibility(View.GONE);
            }
        });

        holder.assistantTpoSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.editAssistantTpoDeptName.length()==0 ||
                        holder.editAssistantTpoName.length()==0 ||
                        holder.editAssistantTpoDesignation.length()==0
                        || holder.editAssistantTpoWorkMail.length()==0
                        || holder.editAssistantTpoPersonalMail.length()==0
                        || holder.editAssistantTpoContactNo.length()==0
                )
                {
                    Toast.makeText(context,"Enter All Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    String assistantDeptName= holder.editAssistantTpoDeptName.getText().toString();
                    String assistantTpoName = holder.editAssistantTpoName.getText().toString();
                    String assistantTpoDesignation = holder.editAssistantTpoDesignation.getText().toString();
                    String assistantTpoWorkMail = holder.editAssistantTpoWorkMail.getText().toString();
                    String assistantTpoPersonalMail = holder.editAssistantTpoPersonalMail.getText().toString();
                    String assistantTpoContactNo = holder.editAssistantTpoContactNo.getText().toString();

                    AssistantTpoItem currAssistantTpoItem = new AssistantTpoItem(assistantDeptName, assistantTpoName, assistantTpoDesignation, assistantTpoWorkMail, assistantTpoPersonalMail, assistantTpoContactNo);

                    mAssistantTpoDatabaseReference.child(String.valueOf(position)).setValue(currAssistantTpoItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Your data has been submitted successfully", Toast.LENGTH_SHORT).show();
                                    holder.assistantTpoDeptName.setText(assistantDeptName);
                                    holder.assistantTpoName.setText(assistantTpoName);
                                    holder.assistantTpoDesignation.setText(assistantTpoDesignation);
                                    holder.assistantTpoWorkMail.setText(assistantTpoWorkMail);
                                    holder.assistantTpoPersonalMail.setText(assistantTpoPersonalMail);
                                    holder.assistantTpoContactNo.setText(assistantTpoContactNo);
                                    holder.assistantTpoLayout.setVisibility(View.VISIBLE);
                                    holder.editAssistantTpoLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(holder.editAssistantTpoLayout.getWindowToken(), 0);
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
        return assistantTpoItemList.size();

    }

    public void getProfileImage(AssistantTpoViewHolder holder, String name){
        name = name.trim();
        name = name.replace(" ", "_");
        Bitmap bitmap = AssistantTpoImageProvider.getImage(context, name);
        if (bitmap != null) {
            // Image is in storage
            Glide.with(context)
                    .load(bitmap)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(holder.assistantTpoPhoto);
        }else{
            final StorageReference filepath = mProfilePhotoStorageReference.child(name + "." + "png");
            String finalName = name;
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    Glide.with(context)
                            .load(imageURL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Bitmap imageBitmap = ((BitmapDrawable) resource).getBitmap();
                                    AssistantTpoImageProvider.saveImage(context, imageBitmap, finalName);
                                    return false;
                                }
                            })
                            .circleCrop()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .into(holder.assistantTpoPhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }
}