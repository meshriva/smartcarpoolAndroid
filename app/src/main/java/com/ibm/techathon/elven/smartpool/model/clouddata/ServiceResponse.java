package com.ibm.techathon.elven.smartpool.model.clouddata;

/**
 * Created by meshriva on 11/10/2014.
 */
public class ServiceResponse<K> {

    private String status;
    private K response;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
