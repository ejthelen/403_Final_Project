package com.example.a403finalproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.transition.TransitionManager;
import android.transition.AutoTransition;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.cardview.widget.CardView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestAdaptor extends BaseAdapter {
    ArrayList<WalkerRequest> requests;
    ActivityResultLauncher resultLauncher;
    RequestQueue queue;
    Context context;

    // Place holder (change for actual implementation)
    int reqTuID = 1;

    public RequestAdaptor(Context context, ArrayList<WalkerRequest> requests) {
        this.context = context;
        this.requests = requests;

    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int i) {
        return requests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_request_item, parent, false);
        WalkerRequest request = requests.get(i);
        CardView cardViewReq = view.findViewById(R.id.cardViewReq);
        TextView txtRequestName = view.findViewById(R.id.txtRequestName);
        TextView txtContactInfo = view.findViewById(R.id.txtContactInfo);

        // Initialize the RequestQueue
        queue = Volley.newRequestQueue(context);


        // Update the card to show the request information
        txtRequestName.setText("Requested by: "+request.requester_name);
        txtContactInfo.setText("Contact info: "+request.phone_number+" or " + request.email);

//        // When button is clicked, the request will be removed from the database and the users list
//        btnRemoveReq.setOnClickListener(e -> {
//            JSONObject deleteReq = new JSONObject();
//            try {
//                deleteReq.put("TuID", reqTuID);
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//            String updateUrl = "https://cs403api20231121223109.azurewebsites.net/SVSU_CS403/DeleteRequest";
//
//            JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, updateUrl, deleteReq,
//                    response -> {
//                        // Handle successful update
//                        Log.d("Delete", "Data deleted successfully");
//                    },
//                    error -> {
//                        // Handle error
//                        Log.e("Delete", "Error deleted data: " + error.toString());
//                    });
//
//            queue.add(updateRequest);
//            notifyDataSetChanged();
//            resultLauncher.launch(i);
//        });
       return view;
    }
}
