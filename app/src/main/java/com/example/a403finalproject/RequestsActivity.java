package com.example.a403finalproject;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    ListView lstRequests;
    ArrayList<WalkerRequest> requests;
    RequestAdaptor adapter;
    ImageButton btnToAptFromReq, btnToPetsFromReq, btnToHomeFromReq, btnToProfileFromReq, btnToInfoFromReq;
    RequestQueue queue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);

        // Get user from shared preferences
        String user = sharedPreferences.getString("username", "def");

        btnToPetsFromReq = findViewById(R.id.btnToPetsFromReq);
        btnToHomeFromReq = findViewById(R.id.btnToHomeFromReq);
        btnToProfileFromReq = findViewById(R.id.btnToProfileFromReq);
        btnToInfoFromReq = findViewById(R.id.btnToInfoFromReq);

        // These image buttons will act as a navigation bar
        btnToPetsFromReq.setOnClickListener(e -> {
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);
        });

        btnToHomeFromReq.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);
            resultLauncher.launch(i);
        });

        btnToProfileFromReq.setOnClickListener(e -> {
            Intent i = new Intent(this, Profile_View.class);
            resultLauncher.launch(i);
        });

        btnToInfoFromReq.setOnClickListener(e -> {
            Intent i = new Intent(this, InfoScrolling.class);
            resultLauncher.launch(i);
        });

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(this);

        // Will hold a users requests
        requests = new ArrayList<>();

        JSONObject newWalker = new JSONObject();
        try {
            newWalker.put("walker_username", "mikasa");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // Update when api is made
        // Will get all of users requests
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetClientinfo";

//        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, newWalker,
//                response -> {
//                    try {
//                        // Go through each pet in the database
//                        for (int i = 0; i < response.length(); i++) {
//                            JsonSerializer categoryObj = response.getJSONObject(i);
//
//                            // Get username from database
//                            String username = categoryObj.getString("person_username");
//
//                            // Check if the request is owned by user and add request to list
//                                WalkerRequest p = new WalkerRequest(categoryObj.getString("req_username"), categoryObj.getString("req_name"), categoryObj.getString("walker_username"), categoryObj.getString("pet_name"), categoryObj.getString("phone_number"), categoryObj.getString("email"), categoryObj.getString("pet_description"));
//                                requests.add(p);
//
//                        }
//
//                        adapter = new RequestAdaptor(this, requests);
//
//                        lstRequests = findViewById(R.id.lstRequests);
//                        lstRequests.setAdapter(adapter);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                },
//                error -> Log.d("DDD", error.toString()));
//
//        queue.add(request);


        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}