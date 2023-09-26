package com.example.rideredirverapplication.Classes;

import java.io.Serializable;

public class Driver implements Serializable {

    public int userType, isVerified;
    public String name, email, contact_no;

    public Driver() {
    }

    public Driver(String name, String email, String contact_no, int userType, int isVerified) {
        this.name = name;
        this.contact_no = contact_no;
        this.email = email;
        this.userType = userType;
        this.isVerified = isVerified;
    }

    public Driver(String name, String email, String contact_no, int i, int i1, String location) {
    }
}
