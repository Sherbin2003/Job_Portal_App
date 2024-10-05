package com.example.finale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostJobActivity extends AppCompatActivity {

    EditText editTextJobTitle, editTextJobDescription, editTextCompanyName, editTextJobLocation, editTextDegree, editTextEmail;
    Button buttonPostJob;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        editTextJobTitle = findViewById(R.id.editTextJobTitle);
        editTextJobDescription = findViewById(R.id.editTextJobDescription);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextJobLocation = findViewById(R.id.editTextJobLocation);
        editTextDegree = findViewById(R.id.editTextDegree);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonPostJob = findViewById(R.id.buttonPostJob);

        buttonPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJob();
            }
        });
    }

    private void postJob() {
        String jobTitle = editTextJobTitle.getText().toString().trim();
        String jobDescription = editTextJobDescription.getText().toString().trim();
        String companyName = editTextCompanyName.getText().toString().trim();
        String jobLocation = editTextJobLocation.getText().toString().trim();
        String degreeRequired = editTextDegree.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(jobDescription) || TextUtils.isEmpty(companyName)
                || TextUtils.isEmpty(jobLocation) || TextUtils.isEmpty(degreeRequired) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new job post with the entered details
        Map<String, Object> jobPost = new HashMap<>();
        jobPost.put("jobTitle", jobTitle);
        jobPost.put("jobDescription", jobDescription);
        jobPost.put("companyName", companyName);
        jobPost.put("jobLocation", jobLocation);
        jobPost.put("degreeRequired", degreeRequired);
        jobPost.put("posterEmail", email); // Use a clear key for the poster's email

        // Add a new document with a generated ID
        db.collection("jobPosts")
                .add(jobPost)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PostJobActivity.this, "Job posted successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to job list activity
                    Intent intent = new Intent(PostJobActivity.this, JobListActivity.class);
                    intent.putExtra("jobTitle", jobTitle);
                    intent.putExtra("jobDescription", jobDescription);
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("jobLocation", jobLocation);
                    intent.putExtra("degreeRequired", degreeRequired);
                    intent.putExtra("posterEmail", email); // Pass email to JobListActivity
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PostJobActivity.this, "Error posting job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
