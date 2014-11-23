package com.ibm.techathon.elven.smartpool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ibm.techathon.elven.smartpool.R;

/**
 * Created by meshriva on 9/23/2014.
 */
public class StoreMapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {

    // static variable for continue button text
    private static final String CONTINUE_BUTTON_TEXT = "Continue";
    public static final String LOCATION_POINT_LAT = "locationPointLat";
    public static final String LOCATION_POINT_LONG = "locationPointLong";

    /**
     * instance of Google map object
     */
    private GoogleMap mMap;


    /**
     * current marker
     */
    private Marker mMarker;

    /**
     * selected lattitude and longitude
     *
     */
    private LatLng mPoint;


    //button for continue
    private Button mButton;

    /**
     * Used to store the result settings value
     *
     */
    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_map_activity);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.storeMap)).getMap();
        if (mMap == null) {
            Toast.makeText(this, "Google Maps not available",
                    Toast.LENGTH_LONG).show();
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);


        Location currentLocation = getMyLocation();

        if(currentLocation!=null){
            LatLng currentCoordinates = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, 12));
        }

        mButton = new Button(this);
        mButton.setText(CONTINUE_BUTTON_TEXT);
        mButton.setVisibility(View.GONE);
        addContentView(mButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // return the result to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra(LOCATION_POINT_LAT,mPoint.latitude);
                resultIntent.putExtra(LOCATION_POINT_LONG,mPoint.longitude);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is
        // present.
        getMenuInflater().inflate(R.menu.store_map_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_map_showcurrentlocation:

                Location myLocation = mMap.getMyLocation();
                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                        myLocation.getLongitude());

                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(myLatLng).zoom(17).bearing(90).tilt(30).build();
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(myPosition));
                break;

            case R.id.menu_map_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
        }
        return true;
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

    @Override
    public void onMapClick(LatLng point) {
        mPoint = point;

        if(mMarker!=null){
            mMarker.remove();
        }
        // add a marker on selected point
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED)));
        // make the overlay button visible
        mButton.setVisibility(View.VISIBLE);

    }
}

