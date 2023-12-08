package com.example.a403finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPet extends AppCompatActivity {
    RequestQueue queue;
    ActivityResultLauncher resultLauncher;
    EditText edPetNameED, edPetBreedED, edPetDescED;
    Button btnDeletePet, btnUpdatePet, btnBackToPetsED;

    // String that will hold the pets owner and int value for pet
    // Both passed from bundle in pet activity
    String owner;
    int tuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        edPetBreedED = findViewById(R.id.edPetBreedED);
        edPetDescED = findViewById(R.id.edPetDescED);
        edPetNameED = findViewById(R.id.edPetNameED);

        btnUpdatePet = findViewById(R.id.btnUpdatePet);
        btnDeletePet = findViewById(R.id.btnDeletePet);
        btnBackToPetsED = findViewById(R.id.btnBackToPetsED);

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(this);

        // Get bundle data from pet activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tuid = bundle.getInt("pet_tuid");
        owner = bundle.getString("pet_owner");

        // Get pet data using method
        getData();

        // When button is clicked grab data from edit texts and add them to a JSONObject
        btnUpdatePet.setOnClickListener(e -> {
            JSONObject updatedData = new JSONObject();
            try {
                updatedData.put("TuID",tuid);
                updatedData.put("pet_name", edPetNameED.getText() + "");
                updatedData.put("pet_breed", edPetBreedED.getText() + "");
                updatedData.put("pet_description", edPetDescED.getText()+"");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            // Make request to update a pet using the created pet object
            String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/UpdatePet";

            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, updatedData,
                    response -> {
                        // Handle successful update
                        Log.d("Update", "Data updated successfully");
                    },
                    error -> {
                        // Handle error
                        Log.e("Update", "Error updating data: " + error.toString());
                    });

            queue.add(updateRequest);
            // Go back to pet activity
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);

        });

        // When button is clicked, the pet will be removed from the database
        btnDeletePet.setOnClickListener(e -> {
            // Create object using the tuid of the pet user wishes to delete
            JSONObject deletePet = new JSONObject();
            try {
                deletePet.put("TuID", tuid);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/DeletePet";

            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, deletePet,
                    response -> {
                        // Handle successful update
                        Log.d("Delete", "Data deleted successfully");
                    },
                    error -> {
                        // Handle error
                        Log.e("Delete", "Error deleted data: " + error.toString());
                    });

            queue.add(updateRequest);

            // Go back to pet activity
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);
        });

        // Button to go back to pets page without editing pet
        btnBackToPetsED.setOnClickListener(e -> {
            Intent i = new Intent(this, PetsActivity.class);

            resultLauncher.launch(i);
        });

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }

    // Get pet data using a GET request
    public void getData(){
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllPets";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for(int i = 0; i < response.length(); i++){
                            JSONObject categoryObj = response.getJSONObject(i);
                            int id = categoryObj.getInt("TuID");

                            // If the tuids match, you have the correct pet
                            if (id == tuid) {
                                edPetNameED.setText(categoryObj.getString("pet_name"));
                                edPetBreedED.setText(categoryObj.getString("pet_breed"));
                                edPetDescED.setText(categoryObj.getString("pet_description"));
                            }

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD",error.toString()));

        queue.add(request);

    }
}