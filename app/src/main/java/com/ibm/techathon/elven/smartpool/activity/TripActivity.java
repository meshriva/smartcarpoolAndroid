package com.ibm.techathon.elven.smartpool.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.fragment.CreateTripFragment;
import com.ibm.techathon.elven.smartpool.fragment.CurrentTripsFragment;
import com.ibm.techathon.elven.smartpool.fragment.SearchTripsFragment;
import com.ibm.techathon.elven.smartpool.model.Trip;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.util.IBMHttpResponseUtil;
import com.ibm.techathon.elven.smartpool.util.JSONUtil;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class TripActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    // static class variable
    public static final String CLASS_NAME ="TripActivity";

    // trip list
    private static List<Trip> mTripList;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // list of options for CircleActivitiy
    private String[] tripNavigationOptions;

    // user details sent along with the intent
    public UserType mUser;

    // list of Trust Circle
    public String mTripCardResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        // get the navigation options
        tripNavigationOptions = getResources().getStringArray(R.array.home_activity_trip_navigation_options);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // Show the Up button in the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                       tripNavigationOptions),
                this);

        // get the current passed intent
        Intent intent = getIntent();
        // get the email address from the intent
        mUser = (UserType) intent.getSerializableExtra("user");
        if(intent.getSerializableExtra("tripCardList")!=null){
            mTripCardResponse = (String) intent.getSerializableExtra("tripCardList");
            Log.d(CLASS_NAME,"mTripCardResponse:"+mTripCardResponse);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // intialise the fragment manager
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        boolean isFragment =true;
        switch(position){
            case 0:
                isFragment = false;
                getCurrentCircles();
                break;

            case 1:
                isFragment = false;
                getCurrentTrips();
                break;

            case 2:
            fragment = CreateTripFragment.newInstance(mUser);
                break;

            default:
                fragment = PlaceholderFragment.newInstance(position + 1);
        }
        // add the section number as bundle argument
        if(isFragment) {
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        return true;
    }

    /**
     * class to invoke IBM Cloud service to get current trips
     */
    public void getCurrentTrips(){

        if(mUser ==null){
            this.mUser = ((SmartPoolApplication)getApplication()).mUser ;
        }
        String uri ="trips/find/activeTrips/"+mUser.getEmail();
        SmartPoolApplication.cloudCodeService.get(uri).continueWith(new Continuation<IBMHttpResponse, Void>() {

            @Override
            public Void then(Task<IBMHttpResponse> task) throws Exception {
                // first remove the progress bar
                // showProgress(false);
                if (task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                } else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                } else {
                    String responseString = IBMHttpResponseUtil.getResponseBody(task.getResult().getInputStream());
                    Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode()+" Response Body: "+responseString);
                    // create a list of TrustCircle data type
                    List<Trip> tripArrayList = null;
                    if (200 == task.getResult().getHttpResponseCode()) {

                        // deserialise the response and call the next fragment
                        mTripList = JSONUtil.getCurrentTrips(responseString);

                    } else {
                        Log.e(CLASS_NAME, "Unable to complete update of details");

                    }
                    // once all the values are captured pass the details to the next fragement
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = CurrentTripsFragment.newInstance(mUser,mTripList);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
                return null;
            }
        });
    }


    /**
     * method is used to load the circles available for the user
     * and once the list is received call the SearchTripsFragment class
     */
    public void getCurrentCircles() {
        if(mUser ==null){
            this.mUser = ((SmartPoolApplication)getApplication()).mUser ;
        }
            String uri = "/circleUsers/" + mUser.getEmail();
            SmartPoolApplication.cloudCodeService.get(uri).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                    } else {
                        String responseString = IBMHttpResponseUtil.getResponseBody(task.getResult().getInputStream());
                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode() + " Response Body: " + responseString);
                        // create a list of TrustCircle data type
                        List<TrustCircle> trustCircleList = null;
                        if (200 == task.getResult().getHttpResponseCode()) {
                            // deserialise the response and call the next fragment
                            trustCircleList = JSONUtil.getCurrentTrustCircles(responseString);

                        } else {
                            Log.e(CLASS_NAME, "Unable to get the details of details");

                        }
                        // once all the values are captured send the details to the required fragement
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment fragment = SearchTripsFragment.newInstance(mUser,trustCircleList);
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();

                    }
                    return null;
                }
            });
        }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_trip, container, false);
            return rootView;
        }
    }

}
