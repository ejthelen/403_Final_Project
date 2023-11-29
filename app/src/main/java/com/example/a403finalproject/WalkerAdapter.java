package com.example.a403finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class WalkerAdapter extends BaseAdapter {

    ArrayList<Walker> walkers;
    Context context;


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
        view =  LayoutInflater.from(context).inflate(R.layout.layout_walker,parent,false);
        Walker walker = walkers.get(i);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtShortDesc = view.findViewById(R.id.txtShortDesc);
        TextView txtDistanceAway = view.findViewById(R.id.txtDistanceAway);
        TextView txtCharge = view.findViewById(R.id.txtCharge);
        ConstraintLayout clHousing = view.findViewById(R.id.clHousing);
        Button btnBook = view.findViewById(R.id.btnBook);
        TextView txtLongDesc = view.findViewById(R.id.txtLongDesc);
        TextView txtEC = view.findViewById(R.id.txtExpand);

        txtName.setText("Name: "+walker.fName+" "+walker.lName);
        txtShortDesc.setText(""+walker.sDesc);
        //txtDistanceAway.setText("Name: "+walker.distanceAway);
        txtCharge.setText("Price: "+walker.Charge);
        txtLongDesc.setText(""+walker.lDesc);

        clHousing.setMaxHeight(300);

        clHousing.setOnClickListener(e->{
            //clHousing.setMaxHeight(300);

            if(clHousing.getMaxHeight()==300){
                clHousing.setMaxHeight(900);
                txtEC.setText("^");

            }
            else if(clHousing.getMaxHeight()==900){
                clHousing.setMaxHeight(300);
                txtEC.setText("v");

            }

        });

        return view;
    }
}
