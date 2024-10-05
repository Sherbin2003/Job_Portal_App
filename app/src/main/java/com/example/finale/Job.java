package com.example.finale;

public class Job {

    private String id;
    private String companyName;
    private String degreeRequired;
    private String jobDescription;
    private String jobLocation;
    private String jobTitle;
    private String posterEmail;

    public Job() {
        // Required empty constructor for Firestore
    }

    public Job(String id, String companyName, String degreeRequired, String jobDescription, String jobLocation, String jobTitle, String posterEmail) {
        this.id = id;
        this.companyName = companyName;
        this.degreeRequired = degreeRequired;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.jobTitle = jobTitle;
        this.posterEmail = posterEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDegreeRequired() {
        return degreeRequired;
    }

    public void setDegreeRequired(String degreeRequired) {
        this.degreeRequired = degreeRequired;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getPosterEmail() {
        return posterEmail;
    }

    public void setPosterEmail(String posterEmail) {
        this.posterEmail = posterEmail;
    }
}
