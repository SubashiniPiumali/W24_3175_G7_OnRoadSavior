package com.example.w24_3175_g7_onroadsavior.Model;

public class UserHelperClass {

    private  String uID;
    private String fullName;
    private String userName;
    private String email;
    private String contactNumber;
    private String password;
    private String userType;
    private String serviceType;
    private String location;

    //constructors

    public UserHelperClass(String uID, String fullName, String userName, String email, String contactNumber, String password, String userType, String location, String serviceType) {
        this.uID = uID;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.userType = userType;
        this.location = location;
        this.serviceType = serviceType;
    }

    public UserHelperClass(String fullName, String userName, String email, String contactNumber, String password, String userType, String serviceType) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.password = password;
        this.userType = userType;
        this.serviceType = serviceType;
    }

    public UserHelperClass() {

    }

    //getters setters

    public String getFullName() {
        return fullName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
