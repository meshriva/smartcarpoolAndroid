package com.ibm.techathon.elven.smartpool.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
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
import com.ibm.techathon.elven.smartpool.fragment.CreateNewCircleFragment;
import com.ibm.techathon.elven.smartpool.fragment.CurrentCirclesFragment;
import com.ibm.techathon.elven.smartpool.fragment.SearchCirclesFragment;
import com.ibm.techathon.elven.smartpool.fragment.UpdateCircleAccessRequestFragment;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.UserType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class CircleActivity extends Activity implements ActionBar.OnNavigationListener {

    /*
     *  class name used in the logs
      */
    public static final String CLASS_NAME="CircleActivity";

    /**
     * Used to store the result settings value
     *
     */
    private static final int RESULT_SETTINGS = 1;


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
    private String[] circleNavigationOptions;

    // user details sent along with the intent
    public UserType mUser;

    // list of Trust Circle
    public String mTrustCircleCardResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        // get the navigation options
        circleNavigationOptions = getResources().getStringArray(R.array.circle_activity_navigation_options);

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
                        circleNavigationOptions),
                this);

        // get the current passed intent
        Intent intent = getIntent();
        // get the email address from the intent
        mUser = (UserType) intent.getSerializableExtra("user");
        if(intent.getSerializableExtra("trustCircleCardList")!=null){
            mTrustCircleCardResponse = (String) intent.getSerializableExtra("trustCircleCardList");
            Log.d(CLASS_NAME,"mTrustCircleCardResponse:"+mTrustCircleCardResponse);
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
        getMenuInflater().inflate(R.menu.menu_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, UserSettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;
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
                fragment = SearchCirclesFragment.newInstance();
                break;
            case 1:
                // call a method which will get the current circles
                isFragment = false;
                getCurrentCircles();
                break;
            case 2:
                fragment = CreateNewCircleFragment.newInstance(mUser);
                break;
            case 3:
                isFragment =false;
                getPendingCircleRequest();

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
     * class to invoke IBM Cloud service to get current circles
     */
    public void getCurrentCircles(){

        if(mUser ==null){
            this.mUser = ((SmartPoolApplication)getApplication()).mUser ;
        }
       String uri = "/circleUsers/"+mUser.getEmail();
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
                    InputStream is = task.getResult().getInputStream();
                    String responseString = "";
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        String myString = "";
                        while ((myString = in.readLine()) != null)
                            responseString += myString;

                        in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode()+" Response Body: "+responseString);
                    // create a list of TrustCircle data type
                    List<TrustCircle> trustCircleList = null;
                    if (200 == task.getResult().getHttpResponseCode()) {

                        // deserialise the response and call the next fragment

                        trustCircleList = new ArrayList<TrustCircle>();
                        // deserialise the response string into JSON object
                        JSONObject jObj = new JSONObject(responseString);
                        JSONArray jsonArray = jObj.getJSONArray("reason");

                        if(jsonArray!=null){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if(jsonObject!=null && jsonObject.getJSONObject("attributes")!=null){
                                    // get the attribute object
                                    JSONObject attribute = jsonObject.getJSONObject("attributes");
                                    // set the value is trust circle variable
                                    TrustCircle trustCircle = new TrustCircle();
                                    trustCircle.setName(attribute.getString("circle_name")!=null ? attribute.getString("circle_name"):"");
                                    trustCircle.setDesc(attribute.getString("circle_desc")!=null ? attribute.getString("circle_desc"):"");
                                    trustCircle.setUser(attribute.getString("user")!=null ? attribute.getString("user"):"");
                                    trustCircle.setAdmin(attribute.getString("circle_admin")!=null ? attribute.getString("circle_admin"):"");
                                    trustCircle.setActive(attribute.getString("circle_active")!=null ? attribute.getString("circle_active"):"");
                                    trustCircle.setOpen(attribute.getString("circle_open")!=null ? attribute.getString("circle_open"):"");
                                    trustCircle.setTrustId(attribute.getString("trustId")!=null ? attribute.getString("trustId"):"");

                                    // add the class into the list
                                    trustCircleList.add(trustCircle);
                                }

                            }
                        }



                    } else {
                        Log.e(CLASS_NAME, "Unable to complete update of details");

                    }
                    // once all the values are captured pass the details to the next fragement
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = CurrentCirclesFragment.newInstance(trustCircleList);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
                return null;
            }
        });
    }

    /**
     * class to invoke IBM Cloud service to get pending circle requests
     */
    public void getPendingCircleRequest(){
        if(mUser ==null){
            this.mUser = ((SmartPoolApplication)getApplication()).mUser ;
        }
        String uri = "circleUsersPending/"+mUser.getEmail();
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
                    InputStream is = task.getResult().getInputStream();
                    String responseString = "";
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        String myString = "";
                        while ((myString = in.readLine()) != null)
                            responseString += myString;

                        in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode()+" Response Body: "+responseString);
                    // create a list of TrustCircle data type
                    List<TrustCircle> trustCircleList = null;
                    if (200 == task.getResult().getHttpResponseCode()) {

                        // deserialise the response and call the next fragment

                        trustCircleList = new ArrayList<TrustCircle>();
                        // deserialise the response string into JSON object
                        JSONObject jObj = new JSONObject(responseString);
                        JSONArray jsonArray = jObj.getJSONArray("reason");

                        if(jsonArray!=null){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if(jsonObject!=null && jsonObject.getJSONObject("attributes")!=null){
                                    // get the attribute object
                                    JSONObject attribute = jsonObject.getJSONObject("attributes");
                                    // set the value is trust circle variable
                                    TrustCircle trustCircle = new TrustCircle();
                                    trustCircle.setName(attribute.getString("circle_name")!=null ? attribute.getString("circle_name"):"");
                                    trustCircle.setDesc(attribute.getString("circle_desc")!=null ? attribute.getString("circle_desc"):"");
                                    trustCircle.setUser(attribute.getString("user")!=null ? attribute.getString("user"):"");
                                    trustCircle.setAdmin(attribute.getString("circle_admin")!=null ? attribute.getString("circle_admin"):"");
                                    trustCircle.setActive(attribute.getString("circle_active")!=null ? attribute.getString("circle_active"):"");
                                    trustCircle.setOpen(attribute.getString("circle_open")!=null ? attribute.getString("circle_open"):"");
                                    trustCircle.setTrustId(attribute.getString("trustId")!=null ? attribute.getString("trustId"):"");

                                    // add the class into the list
                                    trustCircleList.add(trustCircle);
                                }

                            }
                        }



                    } else {
                        Log.e(CLASS_NAME, "Unable to complete update of details");

                    }
                    // once all the values are captured pass the details to the next fragement
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = UpdateCircleAccessRequestFragment.newInstance(trustCircleList);
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
            View rootView = inflater.inflate(R.layout.fragment_circle, container, false);
            return rootView;
        }
    }

}
