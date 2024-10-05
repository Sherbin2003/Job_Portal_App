package com.example.finale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SearchJob extends AppCompatActivity {

    EditText editTextSearchJobTitle, editTextSearchJobLocation, editTextSearchCompany, editTextSearchDegree;
    Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);

        // Initialize views
        editTextSearchJobTitle = findViewById(R.id.editTextSearchJobTitle);
        editTextSearchJobLocation = findViewById(R.id.editTextSearchJobLocation);
        editTextSearchCompany = findViewById(R.id.editTextSearchCompany);
        editTextSearchDegree = findViewById(R.id.editTextSearchDegree);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Set click listener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform validation before proceeding with the search
                if (validateInput()) {
                    // If validation passes, navigate to JobListActivity and pass search criteria
                    navigateToJobList();
                }
            }
        });
    }

    private boolean validateInput() {
        // Check if at least one field is filled
        if (TextUtils.isEmpty(editTextSearchJobTitle.getText().toString().trim()) &&
                TextUtils.isEmpty(editTextSearchJobLocation.getText().toString().trim()) &&
                TextUtils.isEmpty(editTextSearchCompany.getText().toString().trim()) &&
                TextUtils.isEmpty(editTextSearchDegree.getText().toString().trim())) {

            // If no field is filled, show a toast message
            Toast.makeText(this, "Please enter at least one search criteria", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateToJobList() {
        // Navigate to JobListActivity
        Intent intent = new Intent(SearchJob.this, JobListActivity.class);
        // Start the JobListActivity
        startActivity(intent);
    }
}
