package com.example.finale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class JobDetailsActivity extends AppCompatActivity {

    TextView jobTitle, companyName, jobDescription, degreeRequired, jobLocation;
    Button registerButton, btnSelectResume;
    String jobId, posterEmail;

    // Define popup dimensions constants
    private static final int POPUP_WIDTH_DP = 300; // in dp
    private static final int POPUP_HEIGHT_DP = 400; // in dp
    private static final int PICK_PDF_REQUEST = 1;
    private Uri resumeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        // Initialize views
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        jobDescription = findViewById(R.id.jobDescription);
        degreeRequired = findViewById(R.id.degreeRequired);
        jobLocation = findViewById(R.id.jobLocation);
        registerButton = findViewById(R.id.registerButton);

        // Get data from intent
        Intent intent = getIntent();
        jobId = intent.getStringExtra("jobId");
        String title = intent.getStringExtra("jobTitle");
        String company = intent.getStringExtra("companyName");
        String description = intent.getStringExtra("jobDescription");
        String degree = intent.getStringExtra("degreeRequired");
        String location = intent.getStringExtra("jobLocation");

        // Set data to views
        jobTitle.setText(title);
        companyName.setText(company);
        jobDescription.setText(description);
        degreeRequired.setText(degree);
        jobLocation.setText(location);

        // Retrieve the job poster's email from Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("jobPosts").document(jobId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    posterEmail = document.getString("posterEmail");
                } else {
                    Toast.makeText(JobDetailsActivity.this, "No such job found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(JobDetailsActivity.this, "Failed to retrieve job details!", Toast.LENGTH_SHORT).show();
            }
        });

        // Register Button click listener
        registerButton.setOnClickListener(v -> showPopupRegistrationForm());
    }

    private void showPopupRegistrationForm() {
        // Inflate the popup_registration_form.xml layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_registration_form, null);

        // Initialize a new instance of PopupWindow
        int width = convertDpToPixels(POPUP_WIDTH_DP);
        int height = convertDpToPixels(POPUP_HEIGHT_DP);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // Set an elevation for the popup window
        popupWindow.setElevation(10);

        // Set a solid background color for the popup window
        popupView.setBackgroundResource(R.drawable.glassy_background);

        // Find views in the popup layout
        EditText etName = popupView.findViewById(R.id.etName);
        EditText etEmail = popupView.findViewById(R.id.etEmail);
        EditText etPhone = popupView.findViewById(R.id.etPhone);
        EditText etDegree = popupView.findViewById(R.id.etDegree);
        EditText etSkills = popupView.findViewById(R.id.etSkills);
        EditText etExperience = popupView.findViewById(R.id.etExperience);
        Button btnApply = popupView.findViewById(R.id.btnApply);
        btnSelectResume = popupView.findViewById(R.id.btnSelectResume);

        // Apply button click listener
        btnApply.setOnClickListener(v -> {
            // Get values from EditText fields
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String degree = etDegree.getText().toString();
            String skills = etSkills.getText().toString();
            String experience = etExperience.getText().toString();

            // Validate email format
            if (!isValidEmail(email)) {
                etEmail.setError("Invalid email");
                return;
            }

            // Send email function with resume attachment
            sendEmail(name, email, phone, degree, skills, experience);

            // Dismiss the popup window
            popupWindow.dismiss();

            // Show confirmation toast
            Toast.makeText(JobDetailsActivity.this, "Application submitted!", Toast.LENGTH_SHORT).show();
            NotificationHelper.createNotificationChannel(JobDetailsActivity.this,name);
            NotificationHelper.showNotification(JobDetailsActivity.this,"You have successfully registered for the role " + jobTitle.getText().toString() + " in " + companyName.getText().toString(),name);
        });

        // Select Resume Button click listener
        btnSelectResume.setOnClickListener(v -> pickResumeFile());

        // Show the popup window at a centered location on the screen
        popupWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.CENTER, 0, 0);
    }

    private void sendEmail(String name, String email, String phone, String degree, String skills, String experience) {
        // Construct the email message
        String subject = "Job Application for " + jobTitle.getText().toString() + " at " + companyName.getText().toString();
        String message = "Dear " + posterEmail + "!\n\n" +
                "I would like to apply for the role of " + jobTitle.getText().toString() + " at your company, " +
                companyName.getText().toString() + ".\n\n" +
                "Here are my details:\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Degree: " + degree + "\n" +
                "Skills: " + skills + "\n" +
                "Experience: " + experience + "\n\n" +
                "Thank you for considering my application.\n\n" +
                "Best regards,\n" +
                name;

        // Create an implicit intent to send email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf"); // Set the MIME type for PDF files
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{posterEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Attach the resume URI
        if (resumeUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, resumeUri);
        }

        // Start an activity with the intent to send an email
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void pickResumeFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Resume"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            resumeUri = data.getData();
            btnSelectResume.setText("Resume Selected");
            btnSelectResume.setEnabled(false);
            // Darken the background color of the button
            btnSelectResume.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_shadow_end_color)); // Replace with your desired color resource
            // Optionally, you can also change the text color to ensure visibility
            btnSelectResume.setTextColor(getResources().getColor(android.R.color.white)); // Replace with your desired color resource
        }
    }

    private int convertDpToPixels(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
