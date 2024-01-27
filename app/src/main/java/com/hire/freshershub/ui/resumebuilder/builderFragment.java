package com.hire.freshershub.ui.resumebuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hire.freshershub.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link builderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class builderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public builderFragment() {
        // Required empty public constructor
    }

    public static builderFragment newInstance(String param1, String param2) {
        builderFragment fragment = new builderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void openResumeBuilderLink(View view) {
        // Open the link to the Resume Builder in the default browser
        String url = "https://your_resume_builder_url_here";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Your existing builderFragment code

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_builder, container, false);

        TextView linkTextView = view.findViewById(R.id.nav_resume_builder);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser("https://novoresume.com/?noRedirect=true");
            }
        });

        return view;
    }


    private void openUrlInBrowser(String url) {
        // Specify the package for Chrome
        String chromePackageName = "com.android.chrome";

        // Create an Intent with the specified package
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.setPackage(chromePackageName);

        // Check if there's an activity to handle the intent
        if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            // If Chrome is not installed, open the URL in the default browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
