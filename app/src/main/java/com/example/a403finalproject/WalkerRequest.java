package com.example.a403finalproject;
public class WalkerRequest {
    String requester_username;
    String requester_name;
    String walker_username;
    String phone_number;
    String email;
    String pet_description;

    public WalkerRequest(String requester_username, String requester_name, String walker_username, String phone_number, String email) {
        this.requester_username = requester_username;
        this.requester_name = requester_name;
        this.walker_username = walker_username;
        this.phone_number = phone_number;
        this.email = email;
    }
}
