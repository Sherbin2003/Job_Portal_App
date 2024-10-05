package com.example.finale;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class JobListActivity extends AppCompatActivity {

    ListView listViewJobs;
    TextView textViewNoResults;
    EditText searchBar;
    ArrayList<Job> jobList;
    ArrayList<Job> filteredJobList;
    JobAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        // Initialize views
        listViewJobs = findViewById(R.id.listViewJobs);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        searchBar = findViewById(R.id.searchBar);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize job lists
        jobList = new ArrayList<>();
        filteredJobList = new ArrayList<>();

        // Create an adapter for your ListView
        adapter = new JobAdapter(this, filteredJobList);

        // Set the adapter to the ListView
        listViewJobs.setAdapter(adapter);

        // Set item click listener to the ListView
        listViewJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Retrieve the selected job
                    Job selectedJob = filteredJobList.get(position);

                    // Pass job details to the JobDetailsActivity
                    Intent intent = new Intent(JobListActivity.this, JobDetailsActivity.class);
                    intent.putExtra("jobId", selectedJob.getId());
                    intent.putExtra("jobTitle", selectedJob.getJobTitle());
                    intent.putExtra("companyName", selectedJob.getCompanyName());
                    intent.putExtra("jobDescription", selectedJob.getJobDescription());
                    intent.putExtra("degreeRequired", selectedJob.getDegreeRequired());
                    intent.putExtra("jobLocation", selectedJob.getJobLocation());
                    intent.putExtra("posterEmail", selectedJob.getPosterEmail());
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e("JobListActivity", "Error launching JobDetailsActivity", e);
                    Toast.makeText(JobListActivity.this, "Failed to open job details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add text change listener to the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterJobs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Fetch jobs from Firestore
        fetchJobsFromFirestore();
    }

    // Update the fetchJobsFromFirestore method to correctly retrieve jobTitle and companyName
    private void fetchJobsFromFirestore() {
        db.collection("jobPosts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        jobList.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String jobId = document.getId(); // Get the document ID
                            String jobTitle = document.getString("jobTitle");
                            String companyName = document.getString("companyName");
                            String jobDescription = document.getString("jobDescription");
                            String degreeRequired = document.getString("degreeRequired");
                            String jobLocation = document.getString("jobLocation");
                            String posterEmail = document.getString("posterEmail");

                            // Create a Job object with retrieved data
                            Job job = new Job(jobId, companyName, degreeRequired, jobDescription, jobLocation, jobTitle, posterEmail);
                            jobList.add(job);
                        }
                        filteredJobList.addAll(jobList);
                        adapter.notifyDataSetChanged();

                        // Show or hide the "No results" text view
                        if (filteredJobList.isEmpty()) {
                            textViewNoResults.setVisibility(View.VISIBLE);
                            listViewJobs.setVisibility(View.GONE);
                        } else {
                            textViewNoResults.setVisibility(View.GONE);
                            listViewJobs.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("JobListActivity", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void filterJobs(String query) {
        filteredJobList.clear();
        if (query.isEmpty()) {
            filteredJobList.addAll(jobList);
        } else {
            for (Job job : jobList) {
                if (job.getJobTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault())) ||
                        job.getCompanyName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredJobList.add(job);
                }
            }
        }
        adapter.notifyDataSetChanged();

        // Show or hide the "No results" text view
        if (filteredJobList.isEmpty()) {
            textViewNoResults.setVisibility(View.VISIBLE);
            listViewJobs.setVisibility(View.GONE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
            listViewJobs.setVisibility(View.VISIBLE);
        }
    }
}
