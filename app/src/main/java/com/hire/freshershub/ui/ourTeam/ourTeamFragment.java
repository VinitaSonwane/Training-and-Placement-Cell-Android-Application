package com.hire.freshershub.ui.ourTeam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentOurTeamBinding;
import com.hire.freshershub.databinding.FragmentTnpDetailsBinding;

public class ourTeamFragment extends Fragment {

    private FragmentOurTeamBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOurTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}