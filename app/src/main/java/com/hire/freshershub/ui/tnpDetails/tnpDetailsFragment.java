package com.hire.freshershub.ui.tnpDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentTnpDetailsBinding;

public class tnpDetailsFragment extends Fragment {

    private FragmentTnpDetailsBinding binding;

    ImageView tpoMailImageView;
    ImageView tpoCallImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTnpDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tpoMailImageView = root.findViewById(R.id.tpo_mail_imageView);
        tpoCallImageView = root.findViewById(R.id.tpo_call_imageView);

        tpoMailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "tpo@gcoeara.ac.in"));
                    startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(getContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        tpoCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:8459032813"));
                startActivity(callIntent);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}