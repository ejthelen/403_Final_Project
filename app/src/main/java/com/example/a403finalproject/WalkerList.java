package com.example.a403finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.ImageButton;
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

    //initialize variables.
    ActivityResultLauncher resultLauncher;

    ListView lstWalkers;
    EditText edFilter;
    Button btnFilter;
    WalkerAdapter adapter;
    ArrayList<Walker> walker;

    Button btnLocation;
    ImageButton btnToReqFromHome, btnToPetsFromHome, btnToHomeFromHome, btnToProfileFromHome, btnToInfoFromHome;
    RequestQueue queue;

    SharedPreferences sp;
    SharedPreferences sharedPreferences;
    String spTAG = "LOCATION";

    Context context;

    ConstraintLayout clFilter;
    SeekBar sbDistance;
    TextView edDistance;
    EditText edMaxPrice;

    Walker w;

    static String uname;
    SharedPreferences.Editor editor;

    //oncreate to load visible data.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        //sharedpreferences caller
        sp = getSharedPreferences("HESH",MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);


        uname = sp.getString("uname","err");
        SharedPreferences.Editor ed = sp.edit();


        Log.d("HESH",uname+"");

        //apply field values.
        lstWalkers = findViewById(R.id.lstWalkers);
        edFilter = findViewById(R.id.edFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnLocation = findViewById(R.id.btnLocation);
        sbDistance = findViewById(R.id.sbDistance);
        edDistance = findViewById(R.id.edDistance);
        clFilter = findViewById(R.id.clFilter);
        edMaxPrice = findViewById(R.id.edMaxPrice);
        btnToPetsFromHome = findViewById(R.id.btnToPetsFromHome);
        btnToReqFromHome = findViewById(R.id.btnToReqFromHome);
        btnToProfileFromHome = findViewById(R.id.btnToProfileFromHome);
        btnToInfoFromHome = findViewById(R.id.btnToInfoFromHome);

        //navbar properties.
        btnToPetsFromHome.setOnClickListener(e -> {

            Intent i = new Intent(this, RequestsActivity.class);

            resultLauncher.launch(i);
        });

        btnToReqFromHome.setOnClickListener(e -> {
            Intent i = new Intent(this, WalkerList.class);
            resultLauncher.launch(i);
        });

        btnToProfileFromHome.setOnClickListener(e -> {
            Intent i = new Intent(this, Profile_View.class);
            resultLauncher.launch(i);
        });

        btnToInfoFromHome.setOnClickListener(e -> {
            Intent i = new Intent(this, InfoScrolling.class);
            resultLauncher.launch(i);
        });

        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });

        //create request.
        queue = Volley.newRequestQueue(this);

        //get context.
        context = this;
        double lat = (double)sharedPreferences.getFloat("lat",0);
        double lon = (double)sharedPreferences.getFloat("lon", 0);

        Log.d("lat and lon", lat + " " + lon);

        //initialize walker arraylist.
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

                            if (!username.matches(uname)) {
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

        //sleep thread to let data load.
        try {
            // Sleep for 3 seconds (3000 milliseconds)
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //get walker value for current user.
        int tuid = sp.getInt("TUID", 1);
        for(int j = 0;j<walker.size();j++){
            if(walker.get(j).getTUID()==tuid){
                w = walker.get(j);
            }
        }

        //apply adapter values
        adapter = new WalkerAdapter(this,walker);

        //set the adapter to the lstview.
        lstWalkers.setAdapter(adapter);

        //notify data changed for redundancy.
        adapter.notifyDataSetChanged();

        //all filtering in the listview.
        lstWalkers.setTextFilterEnabled(true);

        if (Objects.isNull(getIntent()) == false) {
            Intent mapRecievedIntent = getIntent();
            int walkerID = mapRecievedIntent.getIntExtra("walker_ID", -1);
        }

        //on btnLocation selection.
        btnLocation.setOnClickListener(e->{
            //get intent.
            Intent i = new Intent(this, Walker_Map.class);
            //create arraylist to carry values.
            ArrayList<String> walkerArrayList = new ArrayList<>();

            //create bundle.
            Bundle bundle = new Bundle();

            //insert values into the walkerArrayList.
            for (Walker walker1 : walker) {
                walkerArrayList.add(walker1.getfName() + "," + walker1.getlName() + "," +
                        walker1.getUserName() + "," +
                        walker1.getAddress() + "," + walker1.getCity() + "," + walker1.getState() + "," +
                        walker1.getCountry() + "," + walker1.isWalker() + "," + walker1.getWalkRate() + "," +
                        walker1.getsDesc() + "," + walker1.getCharge() + "," +walker1.getlDesc() + "," +
                        walker1.getPhoneNumber() +  "," +walker1.getEmail() + "," + walker1.getLatitude() + "," +
                        walker1.getLongitude() + "," + walker1.getTUID());
            }
            //apply arraylist to bundle.
            bundle.putStringArrayList("walkerArrayList", walkerArrayList);

            //apply bundle and start.
            i.putStringArrayListExtra("walkerArrayList", walkerArrayList);
            startActivity(i);
        });

        //on filter clicked expand/contract when necessary.
        clFilter.setMaxHeight(0);
        btnFilter.setOnClickListener(e->{
            if(clFilter.getMaxHeight()==1440){
                clFilter.setMaxHeight(0);

            }else{
                clFilter.setMaxHeight(1440);
            }

        });

        //on edMaxPrice text changed.
        edMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //loop through the arraylist of walker and apply their values to a new arraylist.

                ArrayList<Walker> f = new ArrayList<>();
                try {
                    for (int i = 0; i < walker.size(); i++) {
                        if (walker.get(i).walkRate <= Double.parseDouble(edMaxPrice.getText() + "")) {
                            f.add(walker.get(i));
                            Log.d("added", "There was text");
                        }

                    }
                    ////Log.d("HESH",f.toString());
                    adapter.notifyDataSetChanged();
                    adapter = new WalkerAdapter(context, f);

                    lstWalkers.setAdapter(adapter);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Walker> f = new ArrayList<>();

                try {
                    for (int i = 0; i < walker.size(); i++) {
                        if (edMaxPrice.getText().toString().isEmpty()) {
                            f.add(walker.get(i));
                            Log.d("nothing", "There was no text");
                        } else if (walker.get(i).walkRate <= Double.parseDouble(edMaxPrice.getText() + "")) {
                            f.add(walker.get(i));
                            Log.d("added", "There was text");
                        }

                    }
                    ////Log.d("HESH",f.toString());
                    adapter.notifyDataSetChanged();
                    adapter = new WalkerAdapter(context, f);

                    lstWalkers.setAdapter(adapter);
                }catch (Exception e){

                }
            }
        });

        //prompts to occur when text changes in the name filter textfield.
        edFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //loop through all walkers and check by first name.

                ArrayList<Walker> f = new ArrayList<>();
                for(int i = 0;i<walker.size();i++){
                    if(walker.get(i).lName.toLowerCase().contains(edFilter.getText()+"".toLowerCase())||walker.get(i).fName.toLowerCase().contains(edFilter.getText()+"".toLowerCase())){
                        f.add(walker.get(i));

                    }

                }
                ////Log.d("HESH",f.toString());

                adapter.notifyDataSetChanged();
                adapter = new WalkerAdapter(context,f);
                lstWalkers.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //on sbdistance value changes.
        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //loop through walkers and apply only those within the required distance.

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

        //sbSpeed seekbar change listener for checking max speed.
        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //loop through all walkers and find the ones with a speed less than or
                //equal to the sb progress.

                edSpeed.setText(""+progress);
                ArrayList<Walker> f = new ArrayList<>();
                for(int i = 0;i<=walker.size();i++){
                    if(walker.get(i).walkRate < progress){
                for(int i = 0;i<=walker.size() - 1;i++){
                    if(Distance.calculateDistance(walker.get(i).getLatitude(),walker.get(i).getLongitude(),lat,lon) < progress){
                        f.add(walker.get(i));

                    }

                }
                ////Log.d("HESH",f.toString());

                adapter = new WalkerAdapter(context,f);
                adapter.notifyDataSetChanged();
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


    public static String getUsername(){return uname;}



}