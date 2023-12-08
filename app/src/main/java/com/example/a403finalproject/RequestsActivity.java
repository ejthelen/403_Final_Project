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
import com.android.volley.toolbox.Volley;

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

        String user = sharedPreferences.getString("username", "def");
//        Log.d("USERNAME IN PROFILE","PASSED FROM LOGIN: " + user);


        btnToPetsFromReq = findViewById(R.id.btnToPetsFromReq);
        btnToHomeFromReq = findViewById(R.id.btnToHomeFromReq);
        btnToProfileFromReq = findViewById(R.id.btnToProfileFromReq);
        btnToInfoFromReq = findViewById(R.id.btnToInfoFromReq);

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

        // Will add actual stuff when the getPets is made
        requests = new ArrayList<>();
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetRequests";

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
                                WalkerRequest p = new WalkerRequest(categoryObj.getString("req_username"), categoryObj.getString("req_name"), categoryObj.getString("walker_username"), categoryObj.getString("pet_name"), categoryObj.getString("phone_number"), categoryObj.getString("email"));
                                requests.add(p);

                            }
                        }

                        adapter = new RequestAdaptor(this, requests);

                        lstRequests = findViewById(R.id.lstRequests);
                        lstRequests.setAdapter(adapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD", error.toString()));

        queue.add(request);


        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}