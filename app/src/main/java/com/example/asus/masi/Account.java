package com.example.asus.masi;

import android.net.Uri;

public class Account {

    private int custId;
    private Uri profilePic;
    private String username, password, firstname, lastname, gender, email, contact, address, status;
    private double totalBal;

    public Account(int userId, Uri profilepic, String username, String password , String fname, String lname, String gender, String email, String contact, String address, double totalBal, String status) {
        this.custId = userId;
        this.profilePic = profilepic;
        this.username = username;
        this.password = password;
        this.firstname = fname;
        this.lastname = lname;
        this.gender = gender;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.totalBal = totalBal;
        this.status = status;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int userId) {
        this.custId = userId;
    }

    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender(){
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalBal() {
        return totalBal;
    }

    public void setTotalBal(double totalBal) {
        this.totalBal = totalBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}