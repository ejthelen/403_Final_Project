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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class StartupLogin extends AppCompatActivity {
    Button btnSignUp, btnSignIn;

    TextView txtTestResponse;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    TextInputLayout textInputLayoutPassword,textInputLayoutUsername;
    TextInputEditText textInputEditTextPassword,textInputEditTextUsername;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_login);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        txtTestResponse = findViewById(R.id.txtTestResponse);
        textInputLayoutPassword = findViewById(R.id.txtInputLayout);
        textInputEditTextPassword = findViewById(R.id.txtInputEditText);

        textInputLayoutUsername = findViewById(R.id.txtInputLayout2);
        textInputEditTextUsername = findViewById(R.id.txtInputEditText2);

        requestQueue = Volley.newRequestQueue(this);

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
        btnSignIn.setOnClickListener(v->{
            login();
        });
    }
    private void login() {
        String username = Objects.requireNonNull(textInputEditTextUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(textInputEditTextPassword.getText()).toString().trim();

        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/IsValidPassword";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    // Handle the response as needed
                    try {
                        boolean isValid = response.getBoolean("IsValid");
                        // Set the response message to the textView
                        String message = isValid ? "Login successful" : "Login failed";
                        txtTestResponse.setText(message);
                        Log.d("Response", response.toString());

                        if (isValid) {
                            editor = sharedPreferences.edit();
                            Log.d("USERNAME", username);
                            editor.putString("username", username);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            startActivity(new Intent(StartupLogin.this, Profile_View.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Login Failed", "Error during failed login: " + error.toString(), error);
                    // Set an error message to the textView
                    txtTestResponse.setText("Login failed. Please try again.");
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginReq);
    }

}