package com.ibm.techathon.elven.smartpool.model;

import java.io.Serializable;

/**
 * Created by meshriva on 11/14/2014.
 */
public class TrustCircle implements Serializable{

    private String name;
    private String desc;
    private String user;
    private String admin;
    private String location;
    private String open;
    private String active;
    private String trustId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TrustCircle{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", user='" + user + '\'' +
                ", admin='" + admin + '\'' +
                ", location='" + location + '\'' +
                ", open='" + open + '\'' +
                ", active='" + active + '\'' +
                ", trustId='" + trustId + '\'' +
                '}';
    }
}
