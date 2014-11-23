package com.ibm.techathon.elven.smartpool.model.ibmdata;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

/**
 * data type class for trust circle
 * Created by meshriva on 11/6/2014.
 */
@IBMDataObjectSpecialization("TrustCircle")
public class TrustCircle extends IBMDataObject {

    public static final String CLASS_NAME = "TrustCircle";

    // name of the trust circle
    private static final String NAME ="name";
    // a general description fo the trust circle
    private static final String DESCRIPTION ="description";
    // captures the email address of the user who has created the trust circle
    // depending upon trust circle being as an exclusive one the owner will have ability
    // to decide if the user can decide
    private static final String OWNER ="owner";
    // if the trust circle is open, it will be available for a;l the users
    private static final String OPEN ="open";
    // if the trust circle is still active , then this filed will be set to true
    private static final String ACTIVE= "active";


    /**
     * Gets the name of the TrustCircle.
     * @return String name
     */
    public String getName() {
        return (String) getObject(NAME);
    }

    /**
     * Sets the name of a TrustCircle, as well as calls setCreationTime().
     * @param name
     */
    public void setName(String name) {
        setObject(NAME, (name != null) ? name : "");
    }

    /**
     * Gets the description of the TrustCircle.
     * @return String description
     */
    public String getDescription() {
        return (String) getObject(DESCRIPTION);
    }

    /**
     * Sets the description of a TrustCircle, as well as calls setCreationTime().
     * @param description
     */
    public void setDescription(String description) {
        setObject(DESCRIPTION, (description != null) ? description : "");
    }

    /** Gets the owner of the TrustCircle.
            * @return String owner
    */
    public String getOwner() {
        return (String) getObject(OWNER);
    }

    /**
     * Sets the owner of a TrustCircle, as well as calls setCreationTime().
     * @param owner
     */
    public void setOwner(String owner) {
        setObject(OWNER, (owner != null) ? owner : "");
    }

    /** Gets the open of the TrustCircle.
     * @return String open
     */
    public String getOpen() {
        return (String) getObject(OPEN);
    }

    /**
     * Sets the open of a TrustCircle, as well as calls setCreationTime().
     * @param open
     */
    public void setOpen(String open) {
        setObject(OPEN, (open != null) ? open : "");
    }

}
