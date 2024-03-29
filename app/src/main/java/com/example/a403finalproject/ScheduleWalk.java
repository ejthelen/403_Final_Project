package com.example.a403finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleWalk extends AppCompatActivity {

    //initialize variables.
    Button btnConfirmBooking;
    CalendarView cvDate;
    EditText edTime;

    RadioButton rdoAM, rdoPM;
    RequestQueue requestQueue;

    String walkerUsername, clientUsername;
    String appointmentDate;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView txtHeader;


    //oncreate load data.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_walk);

        //get header text.
        txtHeader = findViewById(R.id.txtHeader);

        //get preferences and pull values.
        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        Intent i = getIntent();

        walkerUsername = i.getStringExtra("WU");
        clientUsername = i.getStringExtra("CU");

        Log.d("HESH", walkerUsername + " f " + clientUsername);

        txtHeader.setText("Select a date that aligns with "+walkerUsername+"'s schedule:");


        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        cvDate = findViewById(R.id.cvDate);
        edTime = findViewById(R.id.edTime);
        rdoAM = findViewById(R.id.rdoAM);
        rdoPM = findViewById(R.id.rdoPM);
        requestQueue = Volley.newRequestQueue(this);

        //default PM to be checked off.
        rdoPM.setChecked(true);

        //set default calendar values.
        Calendar c = Calendar.getInstance();

        cvDate.setDate(c.getTimeInMillis());
        edTime.setText("12:01");

        //on cvDate value being edited apply necessary values.
        cvDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String readableDate = getReadableDate(year, month, dayOfMonth);
            appointmentDate = readableDate;
        });


        //on btnconfirm booking being selected.
        btnConfirmBooking.setOnClickListener(e -> {
            //UTC: 2023-12-06 15:00:00

            //check if time value is valid.
            if (checkTime() == true) {
                // Prepare the JSON request data
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("walker_username", walkerUsername + "");
                    requestData.put("client_username", clientUsername + "");
                    requestData.put("appointment_date", appointmentDate + " " + edTime.getText() + ":0");
                    String timeSplit[] = edTime.getText().toString().split(":");
                    int endtime = Integer.parseInt(timeSplit[0]);
                    endtime += 1;

                    //add time for UTC standard.
                    if (rdoPM.isChecked()) {
                        endtime += 12;
                    }

                    //perform time editing.
                    timeVals();
                    String appointmentEndTime;
                    //if > 24 reset back to 1
                    if (endtime > 24) {
                        endtime -= 24;
                        appointmentEndTime = appointmentDate + " 0" + endtime + ":" + timeSplit[1] + ":0";

                    } else {
                        appointmentEndTime = appointmentDate + " " + endtime + ":" + timeSplit[1] + ":0";

                    }




                    requestData.put("appointment_endtime", appointmentEndTime + "");
                    requestData.put("status", "Scheduled");


                } catch (JSONException ex) {
                    ex.printStackTrace();
                    // Handle JSON exception
                }

                String makeAppUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/InsertAppointment";

                // Create a new JsonObjectRequest with POST method
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, makeAppUrl, requestData,
                        response -> {
                            try {
                                // Handle the response, e.g., log success or update UI
                                Toast.makeText(this, "appointment created successfully", Toast.LENGTH_SHORT).show();
                            } catch (Exception excep) {
                                Log.d("ERROr", ":(");
                            }
                        },
                        error -> {
                            // Handle the error, e.g., log error or update UI
                            Toast.makeText(this, "appointment failed to create", Toast.LENGTH_SHORT);
                        });

                // Add the request to the request queue
                requestQueue.add(request);
            } else {
                Toast.makeText(this, "improper time entered", Toast.LENGTH_LONG);
                Log.d("HESH", "IMPROPER TIME ENTERED");
            }


        });


        //flip flop on rdos.
        rdoAM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rdoPM.setChecked(false);
                }
            }
        });
        rdoPM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rdoAM.setChecked(false);
                }
            }
        });


    }


    public void setAppointment() throws ParseException {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/InsertAppointment";

        //UTC: 2023-12-06 15:00:00

        if (checkTime() == true) {
            // Prepare the JSON request data
            JSONObject requestData = new JSONObject();
            try {
                requestData.put("walker_username", walkerUsername + "");
                requestData.put("client_username", clientUsername + "");
                requestData.put("appointment_date", appointmentDate + " " + edTime.getText() + ":0");
                String timeSplit[] = edTime.getText().toString().split(":");
                int endtime = Integer.parseInt(timeSplit[0]);
                endtime += 1;

                if (rdoPM.isChecked()) {
                    endtime += 12;
                }

                timeVals();
                String appointmentEndTime;
                if (endtime > 24) {
                    endtime -= 24;
                    appointmentEndTime = appointmentDate + " 0" + endtime + ":" + timeSplit[1] + ":0";

                } else {
                    appointmentEndTime = appointmentDate + " " + endtime + ":" + timeSplit[1] + ":0";

                }


                requestData.put("appointment_endtime", appointmentEndTime + "");
                requestData.put("status", "Scheduled");


            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON exception
            }

            // Create a new JsonObjectRequest with POST method
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                    response -> {
                        // Handle the response, e.g., log success or update UI
                        Log.d("HESH", "Response: " + response.toString());
                        Toast.makeText(this, "appointment created successfully", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(this, Profile_View.class);

                        startActivity(i);
                    },
                    error -> {
                        // Handle the error, e.g., log error or update UI
                        Log.d("HESH", "Error creating appointment: " + error.toString());
                        Toast.makeText(this, "appointment failed to create", Toast.LENGTH_SHORT);
                    });

            // Add the request to the request queue
            requestQueue.add(request);
        } else {
            Toast.makeText(this, "improper time entered", Toast.LENGTH_LONG);
            Log.d("HESH", "IMPROPER TIME ENTERED");
        }


    }

    //returns UTC formatted date.
    private String getReadableDate(int year, int month, int day) {
        month++;

        return String.format("%04d-%02d-%02d", year, month, day);
    }

    //checks if the time is valid by seeing if hour entered is <= 12 and if char's are valid
    private Boolean checkTime() {
        boolean b = true;

        int hrs = Integer.parseInt(edTime.getText().toString().trim().split(":")[0]);

        if (edTime.getText().toString().trim().length() >= 6) {
            b = false;
        }

        if (edTime.getText().toString().trim().matches("[0-9:]+")) {
            b = true;
        } else {
            b = false;
        }

        if (hrs > 12) {
            b = false;
        }


        return b;
    }

    //apply time changes for PM as UTC uses 24 hour clock.
    private void timeVals() {

        int hrs = Integer.parseInt(edTime.getText().toString().trim().split(":")[0]);

        if (rdoAM.isChecked()) {

        } else if (rdoPM.isChecked()) {
            hrs += 12;
        }

        edTime.setText(hrs + ":" + edTime.getText().toString().trim().split(":")[1]);
    }

}