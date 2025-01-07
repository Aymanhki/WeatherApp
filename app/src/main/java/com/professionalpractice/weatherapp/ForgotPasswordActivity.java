package com.professionalpractice.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.professionalpractice.weatherapp.databinding.ForgotPasswordBinding;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.professionalpractice.weatherapp.firebase.FirebaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ForgotPasswordBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseHelper.getAuth();
        setupClickListeners();
        binding.loginTv.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void setupClickListeners() {
        binding.resetPasswordBtn.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.emailEt.getText().toString().trim();

        if (email.isEmpty()) {
            binding.emailEt.setError("Email is required");
            binding.emailEt.requestFocus();
            return;
        }

        showProgress(true);

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgress(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    private void showProgress(boolean show) {
        binding.resetPasswordBtn.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}