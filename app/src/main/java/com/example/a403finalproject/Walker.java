package com.example.a403finalproject;

public class Walker {
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    String streetAddress;
    String city;
    String state;
    String country;
    String shortDescription;
    String longDescription;
    String username;
    double walkRate;

    public Walker(String firstName, String lastName, String phoneNumber, String email, String streetAddress, String city, String state, String country, String shortDescription, String longDescription, String username, double walkRate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = country;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.username = username;
        this.walkRate = walkRate;
    }
}
