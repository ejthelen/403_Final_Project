package com.example.a403finalproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.a403finalproject.databinding.ActivityInfoScrollingBinding;

public class InfoScrolling extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    private ActivityInfoScrollingBinding binding;
    ImageButton btnToAptFromInfo, btnToPetsFromInfo, btnToHomeFromInfo, btnToProfileFromInfo, btnToInfoFromInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInfoScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnToAptFromInfo = findViewById(R.id.btnToAptFromInfo);
        btnToHomeFromInfo = findViewById(R.id.btnToHomeFromInfo);
        btnToProfileFromInfo = findViewById(R.id.btnToProfileFromInfo);
        btnToPetsFromInfo = findViewById(R.id.btnToPetsFromInfo);

        btnToAptFromInfo.setOnClickListener(e -> {
            Intent i = new Intent(this, RequestsActivity.class);
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


        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}