package com.hire.freshershub.ui.groupChat;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentGroupChatBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class GroupChatFragment extends Fragment {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;

    private FragmentGroupChatBinding binding;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private RecyclerView mMessageListView;
    private MessageAdapter mMessageAdapter;
    ArrayList<Message> messageArrayList;

    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;
    private String oldestMessageId;

    boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        mProgressBar = root.findViewById(R.id.progressBar);
        mMessageListView = root.findViewById(R.id.messageListView);
        mPhotoPickerButton = root.findViewById(R.id.photoPickerButton);
        mMessageEditText = root.findViewById(R.id.messageEditText);
        mSendButton = root.findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        messageArrayList = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(root.getContext(), messageArrayList);
        LinearLayoutManager linearLayoutManagerForGroupChat = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        mMessageListView.setLayoutManager(linearLayoutManagerForGroupChat);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        onSignedInInitialize(user.getDisplayName());

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "This functionality is not available for now", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(mMessageEditText.getText().toString(), mUsername, null);
                mMessagesDatabaseReference.push().setValue(message);
                // Clear input box
                mMessageEditText.setText("");
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", message.getName(), message.getText(), "group_chat", getContext(), getActivity());
                notificationsSender.SendNotifications();
            }
        });

        mMessageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                    isLoading = true;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mMessagesDatabaseReference.orderByKey().endBefore(oldestMessageId).limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int index = 0;
                            if(dataSnapshot.getChildrenCount()==0){
                                mProgressBar.setVisibility(View.GONE);
                            }
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if(index==0){
                                    oldestMessageId=child.getKey();
                                }
                                Message message = child.getValue(Message.class);
                                mMessageListView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMessageListView.scrollToPosition(messageArrayList.size()-20);
                                    }
                                },100L);
                                mProgressBar.setVisibility(View.GONE);
                                messageArrayList.add(messageArrayList.size()-index, message);
                                mMessageAdapter.notifyItemInserted(messageArrayList.size()-index);
                                index++;
                            }
                            isLoading = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(getView().getContext(), "Signed in!", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getView().getContext(), "Signed in canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){

            Uri selectedImageUri = data.getData();

            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            photoRef.putFile(selectedImageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return photoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Message message = new Message(null, mUsername, downloadUri.toString());
                                mMessagesDatabaseReference.push().setValue(message);
                            } else {
                                Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListener == null){
            boolean oldestMessageFound = false;
            mChildEventListener = new ChildEventListener() {
                boolean oldestMessageFound = false;
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(!oldestMessageFound){
                        oldestMessageId=snapshot.getKey();
                        oldestMessageFound=true;
                    }
                    Message message = snapshot.getValue(Message.class);
                    messageArrayList.add(0,message);
                    mMessageAdapter.notifyItemInserted(0);
                    mMessageListView.scrollToPosition(0);
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
            mMessagesDatabaseReference.limitToLast(20).addChildEventListener(mChildEventListener);
        }

//        mMessagesDatabaseReference.limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean oldestMessageFound = false;
//                for(DataSnapshot child : snapshot.getChildren()) {
//                    if(!oldestMessageFound){
//                        oldestMessageId=child.getKey();
//                        oldestMessageFound=true;
//                    }
//                    Message message = child.getValue(Message.class);
//                    messageArrayList.add(0,message);
//                    mMessageAdapter.notifyItemInserted(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null){
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        messageArrayList.clear();
        detachDatabaseReadListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            detachDatabaseReadListener();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}