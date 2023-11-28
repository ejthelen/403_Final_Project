package com.example.a403finalproject;

public class Walker {

    String name;
    String sDesc;
    double distanceAway;
    double Charge;
    String lDesc;
    String PhoneNumber;
    String Email;
    String Schedule;

    double Lattitude;
    double Longitude;


    public Walker(String name, String sDesc, double distanceAway, double charge, String lDesc, String phoneNumber, String email, String schedule, double lattitude, double longitude) {
        this.name = name;
        this.sDesc = sDesc;
        this.distanceAway = distanceAway;
        Charge = charge;
        this.lDesc = lDesc;
        PhoneNumber = phoneNumber;
        Email = email;
        Schedule = schedule;
        Lattitude = lattitude;
        Longitude = longitude;
    }
}
