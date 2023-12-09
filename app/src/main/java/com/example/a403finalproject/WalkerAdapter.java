package com.example.a403finalproject;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WalkerAdapter extends BaseAdapter {
    RequestQueue queue;

    //initialize variables.
    ArrayList<Walker> walkers;
    Context context;
    Walker w;
    String username;
    SharedPreferences sp;
    SharedPreferences sharedPreferences;


    //get context and walkers.
    public WalkerAdapter(Context context, ArrayList<Walker> walkers) {
        this.walkers = walkers;
        this.context = context;
    }

    //get values
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

        //get current walker instance.
        Walker walker = walkers.get(i);

        //apply textviews.
        CardView cardViewWalker = view.findViewById(R.id.cardViewWalker);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtShortDesc = view.findViewById(R.id.txtShortDesc);
        TextView txtDistanceAway = view.findViewById(R.id.txtDistanceAway);
        TextView txtCharge = view.findViewById(R.id.txtCharge);
        Button btnRequest = view.findViewById(R.id.btnRequest);
        TextView txtLongDesc = view.findViewById(R.id.txtLongDesc);
        txtName.setText("Name: " + walker.fName + " " + walker.lName);
        txtShortDesc.setText("" + walker.sDesc);
        //txtDistanceAway.setText("Name: "+walker.distanceAway);
        txtCharge.setText("Price: " + walker.walkRate);
        txtLongDesc.setText("" + walker.lDesc);


        w = new Walker();


        //apply walker value.
        for (Walker walker1 : walkers) {
            if (username.equals(walker1.getUserName())) {
                w = walker1;
            }
        }


        float lat = sharedPreferences.getFloat("lat", 0);
        float lon = sharedPreferences.getFloat("lon", 0);

        Log.d("lat and lon", lat + " " + lon);
        txtDistanceAway.setText("Distance: " + Distance.calculateDistance(walker.getLatitude(), walker.getLongitude(), lat, lon));

        //notify dataset changed.
        notifyDataSetChanged();
        cardViewWalker.setOnClickListener(v -> {
            //Animation when the card expands
            TransitionManager.beginDelayedTransition(cardViewWalker, new AutoTransition());

            // checks if the description is currently visible and toggles the visibility
            if (txtShortDesc.getVisibility() == View.VISIBLE && txtLongDesc.getVisibility() == View.VISIBLE && btnRequest.getVisibility() == View.VISIBLE) {
                txtShortDesc.setVisibility(View.GONE);
                txtLongDesc.setVisibility(View.GONE);
                btnRequest.setVisibility(View.GONE);

            } else {
                txtShortDesc.setVisibility(View.VISIBLE);
                txtLongDesc.setVisibility(View.VISIBLE);
                btnRequest.setVisibility(View.VISIBLE);
            }
        });


        btnRequest.setOnClickListener(e -> {
            Intent intent = new Intent(context, WalkerProfile.class);

            Bundle b = new Bundle();

            b.putString("walker_username", walker.userName);
            b.putString("first", "" + walker.fName);
            b.putString("last", "" + walker.lName);
            b.putString("short", walker.sDesc);
            b.putString("long", walker.lDesc);
            b.putString("rate", "" + walker.walkRate);
            b.putString("email", walker.Email);
            b.putString("number", walker.PhoneNumber);

            b.putString("client_username", username);

            intent.putExtras(b);
            startActivity(context, intent, b);
            Log.d("here", "jijij");
        });
        return view;
    }


}
