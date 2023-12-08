package com.example.a403finalproject;

public class Walker {

    String fName;
    String lName;
    String userName;
    String address;
    String City;


    String state;
    String country;
    boolean isWalker;
    double walkRate;

    double Charge;
    String lDesc;
    String PhoneNumber;
    String Email;
    double latitude;
    double longitude;
    int TUID;

    String sDesc;

    public Walker(String fName, String lName, String userName, String address, String city, String state, String country, boolean isWalker, double walkRate, String sDesc, double charge, String lDesc, String phoneNumber, String email, double latitude, double longitude, int TUID) {
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.address = address;
        City = city;
        this.state = state;
        this.country = country;
        this.isWalker = isWalker;
        this.walkRate = walkRate;
        this.sDesc = sDesc;
        Charge = charge;
        this.lDesc = lDesc;
        PhoneNumber = phoneNumber;
        Email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.TUID = TUID;
    }

    @Override
    public String toString() {
        return "Walker{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Walker() {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        longitude = longitude;
    }

    public int getTUID() {
        return TUID;
    }

    public void setTUID(int TUID) {
        this.TUID = TUID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
