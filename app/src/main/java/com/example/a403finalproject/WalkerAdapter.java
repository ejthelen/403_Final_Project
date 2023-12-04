package com.example.a403finalproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
        btnBook.setVisibility(View.INVISIBLE);

        txtLongDesc.setMovementMethod(new ScrollingMovementMethod());





        clHousing.setOnClickListener(e->{
            //clHousing.setMaxHeight(300);

            if(clHousing.getMaxHeight()==300){
                clHousing.setMaxHeight(1000);
                Log.d("HESH",txtLongDesc.getHeight()+"");
                txtEC.setText("^");
                btnBook.setVisibility(View.VISIBLE);
            }
            else if(clHousing.getMaxHeight()==1000){
                clHousing.setMaxHeight(300);
                txtEC.setText("v");
                btnBook.setVisibility(View.INVISIBLE);

            }

        });

        btnBook.setOnClickListener(e->{
            Intent intent = new Intent(context,ScheduleWalk.class);

            Bundle b = new Bundle();

            b.putString("TUID",""+walker.getTUID());
            Log.d("HESH",""+walker.getTUID());

            intent.putExtras(b);
            startActivity(context,intent,b);
        });

        return view;
    }


}
