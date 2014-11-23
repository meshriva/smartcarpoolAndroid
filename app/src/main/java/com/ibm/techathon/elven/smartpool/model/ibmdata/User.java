package com.ibm.techathon.elven.smartpool.model.ibmdata;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * data type class for user
 * Created by meshriva on 11/6/2014.
 */
@IBMDataObjectSpecialization("User")
public class User extends IBMDataObject {

    public static final String CLASS_NAME = "User";

    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String CURRENT_LOCATION = "currentLocation";
    private static final String VECHILE_REGISTERATION_NUMBER = "vechileRegisterationNumber";

    /**
     * Gets the email of the User.
     * @return String email
     */
    public String getEmail() {
        return (String) getObject(EMAIL);
    }

    /**
     * Sets the email of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setEmail(String email) {
        setObject(EMAIL, (email != null) ? email : "");
    }

    /**
     * Gets the name of the User.
     * @return String name
     */
    public String getName() {
        return (String) getObject(NAME);
    }

    /**
     * Sets the name of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setName(String name) {
        setObject(NAME, (name != null) ? name : "");
    }

    /**
     * Gets the password of the User.
     * @return String password
     */
    public String getPassword() {
        return (String) getObject(PASSWORD);
    }

    /**
     * Sets the password of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setPassword(String password) {
        setObject(PASSWORD, (password != null) ? password : "");
    }

    /**
     * Gets the phoneNumber of the User.
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return (String) getObject(PHONE_NUMBER);
    }

    /**
     * Sets the phoneNumber of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setPhoneNumber(String phoneNumber) {
        setObject(PHONE_NUMBER, (phoneNumber != null) ? phoneNumber : "");
    }

    /**
     * Gets the currentLocation of the User.
     * @return String currentLocation
     */
    public String getCurrentLocation() {
        return (String) getObject(CURRENT_LOCATION);
    }

    /**
     * Sets the currentLocation of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setCurrentLocation(String currentLocation) {
        setObject(CURRENT_LOCATION, (currentLocation != null) ? currentLocation : "");
    }

    /**
     * Gets the vechileRegisterationNumber of the User.
     * @return String vechileRegisterationNumber
     */
    public String getVechileRegisterationNumber() {
        return (String) getObject(VECHILE_REGISTERATION_NUMBER);
    }

    /**
     * Sets the vechileRegisterationNumber of a list item, as well as calls setCreationTime().
     * @param
     */
    public void setVechileRegisterationNumber(String vechileRegisterationNumber) {
        setObject(VECHILE_REGISTERATION_NUMBER, (vechileRegisterationNumber != null) ? vechileRegisterationNumber : "");
    }

    public User() {
    }

    public User(String email,String name,String password,String phoneNumber, String currentLocation,String vechileRegisterationNumber ) {
        super();
        setEmail(email);
        setName(name);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setCurrentLocation(currentLocation);
        setVechileRegisterationNumber(vechileRegisterationNumber);
    }
}
