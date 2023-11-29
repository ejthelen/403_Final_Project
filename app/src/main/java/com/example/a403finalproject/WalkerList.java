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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalkerList extends AppCompatActivity {

    ListView lstWalkers;
    EditText edFilter;

    Button btnFilter;
    WalkerAdapter adapter;
    ArrayList<Walker> walker;

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

        queue = Volley.newRequestQueue(this);

        walker = new ArrayList<>();

        //Walker f = new Walker("john","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1);

        //walker.add(new Walker("1","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1));
        //walker.add(new Walker("2","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1));
        //walker.add(new Walker("3","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1));
        //walker.add(new Walker("4","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1));
        walker.add(new Walker("5","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes t asdfajk;lsdf;lkasjfkl;djasl;kfjsal;kdfjsalkdjfasl;kdjfl;kasjdflsdafdalkjfjsdakl;fjasl;kdjfakl;sjfasl;kdjfalsk;djfalk;sjdfklasdjfl;kaskjfl;kasdjfal;skdjflak;sdjfl;kasjdfa;kasdjfklwneqtjbghbuiobcvbxo walk","989 989 8998","a@aol.com",45.75,88.75,1));

        getData();
        adapter = new WalkerAdapter(this,walker);

        lstWalkers.setAdapter(adapter);


    }

    public void getData() {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetActiveWalkers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {

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
                            Walker w = new Walker();
                            w.setTUID(tuId);
                            w.setfName(firstName);
                            w.setlName(lastName);
                            w.setPhoneNumber(phoneNumber);
                            w.setEmail(email);
                            w.setAddress(streetAddress);
                            w.setCity(city);
                            w.setState(state);
                            w.setCountry(country);
                            w.setWalker(isWalker);
                            w.setWalkRate(walkRate);
                            w.setsDesc(shortDescription);
                            w.setlDesc(longDescription);
                            //walker.setPassword(password);
                            //walker.setUsername(username);

                            w.setLatitude(latitude);
                            w.setLongitude(longitude);

                            walker.add(w);
                        }

                        // Process the list of walkers as needed
                        // Note: You may want to update the UI or perform other actions with the walker data.

                    } catch (JSONException e) {
                        Log.e("Error", "Error parsing JSON response", e);
                    }
                },
                error -> Log.e("Error", "Error fetching data: " + error));

        // Add the request to the request queue
        queue.add(request);
    }




}