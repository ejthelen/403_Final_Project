package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile_View extends AppCompatActivity {

    Switch swActivate;
    EditText edFirstName, edLastName, edNumber, edMail, edShort, edLong;
    SeekBar skRate;
    Button btnUpdate;
    RequestQueue queue;

    int tuid = 9;
    String first = "";
    String last = "";
    String number = "";
    String email = "";
    String shortDes = "";
    String longDes = "";
    double walkRate = 0;

    TextView txtRate, txtRateChange;

    // Boolean value to check if walking account is active
    static boolean walkingStatus;

    // Will fill profile page with user information from database
    public void getData(){
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetAllWalkers";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for(int i = 0; i < response.length(); i++){
                            JSONObject categoryObj = response.getJSONObject(i);
                            int id = categoryObj.getInt("TuID");

                            if (id == tuid) {
                                Log.d("UHH", id + "");
                                edFirstName.setText(categoryObj.getString("first_name"));
                                edLastName.setText(categoryObj.getString("last_name"));
                                edNumber.setText(categoryObj.getString("phone_number"));
                                edMail.setText(categoryObj.getString("email"));
                                edShort.setText(categoryObj.getString("short_description"));
                                edLong.setText(categoryObj.getString("long_description"));
                                skRate.setProgress((int)categoryObj.getDouble("walk_rate"));
                                Log.d("gt", email);

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
        Log.d("gt", email);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(this);

        // Call methods to get user data from database,
        // change text edits to allow user to change profile based on if their account is active,
        // and check is the account is active or not
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
            // Toast
            // Make toast depending on whether account is active or not
            String activeStatus;

            if (walkingStatus) {
                activeStatus = "active";
            } else {
                activeStatus = "inactive";
            }

            Toast.makeText(Profile_View.this, "Your profile was updated, and your walking account is now " + activeStatus, Toast.LENGTH_SHORT).show();


            // Change walking active status and update database info about user profile
            JSONObject updatedData = new JSONObject();
            try {
                updatedData.put("first_name", edFirstName.getText() + "");
                updatedData.put("last_name", edLastName.getText() + "");
                updatedData.put("phone_number", edNumber.getText() + "");
                updatedData.put("email", edMail.getText()+"");
                updatedData.put("street_address", "");
                updatedData.put("city", "");
                updatedData.put("state", "");
                updatedData.put("country", "");
                updatedData.put("walk_rate", (double)skRate.getProgress());
                updatedData.put("is_walker", walkingStatus);
                updatedData.put("short_description", edShort.getText() + "");
                updatedData.put("long_description", edLong.getText() + "");
                updatedData.put("password", "");
                updatedData.put("username", "testuserbob");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/UpdateUser";

            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, updatedData,
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

}