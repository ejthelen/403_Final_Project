package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    //Regex expression to validation password. Must have atleast 1 lowercase letter, 1 uppercase, and atleast 1 digit
    // and must be a atleast 8 chraacters long
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8sdfdsfds characters
                    "$");
    EditText etFirstName, etLastName, etEmail,etCountry, etPhone, etCity, etState, etAddress, etPassword,etShortDescription,etLongDescription,etSignUpUserName;
    Button btnResgister;
    RequestQueue requestQueue;
    TextView txtCheckStuff;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Call the findById method for each EditText
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etSignUpEmail);
        etPhone = findViewById(R.id.etSignUpPhoneNumber);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etAddress = findViewById(R.id.etStreetAddy);
        etPassword = findViewById(R.id.etPasswordSignUp);
        btnResgister = findViewById(R.id.btnSignUpForeal);
        etShortDescription = findViewById(R.id.etShortDescription);
        etLongDescription = findViewById(R.id.etLongDescription);
        etSignUpUserName = findViewById(R.id.etSignUpUserName);
        etCountry = findViewById(R.id.etCountry);
        requestQueue = Volley.newRequestQueue(this);
        txtCheckStuff = findViewById(R.id.txtCheckStuff);





        btnResgister.setOnClickListener((e -> {
            boolean isValidEmail = validateEmail();
            boolean isValidPassword = validatePassword();
            boolean isValidPhoneNumber = validatePhoneNumber();

            if (isValidEmail && isValidPassword&&isValidPhoneNumber) {
                setUser();
            }
        }));



    }

    //validates email address based off the regex expression in the Patterns class
    public boolean validateEmail() {
        String emailInput = etEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            etEmail.setError("Field cant be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.setError("Enter a valid email address");
            return false;
        }
        return true;

    }

    //validates password based off PASSWORD_PATTERN regex
    public boolean validatePassword() {
        String password = etPassword.getText().toString().trim();
        System.out.println("Password: " + password); // Check the value

        if (password.isEmpty()) {
            etPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            etPassword.setError("Password is too weak");
            return false;
        }
        return true;
    }
    public void setUser() {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/CreateUser";
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String short_description = etShortDescription.getText().toString().trim();
        String long_description = etLongDescription.getText().toString().trim();
        String username = etSignUpUserName.getText().toString().trim();
        // Prepare the JSON request data
        JSONObject requestData = new JSONObject();
        try {

            requestData.put("first_name", firstName);
            requestData.put("last_name", lastName);
            requestData.put("phone_number", phone);
            requestData.put("email", email);
            requestData.put("street_address", address);
            requestData.put("city", city);
            requestData.put("state", state);
            requestData.put("country", country);
            requestData.put("walk_rate", 0.0);
            requestData.put("short_description", short_description);
            requestData.put("long_description", long_description);
            requestData.put("password", password);
            requestData.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON exception
        }

        // Create a new JsonObjectRequest with POST method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                response -> {
                    // Handle the response, e.g., log success or update UI
                    Log.d("SetUser", "Response: " + response.toString());
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle the error, e.g., log error or update UI
                    Log.e("UserSetError", "Error creating user: " + error.toString());
                    Toast.makeText(this,"User failed to create",Toast.LENGTH_SHORT);
                });

        // Add the request to the request queue
        requestQueue.add(request);
    }
    public boolean validatePhoneNumber(){
        String phoneNumber = etPhone.getText().toString().trim();
        if (phoneNumber.isEmpty()){
            etPhone.setError("Field can't be empty");
            return false;
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            etPhone.setError("Invalid phone number");
            return false;
        }
        return true;
    }




}