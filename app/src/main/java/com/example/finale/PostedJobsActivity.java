package com.example.finale;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostedJobsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostedJobsAdapter adapter;
    private List<Job> jobPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_jobs);

        recyclerView = findViewById(R.id.recycler_view_posted_jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobPosts = new ArrayList<>();
        adapter = new PostedJobsAdapter(jobPosts, position -> deleteJob(position));
        recyclerView.setAdapter(adapter);

        fetchPostedJobs();
    }

    private void fetchPostedJobs() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            CollectionReference jobsRef = FirebaseFirestore.getInstance().collection("jobPosts");

            Query query = jobsRef.whereEqualTo("posterEmail", userEmail);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    jobPosts.clear(); // Clear existing data
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String companyName = document.getString("companyName");
                        String degreeRequired = document.getString("degreeRequired");
                        String jobDescription = document.getString("jobDescription");
                        String jobLocation = document.getString("jobLocation");
                        String jobTitle = document.getString("jobTitle");
                        String posterEmail = document.getString("posterEmail");

                        Job job = new Job(id, companyName, degreeRequired, jobDescription, jobLocation, jobTitle, posterEmail);
                        jobPosts.add(job);
                    }
                    adapter.notifyDataSetChanged();

                    // Show toast if no jobs are posted
                    if (jobPosts.isEmpty()) {
                        showToast("You haven't posted any jobs.");
                    }
                } else {
                    showToast("Failed to fetch posted jobs.");
                }
            });
        }
    }

    private void deleteJob(int position) {
        Job jobToDelete = jobPosts.get(position);
        FirebaseFirestore.getInstance().collection("jobPosts").document(jobToDelete.getId()).delete();
        jobPosts.remove(position);
        adapter.notifyItemRemoved(position);
        showToast("Job deleted successfully");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
