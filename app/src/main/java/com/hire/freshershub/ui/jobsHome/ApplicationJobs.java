package com.hire.freshershub.ui.jobsHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hire.freshershub.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplicationJobs extends Fragment implements JobAdapter.customButtonListener{

    RequestQueue requestQueue;

    JobAdapter jobAdapter;
    ArrayList<Job> jobs;
    RecyclerView jobsRecyclerView;
    ImageView jobItemBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jobs_list, container, false);

        jobs = new ArrayList<>();

        jobsRecyclerView = rootView.findViewById(R.id.jobs_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        jobsRecyclerView.setLayoutManager(linearLayoutManager);
        jobAdapter = new JobAdapter(getContext(), jobs);
        jobAdapter.setCustomButtonListener(this);
        jobsRecyclerView.setAdapter(jobAdapter);

        requestQueue = MySingleton.getInstance(getContext()).getRequestQueue();

        getJobsDataFromAPI();

        return rootView;
    }

    private void getJobsDataFromAPI(){
        String url = "https://api.adzuna.com/v1/api/jobs/in/search/1?app_id=586f2527&app_key=27e54719a19fea00b24ec07b292af0fb&results_per_page=20";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for(int i = 0; i<results.length(); i++){
                                JSONObject jobDetails = results.getJSONObject(i);
                                JSONObject companyDetails = jobDetails.getJSONObject("company");
                                String name = companyDetails.getString("display_name");
                                String position = jobDetails.getString("title");
                                JSONObject locationDetails = jobDetails.getJSONObject("location");
                                String location = locationDetails.getString("display_name");
                                String salary = jobDetails.getString("salary_is_predicted");
                                String description = jobDetails.getString("description");
                                String redirect_url = jobDetails.getString("redirect_url");
                                Job job = new Job(name, position, location, salary, description, redirect_url);
                                jobs.add(job);
                                jobAdapter.notifyItemInserted(jobs.size()-1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Problem while loading the data from API", Toast.LENGTH_SHORT).show();
                    }
                });
//      Adding the api request to the requestQueue
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onShowMoreButtonClickListener(int position) {
//        Toast.makeText(getContext(), "Position "+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), JobDetails.class);
        intent.putExtra("type", "application");
        intent.putExtra("name", jobs.get(position).getCompanyName());
        intent.putExtra("position", jobs.get(position).getPosition());
        intent.putExtra("location", jobs.get(position).getLocation());
        intent.putExtra("salary", jobs.get(position).getSalary());
        intent.putExtra("description", jobs.get(position).getJobDescription());
        intent.putExtra("redirect_url", jobs.get(position).getUrl());
        startActivity(intent);
    }

    @Override
    public void onSetBookmarkClickListener(int position) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
