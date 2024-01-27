package com.hire.freshershub.ui.updateProfile;

public class InternshipItem {
    private String internshipPos;
    private String internshipMode;
    private String internshipOrgName;
    private String internshipStartMonth;
    private String internshipStartYear;
    private String internshipEndMonth;
    private String internshipEndYear;
    private String internshipDescription;

    public InternshipItem(){}

    public InternshipItem(String internshipPos, String internshipMode, String internshipOrgName, String internshipStartMonth, String internshipStartYear, String internshipEndMonth, String internshipEndYear, String internshipDescription) {
        this.internshipPos = internshipPos;
        this.internshipMode = internshipMode;
        this.internshipOrgName = internshipOrgName;
        this.internshipStartMonth = internshipStartMonth;
        this.internshipStartYear = internshipStartYear;
        this.internshipEndMonth = internshipEndMonth;
        this.internshipEndYear = internshipEndYear;
        this.internshipDescription = internshipDescription;
    }

    public String getInternshipPos() {
        return internshipPos;
    }

    public String getInternshipMode() {
        return internshipMode;
    }

    public String getInternshipOrgName() {
        return internshipOrgName;
    }

    public String getInternshipStartMonth() {
        return internshipStartMonth;
    }

    public String getInternshipStartYear() {
        return internshipStartYear;
    }

    public String getInternshipEndMonth() {
        return internshipEndMonth;
    }

    public String getInternshipEndYear() {
        return internshipEndYear;
    }

    public String getInternshipDescription() {
        return internshipDescription;
    }

}
