package com.example.a403finalproject;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toolbar;

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
    SharedPreferences sp;
    SharedPreferences sharedPreferences;
    String username;
    Button btnMapBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWalkerMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        distance = new Distance();
        queue = Volley.newRequestQueue(this);




        sp = getSharedPreferences("HESH",MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("MODE",MODE_PRIVATE);
        username = sp.getString("uname","err");
        SharedPreferences.Editor ed = sp.edit();
        btnMapBack = findViewById(R.id.btnMapBack);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnMapBack.setOnClickListener(v->{
            Intent intent = new Intent(this,WalkerList.class);
            startActivity(intent);
            finish();
        });
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

        Intent i = getIntent();
//        Bundle bundle = i.getBundleExtra("walkerArrayList");
        walkerArrayListString = i.getStringArrayListExtra("walkerArrayList");

        for (String walkerCSV : walkerArrayListString) {
            String[] walkerSplitString = walkerCSV.split(",");
            int index = 0;
//                public Walker(String fName, String lName, String userName, String address, String city, String state, String country, boolean isWalker, double walkRate, String sDesc, double charge, String lDesc, String phoneNumber, String email, double latitude, double longitude, int TUID) {

                Walker walker1 = new Walker(walkerSplitString[0],
                    walkerSplitString[1], walkerSplitString[2], walkerSplitString[3], walkerSplitString[4],
                    walkerSplitString[5], (walkerSplitString[6]), Boolean.parseBoolean(walkerSplitString[7]),
                    Double.parseDouble(walkerSplitString[8]), walkerSplitString[9], Double.parseDouble(walkerSplitString[10]),
                    walkerSplitString[11], walkerSplitString[12], (walkerSplitString[13]),
                    Double.parseDouble(walkerSplitString[14]), Double.parseDouble(walkerSplitString[15]),
                    Integer.parseInt(walkerSplitString[16]));
            walker.add(walker1);
        }
        for (Walker walker1 : walker) {
            if (username.equals(walker1.getUserName())) {
//                currentWalker = getUser();
            }
        }

//        currentWalker = walker.get(2);

        setWalkerMarkers();

        mMap.setOnInfoWindowLongClickListener(marker -> {
            Walker w = (Walker) marker.getTag();
            Intent walkerListIntent = new Intent(Walker_Map.this, WalkerList.class);
            walkerListIntent.putExtra("walker_ID", w.getTUID());
            startActivity(walkerListIntent);
        });
    }

    private void setWalkerMarkers() {
        double lat = (double)sharedPreferences.getFloat("lat",0);
        double lon = (double)sharedPreferences.getFloat("lon", 0);
        LatLng latLngCurrentWalker = new LatLng(lat,
                lon);

        int tuid = sharedPreferences.getInt("userTUID", 1);
            for (Walker walker : walker) {
                if (walker.getTUID() != tuid) {
                    LatLng walkerPosition = new LatLng(walker.getLatitude(), walker.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(walkerPosition));
                    marker.setTitle((walker.getfName()));
                    marker.setTag(walker);
                } else {
                    int markerColor = Color.BLUE;
                    addCustomMarker(latLngCurrentWalker, markerColor);
                }
            }
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrentWalker));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLngCurrentWalker)
                .zoom(12)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addCustomMarker(LatLng position, int color) {
        BitmapDescriptor customMarker = getMarkerIconFromColor(color);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(customMarker));
        marker.setTitle("You");
        marker.setTag("user");

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

            float lat = sharedPreferences.getFloat("lat",0);
            float lon = sharedPreferences.getFloat("lon", 0);
            int id = sharedPreferences.getInt("userTUID", 1);
            if (walker1.getTUID() != id) {
                txtPrice.setText(String.format(Locale.US, "Charge: $%,.2f",
                        walker1.getCharge()));
                txtWalkerDistance.setText("Distance: " +
                        distance.calculateDistance(walker1.getLatitude(),
                                walker1.getLongitude(), lat,
                                lon) + " miles");
                txtWalkerRate.setText(String.format(Locale.US, "Rate: $%,.2f per walk",
                        walker1.getWalkRate()));
            } else {
//                txtWalkerName.setText(txtWalkerName.getText());
//                txtPrice.setText("City: " + walker1.getCity().substring(0,1).toUpperCase() +
//                        walker1.getCity().substring(1, walker1.getCity().length()));
//                txtWalkerDistance.setText("State: " + walker1.getState().toUpperCase());
//                txtWalkerRate.setText("Country: " + walker1.getCountry());
            }

            return v;
        }
    }
}