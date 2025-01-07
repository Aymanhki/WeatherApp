package com.professionalpractice.weatherapp;

import android.app.Application;
import com.professionalpractice.weatherapp.firebase.FirebaseHelper;

public class WeatherApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseHelper.initialize();
    }
}