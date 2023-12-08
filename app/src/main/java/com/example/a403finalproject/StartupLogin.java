package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class StartupLogin extends AppCompatActivity {
    public static final String TAG = "FinalProject";
    public static final int NOTIFICATION_REQUEST_CODE = 1;
    public static final String CHANNEL_ID = "111";

    public static final int NOTIFICATION_ID = 1;

    Button btnSignUp, btnSignIn;

    TextView txtTestResponse;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    TextInputLayout textInputLayoutPassword,textInputLayoutUsername;
    TextInputEditText textInputEditTextPassword,textInputEditTextUsername;
    SharedPreferences.Editor editor;
    String message;

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions NOT granted, requesting....");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Permissions already granted");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_login);
        checkPermissions();
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

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
                         message = isValid ? "Login successful" : "Login failed";


                        if (isValid) {
                            editor = sharedPreferences.edit();
                            Log.d("USERNAME", username);
                            editor.putString("username", username);
                            editor.putBoolean("isLoggedIn", true);

                            editor.apply();
                            Log.d("Response", response.toString() + ", " + message);
                            Toast toast = Toast.makeText(this,"Login successful", Toast.LENGTH_LONG);
                            toast.show();
                            startActivity(new Intent(StartupLogin.this, Profile_View.class));
                            finish();
                        }else {
                            Log.d("Response", response + ", " + message);

                            Toast toast = Toast.makeText(this,"Incorrect password", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error != null) {
                        if (((VolleyError) error).networkResponse != null) {
                            int statusCode = ((VolleyError) error).networkResponse.statusCode;

                            if (statusCode == 404) {
                                // Handle 404 error
                                Log.e("Volley Error", "Resource not found (404)");
                                Log.d("Response", error.toString() + ", " + message);
                                Toast toast = Toast.makeText(this,"Login failed", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                // Handle other errors
                                String errorMessage = new String(((VolleyError) error).networkResponse.data);
                                Log.e("Volley Error", errorMessage);
                                Log.d("Response", error.toString() + ", " + message);
                                Toast toast = Toast.makeText(this,"Login failed", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginReq);
    }

}