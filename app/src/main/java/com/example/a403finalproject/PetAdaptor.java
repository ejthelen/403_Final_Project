package com.example.a403finalproject;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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
        CardView cardView = view.findViewById(R.id.cardView);
        ImageView imgDog = view.findViewById(R.id.ivDog);
        TextView txtName = view.findViewById(R.id.txtDogName);
        TextView txtBreed = view.findViewById(R.id.txtBreed);
        TextView txtDesc = view.findViewById(R.id.txtDesc);



        // Will update when we have to getPet api
        imgDog.setImageResource(R.drawable.doggo);
        txtName.setText("Name: "+pet.name);
        txtBreed.setText("Breed: "+pet.breed+"");
        txtDesc.setText("Description: "+pet.description+"");

        cardView.setOnClickListener(v->{
            //Animation when the card expands
            TransitionManager.beginDelayedTransition(cardView,new AutoTransition());

            // checks if the description is currently visible and toggles the visibility
            if (txtDesc.getVisibility() == View.VISIBLE && txtBreed.getVisibility() == View.VISIBLE) {
                txtDesc.setVisibility(View.GONE);
                txtBreed.setVisibility(View.GONE);

            } else {
                txtDesc.setVisibility(View.VISIBLE);
                txtBreed.setVisibility(View.VISIBLE);

            }
        });



        return view;
    }
}
