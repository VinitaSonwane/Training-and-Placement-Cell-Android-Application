package com.hire.freshershub.ui.placementStatistics;

public class BranchValues {
    private String Branch;
    private int placeStud;

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public void setPlaceStud(int placeStud) {
        this.placeStud = placeStud;
    }

    public int getPlaceStud() {
        return placeStud;
    }

    public BranchValues(String branch, int placeStud) {
        Branch = branch;
        this.placeStud = placeStud;
    }
}
