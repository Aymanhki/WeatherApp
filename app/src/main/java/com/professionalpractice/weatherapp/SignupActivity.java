package com.professionalpractice.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.professionalpractice.weatherapp.databinding.SignupLayoutBinding;
import com.professionalpractice.weatherapp.firebase.FirebaseHelper;
import com.professionalpractice.weatherapp.model.User;
import com.professionalpractice.weatherapp.toast.Toaster;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private SignupLayoutBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        firebaseAuth = FirebaseHelper.getAuth();

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Sign up button click
        binding.signupBtn.setOnClickListener(v -> registerUser());

        // Login text click
        binding.loginTv.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String firstName = binding.firstNameEt.getText().toString().trim();
        String lastName = binding.lastNameEt.getText().toString().trim();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        // Validate input
        if (firstName.isEmpty()) {
            binding.firstNameEt.setError("First name is required");
            binding.firstNameEt.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            binding.lastNameEt.setError("Last name is required");
            binding.lastNameEt.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            binding.emailEt.setError("Email is required");
            binding.emailEt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.passwordEt.setError("Password is required");
            binding.passwordEt.requestFocus();
            return;
        }

        // Show progress
        showProgress(true);

        // Create user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgress(false);
                    if (task.isSuccessful()) {
                        // Registration successful
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName)
                                    .build();

                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            User user = new User(firebaseUser.getUid(), firstName, lastName, email);
                                            FirebaseHelper.getUserReference().setValue(user)
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                                            finish();
                                                        } else {
                                                            Toaster.errorToast(SignupActivity.this, "Failed to save user data: " + task2.getException().getMessage());
                                                        }
                                                    });
                                        } else {
                                            Toaster.errorToast(SignupActivity.this, "Failed to update profile: " + task1.getException().getMessage());
                                        }
                                    });
                        }
                    } else {
                        Toaster.errorToast(SignupActivity.this, "Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showProgress(boolean show) {
        binding.signupBtn.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}