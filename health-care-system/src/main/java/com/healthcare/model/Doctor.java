package com.healthcare.model;

public class Doctor {
    private String username;
    private String fullName;


    public Doctor(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }


    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
}

