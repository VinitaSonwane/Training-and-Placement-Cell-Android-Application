package com.hire.freshershub.ui.updateProfile;

public class EducationItem {
    private String qualification;
    private String passingYear;
    private String specialization;
    private String collegeName;
    private String percent;

    public EducationItem(){}

    public EducationItem(String qualification,String passingYear, String specialization, String collegeName, String percent) {
        this.qualification = qualification;
        this.passingYear = passingYear;
        this.specialization = specialization;
        this.collegeName = collegeName;
        this.percent = percent;

    }

    public String getQualification() {
        return qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getPercent() {
        return percent;
    }

    public String getPassingYear() {
        return passingYear;
    }
}