package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.TripActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.cards.SuggestedCard;
import com.ibm.techathon.elven.smartpool.model.Trip;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.util.IBMHttpResponseUtil;
import com.ibm.techathon.elven.smartpool.util.JSONUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTripsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

     // static tags used for date and time picker
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    // static string to check if trip is open
    public static final String OPEN_TO_ALL ="Open to all";
    public static final String OPEN_TO_ALL_TRUST_ID ="0";

    // static variable for userType
    private static UserType mUser;

    // static variable for trustCircleList
    private static List<TrustCircle> mTrustCircleList;

    // class name used for logging purpose
    public static final String CLASS_NAME = "SearchCirclesFragment";

    // static variable to store the activity
    private static Activity mActivity;

    // form instance variables
    private AutoCompleteTextView mStartLocation;
    private EditText mStartLocationDate;
    private EditText mStartLocationTime;
    private AutoCompleteTextView mEndLocation;
    private EditText mEndLocationDate;
    private EditText mEndLocationTime;
    private Spinner mCircleOptions;
    private TextView mNoDataText;
    private CardArrayAdapter mCardArrayAdapter;
    private CardListView mListView;
    private Button mSearchButton;
    private Button mShowSearchOptionsButton;

    // variables to check which date and time has been clicked
    private boolean startDate;
    private boolean startTime;
    private boolean endDate;
    private boolean endTime;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType
     * @param trustCirclesList
     * @return A new instance of fragment SearchTripsFragment.
     */
    public static SearchTripsFragment newInstance(UserType userType, List<TrustCircle> trustCirclesList) {
        SearchTripsFragment fragment = new SearchTripsFragment();
        mUser = userType;
        mTrustCircleList =trustCirclesList;
        return fragment;
    }

    public SearchTripsFragment() {
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
        View inflatedView = inflater.inflate(R.layout.fragment_search_trips, container, false);

        // get Activity copied in a variable
        mActivity = getActivity();

        // set up the form
        mStartLocation = (AutoCompleteTextView) inflatedView.findViewById(R.id.editText_search_trips_location);
        mStartLocationDate = (EditText) inflatedView.findViewById(R.id.editText_search_trips_date);
        mStartLocationTime = (EditText) inflatedView.findViewById(R.id.editText_search_trips_time);
        mEndLocation = (AutoCompleteTextView) inflatedView.findViewById(R.id.editText_search_trips_end_location);
        mEndLocationDate = (EditText) inflatedView.findViewById(R.id.editText_search_trips_end_date);
        mEndLocationTime = (EditText) inflatedView.findViewById(R.id.editText_search_trips_end_time);
        mCircleOptions =(Spinner) inflatedView.findViewById(R.id.spinner_search_trips_circles_open);
        mNoDataText = (TextView) inflatedView.findViewById(R.id.button_show_search_trips_options);
        mListView =(CardListView) inflatedView.findViewById(R.id.card_search_trips_list);


        String[] locations = getResources().
                getStringArray(R.array.locations);

        // set up auto complete for start location
        ArrayAdapter startLocationAdapter = new ArrayAdapter
                (mActivity,android.R.layout.simple_list_item_1,locations);
        mStartLocation.setAdapter(startLocationAdapter);

        // set up auto complete for end location
        ArrayAdapter endLocationAdapter = new ArrayAdapter
                (mActivity,android.R.layout.simple_list_item_1,locations);
        mEndLocation.setAdapter(endLocationAdapter);

        // set up for date and time listeners for both start and end location
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

        // set up adapter for the circles spinner
        ArrayList<String> mCircleOptionsList = new ArrayList<String>();
        mCircleOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.spinner_create_trip_circles)));
        // populate the current circles
        if(mTrustCircleList!=null){
            for(TrustCircle trustCircle:mTrustCircleList){
                mCircleOptionsList.add(trustCircle.getName());
            }
        }
        ArrayAdapter<String> mCircleOptionsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mCircleOptionsList
        );
        // Specify the layout to use when the list of choices appears
        mCircleOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCircleOptionsAdapter.setNotifyOnChange(true);
        // Apply the adapter to the spinner
        mCircleOptions.setAdapter(mCircleOptionsAdapter);

        // create an adapter for the cards list
        String tripCardResponse = ((TripActivity)getActivity()).mTripCardResponse;
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), getTripCards(tripCardResponse));

        // update the view
        if (mListView!= null) {
            mListView.setAdapter(mCardArrayAdapter);
        }

        mSearchButton = (Button) inflatedView.findViewById(R.id.button_search_trips);
        mShowSearchOptionsButton= (Button) inflatedView.findViewById(R.id.button_show_search_trips_options);

        mShowSearchOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send it back to the circle activity with no trustCircleCardList
                Intent intent = new Intent(getActivity(), TripActivity.class);
                TripActivity tripActivity = (TripActivity) mActivity;
                intent.putExtra("user",tripActivity.mUser);
                startActivity(intent);

            }
        });

        // set up button and listener for search
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTrips();
            }
        });

        if(tripCardResponse==null || tripCardResponse==""){
            mShowSearchOptionsButton.setVisibility(View.GONE);
        }else{

            ((TextView)(inflatedView.findViewById(R.id.textView_search_trips))).setVisibility(View.GONE);
            ((View)(inflatedView.findViewById(R.id.divider_search_trips))).setVisibility(View.GONE);

            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_trips_location))).setVisibility(View.GONE);
            mStartLocation.setVisibility(View.GONE);

            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_trips_time))).setVisibility(View.GONE);
            mStartLocationDate.setVisibility(View.GONE);
            mStartLocationTime.setVisibility(View.GONE);

            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_trips_end_location))).setVisibility(View.GONE);
            mEndLocation.setVisibility(View.GONE);

            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_trips_end_time))).setVisibility(View.GONE);
            mEndLocationDate.setVisibility(View.GONE);
            mEndLocationTime.setVisibility(View.GONE);

            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_trips_circle_open))).setVisibility(View.GONE);
            mCircleOptions.setVisibility(View.GONE);

            mSearchButton.setVisibility(View.GONE);

            ((View)(inflatedView.findViewById(R.id.linearLayout_start_location))).setVisibility(View.GONE);
            ((View)(inflatedView.findViewById(R.id.linearLayout_start_date_time))).setVisibility(View.GONE);
            ((View)(inflatedView.findViewById(R.id.linearLayout_end_location))).setVisibility(View.GONE);
            ((View)(inflatedView.findViewById(R.id.linearLayout_end_date_time))).setVisibility(View.GONE);
        }


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

    /**
     * method to call back end cloud service and get the list of trips
     * based on the search parameters
     */
    public void searchTrips(){

        // create JSON object
        final JSONObject jsonObj = new JSONObject();
        final JSONObject tripQuery = new JSONObject();
        try {

            if(mStartLocation!=null && !mStartLocation.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mStartLocation:"+mStartLocation.getText().toString()+":");
                tripQuery.put("startLocationCity",mStartLocation.getText().toString());
            }

            if(mStartLocationDate!=null && !mStartLocationDate.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mStartLocationDate:"+mStartLocationDate.getText().toString()+":");
                tripQuery.put("startLocationDate",mStartLocationDate.getText().toString());
            }

            if(mStartLocationTime!=null && !mStartLocationTime.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mStartLocationTime:"+mStartLocationTime.getText().toString()+":");
                tripQuery.put("startLocationTime",mStartLocationTime.getText().toString());
            }

            if(mEndLocation!=null && !mEndLocation.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mEndLocation:"+mEndLocation.getText().toString()+":");
                tripQuery.put("endLocationCity",mEndLocation.getText().toString());
            }

            if(mEndLocationDate!=null && !mEndLocationDate.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mEndLocationDate:"+mEndLocationDate.getText().toString()+":");
                tripQuery.put("endLocationDate",mEndLocationDate.getText().toString());
            }

            if(mEndLocationTime!=null && !mEndLocationTime.getText().toString().trim().equals("")){
                Log.d(CLASS_NAME,"mEndLocationTime:"+mEndLocationTime.getText().toString()+":");
                tripQuery.put("endLocationTime",mEndLocationTime.getText().toString());
            }

            String trustId =OPEN_TO_ALL_TRUST_ID;
            if(mCircleOptions!=null && !mCircleOptions.getSelectedItem().toString().equals(OPEN_TO_ALL)){
                 if(mTrustCircleList!=null){
                     for(TrustCircle trustCircle:mTrustCircleList){
                         if(trustCircle!=null && trustCircle.getName().equals(mCircleOptions.getSelectedItem().toString())){
                             trustId = trustCircle.getTrustId();
                             break;
                         }
                     }
                 }
            }

            Log.d(CLASS_NAME,"trustId:"+trustId+":");
            tripQuery.put("trustId",trustId);

            jsonObj.put("tripQuery",tripQuery);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // connect to back end to get current circles
        SmartPoolApplication.cloudCodeService.post("trips/find", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                    // get the response body
                    String responseString = IBMHttpResponseUtil.getResponseBody(is);

                    Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode()+" Response Body: "+responseString);
                    if (200 == task.getResult().getHttpResponseCode()) {

                        // update the content of the fragment with the task information
                        // List<TrustCircle> trustCircleList = JSONUtil.createTrustCircleList(responseString);
                        Intent intent = new Intent(getActivity(), TripActivity.class);
                        TripActivity tripActivity = (TripActivity) mActivity;
                        intent.putExtra("user",tripActivity.mUser);
                        intent.putExtra("tripCardList",responseString);
                        startActivity(intent);
                        //updateListView(trustCircleList);

                    } else {
                        Log.e(CLASS_NAME, "Unable to search requested details");

                        // send it back to the circle activity with no trustCircleCardList
                        Intent intent = new Intent(getActivity(), TripActivity.class);
                        TripActivity tripActivity = (TripActivity) mActivity;
                        intent.putExtra("user",tripActivity.mUser);
                        startActivity(intent);

                        Toast.makeText(getActivity(), "Unable to search details ,please try again later", Toast.LENGTH_LONG).show();

                    }
                }
                return null;
            }
        });

    }

    /**
     * method to populate the List of cards from string response
     */
    public List<Card> getTripCards(String responseString){
        ArrayList<Card> cards = new ArrayList<Card>();
        if(responseString!=null && responseString!=""){
            List<Trip> tripList = JSONUtil.getTripList(responseString);
            if(tripList!=null){
                for (Trip trip : tripList) {
                    Log.d(CLASS_NAME, "Trip " + trip);
                    // initialise the card
                    SuggestedCard card = new SuggestedCard(getActivity(), trip, SuggestedCard.AVAILABLE_TRIPS);
                    card.init();
                    // add the card inside the card arraylist
                    cards.add(card);
                }
            }
        }
        return cards;


    }

}
