package com.example.a403finalproject;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetsActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    ListView lstPets;
    ArrayList<Pet> pets;
    PetAdaptor adapter;

    Button btnAddPetPage;

    ImageButton btnToAptFromPets, btnToPetsFromPets, btnToHomeFromPets, btnToProfileFromPets, btnToInfoFromPets;

    RequestQueue queue;

    String user = "testuserstan";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        btnToAptFromPets = findViewById(R.id.btnToAptFromPets);
        btnToHomeFromPets = findViewById(R.id.btnToHomeFromPets);
        btnToProfileFromPets = findViewById(R.id.btnToProfileFromPets);
        btnToInfoFromPets = findViewById(R.id.btnToInfoFromPets);

        //        btnToAptFromPets.setOnClickListener(e -> {
//
//            Intent i = new Intent(this, Appointments.class);
//
//            resultLauncher.launch(i);
//        });

        btnToHomeFromPets.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);
            resultLauncher.launch(i);
        });

        btnToProfileFromPets.setOnClickListener(e -> {
            Intent i = new Intent(this, Profile_View.class);
            resultLauncher.launch(i);
        });

        btnToInfoFromPets.setOnClickListener(e -> {
            Intent i = new Intent(this, InfoScrolling.class);
            resultLauncher.launch(i);
        });

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(this);

        btnAddPetPage = findViewById(R.id.btnAddPetPage);

        // Will add actual stuff when the getPets is made
        pets = new ArrayList<>();
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllPets";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Go through each pet in the database
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject categoryObj = response.getJSONObject(i);

                            // Get username from database
                            String username = categoryObj.getString("person_username");

                            // Check if the dog is owned by user
                            if (username.matches(user)) {
                                Pet p = new Pet(categoryObj.getString("pet_name"), categoryObj.getString("pet_breed"), categoryObj.getString("pet_description"));
                                pets.add(p);

                            }
                        }

                        adapter = new PetAdaptor(this, pets);

                        lstPets = findViewById(R.id.lstPets);
                        lstPets.setAdapter(adapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD", error.toString()));

        queue.add(request);


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