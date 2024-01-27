package com.hire.freshershub.ui.jobsHome;

public class Job {
    private int id;
    private String position;
    private String companyName;
    private String experience;
    private String openings;
    private String location;
    private String salary;
    private String skills;
    private String jobDescription;
    private String url;

    public Job() {
    }

    public Job(String position, String companyName) {
        this.position = position;
        this.companyName = companyName;
    }

    public Job(int id, String position, String companyName) {
        this.id = id;
        this.position = position;
        this.companyName = companyName;
    }

    public Job(int id, String position, String companyName, String url) {
        this.id = id;
        this.position = position;
        this.companyName = companyName;
        this.url = url;
    }

    public Job(int id, String position, String companyName, String experience, String openings, String location, String salary, String skills, String url, String jobDescription) {
        this.id = id;
        this.position = position;
        this.companyName = companyName;
        this.experience = experience;
        this.openings = openings;
        this.location = location;
        this.salary = salary;
        this.skills = skills;
        this.url = url;
        this.jobDescription = jobDescription;
    }

    public Job(String companyName, String position, String location, String salary, String jobDescription, String url) {
        this.companyName = companyName;
        this.position = position;
        this.location = location;
        this.salary = salary;
        this.jobDescription = jobDescription;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getExperience() {
        return experience;
    }

    public String getOpenings() {
        return openings;
    }

    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getSkills() {
        return skills;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getUrl() {
        return url;
    }
}
