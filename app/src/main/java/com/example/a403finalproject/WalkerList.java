package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalkerList extends AppCompatActivity {

    ListView lstWalkers;
    EditText edFilter;

    Button btnFilter;

    Button btnLocation;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        lstWalkers = findViewById(R.id.lstWalkers);
        edFilter = findViewById(R.id.edFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnLocation = findViewById(R.id.btnLocation);




    }

    public void getData() {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllWalkers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Walker> walkers = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject walkerObj = response.getJSONObject(i);

                            // Parse walker details
                            int tuId = walkerObj.getInt("TuID");
                            String firstName = walkerObj.getString("first_name");
                            String lastName = walkerObj.getString("last_name");
                            String phoneNumber = walkerObj.getString("phone_number");
                            String email = walkerObj.getString("email");
                            String streetAddress = walkerObj.getString("street_address");
                            String city = walkerObj.getString("city");
                            String state = walkerObj.getString("state");
                            String country = walkerObj.getString("country");
                            boolean isWalker = walkerObj.getBoolean("is_walker");
                            double walkRate = walkerObj.getDouble("walk_rate");
                            String shortDescription = walkerObj.getString("short_description");
                            String longDescription = walkerObj.getString("long_description");
                            String password = walkerObj.getString("password");
                            String username = walkerObj.getString("username");

                            double latitude = walkerObj.getDouble("latitude");
                            double longitude = walkerObj.getDouble("longitude");

                            // Create a Walker object
                            Walker walker = new Walker();
                            walker.setTUID(tuId);
                            walker.setfName(firstName);
                            walker.setlName(lastName);
                            walker.setPhoneNumber(phoneNumber);
                            walker.setEmail(email);
                            walker.setAddress(streetAddress);
                            walker.setCity(city);
                            walker.setState(state);
                            walker.setCountry(country);
                            walker.setWalker(isWalker);
                            walker.setWalkRate(walkRate);
                            walker.setsDesc(shortDescription);
                            walker.setlDesc(longDescription);
                            //walker.setPassword(password);
                            //walker.setUsername(username);

                            walker.setLatitude(latitude);
                            walker.setLongitude(longitude);

                            walkers.add(walker);
                        }

                        // Process the list of walkers as needed
                        // Note: You may want to update the UI or perform other actions with the walker data.

                    } catch (JSONException e) {
                        Log.e("Error", "Error parsing JSON response", e);
                    }
                },
                error -> Log.e("Error", "Error fetching data: " + error.toString()));

        // Add the request to the request queue
        queue.add(request);
    }




}