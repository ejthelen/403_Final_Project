package com.example.a403finalproject;

public class Pet {
    String name;
    String breed;
    String description;

    public Pet(String name, String breed, String description) {
        this.name = name;
        this.breed = breed;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", breed=" + breed +
                ", description=" + description +
                '}';
    }
}
