package com.example.a403finalproject;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PetsActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    ListView lstPets;
    ArrayList<Pet> pets;
    PetAdaptor adapter;

    Button btnAddPetPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        btnAddPetPage = findViewById(R.id.btnAddPetPage);

        // Will add actual stuff when the getPets is made
        pets = new ArrayList<>();
        pets.add(new Pet("HI","hi","hi"));


        adapter = new PetAdaptor(this,pets);

        lstPets = findViewById(R.id.lstPets);
        lstPets.setAdapter(adapter);

        btnAddPetPage.setOnClickListener(e -> {
            Intent i = new Intent(this, AddPetActivity.class);

            resultLauncher.launch(i);
        });

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}