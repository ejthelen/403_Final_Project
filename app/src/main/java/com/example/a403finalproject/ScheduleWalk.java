package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ScheduleWalk extends AppCompatActivity {

    Button btnConfirmBooking;
    CalendarView cvDate;
    EditText edTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_walk);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        cvDate = findViewById(R.id.cvDate);
        edTime = findViewById(R.id.edTime);

        Calendar c = Calendar.getInstance();

        cvDate.setDate(c.getTimeInMillis());
        edTime.setText("12:00");


        btnConfirmBooking.setOnClickListener(e->{

        });
    }
}