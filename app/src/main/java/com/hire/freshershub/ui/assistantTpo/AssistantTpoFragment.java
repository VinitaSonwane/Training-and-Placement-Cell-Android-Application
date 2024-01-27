package com.hire.freshershub.ui.assistantTpo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentAssistantTpoBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssistantTpoFragment extends Fragment {

    private FragmentAssistantTpoBinding binding;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mAssistantTpoDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseAuthCurrentUser;

    RecyclerView assistantTpoRecyclerView;
    ArrayList<AssistantTpoItem> assistantTpoList;
    AssistantTpoAdapter assistantTpoAdapter;
    FloatingActionButton assistantTpoAddBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAssistantTpoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAssistantTpoDatabaseReference = mFirebaseDatabase.getReference().child("assistantTPOs");

        assistantTpoRecyclerView = root.findViewById(R.id.assistant_tpo_details);
        assistantTpoAddBtn = root.findViewById(R.id.assistant_tpo_add_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String [] special = getResources().getStringArray(R.array.special_access);
            for(int i = 0; i< special.length; i++){
                if(user.getUid().equals(special[i])){
                    assistantTpoAddBtn.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        assistantTpoList = new ArrayList<>();
        assistantTpoAdapter = new AssistantTpoAdapter(getContext(), assistantTpoList);

//        assistantTpoList.add(new AssistantTpoItem("Computer Engineering","R.P. Bagawade","Assistant Professor","tpo.comp@gcoeara.ac.in","bagawaderp@gmail.com","7465479579"));
//
//        assistantTpoList.add(new AssistantTpoItem("Electronics and Telecommunication Engineering","R.P. Bagawade","Assistant Professor","tpo.comp@gcoeara.ac.in","bagawaderp@gmail.com","7465479579"));

        LinearLayoutManager linearLayoutManagerForAssistantTpo = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        assistantTpoRecyclerView.setLayoutManager(linearLayoutManagerForAssistantTpo);
        assistantTpoRecyclerView.setAdapter(assistantTpoAdapter);

        assistantTpoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!assistantTpoList.isEmpty()){
                    AssistantTpoItem assistantTpoItem = assistantTpoList.get(assistantTpoList.size()-1);
                    if(assistantTpoItem.getAssistantDeptName().length()!=0 && assistantTpoItem.getAssistantTpoDesignation().length() != 0 && assistantTpoItem.getAssistantTpoWorkMail().length()!=0 && assistantTpoItem.getAssistantTpoPersonalMail().length()!=0 && assistantTpoItem.getAssistantTpoContactNo().length()!=0 ){
                        assistantTpoList.add(new AssistantTpoItem("","","","","",""));
                        assistantTpoAdapter.notifyItemInserted(assistantTpoList.size()-1);
                    }
                }else{
                    assistantTpoList.add(new AssistantTpoItem("","","","","",""));
                    assistantTpoAdapter.notifyItemInserted(0);
                }
            }
        });

        getAssistantTPOsDataFromDatabase();

        return root;
    }

    private void getAssistantTPOsDataFromDatabase(){
        mAssistantTpoDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    AssistantTpoItem assistantTpoItem = postSnapshot.getValue(AssistantTpoItem.class);
                    assistantTpoList.add(assistantTpoItem);
                    assistantTpoAdapter.notifyItemInserted(assistantTpoList.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}