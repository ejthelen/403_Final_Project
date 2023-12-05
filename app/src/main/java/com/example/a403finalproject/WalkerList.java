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

    String streetAddress;
    String City;
    String State;

    Context context;

    ConstraintLayout clFilter;
    SeekBar sbDistance;
    SeekBar sbSpeed;
    TextView edDistance;
    TextView edSpeed;
    EditText edMaxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        sp = getSharedPreferences(spTAG, MODE_PRIVATE);

        streetAddress = sp.getString("SA","0.0");
        City = sp.getString("CITY","0.0");
        State = sp.getString("STATE","0.0");

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

        //Walker f = new Walker("john","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1);

        walker.add(new Walker("1","johnson","jane road","1j","johnsville","ms","USA",true,20.0,"likes to walk",10.0,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,1));
        walker.add(new Walker("2","johnson","jane road","2j","johnsville","ms","USA",true,15.0,"likes to walk",20.0,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,2));
        walker.add(new Walker("3","johnson","jane road","3j","johnsville","ms","USA",true,10.0,"likes to walk",30.0,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,3));
        walker.add(new Walker("4","johnson","jane road","4j","johnsville","ms","USA",true,2.0,"likes to walk",40.0,"really really likes to walk","989 989 8998","a@aol.com",45.75,88.75,4));
        //walker.add(new Walker("5","johnson","jane road","johnsville","ms","USA",true,2.0,"likes to walk",25.7,"really really likes t asdfajk;lsdf;lkasjfkl;djasl;kfjsal;kdfjsalkdjfasl;kdjfl;kasjdflsdafdalkjfjsdakl;fjasl;kdjfakl;sjfasl;kdjfalsk;djfalk;sjdfklasdjfl;kaskjfl;kasdjfal;skdjflak;sdjfl;kasjdfa;kasdjfklwneqtjbghbuiobcvbxo walkreally really likes t asdfajk;lsdf;lkasjfkl;djasl;kfjsal;kdfjsalkdjfasl;kdjfl;kasjdflsdafdalkjfjsdakl;fjasl;kdjfakl;sjfasl;kdjfalsk;djfalk;sjdfklasdjfl;kaskjfl;kasdjfal;skdjflak;sdjfl;kasjdfa;kasdjfklwneqtjbghbuiobcvbxo walkreally really likes t asdfajk;lsdf;lkasjfkl;djasl;kfjsal;kdfjsalkdjfasl;kdjfl;kasjdflsdafdalkjfjsdakl;fjasl;kdjfakl;sjfasl;kdjfalsk;djfalk;sjdfklasdjfl;kaskjfl;kasdjfal;skdjflak;sdjfl;kasjdfa;kasdjfklwneqtjbghbuiobcvbxo walkreally really likes t asdfajk;lsdf;lkasjfkl;djasl;kfjsal;kdfjsalkdjfasl;kdjfl;kasjdflsdafdalkjfjsdakl;fjasl;kdjfakl;sjfasl;kdjfalsk;djfalk;sjdfklasdjfl;kaskjfl;kasdjfal;skdjflak;sdjfl;kasjdfa;kasdjfklwneqtjbghbuiobcvbxo walk","989 989 8998","a@aol.com",45.75,88.75,7));

        getData();
        adapter = new WalkerAdapter(this,walker);

        lstWalkers.setAdapter(adapter);
        lstWalkers.setTextFilterEnabled(true);

        if (Objects.isNull(getIntent()) == false) {
            Intent mapRecievedIntent = getIntent();
            int walkerID = mapRecievedIntent.getIntExtra("walker_ID", -1);

        }

        btnLocation.setOnClickListener(e->{
            Intent i = new Intent(this, Walker_Map.class);
            startActivity(i);
        });


        clFilter.setMaxHeight(0);
        btnFilter.setOnClickListener(e->{
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
                    //Log.d("HESH",f.toString());

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
                //Log.d("HESH",f.toString());

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
                //Log.d("HESH",f.toString());

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

    public void getData() {
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
                            //walker.setUsername(username);

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
    }




}