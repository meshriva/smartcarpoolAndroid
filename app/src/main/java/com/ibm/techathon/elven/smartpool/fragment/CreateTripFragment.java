package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.StoreMapActivity;
import com.ibm.techathon.elven.smartpool.activity.TripActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.util.IBMHttpResponseUtil;
import com.ibm.techathon.elven.smartpool.util.JSONUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTripFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTripFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    // string sent with activity result
    public static final int START_LOCATION_MAP_RESULT = 1;
    public static final int END_LOCATION_MAP_RESULT =2;

    // static variable class name for loggers
    public static final String CLASS_NAME="CreateTripFragment";

    // user details
    private static UserType mUser;

    // instance variable for the UI form
    private Switch mCircleOpen;
    private Spinner mCircleOptions;
    private AutoCompleteTextView mVechileRegisterationNumber;
    private EditText mVechileType;
    private Spinner mOpenSeats;
    private AutoCompleteTextView mStartLocationCity;
    private EditText mStartLocationPlace;
    private EditText mStartLocationDate;
    private EditText mStartLocationTime;
    private AutoCompleteTextView mEndLocationCity;
    private EditText mEndLocationPlace;
    private EditText mEndLocationDate;
    private EditText mEndLocationTime;

    private ImageView mStartLocationPlaceIcon;
    private ImageView mEndLocationPlaceIcon;

    private Button mSubmit;

    // variables to check which date and time has been clicked
    private boolean startDate;
    private boolean startTime;
    private boolean endDate;
    private boolean endTime;

    // default value is true
    private String mOpen = "true";

    // array for circle list
    private String[] circleOptions;

    // list of active trustCirlces
    private List<TrustCircle> mTrustCircleList;

    private ArrayAdapter<String> mCircleOptionsAdapter;

    private String mStartLocationLat;
    private String mStartLocationLong;
    private String mEndLocationLat;
    private String mEndLocationLong;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CreateTripFragment.
     */

    public static CreateTripFragment newInstance(UserType userType) {
        CreateTripFragment fragment = new CreateTripFragment();
        mUser = userType;
        return fragment;
    }

    public CreateTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView =  inflater.inflate(R.layout.fragment_create_trip, container, false);

        // set up the update user form
        mCircleOpen = (Switch) inflatedView.findViewById(R.id.switch_create_trip_circle_open);
        mCircleOptions = (Spinner)inflatedView.findViewById(R.id.spinner_create_trip_circles);
        mVechileRegisterationNumber = (AutoCompleteTextView) inflatedView.findViewById(R.id.autoCompleteTextView_create_trip_vechile_reg_num);
        mVechileType = (EditText) inflatedView.findViewById(R.id.editText_create_trip_vechile_type);
        mOpenSeats = (Spinner) inflatedView.findViewById(R.id.spinner_create_trip_open_seats);
        mStartLocationCity= (AutoCompleteTextView) inflatedView.findViewById(R.id.autoCompleteTextView_create_trip_start_location);
        mStartLocationPlace =(EditText) inflatedView.findViewById(R.id.editText_create_trip_start_location_place);
        mStartLocationDate = (EditText) inflatedView.findViewById(R.id.editText_create_trip_start_date);
        mStartLocationTime = (EditText) inflatedView.findViewById(R.id.editText_create_trip_start_time);
        //mActionGetStartDateTime = (Button) inflatedView.findViewById(R.id.button_create_trip_date_time);
        mEndLocationCity = (AutoCompleteTextView)inflatedView.findViewById(R.id.autoCompleteTextView_create_trip_end_location);
        mEndLocationPlace = (EditText) inflatedView.findViewById(R.id.editText_create_trip_end_location_place);
        mEndLocationDate = (EditText) inflatedView.findViewById(R.id.editText_create_trip_end_date);
        mEndLocationTime = (EditText) inflatedView.findViewById(R.id.editText_create_trip_end_time);
      //  mActionGetEndDateTime = (Button) inflatedView.findViewById(R.id.button_create_trip_date_time2);
        mSubmit = (Button) inflatedView.findViewById(R.id.button_submit_create_trip);
        mStartLocationPlaceIcon = (ImageView) inflatedView.findViewById(R.id.imageView_create_trip_start_location_place);
        mEndLocationPlaceIcon = (ImageView) inflatedView.findViewById(R.id.imageView_create_trip_end_location_place);

        // set up for date & time
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        mStartLocationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = true;
                datePickerDialog.setYearRange(2014, 2028);
                TripActivity activity = (TripActivity) getActivity();
                datePickerDialog.show(activity.getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        mStartLocationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = true;
                TripActivity activity = (TripActivity) getActivity();
                timePickerDialog.show(activity.getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });
        mEndLocationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate = true;
                datePickerDialog.setYearRange(2014, 2028);
                TripActivity activity = (TripActivity) getActivity();
                datePickerDialog.show(activity.getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        mEndLocationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime =true;
                TripActivity activity = (TripActivity) getActivity();
                timePickerDialog.show(activity.getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            TripActivity activity = (TripActivity)getActivity();
            DatePickerDialog dpd = (DatePickerDialog) activity.getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }


            TimePickerDialog tpd = (TimePickerDialog) activity.getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }

        }

        // set up for circle options spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        //mCircleOptionsAdapter = ArrayAdapter.createFromResource(getActivity(),
           //     R.array.spinner_create_trip_circles, android.R.layout.simple_spinner_item);
        ArrayList<String> mCircleOptionsList = new ArrayList<String>();
        mCircleOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.spinner_create_trip_circles)));
        mCircleOptionsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mCircleOptionsList
        );
        // Specify the layout to use when the list of choices appears
        mCircleOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCircleOptionsAdapter.setNotifyOnChange(true);
        // Apply the adapter to the spinner
        mCircleOptions.setAdapter(mCircleOptionsAdapter);

        // set action to update setting when switched is selected
        mCircleOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOpen ="false";
                    reloadCircleOptions(false);
                    // reload the adapter with the current trust circle values
                } else {
                    mOpen ="true";
                    reloadCircleOptions(true);

                    // The toggle is disabled
                    // update back the spinner to default to option.
                }
            }
        });

        // set up for open seats spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> openSeatsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_create_trip_open_seats, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        openSeatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mOpenSeats.setAdapter(openSeatsAdapter);

        // set up auto complete for current location
        String[] locations = getResources().
                getStringArray(R.array.locations);
        ArrayAdapter locationAdapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_list_item_1, locations);
        if (mStartLocationCity != null) {
            mStartLocationCity.setAdapter(locationAdapter);
        }
        if(mEndLocationCity!=null){
            mEndLocationCity.setAdapter(locationAdapter);
        }

        // onclick listener for maps icon for start location is clicked
        mStartLocationPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeMapIntent = new Intent(getActivity(),StoreMapActivity.class);
                startActivityForResult(storeMapIntent, START_LOCATION_MAP_RESULT);
            }
        });

        // onclick listener for maps icon for end location is clicked
        mEndLocationPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storeMapIntent = new Intent(getActivity(),StoreMapActivity.class);
                startActivityForResult(storeMapIntent, END_LOCATION_MAP_RESULT);
            }
        });

        // set up action when submit is submitted
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check if the user data passes the validation
                boolean cancel = false;
                View focusView = null;

                //Check if mVechileRegisterationNumber is empty
                if (TextUtils.isEmpty(mVechileRegisterationNumber.getText().toString())) {
                    mVechileRegisterationNumber.setError(getString(R.string.error_field_required));
                    focusView = mVechileRegisterationNumber;
                    cancel = true;
                }

                //Check if  mStartLocationCity is empty
                if (TextUtils.isEmpty(mStartLocationCity.getText().toString())) {
                    mStartLocationCity.setError(getString(R.string.error_field_required));
                    focusView = mStartLocationCity;
                    cancel = true;
                }

                //Check if  mStartLocationDate is empty
                if (TextUtils.isEmpty(mStartLocationDate.getText().toString())) {
                    mStartLocationDate.setError(getString(R.string.error_field_required));
                    focusView = mStartLocationDate;
                    cancel = true;
                }

                //Check if  mStartLocationTime is empty
                if (TextUtils.isEmpty(mStartLocationTime.getText().toString())) {
                    mStartLocationTime.setError(getString(R.string.error_field_required));
                    focusView = mStartLocationTime;
                    cancel = true;
                }

                //Check if  mEndLocationCity is empty
                if (TextUtils.isEmpty(mEndLocationCity.getText().toString())) {
                    mEndLocationCity.setError(getString(R.string.error_field_required));
                    focusView = mEndLocationCity;
                    cancel = true;
                }

                //Check if  mEndLocationDate is empty
                if (TextUtils.isEmpty(mEndLocationDate.getText().toString())) {
                    mEndLocationDate.setError(getString(R.string.error_field_required));
                    focusView = mEndLocationDate;
                    cancel = true;
                }

                //Check if  mEndLocationTime is empty
                if (TextUtils.isEmpty(mEndLocationTime.getText().toString())) {
                    mEndLocationTime.setError(getString(R.string.error_field_required));
                    focusView = mEndLocationTime;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else

                {
                    // validation done now try to submit the request to back end
                    createNewTrip();
                }

            }
        });

        return inflatedView;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(startDate){
            startDate =false;
            mStartLocationDate.setText(year + "-" + month + "-" + day);
        }else if(endDate){
            endDate =false;
            mEndLocationDate.setText(year + "-" + month + "-" + day);
        }
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if(startTime){
            startTime =false;
            mStartLocationTime.setText(hourOfDay + ":" + minute);
        }else if(endTime){
            endTime =false;
            mEndLocationTime.setText(hourOfDay + ":" + minute);
        }
    }


   public void createNewTrip(){
     // show a toast that update is happening
       Toast.makeText(getActivity(), "Submitting request to create new trip", Toast.LENGTH_LONG).show();

       // create the JSON request object
       // populate JSON object
       final JSONObject jsonObj = new JSONObject();
       UserType userType = ((TripActivity)getActivity()).mUser;

       try {
           if(mUser!=null){
               jsonObj.put("creator",mUser.getEmail());
           }
           jsonObj.put("vechileRegisterationNumber",mVechileRegisterationNumber.getText().toString());
           if(mVechileRegisterationNumber.getText()!=null) {
               jsonObj.put("vechileName",mVechileType.getText().toString());
           }
           jsonObj.put("openSeats",mOpenSeats.getSelectedItem().toString());

           jsonObj.put("startLocationCity",mStartLocationCity.getText().toString());
           jsonObj.put("startLocationPlace",mStartLocationPlace.getText().toString());

           jsonObj.put("startLocationLat",mStartLocationLat!=null ?mStartLocationLat:"0.00");
           jsonObj.put("startLocationLang",mStartLocationLong!=null ?mStartLocationLong:"0.00");

           jsonObj.put("startLocationDate",mStartLocationDate.getText().toString());
           jsonObj.put("startLocationTime",mStartLocationTime.getText().toString());

           jsonObj.put("endLocationCity",mEndLocationCity.getText().toString());
           jsonObj.put("endLocationPlace",mEndLocationPlace.getText().toString());


           jsonObj.put("endLocationLat",mEndLocationLat!=null ?mEndLocationLat:"0.00");
           jsonObj.put("endLocationLang",mEndLocationLong!=null ?mEndLocationLong:"0.00");

           jsonObj.put("endLocationDate",mEndLocationDate.getText().toString());
           jsonObj.put("endLocationTime",mEndLocationTime.getText().toString());

           jsonObj.put("open",mOpen);
           // as this is a new trip
           jsonObj.put("active","true");
           // get the trust id ,default value is 0
           String trustId = "0";
           String circleName = mCircleOptions.getSelectedItem().toString();
           if(mTrustCircleList!=null){
               for(TrustCircle trustCircle:mTrustCircleList){
                   if(circleName!=null && circleName.equals(trustCircle.getName())){
                       trustId = trustCircle.getTrustId();
                   }
               }
           }

           jsonObj.put("trustId",trustId);

       } catch (JSONException e) {
           e.printStackTrace();
       }

       SmartPoolApplication.cloudCodeService.post("trips", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                   if (200 == task.getResult().getHttpResponseCode()) {

                       // get TripId from request and subscriber for tag notification
                       String tripId = JSONUtil.getTripId(responseString);
                       if(tripId!=null){
                           ((SmartPoolApplication)getActivity().getApplication()).push.subscribe(tripId);
                       }
                       // update is done,move to the trip activity
                       Intent intent = new Intent(getActivity(), TripActivity.class);
                       intent.putExtra("user", mUser);
                       startActivity(intent);

                   } else {
                       Log.e(CLASS_NAME, "Unable to complete update of details");
                       Toast.makeText(getActivity(), "Unable to store details ,please try again later", Toast.LENGTH_LONG).show();

                   }
               }
               return null;
           }
       });

}

    /**
     * method is used to reload the circles available for the user
     * @param open
     */
    public void reloadCircleOptions(boolean open) {

        if (!open) {
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
                            mTrustCircleList = trustCircleList;
                        } else {
                            Log.e(CLASS_NAME, "Unable to get the details of details");

                        }
                        // once all the values are captured update the value set in adapter
                        if (mTrustCircleList != null) {

                            ArrayAdapter<String> adapter = ((ArrayAdapter<String>) mCircleOptions.getAdapter());
                            int count = 1;
                            ArrayList<String> trustCircleNameList = new ArrayList<String>();
                            for (TrustCircle trustCircle : mTrustCircleList) {
                                trustCircleNameList.add(trustCircle.getName());
                            }
                            adapter.addAll(trustCircleNameList);


                            adapter.notifyDataSetChanged();
                            Log.d(CLASS_NAME, "notifyDataSetChanged called");

                        }


                    }
                    return null;
                }
            });
        }else{

            if (mTrustCircleList != null) {
                ArrayAdapter<String> adapter = ((ArrayAdapter<String>) mCircleOptions.getAdapter());
                for (TrustCircle trustCircle : mTrustCircleList) {
                    adapter.remove(trustCircle.getName());
                }
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == START_LOCATION_MAP_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(),"Start Location information received from maps",Toast.LENGTH_SHORT).show();

                double locationLat =  data.getDoubleExtra(StoreMapActivity.LOCATION_POINT_LAT,0);
                double locationLong = data.getDoubleExtra(StoreMapActivity.LOCATION_POINT_LONG,0);

                Log.d(CLASS_NAME,"locationLat:"+locationLat+",locationLong:"+locationLong);

                // convert double to string and store the details
                mStartLocationLat = String.valueOf(locationLat);
                mStartLocationLong = String.valueOf(locationLong);


            }else{
                Toast.makeText(getActivity(),"Unable to get information from maps",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == END_LOCATION_MAP_RESULT){
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(),"End Location information received from maps",Toast.LENGTH_SHORT).show();

                double locationLat =  data.getDoubleExtra(StoreMapActivity.LOCATION_POINT_LAT,0);
                double locationLong = data.getDoubleExtra(StoreMapActivity.LOCATION_POINT_LONG,0);

                Log.d(CLASS_NAME,"locationLat:"+locationLat+",locationLong:"+locationLong);

                // convert double to string and store the details
                mEndLocationLat = String.valueOf(locationLat);
                mEndLocationLong = String.valueOf(locationLong);

            }else{
                Toast.makeText(getActivity(),"Unable to get information from maps",Toast.LENGTH_SHORT).show();
            }
        }


    }
}
