package com.example.finale;

public class JobPost {

    private String title;
    private String description;
    private String posterEmail;

    public JobPost() {
        // Required empty public constructor
    }

    public JobPost(String title, String description, String posterEmail) {
        this.title = title;
        this.description = description;
        this.posterEmail = posterEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterEmail() {
        return posterEmail;
    }

    public void setPosterEmail(String posterEmail) {
        this.posterEmail = posterEmail;
    }
}
