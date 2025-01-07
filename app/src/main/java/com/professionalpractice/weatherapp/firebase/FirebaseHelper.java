package com.professionalpractice.weatherapp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;

    public static void initialize() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static DatabaseReference getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return mDatabase;
    }

    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    public static void signOut() {
        getAuth().signOut();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public static DatabaseReference getUserReference() {
        String userId = getCurrentUserId();
        return userId != null ? getDatabase().child("users").child(userId) : null;
    }

    public static DatabaseReference getSavedCitiesReference() {
        String userId = getCurrentUserId();
        return userId != null ? getDatabase().child("saved_cities").child(userId) : null;
    }
}