package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WalkerList extends AppCompatActivity {

    ListView lstWalkers;
    EditText edFilter;

    Button btnFilter;

    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        lstWalkers = findViewById(R.id.lstWalkers);
        edFilter = findViewById(R.id.edFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnLocation = findViewById(R.id.btnLocation);




    }
}