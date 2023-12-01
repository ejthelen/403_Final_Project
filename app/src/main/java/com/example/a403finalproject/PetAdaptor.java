package com.example.a403finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PetAdaptor extends BaseAdapter {
    ArrayList<Pet> pets;
    Context context;
    public PetAdaptor( Context context,ArrayList<Pet> pets) {
        this.pets = pets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int i) {
        return pets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view =  LayoutInflater.from(context).inflate(R.layout.activity_pets_page,parent,false);
        Pet pet = pets.get(i);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtBreed = view.findViewById(R.id.txtBreed);
        TextView txtDesc = view.findViewById(R.id.txtDesc);

        // Will update when we have to getPet api
//        txtName.setText("Name: "+pet.name);
//        txtBreed.setText("Breed: "+pet.breed+"");
//        txtDesc.setText("Description: "+description.gpa+"");

        return view;
    }
}
