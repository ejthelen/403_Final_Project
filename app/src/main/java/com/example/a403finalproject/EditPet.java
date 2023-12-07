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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tuid = bundle.getInt("pet_tuid");
        owner = bundle.getString("pet_owner");

        Log.d("PASSED PET ID", tuid + " " + owner);
        getData();

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

            Intent i = new Intent(this, PetsActivity.class);

            resultLauncher.launch(i);
            queue.add(updateRequest);
        });

        btnDeletePet.setOnClickListener(e -> {
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
            Intent i = new Intent(this, PetsActivity.class);

            resultLauncher.launch(i);
        });

        // Button to go back to pets page
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

    public void getData(){
        /** Here ya go I passed the username from login  **/
//        String username = sharedPreferences.getString("username","default_val");
//        Log.d("USERNAME IN PROFILE","PASSED FROM LOGIN: " + username);

        Intent intent = getIntent();

// Retrieve the int value from the bundle

        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllPets";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for(int i = 0; i < response.length(); i++){
                            JSONObject categoryObj = response.getJSONObject(i);
                            int id = categoryObj.getInt("TuID");

                            Log.d("PET ID", id + " for chip");
                            if (id == tuid) {
                                Log.d("UHH", id + " for chip");
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