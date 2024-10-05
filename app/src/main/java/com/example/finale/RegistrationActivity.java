package com.example.finale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextName, editTextEmail, editTextSkill, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewRegistrationStatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);  // Make sure this layout file name is correct

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSkill = findViewById(R.id.editTextSkill);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewRegistrationStatus = findViewById(R.id.textViewRegistrationStatus);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String skill = editTextSkill.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || skill.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            textViewRegistrationStatus.setText("Please fill out all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            textViewRegistrationStatus.setText("Passwords do not match.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = mAuth.getCurrentUser();
                        textViewRegistrationStatus.setText("Registration successful! User ID: " + user.getUid());

                        // Navigate back to MainActivity
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Finish the current activity so the user can't navigate back to it
                    } else {
                        // If sign in fails, display a message to the user.
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            textViewRegistrationStatus.setText("This email address is already in use.");
                        } else {
                            textViewRegistrationStatus.setText("Authentication failed: " + task.getException().getMessage());
                        }
                    }
                });
    }
}
