package com.ibm.techathon.elven.smartpool.model.ibmdata;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * data type class which can be used to map user
 * with trust circle
 * Created by meshriva on 11/6/2014.
 */

@IBMDataObjectSpecialization("UserTrustCircles")
public class UserTrustCircles extends IBMDataObject{

    public static final String CLASS_NAME = "UserTrustCircles";

    // name of the user who has access or have applied for a trust circle
    private static final String USER_NAME ="userName";

    // name of the user who has access or have applied for a trust circle
    private static final String TRUST_NAME ="trustName";

    // captures the status of the connection
    // possible values are A Applied when user has applied for the access
    // , C Connected where the user has access for the trust circle
    // , R rejected when the user has removed the access
    private static final String STATUS = "status";


    /**
     * Gets the userName of the User.
     * @return String userName
     */
    public String getUserName() {
        return (String) getObject(USER_NAME);
    }

    /**
     * Sets the name of the user, as well as calls setCreationTime().
     * @param userName
     */
    public void setUserName(String userName) {
        setObject(USER_NAME, (userName != null) ? userName : "");
    }

    /**
     * Gets the trustName of the Trust Circle.
     * @return String userName
     */
    public String getTrustName() {
        return (String) getObject(TRUST_NAME);
    }

    /**
     * Sets the trustName of the Trust Circle
     * @param trustName
     */
    public void setTrustName(String trustName) {
        setObject(TRUST_NAME, (trustName != null) ? trustName : "");
    }

    /**
     * Gets the status of the Trust Circle connection.
     * @return String userName
     */
    public String getStatus() {
        return (String) getObject(STATUS);
    }

    /**
     * Sets the status of the Trust Circle connection
     * @param status
     */
    public void setStatus(String status) {
        setObject(STATUS, (status != null) ? status : "");
    }


}
