package com.ibm.techathon.elven.smartpool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ibm.techathon.elven.smartpool.R;

/**
 * Created by meshriva on 11/20/2014.
 */
public class LineMapActivity extends FragmentActivity {

    public static final String START_LOCATION_LAT = "startPointLat";
    public static final String START_LOCATION_LONG ="startPointLong";
    public static final String END_LOCATION_LAT = "endPointLat";
    public static final String END_LOCATION_LONG ="endPointLong";
    public static final String LOCATION_SAME_CITY ="sameCity";


    private String mStartPointLat;
    private String mStartPointLong;
    private String mEndPointLat;
    private String mEndPointLong;

    private boolean mSameCity;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_map_activity);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lineMap)).getMap();
        if (mMap == null) {
            Toast.makeText(this, "Google Maps not available",
                    Toast.LENGTH_LONG).show();
        }
        mMap.setMyLocationEnabled(true);
        Location currentLocation = getMyLocation();

        Intent intent = getIntent();

        mStartPointLat = intent.getStringExtra(START_LOCATION_LAT);
        mStartPointLong = intent.getStringExtra(START_LOCATION_LONG);
        mEndPointLat = intent.getStringExtra(END_LOCATION_LAT);
        mEndPointLong = intent.getStringExtra(END_LOCATION_LONG);
        mSameCity = intent.getBooleanExtra(LOCATION_SAME_CITY,true);

        // get the location of start point and end point
        LatLng startPoint = new LatLng(Double.parseDouble(mStartPointLat),Double.parseDouble(mStartPointLong));
        LatLng endPoint = new LatLng(Double.parseDouble(mEndPointLat),Double.parseDouble(mEndPointLong));

        // -- add a marker for start point
        mMap.addMarker(new MarkerOptions()
                .position(startPoint)
                .title("Start Point")
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE)));

        // -- add a marker for end point
        mMap.addMarker(new MarkerOptions()
                .position(endPoint)
                .title("End Point")
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_BLUE)));

        mMap.addPolyline(new PolylineOptions()
                .add(startPoint, endPoint).width(3).color(Color.RED));
        int zoom =12;
        if(!mSameCity){
            zoom =6;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, zoom));
    }

    private Location getMyLocation() {
        // Get location from GPS if it's available
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;
    }

}