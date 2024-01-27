package com.hire.freshershub.ui.jobsHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentJobProfilesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JobProfilesFragment extends Fragment{

    private FragmentJobProfilesBinding binding;

    private FirebaseAuth mFirebaseAuth;

    FloatingActionButton addJobButton;

    ViewPager viewPager;
    JobCategoryAdapter adapter;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJobProfilesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = root.findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new JobCategoryAdapter(getContext(), getChildFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        mFirebaseAuth = FirebaseAuth.getInstance();

        addJobButton = root.findViewById(R.id.add_job_fab_button);

//        for(int i=0; i<jobs.size(); i++){
//            Job temp = jobs.get(i);
//            mJobsDatabaseReference.child(String.valueOf(temp.getId())).setValue(temp);
//        }

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if(user!=null){
            String [] special = getResources().getStringArray(R.array.special_access);
            for(int i = 0; i< special.length; i++){
                if(user.getUid().equals(special[i])){
                    addJobButton.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddJob.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}