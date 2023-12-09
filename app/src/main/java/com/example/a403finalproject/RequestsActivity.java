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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestsActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    ListView lstRequests;
    ArrayList<WalkerRequest> requests;
    RequestAdaptor requestAdaptor;
    // Create an ArrayList to store ClientUsernames
    ArrayList<String> clientUsernames;
    ImageButton btnToAptFromReq, btnToPetsFromReq, btnToHomeFromReq, btnToProfileFromReq, btnToInfoFromReq;
    RequestQueue queue;
    String user;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);


        // Get user from shared preferences
         user = sharedPreferences.getString("username", "def");

        btnToPetsFromReq = findViewById(R.id.btnToPetsFromReq);
        btnToHomeFromReq = findViewById(R.id.btnToHomeFromReq);
        btnToProfileFromReq = findViewById(R.id.btnToProfileFromReq);
        btnToInfoFromReq = findViewById(R.id.btnToInfoFromReq);
        queue = Volley.newRequestQueue(this);

        getClientInfo();

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

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
    private void getClientInfo() {

        String apiUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetClientInfo";

        String walkerUsername = user;


       clientUsernames = new ArrayList<>();



        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("walker_username", user);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Process the JSON response
                            try {
                                JSONArray jsonArray = new JSONArray(response);


                                // Clear the ArrayList before populating
                                clientUsernames.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    String clientUsername = jsonArray.getString(i);
                                    clientUsernames.add(clientUsername);

                                }

                                // Log or use the ArrayList as needed
                                for (String clientUsername : clientUsernames) {
                                    Log.d("ClientUsername", clientUsername);
                                }
                                getData();
                                requestAdaptor = new RequestAdaptor(RequestsActivity.this,requests);
                                lstRequests = findViewById(R.id.lstRequests);
                                lstRequests.setAdapter(requestAdaptor);

                                // Now, clientUsernames ArrayList contains the ClientUsernames
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    // Add parameters to the request body
                    Map<String, String> params = new HashMap<>();
                    params.put("walker_username", walkerUsername);
                    return params;
                }
            };

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getData() {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllWalkers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Clear the requests ArrayList before populating
                        requests.clear();

                        for(int i = 0; i < response.length(); i++){


                            JSONObject categoryObj = response.getJSONObject(i);

                            // Log or use the ArrayList as needed
                            for (String clientUsername : clientUsernames) {
                                Log.d("ClientUsername", clientUsername);
                            }

                            String responseUsername = categoryObj.getString("username");
                            String phone =  categoryObj.getString("phone_number");
                            String email =categoryObj.getString("email");

                            // Log or use the ArrayList as needed
                            for (String clientUsername : clientUsernames) {
                                if (responseUsername.equals(clientUsername)){
                                    // Add each clientUsername to the ArrayList
                                    WalkerRequest wr = new WalkerRequest(clientUsername, clientUsername, clientUsername, phone, email);
                                    requests.add(wr);
                                }
                                    Log.d("ClientUsername", clientUsername);
                            }

                        }
                        requestAdaptor.notifyDataSetChanged(); // Notify adapter that data has changed

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD",error.toString()));

        queue.add(request);
    }

}