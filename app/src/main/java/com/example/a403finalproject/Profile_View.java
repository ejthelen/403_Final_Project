package com.example.a403finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;

// Checking that new cloned repo works

public class Profile_View extends AppCompatActivity {
    public static final int NOTIFICATION_REQUEST_CODE = 1;
    public static final String CHANNEL_ID = "111";
    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "FinalProject";
    ActivityResultLauncher resultLauncher;
    Switch swActivate;
    EditText edFirstName, edLastName, edNumber, edMail, edShort, edLong,
    etCountry2, etCity2, etState2, etAddress2;
    ImageButton btnToAptFromProfile, btnToPetsFromProfile, btnToHomeFromProfile, btnToProfileFromProfile, btnToInfoFromProfile;

    SeekBar skRate;
    Button btnUpdate;
    Button btnLogOut;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public double latitude;
    public double longitude;

    static int tuid;

    String username;

    TextView txtRate, txtRateChange;

    // Boolean value to check if walking account is active
    static boolean walkingStatus;


    String activeStatus;

    // Will fill profile page with user information from database
    public void getData(){
        //get the user's username they logged in with
        username = sharedPreferences.getString("username","default_val");
        Log.d("USERNAME IN PROFILE","PASSED FROM LOGIN: " + username);

        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllWalkers";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for(int i = 0; i < response.length(); i++){
                            JSONObject categoryObj = response.getJSONObject(i);

                            int id = categoryObj.getInt("TuID");
                            editor.putInt("userTUID", id);

                            // Set preferences for lat and lon of user
                            editor.putFloat("lat", Float.parseFloat(categoryObj.getString("latitude")));
                            editor.putFloat("lon", Float.parseFloat(categoryObj.getString("longitude")));
                            editor.apply();

                            String serverUsername = categoryObj.getString("username");
                            Log.d("SERVER-NAME",serverUsername);

                            if (serverUsername.equals(username)) {
                                Log.d("UHH", id + "");
                                edFirstName.setText(categoryObj.getString("first_name"));
                                edLastName.setText(categoryObj.getString("last_name"));
                                edNumber.setText(categoryObj.getString("phone_number"));
                                edMail.setText(categoryObj.getString("email"));
                                edShort.setText(categoryObj.getString("short_description"));
                                edLong.setText(categoryObj.getString("long_description"));
                                skRate.setProgress((int)categoryObj.getDouble("walk_rate"));
                                etAddress2.setText(categoryObj.getString("street_address"));
                                etCity2.setText(categoryObj.getString("city"));
                                etState2.setText(categoryObj.getString("state"));
                                etCountry2.setText(categoryObj.getString("country"));

                                boolean isWalking = categoryObj.getBoolean("is_walker");

                                if (isWalking) {
                                    swActivate.setChecked(true);
                                } else {
                                    swActivate.setChecked(false);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD",error.toString()));

        queue.add(request);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        createNotificationChannel();
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putFloat("lat", (float)latitude);
        editor.putFloat("lon", (float)longitude);
        editor.apply();

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(this);
        getData();

        btnLogOut = findViewById(R.id.btnLogOut);
        btnToAptFromProfile = findViewById(R.id.btnToAptFromProfile);
        btnToHomeFromProfile = findViewById(R.id.btnToHomeFromProfile);
        btnToPetsFromProfile = findViewById(R.id.btnToPetsFromProfile);
        btnToInfoFromProfile = findViewById(R.id.btnToInfoFromProfile);

        etCity2 = findViewById(R.id.etCity2);
        etState2 = findViewById(R.id.etState2);
        etAddress2 = findViewById(R.id.etStreetAddy2);
        etCountry2 = findViewById(R.id.etCountry2);

        // This is where the user will search for with address and the text views will be filled in
        Places.initialize(getApplicationContext(), "AIzaSyB93L6kI1kDueyE7lBAJXBEMJqAzv-Ithw");
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

        assert autocompleteFragment != null;
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

                    // Set the text views
                    etAddress2.setText(address);
                    etCity2.setText(city);
                    etCountry2.setText(country);
                    etState2.setText(state);

                    //Log users address
                    assert cityAddress != null;
                    Log.d("USER ADDRESS", cityAddress);


                } else {
                    Log.e("components are NULL", "NULL");
                }


            }

        });

        btnToAptFromProfile.setOnClickListener(e -> {

            Intent i = new Intent(this, RequestsActivity.class);

            resultLauncher.launch(i);
        });

        btnToHomeFromProfile.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);

            SharedPreferences p = getSharedPreferences("HESH",MODE_PRIVATE);
            SharedPreferences.Editor ed = p.edit();

            ed.putString("uname",username);
            ed.apply();
            resultLauncher.launch(i);
        });

        btnToPetsFromProfile.setOnClickListener(e -> {
            Intent i = new Intent(this, PetsActivity.class);
            resultLauncher.launch(i);
        });

        btnToInfoFromProfile.setOnClickListener(e -> {
            Intent i = new Intent(this, InfoScrolling.class);
            resultLauncher.launch(i);
        });

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
        btnLogOut.setOnClickListener(e->{
            logout();
        });

        //populate the users data when onCreate is called
        getData();
        checkStatus();
        activeStatus(swActivate);

        swActivate = findViewById(R.id.swActivate);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Progress Bar implementation Logic for walking rate
        skRate.setProgress(100);
        txtRateChange.setText(skRate.getProgress() + "");
        skRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtRateChange.setText(i + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // When button is clicked let use know
        // remove from database or add to database that they are walker
        btnUpdate.setOnClickListener(v ->{
            if (walkingStatus) {
                activeStatus = "active";
            } else {
                activeStatus = "inactive";
            }
            updateProfile();
            editor.putFloat("lat", (float)latitude);
            editor.putFloat("lon", (float)longitude);
            editor.apply();

            JSONObject newReq = new JSONObject();
            try {
                newReq.put("walker_username", "mikasa");
                newReq.put("client_username", "hrmox");

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
        });
    }

    // Use this method to check if the user is an active walker
    // Check the switch
    public void checkStatus() {
        swActivate = findViewById(R.id.swActivate);
        if (swActivate.isChecked()) {
            Log.d("HERE", "Active");
            walkingStatus = true;
        } else {
            Log.d("HERE", "Inactive");
            walkingStatus = false;
        }
    }

    // When switch is clicked, the edit text boxes will be turn editable or uneditable
    public void activeStatus(View v) {
        swActivate = findViewById(R.id.swActivate);
        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edNumber = findViewById(R.id.edNumber);
        edMail = findViewById(R.id.edMail);
        edShort = findViewById(R.id.edShort);
        edLong = findViewById(R.id.edLong);
        txtRate = findViewById(R.id.txtRate);
        txtRateChange = findViewById(R.id.txtRateChange);
        skRate = findViewById(R.id.skRate);

        // If the walking account is active, user should be able to edit profile
        // Else, user will be unable to edit their profile
        if (!walkingStatus) {
            walkingStatus = true;
            swActivate.setText("Active");
            edFirstName.setEnabled(true);
            edLastName.setEnabled(true);
            edNumber.setEnabled(true);
            edMail.setEnabled(true);
            edShort.setEnabled(true);
            edLong.setEnabled(true);
            txtRate.setEnabled(true);
            txtRateChange.setEnabled(true);
            skRate.setEnabled(true);
        } else {
            walkingStatus = false;
            swActivate.setText("Inactive");
            edFirstName.setEnabled(false);
            edLastName.setEnabled(false);
            edNumber.setEnabled(false);
            edMail.setEnabled(false);
            edShort.setEnabled(false);
            edLong.setEnabled(false);
            txtRate.setEnabled(false);
            txtRateChange.setEnabled(false);
            skRate.setEnabled(false);
        }
    }
    public void logout(){
        //clear the users data and change their login status to false
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.remove(username);
        Log.d("LOGOUT","User logged out");
        editor.apply();

        Intent intent = new Intent(this,StartupLogin.class);
        startActivity(intent);
        finish();
    }
    public void updateProfile(){
        // Get the user's username they logged in with
        username = sharedPreferences.getString("username","default_val").trim();
        Log.d("USERNAME IN PROFILE","PASSED FROM LOGIN: " + username);


        // Change walking active status and update database info about user profile
        JSONObject updatedData = new JSONObject();
        try {
            updatedData.put("first_name", edFirstName.getText() + "");
            updatedData.put("last_name", edLastName.getText() + "");
            updatedData.put("phone_number", edNumber.getText() + "");
            updatedData.put("email", edMail.getText()+"");
            updatedData.put("street_address", etAddress2.getText() +"");
            updatedData.put("city", etCity2.getText() +"");
            updatedData.put("state", etState2.getText()+"");
            updatedData.put("country", etCountry2.getText()+"");
            updatedData.put("walk_rate", (double)skRate.getProgress());
            updatedData.put("is_walker", walkingStatus);
            Log.d("WALKRATE", skRate.getProgress() + "");
            updatedData.put("short_description", edShort.getText() + "");
            updatedData.put("long_description", edLong.getText() + "");
            updatedData.put("password", "");
            updatedData.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Handle post request here
        String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/UpdateUser";

        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, updatedData,
                response -> {

                    try {
                        String message = response.getString("response");
                        if (message.equals("User updated successfully.")) {
                            // Handle successful update
                            Log.d("Update", "Data updated successfully" + " " + response.toString());
                            displayNotification();
                            Toast.makeText(Profile_View.this, "Your profile was updated, and your walking account is now " + activeStatus, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Update", "Data couldnt be updated" + " " + response.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Log.e("Update", "Didnt UPDATE: " + error.toString());
                    Toast.makeText(Profile_View.this, "An error has occurred while updating", Toast.LENGTH_SHORT).show();
                });

        queue.add(updateRequest);

    }

    // This method returns a part of the address depending on its type
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

    public void createNotificationChannel() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notify App Main Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }
        Log.d(TAG, "Main Channel created");
    }
    public void displayNotification() {
        Notification notification = new NotificationCompat.Builder(Profile_View.this,CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("You have made changes to your profile")
                .setDeleteIntent(setOnDismissAction())
                .build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(NOTIFICATION_ID,notification);
    }
    public PendingIntent setOnTapAction(){
        Intent i = new Intent(this,SignUp.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                i,
                PendingIntent.FLAG_IMMUTABLE
        );
        return pendingIntent;
    }

    public PendingIntent setOnDismissAction(){
        Intent i = new Intent("dismiss_broadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                NOTIFICATION_ID,
                i,
                PendingIntent.FLAG_IMMUTABLE
        );
        return pendingIntent;
    }

    public NotificationCompat.Action setOnReplyAction(){
        Intent i = new Intent(this,SignUp.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                i,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Action action = new NotificationCompat.Action(
                android.R.drawable.btn_minus,
                "REPLY",
                pendingIntent
        );

        return action;



    }
    public static int getTuid() {
        return tuid;
    }

}