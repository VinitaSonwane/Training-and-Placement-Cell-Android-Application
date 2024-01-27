package com.hire.freshershub.ui.updateProfile;

public class User {
    private String name;
    private String email;
    private String phoneNo;

    private String primaryName;
    private String primaryRollNo;
    private String primaryQualification;
    private String primaryPassingYear;
    private String primaryStream;
    private String primaryProfessionalTitle;

    private String contactPhoneNo;
    private String contactCollegeMail;
    private String contactPersonalMail;
    private String contactAddress;

    public User() {
    }

    public User(String name, String email, String phoneNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public User(String primaryName, String primaryRollNo, String primaryQualification, String primaryPassingYear, String primaryStream, String primaryProfessionalTitle) {
        this.primaryName = primaryName;
        this.primaryRollNo = primaryRollNo;
        this.primaryQualification = primaryQualification;
        this.primaryPassingYear = primaryPassingYear;
        this.primaryStream = primaryStream;
        this.primaryProfessionalTitle = primaryProfessionalTitle;
    }

    public User(String contactPhoneNo, String contactCollegeMail, String contactPersonalMail, String contactAddress) {
        this.contactPhoneNo = contactPhoneNo;
        this.contactCollegeMail = contactCollegeMail;
        this.contactPersonalMail = contactPersonalMail;
        this.contactAddress = contactAddress;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public String getPrimaryRollNo() {
        return primaryRollNo;
    }

    public String getPrimaryQualification() {
        return primaryQualification;
    }

    public String getPrimaryPassingYear() {
        return primaryPassingYear;
    }

    public String getPrimaryStream() {
        return primaryStream;
    }

    public String getPrimaryProfessionalTitle() {
        return primaryProfessionalTitle;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public String getContactCollegeMail() {
        return contactCollegeMail;
    }

    public String getContactPersonalMail() {
        return contactPersonalMail;
    }

    public String getContactAddress() {
        return contactAddress;
    }
}
