package com.ibm.techathon.elven.smartpool.model;

import java.io.Serializable;

/**
 * Created by meshriva on 11/20/2014.
 */
public class Trip implements Serializable {

    private String creator;
    private String vechileRegisterationNumber;
    private String vechileName;
    private String openSeats;
    private String startLocationCity;
    private String startLocationPlace;
    private String startLocationLat;
    private String startLocationLang;
    private String startLocationDate;
    private String startLocationTime;
    private String endLocationCity;
    private String endLocationPlace;
    private String endLocationLat;
    private String endLocationLang;
    private String endLocationDate;
    private String endLocationTime;
    private String open;
    private String active;
    private String trustId;
    private String id;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVechileRegisterationNumber() {
        return vechileRegisterationNumber;
    }

    public void setVechileRegisterationNumber(String vechileRegisterationNumber) {
        this.vechileRegisterationNumber = vechileRegisterationNumber;
    }

    public String getOpenSeats() {
        return openSeats;
    }

    public void setOpenSeats(String openSeats) {
        this.openSeats = openSeats;
    }

    public String getStartLocationCity() {
        return startLocationCity;
    }

    public void setStartLocationCity(String startLocationCity) {
        this.startLocationCity = startLocationCity;
    }

    public String getStartLocationPlace() {
        return startLocationPlace;
    }

    public void setStartLocationPlace(String startLocationPlace) {
        this.startLocationPlace = startLocationPlace;
    }

    public String getStartLocationLang() {
        return startLocationLang;
    }

    public void setStartLocationLang(String startLocationLang) {
        this.startLocationLang = startLocationLang;
    }

    public String getStartLocationLat() {
        return startLocationLat;
    }

    public void setStartLocationLat(String startLocationLat) {
        this.startLocationLat = startLocationLat;
    }

    public String getStartLocationTime() {
        return startLocationTime;
    }

    public void setStartLocationTime(String startLocationTime) {
        this.startLocationTime = startLocationTime;
    }

    public String getStartLocationDate() {
        return startLocationDate;
    }

    public void setStartLocationDate(String startLocationDate) {
        this.startLocationDate = startLocationDate;
    }

    public String getEndLocationCity() {
        return endLocationCity;
    }

    public void setEndLocationCity(String endLocationCity) {
        this.endLocationCity = endLocationCity;
    }

    public String getEndLocationPlace() {
        return endLocationPlace;
    }

    public void setEndLocationPlace(String endLocationPlace) {
        this.endLocationPlace = endLocationPlace;
    }

    public String getEndLocationLat() {
        return endLocationLat;
    }

    public void setEndLocationLat(String endLocationLat) {
        this.endLocationLat = endLocationLat;
    }

    public String getEndLocationLang() {
        return endLocationLang;
    }

    public void setEndLocationLang(String endLocationLang) {
        this.endLocationLang = endLocationLang;
    }

    public String getEndLocationDate() {
        return endLocationDate;
    }

    public void setEndLocationDate(String endLocationDate) {
        this.endLocationDate = endLocationDate;
    }

    public String getEndLocationTime() {
        return endLocationTime;
    }

    public void setEndLocationTime(String endLocationTime) {
        this.endLocationTime = endLocationTime;
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

    public String getVechileName() {
        return vechileName;
    }

    public void setVechileName(String vechileName) {
        this.vechileName = vechileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                "creator='" + creator + '\'' +
                ", vechileRegisterationNumber='" + vechileRegisterationNumber + '\'' +
                ", vechileName='" + vechileName + '\'' +
                ", openSeats='" + openSeats + '\'' +
                ", startLocationCity='" + startLocationCity + '\'' +
                ", startLocationPlace='" + startLocationPlace + '\'' +
                ", startLocationLat='" + startLocationLat + '\'' +
                ", startLocationLang='" + startLocationLang + '\'' +
                ", startLocationDate='" + startLocationDate + '\'' +
                ", startLocationTime='" + startLocationTime + '\'' +
                ", endLocationCity='" + endLocationCity + '\'' +
                ", endLocationPlace='" + endLocationPlace + '\'' +
                ", endLocationLat='" + endLocationLat + '\'' +
                ", endLocationLang='" + endLocationLang + '\'' +
                ", endLocationDate='" + endLocationDate + '\'' +
                ", endLocationTime='" + endLocationTime + '\'' +
                ", open='" + open + '\'' +
                ", active='" + active + '\'' +
                ", trustId='" + trustId + '\'' +
                '}';
    }
}
