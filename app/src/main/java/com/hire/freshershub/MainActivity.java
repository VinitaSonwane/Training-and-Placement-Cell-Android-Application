package com.hire.freshershub;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.hire.freshershub.databinding.ActivityMainBinding;
import com.hire.freshershub.ui.groupChat.SharedPreferenceForLogin;
import com.hire.freshershub.ui.updateProfile.ImageProvider;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    TextView nameTextViewHeader;
    ImageView profilePhotoHeader;
    TextView professionalTitleHeader;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_job_profiles, R.id.nav_update_profile, R.id.nav_placement_statistics, R.id.nav_tnp_details, R.id.nav_assistant_tpo, R.id.nav_group_chat, R.id.nav_our_team)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        nameTextViewHeader = header.findViewById(R.id.nameTextViewHeader);
        profilePhotoHeader = header.findViewById(R.id.profilePhotoHeader);
        professionalTitleHeader = header.findViewById(R.id.professionalTitleViewHeader);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mUsername = ANONYMOUS;

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
//                    user is signed in
                    onSignedInInitialize(user.getDisplayName());
                }else{
//                    user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
//                                    To Save Password by System
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .setTheme(R.style.Theme_FreshersHub)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                nameTextViewHeader.setText(mUsername);
                Toast.makeText(MainActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(MainActivity.this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        }
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
        SharedPreferenceForLogin.setUserName(this, username);
        nameTextViewHeader.setText(mUsername);
        DatabaseReference mProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        mProfileDatabaseReference.child("primaryDetails").child("primaryProfessionalTitle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                professionalTitleHeader.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Bitmap bitmap = ImageProvider.getImage(this);
        if (bitmap != null) {
            // Image is in storage
            Glide.with(this)
                    .load(bitmap)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(profilePhotoHeader);
        }
    }
    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
//                sign out
                AuthUI.getInstance().signOut(this);
                SharedPreferenceForLogin.clearUserName(this);
                return true;

            case R.id.search_bar:

            case R.id.action_settings:
                Toast.makeText(this, "This functionality is not available for now", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }

}