package com.example.rideredirverapplication.Classes;

public class ProfileDetailsModel {
    public String profileUrl,firstName,lastName,dateOfBirth, emailid,gender;

    public ProfileDetailsModel(String profileUrl, String firstName, String lastName, String dateOfBirth, String emailid, String gender) {
        this.profileUrl = profileUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.emailid = emailid;
        this.gender = gender;
    }

    public ProfileDetailsModel(String profileUrl, String firstName, String lastName, String dateOfBirth, String emailid) {
        this.profileUrl = profileUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.emailid = emailid;
    }
}
