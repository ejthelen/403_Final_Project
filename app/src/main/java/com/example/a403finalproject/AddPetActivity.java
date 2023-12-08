package com.example.a403finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPetActivity extends AppCompatActivity {
    EditText edPetName, edPetBreed, edPetDesc;
    Button btnAddPet, btnBackToPets;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    ActivityResultLauncher resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        // Get shared preferences
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);

        // Get username from preferences
        String username = sharedPreferences.getString("username","default_val");

        // Initialize queue
        queue = Volley.newRequestQueue(this);

        edPetName = findViewById(R.id.edPetName);
        edPetBreed = findViewById(R.id.edPetBreed);
        edPetDesc = findViewById(R.id.edPetDesc);
        btnBackToPets = findViewById(R.id.btnBackToPets);
        btnAddPet = findViewById(R.id.btnAddPet);
      
        // Set character limit for pet description using a filter
        int maxLong = 155;
        InputFilter[] filter1 = new InputFilter[1];
        filter1[0] = new InputFilter.LengthFilter(maxLong);
        edPetDesc.setFilters(filter1);

        // When button is clicked, use post request to add a pet to the database
        btnAddPet.setOnClickListener(e -> {
            // Create a newPet object for user
            // Get info from the edit texts
            JSONObject newPet = new JSONObject();
            try {
                newPet.put("person_username", username);
                newPet.put("pet_name", edPetName.getText() + "");
                newPet.put("pet_breed", edPetBreed.getText() + "");
                newPet.put("pet_description", edPetDesc.getText()+"");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            // URL for Create Pet
            String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/CreatePet";

            // Send a post request with the newPet
            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, newPet,
                    response -> {
                        // Handle successful update
                        Log.d("Update", "Data updated successfully");
                    },
                    error -> {
                        // Handle error
                        Log.e("Update", "Error updating data: " + error.toString());
                    });

            queue.add(updateRequest);

            // Go back to pets activity
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);
        });


        // Button to go back to pets page without adding pet
        btnBackToPets.setOnClickListener(e -> {
            Intent i = new Intent(this, PetsActivity.class);

            resultLauncher.launch(i);
        });

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}