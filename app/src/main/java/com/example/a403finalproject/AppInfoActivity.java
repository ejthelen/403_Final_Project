package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AppInfoActivity extends AppCompatActivity {

    TextView txtPurpose, txtInstructions, txtCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        txtPurpose = findViewById(R.id.txtPurpose);
        txtInstructions = findViewById(R.id.txtInstructions);
        txtCredits = findViewById(R.id.txtCredits);

        txtPurpose.setText("WagginTrail is a user-friendly app tailored for " +
                "connecting individuals seeking dog walks with experienced walkers, " +
                "particularly when owners are busy or away. Users can effortlessly " +
                "communicate by utilizing email addresses and phone numbers associated " +
                "with their individual profiles. For those in need of dog walking services," +
                " the app offers the convenience of searching for nearby walkers or " +
                "exploring them on a map view. Once a suitable walker is found, users can " +
                "easily schedule an appointment.");

        txtInstructions.setText(" 1. Update your profile on the profile page. Decide there is you would like ot become a walker. \n" +
                "2. Go to your home page to view walkers in your area. \n" +
                "3. Go to your pets page to view and add your pets. \n" +
                "4. Go to your appointments page to view the appointments that you have booked/appointments that you are booked for.");

        txtCredits.setText("How we got the images from::::");
    }
}