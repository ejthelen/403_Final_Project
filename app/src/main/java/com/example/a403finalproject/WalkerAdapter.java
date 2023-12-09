package com.example.a403finalproject;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WalkerAdapter extends BaseAdapter {
    RequestQueue queue;

    ArrayList<Walker> walkers;
    Context context;
    Walker w;
    String username;
    SharedPreferences sp;
    SharedPreferences sharedPreferences;

    public WalkerAdapter(Context context, ArrayList<Walker> walkers){
        this.walkers = walkers;
        this.context = context;
    }


    @Override
    public int getCount() {
        return walkers.size();
    }

    @Override
    public Object getItem(int position) {
        return walkers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_walker, parent, false);

        sp = context.getSharedPreferences("HESH", MODE_PRIVATE);
        username = sp.getString("uname", "err");
        SharedPreferences.Editor ed = sp.edit();
        sharedPreferences = view.getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);

        queue = Volley.newRequestQueue(view.getContext());

        Walker walker = walkers.get(i);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtShortDesc = view.findViewById(R.id.txtShortDesc);
        TextView txtDistanceAway = view.findViewById(R.id.txtDistanceAway);
        TextView txtCharge = view.findViewById(R.id.txtCharge);
        ConstraintLayout clHousing = view.findViewById(R.id.clHousing);
        Button btnBook = view.findViewById(R.id.btnBook);
        TextView txtLongDesc = view.findViewById(R.id.txtLongDesc);
        TextView txtEC = view.findViewById(R.id.txtExpand);
        txtName.setText("Name: " + walker.fName + " " + walker.lName);
        txtShortDesc.setText("" + walker.sDesc);
        //txtDistanceAway.setText("Name: "+walker.distanceAway);
        txtCharge.setText("Price: " + walker.walkRate);
        txtLongDesc.setText("" + walker.lDesc);

            w = new Walker();

//        for(int j = 0;j<walkers.size();j++){
//            //Log.d("HESH","TUID: "+walkers.get(j).getTUID()+ " "+Profile_View.getTuid());
//            if(walkers.get(j).getTUID()==Profile_View.getTuid()){
//                w = walkers.get(j);
//            }
//        }
            for (Walker walker1 : walkers) {
                if (username.equals(walker1.getUserName())) {
                    w = walker1;
                }
            }

        float lat = sharedPreferences.getFloat("lat",0);
        float lon = sharedPreferences.getFloat("lon", 0);

        Log.d("lat and lon", lat + " " + lon);
            txtDistanceAway.setText("Distance: " + Distance.calculateDistance(walker.getLatitude(), walker.getLongitude(), lat, lon));

            //Log.d("HESH",Distance.calculateDistance(walker.getLatitude(),walker.getLongitude(),w.getLatitude(),w.getLongitude())+"");

        clHousing.setMaxHeight(300);
        btnBook.setVisibility(View.INVISIBLE);

        txtLongDesc.setMovementMethod(new ScrollingMovementMethod());

        notifyDataSetChanged();


            clHousing.setOnClickListener(e -> {
                //clHousing.setMaxHeight(300);

                if (clHousing.getMaxHeight() == 300) {
                    clHousing.setMaxHeight(1000);
                    //Log.d("HESH",txtLongDesc.getHeight()+"");
                    txtEC.setText("^");
                    btnBook.setVisibility(View.VISIBLE);
                } else if (clHousing.getMaxHeight() == 1000) {
                    clHousing.setMaxHeight(300);
                    txtEC.setText("v");
                    btnBook.setVisibility(View.INVISIBLE);

                }

            });

            btnBook.setOnClickListener(e -> {

                JSONObject newReq = new JSONObject();
                try {
                    newReq.put("walker_username", walker.userName);
                    newReq.put("client_username", username);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                Log.d("request", newReq +"");
                // URL for Create Request
                String requestURL = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/SetRequest";

                // Send a post request with the newPet
                JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, requestURL, newReq,
                        response -> {
                            // Handle successful update
                            Log.d("Update", "Data updated successfully");
                        },
                        error -> {
                            // Handle error
                            Log.e("Update", "Error updating data: " + error.toString());
                        });

                queue.add(updateRequest);
//                Intent intent = new Intent(context, ScheduleWalk.class);
//
//                Bundle b = new Bundle();
//
//                Log.d("HESH", walker.getUserName() + " " + WalkerList.getUsername());
//
//                b.putString("WU", "" + walker.getUserName());
//                b.putString("CU", "" + WalkerList.getUsername());
//
//                //Log.d("HESH",""+walker.getTUID());
//
//                intent.putExtras(b);
//                startActivity(context, intent, b);
            });
            return view;
        }


}
