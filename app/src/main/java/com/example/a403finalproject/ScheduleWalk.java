package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.time.LocalDateTime;
import java.util.Calendar;

public class ScheduleWalk extends AppCompatActivity {

    Button btnConfirmBooking;
    CalendarView cvDate;
    EditText edTime;

    RadioButton rdoAM, rdoPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_walk);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        cvDate = findViewById(R.id.cvDate);
        edTime = findViewById(R.id.edTime);
        rdoAM = findViewById(R.id.rdoAM);
        rdoPM = findViewById(R.id.rdoPM);

        rdoPM.setChecked(true);

        Calendar c = Calendar.getInstance();

        cvDate.setDate(c.getTimeInMillis());
        edTime.setText("12:01");


        btnConfirmBooking.setOnClickListener(e->{

        });


        rdoAM.setOnCheckedChangeListener((buttonView, isChecked) -> rdoPM.setChecked(false));

        rdoPM.setOnCheckedChangeListener((buttonView, isChecked) -> rdoAM.setChecked(false));



    }


}