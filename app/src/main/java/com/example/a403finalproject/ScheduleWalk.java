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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_walk);

        txtHeader = findViewById(R.id.txtHeader);

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        Intent i = getIntent();

        walkerUsername = i.getStringExtra("WU");
        clientUsername = i.getStringExtra("CU");

        txtHeader.setText("Select a date that aligns with "+walkerUsername+"'s schedule:");

        Log.d("HESH",walkerUsername+" f "+clientUsername);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        cvDate = findViewById(R.id.cvDate);
        edTime = findViewById(R.id.edTime);
        rdoAM = findViewById(R.id.rdoAM);
        rdoPM = findViewById(R.id.rdoPM);
        requestQueue = Volley.newRequestQueue(this);


        rdoPM.setChecked(true);

        Calendar c = Calendar.getInstance();

        cvDate.setDate(c.getTimeInMillis());
        edTime.setText("12:01");

        cvDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String readableDate = getReadableDate(year, month, dayOfMonth);
            appointmentDate = readableDate;

        });


        btnConfirmBooking.setOnClickListener(e->{


            try {
                setAppointment();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }


        });


        rdoAM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If RadioButton 1 is checked, uncheck RadioButton 2
                if (isChecked) {
                    rdoPM.setChecked(false);
                }
            }
        });

        // Set a listener for RadioButton 2
        rdoPM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If RadioButton 2 is checked, uncheck RadioButton 1
                if (isChecked) {
                    rdoAM.setChecked(false);
                }
            }
        });



    }


    public void setAppointment() throws ParseException {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/InsertAppointment";

        //UTC: 2023-12-06 15:00:00

        if(checkTime()==true){
            // Prepare the JSON request data
            JSONObject requestData = new JSONObject();
            try {
                requestData.put("client_username", clientUsername);
                requestData.put("walker_username", walkerUsername);
                requestData.put("appointment_date", appointmentDate+" "+edTime.getText()+":00");
                String timeSplit[] = edTime.getText().toString().split(":");
                int endtime = Integer.parseInt(timeSplit[0]);
                endtime +=1;

                if(rdoPM.isChecked()){
                    endtime+=12;
                }


                timeVals();
                String appointmentEndTime;
                if(endtime>24){
                    endtime-=24;
                    appointmentEndTime = appointmentDate+" 0"+endtime+":"+timeSplit[1]+":00";

                }else{
                    appointmentEndTime = appointmentDate+" "+endtime+":"+timeSplit[1]+":00";

                }

                Log.d("HESH",walkerUsername+" "+clientUsername+" "+appointmentDate+" "+edTime.getText()+":00"+" "+appointmentEndTime);

                requestData.put("appointment_endtime", appointmentEndTime);
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

                        Intent i = new Intent(this,Profile_View.class);

                        startActivity(i);
                    },
                    error -> {
                        // Handle the error, e.g., log error or update UI
                        Log.d("HESH", "Error creating appointment: " + error.toString());
                        Toast.makeText(this,"appointment failed to create",Toast.LENGTH_SHORT);
                    });

            // Add the request to the request queue
            requestQueue.add(request);
        }else{
            Toast.makeText(this,"improper time entered",Toast.LENGTH_LONG);
            Log.d("HESH","IMPROPER TIME ENTERED");
        }


    }
    private String getReadableDate(int year, int month, int day) {
        month++;

        return String.format("%04d-%02d-%02d", year, month, day);
    }

    private Boolean checkTime(){
        boolean b = true;

        int hrs = Integer.parseInt(edTime.getText().toString().trim().split(":")[0]);

        if(edTime.getText().toString().trim().length()>=6){
            b = false;
        }

        if (edTime.getText().toString().trim().matches("[0-9:]+")) {
            b=true;
        }else{
            b=false;
        }

        if(hrs>12){
            b=false;
        }



        return b;
    }

    private void timeVals(){

        int hrs = Integer.parseInt(edTime.getText().toString().trim().split(":")[0]);

        if(rdoAM.isChecked()){

        } else if (rdoPM.isChecked()) {
            hrs+=12;
        }

        edTime.setText(hrs+":"+edTime.getText().toString().trim().split(":")[1]);
    }

}