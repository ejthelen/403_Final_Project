package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WalkerProfile extends AppCompatActivity {

    TextView eduserFirstName, eduserLastName, eduserMail, eduserNumber, eduserShort, eduserLong, eduserRate;
    Button btnMakeRequest, btnBackToWalkerList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_profile);
        queue = Volley.newRequestQueue(this);


        eduserFirstName = findViewById(R.id.eduserFirstName);
        eduserLastName = findViewById(R.id.eduserLastName);
        eduserMail = findViewById(R.id.eduserMail);
        eduserNumber = findViewById(R.id.eduserNumber);
        eduserShort = findViewById(R.id.eduserShort);
        eduserLong = findViewById(R.id.eduserLong);
        eduserRate = findViewById(R.id.eduserRate);

        // Get bundle data from pet activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        eduserFirstName.setText("First name: "+ bundle.getString("first"));
        eduserLastName.setText("Last name: "+ bundle.getString("last"));
        eduserMail.setText("Email: "+ bundle.getString("email"));
        eduserNumber.setText("Phone number: "+ bundle.getString("number"));
        eduserShort.setText("Short description: "+ bundle.getString("short"));
        eduserLong.setText("Long description: "+ bundle.getString("long"));
        eduserRate.setText("Walking rate: "+ bundle.getString("rate"));

        btnMakeRequest = findViewById(R.id.btnMakeRequest);
        btnBackToWalkerList = findViewById(R.id.btnBackToWalkerList);

        String walkerusername = bundle.getString("walker_username");
        String clientusername = bundle.getString("client_username");

        btnBackToWalkerList.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);
            startActivity(i);
        });

        btnMakeRequest.setOnClickListener(e -> {
            JSONObject newReq = new JSONObject();
            try {
                newReq.put("walker_username", walkerusername);
                newReq.put("client_username", clientusername);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            Log.d("request", newReq +"");
            // URL for Create Request
            String requestURL = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/SetRequest";

            // Send a post request with the newPet
            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, requestURL, newReq,
                    response -> {
                        // Handle successful update
                        Log.d("Update", "Data updated successfully");
                    },
                    error -> {
                        // Handle error
                        Log.e("Update", "Error updating data: " + error.toString());
                    });

            queue.add(updateRequest);
            Intent i = new Intent(this, WalkerList.class);
            startActivity(i);
        });

    }
}