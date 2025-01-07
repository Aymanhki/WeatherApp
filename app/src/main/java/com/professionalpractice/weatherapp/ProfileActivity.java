package com.professionalpractice.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.professionalpractice.weatherapp.firebase.FirebaseHelper;
import com.professionalpractice.weatherapp.model.User;
import com.professionalpractice.weatherapp.databinding.ProfileLayoutBinding;
import android.content.Intent;
import android.view.View;



public class ProfileActivity extends AppCompatActivity {

    private ProfileLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.logoutBtn.setOnClickListener(this::logout);
        FirebaseUser currentUser = FirebaseHelper.getCurrentUser();

        if (currentUser != null) {
            binding.displayNameTv.setText("Display Name: " + currentUser.getDisplayName());
            binding.emailTv.setText("Email: " + currentUser.getEmail());
            binding.idTv.setText("ID: " + currentUser.getUid());
            binding.firstNameTv.setText("First Name: " + currentUser.getDisplayName().split(" ")[0]);
            binding.lastNameTv.setText("Last Name: " + currentUser.getDisplayName().split(" ")[1]);
        }

        binding.GoToHomeBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });
    }


    public void logout(View view) {
        FirebaseHelper.signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }



}
