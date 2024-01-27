package com.hire.freshershub.ui.placementStatistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hire.freshershub.R;
import com.hire.freshershub.databinding.FragmentPlacementStatisticsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class placementStatisticsFragment extends Fragment {

    private FragmentPlacementStatisticsBinding binding;
    private Spinner spinneryears;
    TextView placementHeading;
    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelNames;
    ArrayList<BranchValues> branchValues= new ArrayList<>();
    TextView averageCtcCompany;
    TextView averageCtcCandidate;
    TextView maxCtc;
    TextView totalCompanies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPlacementStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        averageCtcCompany = root.findViewById(R.id.tnp_average_ctc_company);
        averageCtcCandidate = root.findViewById(R.id.tnp_average_ctc_candidate);
        maxCtc = root.findViewById(R.id.tnp_max_ctc);
        totalCompanies = root.findViewById(R.id.tnp_company_visited);
        spinneryears = root.findViewById(R.id.placement_details_dropdown);
        placementHeading = root.findViewById(R.id.placement_heading);
        barChart = root.findViewById(R.id.bar_chart);

        String [] years = getResources().getStringArray(R.array.year_array);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryears.setAdapter(adapter);
        spinneryears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getId()==R.id.placement_details_dropdown)
                {
                    String valuefromspinner = parent.getItemAtPosition(position).toString();
                    placementHeading.setText(valuefromspinner);
                    barEntryArrayList = new ArrayList<>();
                    labelNames = new ArrayList<>();
                    if(valuefromspinner.equals("Select Year"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("2018",48));
                        branchValues.add(new BranchValues("2019",106));
                        branchValues.add(new BranchValues("2020",102));
                        branchValues.add(new BranchValues("2021",117));
                        branchValues.add(new BranchValues("2022",152));
                        averageCtcCompany.setText("4.76");
                        averageCtcCandidate.setText("3.35");
                        maxCtc.setText("12.76");
                        totalCompanies.setText("24");
                        placementHeading.setText("");


                    }
                    else if(valuefromspinner.equals("2018"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("COMP",7));
                        branchValues.add(new BranchValues("ENTC",7));
                        branchValues.add(new BranchValues("CIVIL",0));
                        branchValues.add(new BranchValues("INSTRU",0));
                        branchValues.add(new BranchValues("MECH",21));
                        branchValues.add(new BranchValues("AUTO",13));
                        averageCtcCompany.setText("4.76");
                        averageCtcCandidate.setText("3.35");
                        maxCtc.setText("12.76");
                        totalCompanies.setText("24");
                    }else if(valuefromspinner.equals("2019"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("COMP",53));
                        branchValues.add(new BranchValues("ENTC",11));
                        branchValues.add(new BranchValues("CIVIL",0));
                        branchValues.add(new BranchValues("INSTRU",0));
                        branchValues.add(new BranchValues("MECH",10));
                        branchValues.add(new BranchValues("AUTO",24));
                        averageCtcCompany.setText("4.76");
                        averageCtcCandidate.setText("3.35");
                        maxCtc.setText("12.76");
                        totalCompanies.setText("24");
                    }else if(valuefromspinner.equals("2020"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("COMP",47));
                        branchValues.add(new BranchValues("ENTC",12));
                        branchValues.add(new BranchValues("CIVIL",0));
                        branchValues.add(new BranchValues("INSTRU",6));
                        branchValues.add(new BranchValues("MECH",22));
                        branchValues.add(new BranchValues("AUTO",15));
                        averageCtcCompany.setText("1.76");
                        averageCtcCandidate.setText("1.35");
                        maxCtc.setText("2.76");
                        totalCompanies.setText("45");
                    }else if(valuefromspinner.equals("2021"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("COMP",46));
                        branchValues.add(new BranchValues("ENTC",28));
                        branchValues.add(new BranchValues("CIVIL",0));
                        branchValues.add(new BranchValues("INSTRU",13));
                        branchValues.add(new BranchValues("MECH",12));
                        branchValues.add(new BranchValues("AUTO",18));
                        averageCtcCompany.setText("2.76");
                        averageCtcCandidate.setText("6.35");
                        maxCtc.setText("12.76");
                        totalCompanies.setText("24");
                    }else if(valuefromspinner.equals("2022"))
                    {
                        branchValues.clear();
                        branchValues.add(new BranchValues("COMP",48));
                        branchValues.add(new BranchValues("ENTC",30));
                        branchValues.add(new BranchValues("CIVIL",1));
                        branchValues.add(new BranchValues("INSTRU",18));
                        branchValues.add(new BranchValues("MECH",33));
                        branchValues.add(new BranchValues("AUTO",22));
                        averageCtcCompany.setText("3.76");
                        averageCtcCandidate.setText("3.35");
                        maxCtc.setText("12.76");
                        totalCompanies.setText("24");
                    }



                    for(int i=0;i<branchValues.size();i++)
                    {
                        String branch = branchValues.get(i).getBranch();
                        int placeStud = branchValues.get(i).getPlaceStud();
                        barEntryArrayList.add(new BarEntry(i,placeStud));
                        labelNames.add(branch);
                    }
                    BarDataSet barDataSet;
                    if(valuefromspinner.equals("Select Year"))
                    {
                        barDataSet = new BarDataSet(barEntryArrayList,"Year");
                    }
                    else{
                        barDataSet = new BarDataSet(barEntryArrayList,"Branch");
                    }
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    Description description = new Description();
                    description.setText("Branches");
                    barChart.setDescription(description);
                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setDrawAxisLine(false);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(labelNames.size());

                    barChart.animateY(1000);
                    barChart.invalidate();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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