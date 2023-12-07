package com.example.a403finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

public class AppInfoActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    TextView txtPurpose, txtInstructions, txtCredits;

    ImageButton btnToAptFromInfo, btnToPetsFromInfo, btnToHomeFromInfo, btnToProfileFromInfo, btnToInfoFromInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        btnToAptFromInfo = findViewById(R.id.btnToAptFromInfo);
        btnToHomeFromInfo = findViewById(R.id.btnToHomeFromInfo);
        btnToProfileFromInfo = findViewById(R.id.btnToProfileFromInfo);
        btnToPetsFromInfo = findViewById(R.id.btnToPetsFromInfo);

        btnToAptFromInfo.setOnClickListener(e -> {

            Intent i = new Intent(this, ScheduleWalk.class);

            resultLauncher.launch(i);
        });

        btnToHomeFromInfo.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);
            resultLauncher.launch(i);
        });

        btnToPetsFromInfo.setOnClickListener(e -> {
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);
        });

        btnToProfileFromInfo.setOnClickListener(e -> {
            Intent i = new Intent(this, Profile_View.class);
            resultLauncher.launch(i);
        });

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

        txtCredits.setText("Appointment page icon made by Freepik from www.flaticon.com\n" +
                "Pets page icon made by Freepik from www.flaticon.com\n" +
                "Home page icon made by Kiranshastry from www.flaticon.com\n" +
                "Profile page icon made by Freepik from www.flaticon.com\n" +
                "Info page icon made by Freepik from www.flaticon.com\n");

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }


}