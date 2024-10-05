package com.example.finale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Post Job Button
        Button buttonPostJob = findViewById(R.id.buttonPostJob);
        buttonPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Post Job activity
                startActivity(new Intent(HomeActivity.this, PostJobActivity.class));
            }
        });

        // Job Listing Button
        Button buttonSearchJob = findViewById(R.id.buttonSearchJob);
        buttonSearchJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Job List activity
                startActivity(new Intent(HomeActivity.this, JobListActivity.class));
            }
        });

        // Posted Jobs Button
        Button buttonPostedJobs = findViewById(R.id.buttonPostedJobs);
        buttonPostedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostedJobsActivity.class);
                startActivity(intent);
            }
        });
        Button buttonLocation = findViewById(R.id.buttonLocation);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, mapping.class);
                startActivity(intent);
            }
        });
    }
}
