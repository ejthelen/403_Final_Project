package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StartupLogin extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    EditText etPassword, etUserName;
    TextView txtTestResponse;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_login);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        etPassword = findViewById(R.id.editTextTextPassword);
        etUserName = findViewById(R.id.etUserName);
        txtTestResponse = findViewById(R.id.txtTestResponse);
        //String url = "";

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);

        });
        btnSignIn.setOnClickListener(v->{
            login();
        });

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//
//            txtTestResponse.setText(response);
//
//
//
//
//        }, error -> {
//            txtTestResponse.setText(txtTestResponse.toString());
//
//        });
//
//
//        btnSignUp.setOnClickListener(v->{
//            requestQueue.add(stringRequest);
//
//        });
//    }




//        public void setUser () {
//            String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/CreateUser";
//
//            // Prepare the JSON request data
//            JSONObject requestData = new JSONObject();
//            try {
//                requestData.put("first_name", "stan");
//                requestData.put("last_name", "lee");
//                requestData.put("phone_number", "123-456-7890");
//                requestData.put("email", "john.doe@example.com");
//                requestData.put("street_address", "123 Main St");
//                requestData.put("city", "Cityville");
//                requestData.put("state", "ST");
//                requestData.put("country", "Countryland");
//                requestData.put("walk_rate", 0.0);
//                requestData.put("short_description", "Test user");
//                requestData.put("long_description", "This is a test user for Postman");
//                requestData.put("password", "password1234");
//                requestData.put("username", "testuserstan");
//            } catch (JSONException e) {
//                e.printStackTrace();
//                // Handle JSON exception
//            }
//
//            // Create a new JsonObjectRequest with POST method
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
//                    response -> {
//                        // Handle the response, e.g., log success or update UI
//                        Log.d("UserSet", "User created successfully");
//                    },
//                    error -> {
//                        // Handle the error, e.g., log error or update UI
//                        Log.e("UserSetError", "Error creating user: " + error.toString());
//                    });
//
//            // Add the request to the request requestQueue
//            requestQueue.add(request);
//        }
    }
    private void login() {
        String username = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            Log.d("volley stuff", jsonBody.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String url="https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/IsValidPassword";

        JsonObjectRequest loginReq = new JsonObjectRequest(
                Request.Method.POST, url,
                jsonBody,
                response -> {
                    try {

                        boolean success = response.getBoolean("success");
                        if (success) {
                            //
                            String token = response.getString("token");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.apply();

                            startActivity(new Intent(StartupLogin.this, Profile_View.class));
                            finish();
                        } else {
                            Log.d("Login Failed", "Login failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d("Login Failed", "Error during failed login"));
        requestQueue.add(loginReq);

    }
}