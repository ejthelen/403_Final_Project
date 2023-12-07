package com.example.a403finalproject;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a403finalproject.databinding.ActivityWalkerMapBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Walker_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityWalkerMapBinding binding;
    RequestQueue queue;
    ArrayList<Walker> walker = new ArrayList<>();
    ArrayList<String> walkerArrayListString = new ArrayList<>();
    Distance distance;
    Walker currentWalker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWalkerMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        distance = new Distance();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new WalkerInfoWindow());
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        queue = Volley.newRequestQueue(this);
//        getData();
//        walker.add(new Walker("1","Test1","jane road","johnsville","ms","USA",true,20.0,"likes to walk",10.0,"really really likes to walk","989 989 8998","a@aol.com",43.51,-83.95543166233124,1));
//        walker.add(new Walker("2","Test2","jane road","johnsville","ms","USA",true,15.0,"likes to walk",20.0,"really really likes to walk","989 989 8998","a@aol.com",43.52,-83.95543166233124,2));
//        walker.add(new Walker("3","Test3","jane road","johnsville","ms","USA",true,10.0,"likes to walk",30.0,"really really likes to walk","989 989 8998","a@aol.com",43.58,-83.95543166233124,3));
//        walker.add(new Walker("4","Test4","jane road","johnsville","ms","USA",true,2.0,"likes to walk",40.0,"really really likes to walk","989 989 8998","a@aol.com",43.54,-83.95543166233124,4));

        //            Walker currentWalker = walker.get(Profile_View.getTuid());

        Intent i = getIntent();
//        Bundle bundle = i.getBundleExtra("walkerArrayList");
        walkerArrayListString = i.getStringArrayListExtra("walkerArrayList");

        for (String walkerCSV : walkerArrayListString) {
            String[] walkerSplitString = walkerCSV.split(",");
            int index = 0;
            Walker walker1 = new Walker(walkerSplitString[0],
                    walkerSplitString[1], walkerSplitString[2], walkerSplitString[3],
                    walkerSplitString[4], (walkerSplitString[5]), Boolean.parseBoolean(walkerSplitString[6]),
                Double.parseDouble(walkerSplitString[7]), walkerSplitString[8], Double.parseDouble(walkerSplitString[9]),
                    walkerSplitString[10], walkerSplitString[11], (walkerSplitString[12]),
                    Double.parseDouble(walkerSplitString[13]), Double.parseDouble(walkerSplitString[14]),
                    Integer.parseInt(walkerSplitString[15]));
            walker.add(walker1);
        }
        currentWalker = walker.get(2);

        setWalkerMarkers();

        mMap.setOnInfoWindowLongClickListener(marker -> {
            Walker w = (Walker) marker.getTag();
            Intent walkerListIntent = new Intent(Walker_Map.this, WalkerList.class);
            walkerListIntent.putExtra("walker_ID", w.getTUID());
            startActivity(walkerListIntent);
        });
    }

    private void setWalkerMarkers() {
        LatLng latLngCurrentWalker = new LatLng(walker.get(currentWalker.getTUID()-1).getLatitude(),
                walker.get(currentWalker.getTUID()-1).getLongitude());

            for (Walker walker : walker) {
                if (walker.getTUID() != currentWalker.getTUID()) {
                    LatLng walkerPosition = new LatLng(walker.getLatitude(), walker.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(walkerPosition));
                    marker.setTitle((walker.getfName()));
                    marker.setTag(walker);
                } else {
                    int markerColor = Color.BLUE;
                    addCustomMarker(latLngCurrentWalker, markerColor, currentWalker);
                }
            }

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrentWalker));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLngCurrentWalker)
                .zoom(12)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addCustomMarker(LatLng position, int color, Walker walker) {
        BitmapDescriptor customMarker = getMarkerIconFromColor(color);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(customMarker));
        marker.setTitle((walker.getfName()));
        marker.setTag(walker);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    private BitmapDescriptor getMarkerIconFromColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        Bitmap icon = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(50, 50, 50, paint);

        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    public void getData() {
        String url = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/GetActiveWalkers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("HERE", response + "");
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

                            this.walker.add(w);
                        }

                        // Process the list of walkers as needed
                        // Note: You may want to update the UI or perform other actions with the walker data.

                    } catch (JSONException e) {
                        Log.e("Error", "Error parsing JSON response", e);
                    }
                },
                error -> Log.e("Error", "Error fetching data: " + error));

        // Add the request to the request queue
        this.queue.add(request);
    }


    class WalkerInfoWindow implements GoogleMap.InfoWindowAdapter {
        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            View v = LayoutInflater.from(Walker_Map.this).inflate(R.layout.layout_walker_window,
                    null);

            TextView txtWalkerName = v.findViewById(R.id.tvWalkerName);
            TextView txtWalkerDistance = v.findViewById(R.id.tvWalkerDistance);
            TextView txtPrice = v.findViewById(R.id.tvPrice);
            TextView txtWalkerRate = v.findViewById(R.id.txtWalkerRate);
            Walker walker1 = (Walker) marker.getTag();

            txtWalkerName.setText("Name: " + walker1.getlName());

            if (walker1.getTUID() != currentWalker.getTUID()) {
                txtPrice.setText(String.format(Locale.US, "Charge: $%,.2f",
                        walker1.getCharge()));
                txtWalkerDistance.setText("Distance: " +
                        distance.calculateDistance(walker1.getLatitude(),
                                walker1.getLongitude(), currentWalker.getLatitude(),
                                currentWalker.getLongitude()) + " miles");
                txtWalkerRate.setText(String.format(Locale.US, "Rate: $%,.2f per walk",
                        walker1.getWalkRate()));
            } else {
                txtWalkerName.setText(txtWalkerName.getText() + " (Me)");
                txtPrice.setText("City: " + walker1.getCity().substring(0,1).toUpperCase() +
                        walker1.getCity().substring(1, walker1.getCity().length()));
                txtWalkerDistance.setText("State: " + walker1.getState().toUpperCase());
                txtWalkerRate.setText("Country: " + walker1.getCountry());
            }

            return v;
        }
    }

}