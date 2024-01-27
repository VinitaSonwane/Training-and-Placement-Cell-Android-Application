package com.hire.freshershub.ui.updateProfile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentUpdateProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateProfileFragment extends Fragment {

    final private int requestCode = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int REQUEST_SELECT_PDF = 3;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;

    private FragmentUpdateProfileBinding binding;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mResumeStorageReference;
    private StorageReference mProfilePhotoStorageReference;

    LinearLayout editProfilePrimaryInfoLayout;
    EditText editProfilePrimaryName;
    EditText editProfilePrimaryRollNo;
    EditText editProfilePrimaryQualification;
    EditText editProfilePrimaryPassingYear;
    EditText editProfilePrimaryStream;
    EditText editProfilePrimaryProfessionalTitle;
    TextView profilePrimaryName;
    TextView profilePrimaryRollNo;
    TextView profilePrimaryQualification;
    TextView profilePrimaryPassingYear;
    TextView profilePrimaryStream;
    TextView profilePrimaryProfessionalTitle;
    LinearLayout editProfilePrimaryLayout;
    Button profilePrimarySaveBtn;
    Button profilePrimaryCancelBtn;
    ImageView profilePrimaryEditBtn;
    LinearLayout editProfileInnerInfoLayout;

    ImageView profileImage;
    ImageView selectProfilePhoto;

    EditText editContactNo;
    EditText editWorkEmail;
    EditText editPersonalEmail;
    EditText editAddress;
    TextView contactNo;
    TextView workEmail;
    TextView personalEmail;
    TextView address;
    ImageView editContactBtn;
    Button editContactCancelBtn;
    Button editContactSaveBtn;
    LinearLayout contactLayout;
    LinearLayout editContactLayout;

    RecyclerView educationListView;
    ArrayList<EducationItem> educationItemArrayList;
    EducationAdapter educationAdapter;
    ImageView addEducationBtn;

    RecyclerView skillListView;
    ArrayList<String> skillList;
    SkillAdapter skillAdapter;
    ImageView addSkillsBtn;
    LinearLayout editSkillLayout;
    EditText editSkill;
    Button editSkillCancelbtn;
    Button editSkillSavebtn;

    RecyclerView internshipRecyclerView;
    ArrayList<InternshipItem> internshipDetailsArrayList;
    InternshipAdapter internshipAdapter;
    ImageView addInternshipBtn;

    RecyclerView projectRecyclerView;
    ArrayList<ProjectItem> projectArrayList;
    ProjectAdapter projectAdapter;
    ImageView addProjectBtn;

    RecyclerView achievementsRecyclerView;
    ArrayList<AchievementItem> achievementItemArrayList;
    AchievementAdapter achievementAdapter;
    ImageView addAchievementBtn;

    Uri resumeUri = null;
    ProgressDialog dialog;
    LinearLayout resumeAddedLayout;
    LinearLayout resumeNotAddedLayout;
    TextView resumeNameTextView;
    ImageView resumeDownloadButton;
    ImageView resumeUpdateButton;
    ImageView resumeUploadButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfileDatabaseReference = mFirebaseDatabase.getReference().child("users");

        mFirebaseStorage = FirebaseStorage.getInstance();
        mResumeStorageReference = mFirebaseStorage.getReference().child("resumes");
        mProfilePhotoStorageReference = mFirebaseStorage.getReference().child("profile_photo");

        editProfilePrimaryInfoLayout = root.findViewById(R.id.edit_profile_primary_info_layout);
        profilePrimaryName = root.findViewById(R.id.profile_primary_name);
        profilePrimaryRollNo = root.findViewById(R.id.profile_primary_roll_no);
        profilePrimaryQualification = root.findViewById(R.id.profile_primary_qualification);
        profilePrimaryPassingYear = root.findViewById(R.id.profile_primary_passing_year);
        profilePrimaryStream = root.findViewById(R.id.profile_primary_stream);
        profilePrimaryProfessionalTitle = root.findViewById(R.id.profile_professional_title);
        editProfilePrimaryName = root.findViewById(R.id.edit_profile_primary_name);
        editProfilePrimaryRollNo = root.findViewById(R.id.edit_profile_primary_roll_no);
        editProfilePrimaryQualification = root.findViewById(R.id.edit_profile_primary_qualification);
        editProfilePrimaryPassingYear = root.findViewById(R.id.edit_profile_primary_passing_year);
        editProfilePrimaryStream = root.findViewById(R.id.edit_profile_primary_stream);
        editProfilePrimaryProfessionalTitle = root.findViewById(R.id.edit_profile_professional_title);
        profilePrimaryCancelBtn = root.findViewById(R.id.profile_primary_cancel_button);
        profilePrimarySaveBtn = root.findViewById(R.id.profile_primary_save_button);
        profilePrimaryEditBtn = root.findViewById(R.id.edit_profile_primary_details);
        editProfilePrimaryLayout = root.findViewById(R.id.edit_profile_primary_layout);
        editProfileInnerInfoLayout = root.findViewById(R.id.edit_profile_primary_inner_info_layout);

        profileImage = root.findViewById(R.id.profile_primary_image);
        selectProfilePhoto = root.findViewById(R.id.select_profile_photo_button);

        editContactNo = root.findViewById(R.id.edit_profile_contact_no);
        editPersonalEmail = root.findViewById(R.id.edit_profile_personal_email);
        editWorkEmail = root.findViewById(R.id.edit_profile_work_email);
        workEmail = root.findViewById(R.id.profile_work_email);
        personalEmail = root.findViewById(R.id.profile_personal_email);
        contactNo = root.findViewById(R.id.profile_contact_no);
        address = root.findViewById(R.id.profile_address);
        editAddress = root.findViewById(R.id.edit_profile_address);
        editContactBtn = root.findViewById(R.id.edit_contact_edit_button);
        editContactCancelBtn = root.findViewById(R.id.edit_contact_cancel_button);
        editContactSaveBtn = root.findViewById(R.id.edit_contact_save_button);
        contactLayout = root.findViewById(R.id.contact_layout);
        editContactLayout = root.findViewById(R.id.edit_contact_layout);

        educationItemArrayList = new ArrayList<>();
        educationListView = root.findViewById(R.id.educationListView);
        educationAdapter = new EducationAdapter(getContext(), educationItemArrayList);
        addEducationBtn = root.findViewById(R.id.education_add_button);

        editSkillCancelbtn = root.findViewById(R.id.skills_cancel_button);
        editSkillSavebtn = root.findViewById(R.id.skills_save_button);
        editSkillLayout = root.findViewById(R.id.edit_skill_layout);
        editSkill = root.findViewById(R.id.edit_skill);
        skillList = new ArrayList<>();
        skillListView = root.findViewById(R.id.skillslistview);
        skillAdapter = new SkillAdapter(getContext(), skillList);
        addSkillsBtn = root.findViewById(R.id.skills_add_button);

        addInternshipBtn = root.findViewById(R.id.internship_add_button);
        internshipDetailsArrayList = new ArrayList<>();
        internshipRecyclerView = root.findViewById(R.id.internship_details_view);
        internshipAdapter = new InternshipAdapter(getContext(), internshipDetailsArrayList);

        addProjectBtn = root.findViewById(R.id.project_add_button);
        projectArrayList = new ArrayList<>();
        projectRecyclerView = root.findViewById(R.id.project_details_view);
        projectAdapter = new ProjectAdapter(getContext(),projectArrayList);

        addAchievementBtn = root.findViewById(R.id.achievement_add_button);
        achievementItemArrayList = new ArrayList<>();
        achievementsRecyclerView = root.findViewById(R.id.achievements_details_view);
        achievementAdapter = new AchievementAdapter(getContext(),achievementItemArrayList);

        resumeAddedLayout = root.findViewById(R.id.edit_profile_resume_added_layout);
        resumeNotAddedLayout = root.findViewById(R.id.edit_profile_resume_not_added_layout);
        resumeNameTextView = root.findViewById(R.id.edit_profile_resume_name_textView);
        resumeDownloadButton = root.findViewById(R.id.edit_profile_download_resume_button);
        resumeUpdateButton = root.findViewById(R.id.edit_profile_update_resume_button);
        resumeUploadButton = root.findViewById(R.id.edit_profile_upload_resume_button);

        getUserDataFromDatabase();
        getProfileImage();

        selectProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                selectImage();
            }
        });

        profilePrimaryEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilePrimaryLayout.setVisibility(View.VISIBLE);
                editProfilePrimaryName.setText(profilePrimaryName.getText().toString());
                editProfilePrimaryRollNo.setText(profilePrimaryRollNo.getText().toString());
                editProfilePrimaryQualification.setText(profilePrimaryQualification.getText().toString());
                editProfilePrimaryPassingYear.setText(profilePrimaryPassingYear.getText().toString());
                editProfilePrimaryStream.setText(profilePrimaryStream.getText().toString());
                editProfilePrimaryProfessionalTitle.setText(profilePrimaryProfessionalTitle.getText().toString());
            }
        });

        profilePrimaryCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilePrimaryName.setText("");
                editProfilePrimaryRollNo.setText("");
                editProfilePrimaryQualification.setText("");
                editProfilePrimaryPassingYear.setText("");
                editProfilePrimaryStream.setText("");
                editProfilePrimaryProfessionalTitle.setText("");
                editProfilePrimaryLayout.setVisibility(View.GONE);
                editProfilePrimaryName.setError(null);
                editProfilePrimaryRollNo.setError(null);
                editProfilePrimaryQualification.setError(null);
                editProfilePrimaryPassingYear.setError(null);
                editProfilePrimaryStream.setError(null);
                editProfilePrimaryProfessionalTitle.setError(null);
            }
        });

        profilePrimarySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePrimaryDetails())
                {
                    String primaryName = editProfilePrimaryName.getText().toString();
                    String primaryRollNo = editProfilePrimaryRollNo.getText().toString();
                    String primaryQualification = editProfilePrimaryQualification.getText().toString();
                    String primaryPassingYear = editProfilePrimaryPassingYear.getText().toString();
                    String primaryStream = editProfilePrimaryStream.getText().toString();
                    String primaryProfessionalTitle = editProfilePrimaryProfessionalTitle.getText().toString();

                    User primaryUser = new User(primaryName, primaryRollNo, primaryQualification, primaryPassingYear, primaryStream, primaryProfessionalTitle);

//                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).push()
                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("primaryDetails").setValue(primaryUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Your data has been submitted successfully", Toast.LENGTH_SHORT).show();
                                    setPrimaryDetails(primaryUser);
                                    editProfilePrimaryName.setText("");
                                    editProfilePrimaryRollNo.setText("");
                                    editProfilePrimaryQualification.setText("");
                                    editProfilePrimaryPassingYear.setText("");
                                    editProfilePrimaryStream.setText("");
                                    editProfilePrimaryProfessionalTitle.setText("");
                                    editProfilePrimaryLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editProfilePrimaryLayout.getWindowToken(), 0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Problem while uploading the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        editContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPersonalEmail.setText(personalEmail.getText().toString());
                editContactNo.setText(contactNo.getText().toString());
                editWorkEmail.setText(workEmail.getText().toString());
                editAddress.setText(address.getText().toString());
                contactLayout.setVisibility(View.GONE);
                editContactLayout.setVisibility(View.VISIBLE);
            }
        });

        editContactCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editContactNo.getText().toString().length()<1 || editPersonalEmail.getText().toString().length()<1 || editWorkEmail.getText().toString().length()<1 || editAddress.getText().toString().length()<1)
                {
                    contactLayout.setVisibility(View.GONE);
                }
                else{
                    contactLayout.setVisibility(View.VISIBLE);
                }

                editPersonalEmail.setText("");
                editWorkEmail.setText("");
                editContactNo.setText("");
                editAddress.setText("");

                editPersonalEmail.setError(null);
                editWorkEmail.setError(null);
                editContactNo.setError(null);
                editAddress.setError(null);

                editContactLayout.setVisibility(View.GONE);
            }
        });

        editContactSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateContactData())
                {
                    Toast.makeText(getContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    String contactPhoneNumber = editContactNo.getText().toString();
                    String contactCollegeMail = editWorkEmail.getText().toString();
                    String contactPersonalMail = editPersonalEmail.getText().toString();
                    String contactAddress = editAddress.getText().toString();

                    User contactUser = new User(contactPhoneNumber, contactCollegeMail, contactPersonalMail, contactAddress);

                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("contactDetails").setValue(contactUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Your data has been submitted successfully", Toast.LENGTH_SHORT).show();
                                    setContactDetails(contactUser);
                                    editPersonalEmail.setText("");
                                    editWorkEmail.setText("");
                                    editContactNo.setText("");
                                    editAddress.setText("");
                                    editContactLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editContactLayout.getWindowToken(), 0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Problem while uploading the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        LinearLayoutManager linearLayoutManagerForEducation = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        educationListView.setLayoutManager(linearLayoutManagerForEducation);
        educationListView.setAdapter(educationAdapter);

        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!educationItemArrayList.isEmpty()){
                    EducationItem lastEducationItem = educationItemArrayList.get(educationItemArrayList.size()-1);
                    if(lastEducationItem.getQualification().length()!=0 && lastEducationItem.getCollegeName().length()!=0 && lastEducationItem.getPassingYear().length()!=0 && lastEducationItem.getSpecialization().length()!=0 && lastEducationItem.getPercent().length()!=0){
                        educationItemArrayList.add(new EducationItem("","","","",""));
                        educationAdapter.notifyItemInserted(educationItemArrayList.size()-1);
                    }
                }else{
                    educationItemArrayList.add(new EducationItem("","","","",""));
                    educationAdapter.notifyItemInserted(0);
                }
            }
        });

        LinearLayoutManager skillsLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        skillListView.setLayoutManager(skillsLinearLayoutManager);
        skillListView.setAdapter(skillAdapter);

        if(skillList.isEmpty()){
            skillListView.setVisibility(View.GONE);
        }else{
            skillListView.setVisibility(View.VISIBLE);
        }

        addSkillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSkillLayout.setVisibility(view.VISIBLE);
            }
        });

        editSkillCancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSkill.setText("");
                editSkillLayout.setVisibility(View.GONE);
            }
        });

        editSkillSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editSkill.getText().length()<1)
                {
                    Toast.makeText(getContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
                else{
                    String currSkill = editSkill.getText().toString();
                    mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("skills").child(currSkill).setValue(editSkill.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    skillListView.setVisibility(View.VISIBLE);
                                    if(!skillList.contains(currSkill)){
                                        skillList.add(currSkill);
                                        skillAdapter.notifyItemInserted(skillList.size()-1);
                                    }
                                    editSkill.setText("");
                                    editSkillLayout.setVisibility(View.GONE);
                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editSkillLayout.getWindowToken(), 0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Problem while uploading the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        LinearLayoutManager linearLayoutManagerForInternship = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        internshipRecyclerView.setLayoutManager(linearLayoutManagerForInternship);
        internshipRecyclerView.setAdapter(internshipAdapter);

        addInternshipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!internshipDetailsArrayList.isEmpty()){
                    InternshipItem latestInternshipItem = internshipDetailsArrayList.get(internshipDetailsArrayList.size()-1);
                    if(latestInternshipItem.getInternshipPos().length()!=0 &&
                            latestInternshipItem.getInternshipMode().length()!=0 &&
                            latestInternshipItem.getInternshipOrgName().length()!=0 &&
                            latestInternshipItem.getInternshipStartMonth().length()!=0 &&
                            latestInternshipItem.getInternshipStartYear().length()!=0 &&
                            latestInternshipItem.getInternshipEndMonth().length()!=0 &&
                            latestInternshipItem.getInternshipStartYear().length()!=0 &&
                            latestInternshipItem.getInternshipDescription().length()!=0){
                        internshipDetailsArrayList.add(new InternshipItem("","","","","","","",""));
                        internshipAdapter.notifyItemInserted(internshipDetailsArrayList.size()-1);
                    }
                }else{
                    internshipDetailsArrayList.add(new InternshipItem("","","","","","","",""));
                    internshipAdapter.notifyItemInserted(0);
                }
            }
        });

        LinearLayoutManager linearLayoutManagerForProject = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        projectRecyclerView.setLayoutManager(linearLayoutManagerForProject);
        projectRecyclerView.setAdapter(projectAdapter);

        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!projectArrayList.isEmpty()){
                    ProjectItem latestProjectItem = projectArrayList.get(projectArrayList.size()-1);
                    if(latestProjectItem.getProjectTitle().length()!=0 && latestProjectItem.getProjectDomain().length() != 0 && latestProjectItem.getProjectDescription().length()!=0){
                        projectArrayList.add(new ProjectItem("","",""));
                        projectAdapter.notifyItemInserted(projectArrayList.size()-1);
                    }
                }else{
                    projectArrayList.add(new ProjectItem("","",""));
                    projectAdapter.notifyItemInserted(0);
                }
            }
        });

        LinearLayoutManager linearLayoutManagerForAchievement = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        achievementsRecyclerView.setLayoutManager(linearLayoutManagerForAchievement);
        achievementsRecyclerView.setAdapter(achievementAdapter);

        addAchievementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!achievementItemArrayList.isEmpty()){
                    AchievementItem latestAchievementItem = achievementItemArrayList.get(achievementItemArrayList.size()-1);
                    if(latestAchievementItem.getAchievementTitle().length()!=0 && latestAchievementItem.getAchievementIssuer().length() != 0 && latestAchievementItem.getAchievementIssueDate().length()!=0 && latestAchievementItem.getAchievementDescription().length()!=0 ){
                        achievementItemArrayList.add(new AchievementItem("","","",""));
                        achievementAdapter.notifyItemInserted(achievementItemArrayList.size()-1);
                    }
                }else{
                    achievementItemArrayList.add(new AchievementItem("","","",""));
                    achievementAdapter.notifyItemInserted(0);
                }
            }
        });

        checkResumeExistsMethod();

        resumeUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadResumeMethod();
            }
        });

        resumeUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadResumeMethod();
            }
        });

        resumeDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadResumeMethod();
            }
        });

        return root;
    }

    private void getUserDataFromDatabase(){
        mProfileDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    if(postSnapshot.getKey().equals(firebaseAuthCurrentUser.getUid())){
                        for(DataSnapshot innerDataSnapshot: postSnapshot.getChildren()){
                            if(innerDataSnapshot.getKey().equals("primaryDetails")){
                                User primaryUser = innerDataSnapshot.getValue(User.class);
                                setPrimaryDetails(primaryUser);
                            }else if(innerDataSnapshot.getKey().equals("contactDetails")){
                                User contactUser = innerDataSnapshot.getValue(User.class);
                                setContactDetails(contactUser);
                            }else if(innerDataSnapshot.getKey().equals("educationDetails")){
                                for(DataSnapshot educationDataSnapshot: innerDataSnapshot.getChildren()){
                                    EducationItem educationItem = educationDataSnapshot.getValue(EducationItem.class);
                                    educationItemArrayList.add(educationItem);
                                    educationAdapter.notifyItemInserted(educationItemArrayList.size()-1);
                                }
                            }else if(innerDataSnapshot.getKey().equals("skills")){
                                for(DataSnapshot skillsDataSnapshot: innerDataSnapshot.getChildren()){
                                    String currSkill = skillsDataSnapshot.getValue(String.class);
                                    skillListView.setVisibility(View.VISIBLE);
                                    skillList.add(currSkill);
                                    skillAdapter.notifyItemInserted(skillList.size()-1);
                                }
                            }else if(innerDataSnapshot.getKey().equals("internshipDetails")){
                                for(DataSnapshot internshipDataSnapshot: innerDataSnapshot.getChildren()){
                                    InternshipItem currInternshipItem = internshipDataSnapshot.getValue(InternshipItem.class);
                                    internshipDetailsArrayList.add(currInternshipItem);
                                    internshipAdapter.notifyItemInserted(internshipDetailsArrayList.size()-1);
                                }
                            }else if(innerDataSnapshot.getKey().equals("projectDetails")){
                                for(DataSnapshot projectDataSnapshot: innerDataSnapshot.getChildren()){
                                    ProjectItem currProjectItem = projectDataSnapshot.getValue(ProjectItem.class);
                                    projectArrayList.add(currProjectItem);
                                    projectAdapter.notifyItemInserted(projectArrayList.size()-1);
                                }
                            }else if(innerDataSnapshot.getKey().equals("achievementDetails")){
                                for(DataSnapshot achievementDataSnapshot: innerDataSnapshot.getChildren()){
                                    AchievementItem currAchievementItem = achievementDataSnapshot.getValue(AchievementItem.class);
                                    achievementItemArrayList.add(currAchievementItem);
                                    achievementAdapter.notifyItemInserted(achievementItemArrayList.size()-1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPrimaryDetails(User user){
        editProfileInnerInfoLayout.setVisibility(View.VISIBLE);
        profilePrimaryName.setText(user.getPrimaryName());
        profilePrimaryRollNo.setText(user.getPrimaryRollNo());
        profilePrimaryQualification.setText(user.getPrimaryQualification());
        profilePrimaryPassingYear.setText(user.getPrimaryPassingYear());
        profilePrimaryStream.setText(user.getPrimaryStream());
        profilePrimaryProfessionalTitle.setText(user.getPrimaryProfessionalTitle());
    }

    public boolean validatePrimaryDetails(){
        if(editProfilePrimaryName.getText().length()==0){
            editProfilePrimaryName.setError("Please enter valid name");
            return false;
        }

        if(editProfilePrimaryRollNo.getText().length()==0){
            editProfilePrimaryRollNo.setError("Please enter valid roll no");
            return false;
        }else if(editProfilePrimaryRollNo.getText().length()!=8){
            editProfilePrimaryRollNo.setError("Please enter valid roll no");
            return false;
        }

        if(editProfilePrimaryQualification.getText().length()==0){
            editProfilePrimaryQualification.setError("Please enter valid Qualification");
            return false;
        }

        if(editProfilePrimaryPassingYear.getText().length()==0){
            editProfilePrimaryPassingYear.setError("Please enter valid passing year");
            return false;
        }else if(editProfilePrimaryPassingYear.getText().length()!=4){
            editProfilePrimaryPassingYear.setError("Please enter valid passing year");
            return false;
        }

        if(editProfilePrimaryStream.getText().length()==0){
            editProfilePrimaryStream.setError("Please enter valid Branch");
            return false;
        }

        if(editProfilePrimaryProfessionalTitle.getText().length()==0){
            editProfilePrimaryProfessionalTitle.setError("Please enter valid professional title");
            return false;
        }

        return true;
    }

    public void setContactDetails(User user){
        contactLayout.setVisibility(View.VISIBLE);
        contactNo.setText(user.getContactPhoneNo());
        workEmail.setText(user.getContactCollegeMail());
        personalEmail.setText(user.getContactPersonalMail());
        address.setText(user.getContactAddress());
    }

    private boolean validateContactData()
    {
        if(editContactNo.getText().length()!=10)
        {
            editContactNo.setError("Please enter valid phone no");
            return false;
        }

        if(editPersonalEmail.getText().length()==0)
        {
            editPersonalEmail.setError("Enter valid mail");
            return false;
        } else if(!editPersonalEmail.getText().toString().contains("@")){
            editPersonalEmail.setError("Enter valid mail");
            return false;
        }

        if(editWorkEmail.getText().length()==0)
        {
            editWorkEmail.setError("Enter valid college mail");
            return false;
        }
        else if(!editWorkEmail.getText().toString().contains("@gcoeara.ac.in")){
            editWorkEmail.setError("Enter valid college mail");
            return false;
        }

        if(editAddress.getText().toString().length()==0)
        {
            editAddress.setError("Enter valid address");
            return false;
        }
        return true;
    }

    public void getProfileImage(){
        Bitmap bitmap = ImageProvider.getImage(getContext());
        if (bitmap != null) {
            // Image is in storage
            Glide.with(this)
                    .load(bitmap)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(profileImage);
        }else{
            final String fileName = firebaseAuthCurrentUser.getUid();
            final StorageReference filepath = mProfilePhotoStorageReference.child(fileName + "." + "png");
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    Glide.with(getContext())
                            .load(imageURL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Bitmap imageBitmap = ((BitmapDrawable) resource).getBitmap();
                                    ImageProvider.saveImage(getContext(), imageBitmap);
                                    return false;
                                }
                            })
                            .circleCrop()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .into(profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    public void uploadResumeMethod(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, REQUEST_SELECT_PDF);
    }

    public void checkResumeExistsMethod(){
        final String fileName = firebaseAuthCurrentUser.getUid();
        final StorageReference filepath = mResumeStorageReference.child(fileName + "." + "pdf");
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                resumeAddedLayout.setVisibility(View.VISIBLE);
                String setFileName = firebaseAuthCurrentUser.getDisplayName();
                if (setFileName != null) {
                    setFileName = setFileName.replace(" ", "_");
                }
                setFileName+="_Resume.pdf";
                resumeNameTextView.setText(setFileName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resumeNotAddedLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void downloadResumeMethod(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }

        final String fileName = firebaseAuthCurrentUser.getUid();
        final StorageReference filepath = mResumeStorageReference.child(fileName + "." + "pdf");

        String setFileName = firebaseAuthCurrentUser.getDisplayName();
        if (setFileName != null) {
            setFileName = setFileName.replace(" ", "_");
        }

        File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), setFileName+"_Resume.pdf");

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Downloading");

        filepath.getFile(localFile)
                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                        dialog.show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File downloaded successfully
                        dialog.dismiss();
                        Toast.makeText(getContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();
                        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", localFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Failed to downloaded the file", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (this.requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
            } else {
                // Permission is denied
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Photo Using (Limit 256 kB)");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Gallery")) {
                    dispatchSelectFromGalleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    private void dispatchSelectFromGalleryIntent() {
        @SuppressLint("IntentReset") Intent selectFromGalleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectFromGalleryIntent.setType("image/*");
        if (selectFromGalleryIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(selectFromGalleryIntent, REQUEST_GALLERY_IMAGE);
        }
    }

    private void checkAndRequestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        int audioPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        int storagePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private Uri getImageToUpload(Bitmap bitmap) {
        File imageFolder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(getContext(), "com.hire.freshershub.provider", file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri photoUri = getImageToUpload(imageBitmap);
            final String fileName = firebaseAuthCurrentUser.getUid();
            final StorageReference filepath = mProfilePhotoStorageReference.child(fileName + "." + "png");
            filepath.putFile(photoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        ImageProvider.saveImage(getContext(), imageBitmap);
                        Glide.with(requireContext())
                                .load(imageBitmap)
                                .circleCrop()
                                .into(profileImage);
                    } else {
                        Toast.makeText(getContext(), "Upload Failed, Please try again or check the size of image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                Uri photoUri = data.getData();
                final String fileName = firebaseAuthCurrentUser.getUid();
                final StorageReference filepath = mProfilePhotoStorageReference.child(fileName + "." + "png");

                filepath.putFile(photoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            // process the imageBitmap
                            ImageProvider.saveImage(getContext(), imageBitmap);
                            Glide.with(requireContext())
                                    .load(imageBitmap)
                                    .circleCrop()
                                    .into(profileImage);
                        } else {
                            Toast.makeText(getContext(), "Upload Failed, Please try again or check the size of image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_SELECT_PDF && resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Uploading");

            // this will show message uploading
            // while pdf is uploading
            dialog.show();
            resumeUri = data.getData();
            final String fileName = firebaseAuthCurrentUser.getUid();
            final StorageReference filepath = mResumeStorageReference.child(fileName + "." + "pdf");
            filepath.putFile(resumeUri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();
                        resumeAddedLayout.setVisibility(View.VISIBLE);
                        resumeNotAddedLayout.setVisibility(View.GONE);
                        String setFileName = firebaseAuthCurrentUser.getDisplayName();
                        if (setFileName != null) {
                            setFileName = setFileName.replace(" ", "_");
                        }
                        setFileName+="_Resume.pdf";
                        resumeNameTextView.setText(setFileName);
                        mProfileDatabaseReference.child(firebaseAuthCurrentUser.getUid()).child("isResumeAdded").setValue(true);
                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Upload Failed, Please try again  or check the size of image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}