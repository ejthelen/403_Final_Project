package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalkerList extends AppCompatActivity {

    ListView lstWalkers;
    EditText edFilter;

    Button btnFilter;
    WalkerAdapter adapter;
    ArrayList<Walker> walker;

    Button btnLocation;
    RequestQueue queue;

    SharedPreferences sp;
    String spTAG = "LOCATION";

    Context context;

    ConstraintLayout clFilter;
    SeekBar sbDistance;
    SeekBar sbSpeed;
    TextView edDistance;
    TextView edSpeed;
    EditText edMaxPrice;

    Walker w;

    static String username;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        sp = getSharedPreferences("HESH",MODE_PRIVATE);
        //editor = sp.edit();


        username = sp.getString("uname","err");
        SharedPreferences.Editor ed = sp.edit();


        Log.d("HESH",username+"");

        lstWalkers = findViewById(R.id.lstWalkers);
        edFilter = findViewById(R.id.edFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnLocation = findViewById(R.id.btnLocation);
        sbDistance = findViewById(R.id.sbDistance);
        sbSpeed = findViewById(R.id.sbSpeed);
        edDistance = findViewById(R.id.edDistance);
        edSpeed = findViewById(R.id.edSpeed);
        clFilter = findViewById(R.id.clFilter);
        edMaxPrice = findViewById(R.id.edMaxPrice);

        queue = Volley.newRequestQueue(this);

        context = this;

        walker = new ArrayList<>();

        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetActiveWalkers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject walkerObj = response.getJSONObject(i);

                            // Parse walker details
                            int tuId = walkerObj.getInt("TuID");
                            String firstName = walkerObj.getString("first_name");
                            String lastName = walkerObj.getString("last_name");
                            String phoneNumber = walkerObj.getString("phone_number");
                            String email = walkerObj.getString("email");
                            String streetAddress = walkerObj.getString("street_address");
                            String city = walkerObj.getString("city");
                            String state = walkerObj.getString("state");
                            String country = walkerObj.getString("country");
                            boolean isWalker = walkerObj.getBoolean("is_walker");
                            double walkRate = walkerObj.getDouble("walk_rate");
                            String shortDescription = walkerObj.getString("short_description");
                            String longDescription = walkerObj.getString("long_description");
                            String password = walkerObj.getString("password");
                            String username = walkerObj.getString("username");

                            double latitude = walkerObj.getDouble("latitude");
                            double longitude = walkerObj.getDouble("longitude");

                            // Create a Walker object
                            Walker w = new Walker();
                            w.setTUID(tuId);
                            w.setfName(firstName);
                            w.setlName(lastName);
                            w.setPhoneNumber(phoneNumber);
                            w.setEmail(email);
                            w.setAddress(streetAddress);
                            w.setCity(city);
                            w.setState(state);
                            w.setCountry(country);
                            w.setWalker(isWalker);
                            w.setWalkRate(walkRate);
                            w.setsDesc(shortDescription);
                            w.setlDesc(longDescription);
                            //walker.setPassword(password);
                            w.setUserName(username);

                            w.setLatitude(latitude);
                            w.setLongitude(longitude);

                            walker.add(w);
                        }

                        // Process the list of walkers as needed
                        // Note: You may want to update the UI or perform other actions with the walker data.

                    } catch (JSONException e) {
                        Log.e("Error", "Error parsing JSON response", e);
                    }
                },
                error -> Log.e("Error", "Error fetching data: " + error));


        // Add the request to the request queue
        queue.add(request);
        try {
            // Sleep for 3 seconds (3000 milliseconds)
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int j = 0;j<walker.size();j++){
            if(walker.get(j).getTUID()==Profile_View.getTuid()){
                w = walker.get(j);
            }
        }

        adapter = new WalkerAdapter(this,walker);

        lstWalkers.setAdapter(adapter);


        adapter.notifyDataSetChanged();

        lstWalkers.setTextFilterEnabled(true);

        if (Objects.isNull(getIntent()) == false) {
            Intent mapRecievedIntent = getIntent();
            int walkerID = mapRecievedIntent.getIntExtra("walker_ID", -1);
        }

        btnLocation.setOnClickListener(e->{
            Intent i = new Intent(this, Walker_Map.class);
            ArrayList<String> walkerArrayList = new ArrayList<>();

            Bundle bundle = new Bundle();

            for (Walker walker1 : walker) {
                walkerArrayList.add(walker1.getfName() + "," + walker1.getlName() + "," +
                        walker1.getUserName() + "," +
                        walker1.getAddress() + "," + walker1.getCity() + "," + walker1.getState() + "," +
                        walker1.getCountry() + "," + walker1.isWalker() + "," + walker1.getWalkRate() + "," +
                        walker1.getsDesc() + "," + walker1.getCharge() + "," +walker1.getlDesc() + "," +
                        walker1.getPhoneNumber() +  "," +walker1.getEmail() + "," + walker1.getLatitude() + "," +
                        walker1.getLongitude() + "," + walker1.getTUID());
            }
            bundle.putStringArrayList("walkerArrayList", walkerArrayList);

            i.putStringArrayListExtra("walkerArrayList", walkerArrayList);
            startActivity(i);
        });


        clFilter.setMaxHeight(0);
        btnFilter.setOnClickListener(e->{

            for(int i = 0;i<walker.size();i++){
                //Log.d("HESH",walker.get(i).getUserName()+"");
            }
            if(clFilter.getMaxHeight()==1440){
                clFilter.setMaxHeight(0);

            }else{
                clFilter.setMaxHeight(1440);
            }

        });

        edMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Walker> f = new ArrayList<>();
                try {
                    for (int i = 0; i < walker.size(); i++) {
                        if (walker.get(i).Charge < Integer.parseInt(edMaxPrice.getText() + "")) {
                            f.add(walker.get(i));

                        }

                    }
                    ////Log.d("HESH",f.toString());

                    adapter = new WalkerAdapter(context, f);
                    lstWalkers.setAdapter(adapter);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Walker> f = new ArrayList<>();
                for(int i = 0;i<walker.size();i++){
                    if(walker.get(i).lName.contains(edFilter.getText()+"")||walker.get(i).fName.contains(edFilter.getText()+"")){
                        f.add(walker.get(i));

                    }

                }
                ////Log.d("HESH",f.toString());

                adapter = new WalkerAdapter(context,f);
                lstWalkers.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edDistance.setText(""+progress);
                ArrayList<Walker> f = new ArrayList<>();
                for(int i = 0;i<=walker.size();i++){
                    if(Distance.calculateDistance(walker.get(i).getLatitude(),walker.get(i).getLongitude(),w.getLatitude(),w.getLongitude()) < progress){
                        f.add(walker.get(i));

                    }

                }
                ////Log.d("HESH",f.toString());

                adapter = new WalkerAdapter(context,f);
                lstWalkers.setAdapter(adapter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edSpeed.setText(""+progress);
                ArrayList<Walker> f = new ArrayList<>();
                for(int i = 0;i<=walker.size();i++){
                    if(walker.get(i).walkRate < progress){
                        f.add(walker.get(i));

                    }

                }
                ////Log.d("HESH",f.toString());

                adapter = new WalkerAdapter(context,f);
                lstWalkers.setAdapter(adapter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public static String getUsername(){return username;}



}