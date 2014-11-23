package com.ibm.techathon.elven.smartpool.util;

import android.util.Log;

import com.ibm.techathon.elven.smartpool.model.Trip;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meshriva on 11/17/2014.
 */
public class JSONUtil {

    // name of the class for logging purpose
    public static final String CLASS_NAME = "JSONUtil";

    public static List<TrustCircle> createTrustCircleList(String responseString) {

        List<TrustCircle> trustCircleList = new ArrayList<TrustCircle>();
        try {
            // deserialise the response string into JSON object
            JSONObject jObj = new JSONObject(responseString);
            JSONArray jsonArray = jObj.getJSONArray("reason");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject != null && jsonObject.getJSONObject("attributes") != null) {
                        // get the attribute object
                        JSONObject attribute = jsonObject.getJSONObject("attributes");
                        // set the value is trust circle variable
                        TrustCircle trustCircle = new TrustCircle();
                        trustCircle.setName(attribute.getString("circle_name") != null ? attribute.getString("circle_name") : "");
                        trustCircle.setDesc(attribute.getString("circle_desc") != null ? attribute.getString("circle_desc") : "");
                        trustCircle.setUser(attribute.getString("user") != null ? attribute.getString("user") : "");
                        trustCircle.setAdmin(attribute.getString("circle_admin") != null ? attribute.getString("circle_admin") : "");
                        trustCircle.setActive(attribute.getString("circle_active") != null ? attribute.getString("circle_active") : "");
                        trustCircle.setOpen(attribute.getString("circle_open") != null ? attribute.getString("circle_open") : "");
                        trustCircle.setTrustId(attribute.getString("trustId") != null ? attribute.getString("trustId") : "");

                        // add the class into the list
                        trustCircleList.add(trustCircle);

                    }
                }
            }
        } catch (JSONException e) {
            Log.d(CLASS_NAME, e.toString());
            e.printStackTrace();
        }
        return trustCircleList;
    }

    public static List<TrustCircle> getCurrentTrustCirclesFromCircleList(String responseString) {
        List<TrustCircle> trustCircleList = new ArrayList<TrustCircle>();
        // deserialise the response string into JSON object
        JSONObject jObj = null;
        JSONArray jsonArray = null;

        try {
            jObj = new JSONObject(responseString);
            jsonArray = jObj.getJSONArray("reason");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject != null && jsonObject.getJSONObject("attributes") != null) {
                        // get the attribute object
                        JSONObject attribute = jsonObject.getJSONObject("attributes");
                        // set the value is trust circle variable
                        TrustCircle trustCircle = new TrustCircle();
                        trustCircle.setName(attribute.getString("name") != null ? attribute.getString("name") : "");
                        trustCircle.setDesc(attribute.getString("desc") != null ? attribute.getString("desc") : "");
                        trustCircle.setUser(attribute.getString("admin") != null ? attribute.getString("admin") : "");
                        trustCircle.setAdmin(attribute.getString("admin") != null ? attribute.getString("admin") : "");
                        trustCircle.setActive(attribute.getString("active") != null ? attribute.getString("active") : "");
                        trustCircle.setOpen(attribute.getString("open") != null ? attribute.getString("open") : "");
                        JSONObject meta = jsonObject.getJSONObject("_meta");
                        if(meta!=null){
                            trustCircle.setTrustId(meta.getString("objectId") != null ? meta.getString("objectId") : "");
                        }

                        // add the class into the list
                        trustCircleList.add(trustCircle);
                    }

                }

            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return trustCircleList;
    }


    public static List<TrustCircle> getCurrentTrustCircles(String responseString) {
        List<TrustCircle> trustCircleList = new ArrayList<TrustCircle>();
        // deserialise the response string into JSON object
        JSONObject jObj = null;
        JSONArray jsonArray = null;

        try {
            jObj = new JSONObject(responseString);
            jsonArray = jObj.getJSONArray("reason");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject != null && jsonObject.getJSONObject("attributes") != null) {
                        // get the attribute object
                        JSONObject attribute = jsonObject.getJSONObject("attributes");
                        // set the value is trust circle variable
                        TrustCircle trustCircle = new TrustCircle();
                        trustCircle.setName(attribute.getString("circle_name") != null ? attribute.getString("circle_name") : "");
                        trustCircle.setDesc(attribute.getString("circle_desc") != null ? attribute.getString("circle_desc") : "");
                        trustCircle.setUser(attribute.getString("user") != null ? attribute.getString("user") : "");
                        trustCircle.setAdmin(attribute.getString("circle_admin") != null ? attribute.getString("circle_admin") : "");
                        trustCircle.setActive(attribute.getString("circle_active") != null ? attribute.getString("circle_active") : "");
                        trustCircle.setOpen(attribute.getString("circle_open") != null ? attribute.getString("circle_open") : "");
                        trustCircle.setTrustId(attribute.getString("trustId") != null ? attribute.getString("trustId") : "");

                        // add the class into the list
                        trustCircleList.add(trustCircle);
                    }

                }

            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return trustCircleList;
    }

    /**
     * get tripId from JSON response for create trip
     * @param responseString
     * @return String
     */
    public static String getTripId(String responseString){
        String tripId = null;
        JSONObject jObj = null;
        JSONObject tripResponse = null;
        JSONObject attributes = null;
        try {
            jObj = new JSONObject(responseString);
            tripResponse = jObj.getJSONObject("reason");
            if(tripResponse!=null){
               attributes = tripResponse.getJSONObject("attributes");
            }
            if(attributes!=null){
                tripId = attributes.getString("tripId");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return tripId;
    }

    public static List<Trip> getTripList(String responseString) {
        return getCurrentTrips(responseString);
    }

    public static List<Trip> getCurrentTrips(String responseString) {
        List<Trip> tripList = new ArrayList<Trip>();
        // deserialise the response string into JSON object
        JSONObject jObj = null;
        JSONArray jsonArray = null;

        try {
            jObj = new JSONObject(responseString);
            jsonArray = jObj.getJSONArray("reason");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject != null && jsonObject.getJSONObject("attributes") != null) {
                        Trip trip = new Trip();

                        // get the _meta object
                        JSONObject meta = jsonObject.getJSONObject("_meta");
                        //get object id
                        trip.setId(meta.getString("objectId") != null ? meta.getString("objectId") : "");
                        // get the attribute object
                        JSONObject attribute = jsonObject.getJSONObject("attributes");

                        // set the value is trust circle variable

                        trip.setCreator(attribute.getString("creator") != null ? attribute.getString("creator") : "");
                        trip.setVechileRegisterationNumber(attribute.getString("vechileRegisterationNumber") != null ? attribute.getString("vechileRegisterationNumber") : "");
                        trip.setVechileName(attribute.getString("vechileName") != null ? attribute.getString("vechileName") : "");
                        trip.setOpenSeats(attribute.getString("openSeats") != null ? attribute.getString("openSeats") : "");
                        trip.setStartLocationCity(attribute.getString("startLocationCity") != null ? attribute.getString("startLocationCity") : "");
                        trip.setStartLocationPlace(attribute.getString("startLocationPlace") != null ? attribute.getString("startLocationPlace") : "");
                        trip.setStartLocationLat(attribute.getString("startLocationLat") != null ? attribute.getString("startLocationLat") : "");
                        trip.setStartLocationLang(attribute.getString("startLocationLang") != null ? attribute.getString("startLocationLang") : "");
                        trip.setStartLocationDate(attribute.getString("startLocationDate") != null ? attribute.getString("startLocationDate") : "");
                        trip.setStartLocationTime(attribute.getString("startLocationTime") != null ? attribute.getString("startLocationTime") : "");
                        trip.setEndLocationCity(attribute.getString("endLocationCity") != null ? attribute.getString("endLocationCity") : "");
                        trip.setEndLocationPlace(attribute.getString("endLocationPlace") != null ? attribute.getString("endLocationPlace") : "");
                        trip.setEndLocationLat(attribute.getString("endLocationLat") != null ? attribute.getString("endLocationLat") : "");
                        trip.setEndLocationLang(attribute.getString("endLocationLang") != null ? attribute.getString("endLocationLang") : "");
                        trip.setEndLocationDate(attribute.getString("endLocationDate") != null ? attribute.getString("endLocationDate") : "");
                        trip.setEndLocationTime(attribute.getString("endLocationTime") != null ? attribute.getString("endLocationTime") : "");
                        trip.setOpen(attribute.getString("open") != null ? attribute.getString("open") : "");
                        trip.setActive(attribute.getString("active") != null ? attribute.getString("active") : "");
                        trip.setTrustId(attribute.getString("trustId") != null ? attribute.getString("trustId") : "");


                        // add the class into the list
                        tripList.add(trip);
                    }

                }

            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return tripList;
    }



}
