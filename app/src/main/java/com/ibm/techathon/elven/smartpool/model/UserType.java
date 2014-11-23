package com.ibm.techathon.elven.smartpool.model;

import java.io.Serializable;

/**
 * Created by meshriva on 11/10/2014.
 */
public class UserType implements Serializable{

    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private String currentLocation;
    private String vechileRegisterationNumber;

    public UserType(){
        super();
    }

    public UserType(String email, String name, String password, String phoneNumber, String currentLocation, String vechileRegisterationNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.currentLocation = currentLocation;
        this.vechileRegisterationNumber = vechileRegisterationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getVechileRegisterationNumber() {
        return vechileRegisterationNumber;
    }

    public void setVechileRegisterationNumber(String vechileRegisterationNumber) {
        this.vechileRegisterationNumber = vechileRegisterationNumber;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", vechileRegisterationNumber='" + vechileRegisterationNumber + '\'' +
                '}';
    }
}
