package com.example.a403finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                    ".{8,}" +               //at least 8 characters
                    "$");
    EditText etFirstName, etZipCode, etLastName,
            etEmail, etCountry, etPhone, etCity, etState, etAddress,
            etPassword, etShortDescription, etLongDescription, etSignUpUserName, etWalkRate;
    Button btnResgister, btnBackToLogin;
    RequestQueue requestQueue;
    TextView txtCheckStuff;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public double latitude;
    public double longitude;
    boolean exists = false;



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
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        etShortDescription = findViewById(R.id.etShortDescription);
        etLongDescription = findViewById(R.id.etLongDescription);
        etSignUpUserName = findViewById(R.id.etSignUpUserName);
        etCountry = findViewById(R.id.etCountry);
        requestQueue = Volley.newRequestQueue(this);
        txtCheckStuff = findViewById(R.id.txtCheckStuff);
        etZipCode = findViewById(R.id.etZipCode);
        etWalkRate = findViewById(R.id.etWalkRate);
        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        editor = sharedPreferences.edit();




        Places.initialize(getApplicationContext(), "AIzaSyB93L6kI1kDueyE7lBAJXBEMJqAzv-Ithw");


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.e("GOOGLE STUFF", "Error fetching place predictions: " + status);

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                String address = place.getName();
                String cityAddress = place.getAddress();
                LatLng latLng = place.getLatLng();
                // Get the address components
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Log.d("COORDINATES", "" + longitude + "," + latitude);

                // Check if address components are not null
                if (place.getAddressComponents() != null) {
                    // Extract components from the address
                    List<AddressComponent> addressComponents = place.getAddressComponents().asList();

                    String zipCode = getAddressComponent(addressComponents, "postal_code");
                    String city = getAddressComponent(addressComponents, "locality");
                    String country = getAddressComponent(addressComponents, "country");
                    String state = getAddressComponent(addressComponents, "administrative_area_level_1");

                    etAddress.setText(address);
                    etCity.setText(city);
                    etCountry.setText(country);
                    etState.setText(state);
                    etZipCode.setText(zipCode);

                    //Log users address
                    assert cityAddress != null;
                    Log.d("USER ADDRESS", cityAddress);


                } else {
                    Log.e("components are NULL", "NULL");
                }


            }

        });

        btnBackToLogin.setOnClickListener(e -> {
            Intent intent = new Intent(this, StartupLogin.class);
            startActivity(intent);
        });


        btnResgister.setOnClickListener((e -> {
            boolean isValidEmail = validateEmail();
            boolean isValidPassword = validatePassword();
            boolean isValidPhoneNumber = validatePhoneNumber();
            boolean isValidLongDesc = validateLongDescription();
            boolean isValidShortDesc = validateShortDescription();
            boolean isValidWalkRate = validateWalkRate();

            if (isValidEmail && isValidPassword && isValidPhoneNumber && isValidLongDesc && isValidShortDesc && isValidWalkRate) {
               setUser();
            }
        }));




    }

    // this method returns a part of the address depending on its type
    private String getAddressComponent(List<AddressComponent> addressComponents, String type) {
        for (AddressComponent component : addressComponents) {
            for (String componentType : component.getTypes()) {
                if (componentType.equals(type)) {
                    Log.d("COMPONENTS", component.toString());
                    return component.getName();
                }
            }
        }
        return null;
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
        String zipcode = etZipCode.getText().toString().trim();



        String postal_address = address + "," + state + ","+zipcode+"," + country;

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
            requestData.put("zip_code", zipcode);
            requestData.put("walk_rate", 0.0);
            requestData.put("is_walker", false);
            requestData.put("short_description", short_description);
            requestData.put("long_description", long_description);
            requestData.put("password", password);
            requestData.put("username", username);
            requestData.put("longitude", longitude);
            requestData.put("latitude", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON exception
        }

        // Create a new JsonObjectRequest with POST method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                response -> {
                    try {
                        String message = response.getString("response");
                        //the response returned will say whether or not the username is taken or not
                        if ("User Already Exists".equals(message)) {
                            Toast.makeText(this, "This username is already taken. Enter another username", Toast.LENGTH_SHORT).show();
                            Log.d("USERNAME ALREADY EXISTS", username + " is taken");


                            exists = false;
                        } else {
                            // Handle the response and create the user is the username isnt taken
                            Log.d("SetUser", "Response: " + response.toString());
                            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                           //save the users username in shared preferences from sign up
                            Log.d("USERNAME", username);
                            editor.putString("username", username);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            //take the user to their profile
                            Intent intent = new Intent(this, Profile_View.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        // Handle JSON exception
                        Log.e("UserSetError", "Error parsing JSON response: " + e.toString());
                        Toast.makeText(this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle the error, e.g., log error or update UI
                    Log.e("UserSetError", "Error creating user: " + error.toString());
                    Toast.makeText(this, "User failed to create", Toast.LENGTH_SHORT).show();
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

    public boolean validateWalkRate(){
        String walkRate = etWalkRate.getText().toString().trim();
        if(walkRate.isEmpty()){
        etWalkRate.setError("Field can't be empty");
        return false;
    }
        return true;
}

    public boolean validateLongDescription(){
        String longDescription = etLongDescription.getText().toString().trim();
        if(longDescription.isEmpty()){
            etLongDescription.setError("Field can't be empty");
            return false;
        }
        return true;
    }
    public boolean validateShortDescription(){
        String shortDescription = etShortDescription.getText().toString().trim();
        if(shortDescription.isEmpty()){
            etShortDescription.setError("Field can't be empty");
            return false;
        }
        return true;
    }



}