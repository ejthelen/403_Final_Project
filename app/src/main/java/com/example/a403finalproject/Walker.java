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
    int TUID;

    public Walker(String fName, String lName, String address, String state, String country, boolean isWalker, double walkRate, String sDesc, double charge, String lDesc, String phoneNumber, String email, double lattitude, double longitude, int TUID) {
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
        this.TUID = TUID;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isWalker() {
        return isWalker;
    }

    public void setWalker(boolean walker) {
        isWalker = walker;
    }

    public double getWalkRate() {
        return walkRate;
    }

    public void setWalkRate(double walkRate) {
        this.walkRate = walkRate;
    }

    public String getsDesc() {
        return sDesc;
    }

    public void setsDesc(String sDesc) {
        this.sDesc = sDesc;
    }

    public double getCharge() {
        return Charge;
    }

    public void setCharge(double charge) {
        Charge = charge;
    }

    public String getlDesc() {
        return lDesc;
    }

    public void setlDesc(String lDesc) {
        this.lDesc = lDesc;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public double getLattitude() {
        return Lattitude;
    }

    public void setLattitude(double lattitude) {
        Lattitude = lattitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
