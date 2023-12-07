package com.example.a403finalproject;

public class Pet {
    int tuid;
    String owner;
    String name;
    String breed;
    String description;

    public Pet(int tuid, String owner, String name, String breed, String description) {
        this.tuid = tuid;
        this.owner = owner;
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
