package com.example.a403finalproject;

public class Walker {

    String fName;
    String lName;
    String address;
    String state;
    String country;
    boolean isWalker;
    double walkRate;

    String sDesc;
    double Charge;
    String lDesc;
    String PhoneNumber;
    String Email;
    double Lattitude;
    double Longitude;


    public Walker(String fName, String lName, String address, String state, String country, boolean isWalker, double walkRate, String sDesc, double charge, String lDesc, String phoneNumber, String email, double lattitude, double longitude) {
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.state = state;
        this.country = country;
        this.isWalker = isWalker;
        this.walkRate = walkRate;
        this.sDesc = sDesc;
        Charge = charge;
        this.lDesc = lDesc;
        PhoneNumber = phoneNumber;
        Email = email;
        Lattitude = lattitude;
        Longitude = longitude;
    }
}
