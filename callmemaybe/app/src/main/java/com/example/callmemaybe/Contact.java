package com.example.callmemaybe;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String phoneNumber;
    private String observation;
    private Bitmap profilePicture;
    private int id;

    public Contact(){
    }

    public Contact(String name, Bitmap profilePicture, String phoneNumber, String observation ){
        this.name = name;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.observation = observation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservation() { return observation; }

    public void setObservation(String observation) { this.observation = observation; }
}