package com.example.a403finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view =  LayoutInflater.from(context).inflate(R.layout.layout_walker,parent,false);
        Walker walker = walkers.get(i);

        //apply values here

        return view;
    }
}
