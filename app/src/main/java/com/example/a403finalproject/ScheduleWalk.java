package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    String appointmentTime;

    String status;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_walk);

        preferences = getSharedPreferences("SETTINGS",MODE_PRIVATE);
        editor = preferences.edit();

        walkerUsername = preferences.getString("WU","");
        clientUsername = preferences.getString("CU","");

        Log.d("HESH",walkerUsername+" "+clientUsername);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        cvDate = findViewById(R.id.cvDate);
        edTime = findViewById(R.id.edTime);
        rdoAM = findViewById(R.id.rdoAM);
        rdoPM = findViewById(R.id.rdoPM);
        requestQueue = Volley.newRequestQueue(this);

        walkerUsername="";
        clientUsername="";

        rdoPM.setChecked(true);

        Calendar c = Calendar.getInstance();




        btnConfirmBooking.setOnClickListener(e->{
            cvDate.setDate(c.getTimeInMillis());
            edTime.setText("12:01");

            try {
                setAppointment();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });


        rdoAM.setOnCheckedChangeListener((buttonView, isChecked) -> rdoPM.setChecked(false));

        rdoPM.setOnCheckedChangeListener((buttonView, isChecked) -> rdoAM.setChecked(false));



    }


    public void setAppointment() throws ParseException {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/InsertAppointment";
        String status = "Scheduled";

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String utcDateStr = sdf.format(cvDate.getDate());

        utcDateStr +=" "+edTime.getText().toString().trim()+":00";

        Log.d("HESH",utcDateStr+"");

        //UTC: 2023-12-06 15:00:00


        // Prepare the JSON request data
        JSONObject requestData = new JSONObject();
        try {

            requestData.put("walker_username", walkerUsername);
            requestData.put("client_username", clientUsername);
            requestData.put("appointment_date", appointmentDate);



            requestData.put("appointment_endtime", appointmentDate);

            requestData.put("status", "Scheduled");

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON exception
        }

        // Create a new JsonObjectRequest with POST method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                response -> {
                    // Handle the response, e.g., log success or update UI
                    //Log.d("setAppointment", "Response: " + response.toString());
                    //Toast.makeText(this, "appointment created successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle the error, e.g., log error or update UI
                    //Log.e("setAppointmentERROR", "Error creating appointment: " + error.toString());
                    //Toast.makeText(this,"appointment failed to create",Toast.LENGTH_SHORT);
                });

        // Add the request to the request queue
        requestQueue.add(request);

    }

}