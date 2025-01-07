package com.professionalpractice.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.professionalpractice.weatherapp.toast.Toaster;


import com.google.firebase.auth.FirebaseAuth;
import com.professionalpractice.weatherapp.databinding.LoginLayoutBinding;
import com.professionalpractice.weatherapp.firebase.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        firebaseAuth = FirebaseHelper.getAuth();

        // Check if user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Login button click
        binding.loginBtn.setOnClickListener(v -> loginUser());

        // Sign up text click
        binding.signupTv.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        // Forgot password text click
        binding.forgotPasswordTv.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void loginUser() {
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        // Validate input
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

        // Authenticate user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgress(false);
                    if (task.isSuccessful()) {
                        // Login successful
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {

                        Toaster.errorToast(LoginActivity.this, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showProgress(boolean show) {
        binding.loginBtn.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}