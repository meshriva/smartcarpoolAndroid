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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.CircleActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.cards.TrustCircleCard;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.util.IBMHttpResponseUtil;
import com.ibm.techathon.elven.smartpool.util.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchCirclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCirclesFragment extends Fragment {

    // class name used for logging purpose
    public static final String CLASS_NAME = "SearchCirclesFragment";

    // static variable to store the activity
    private static Activity mActivity;

    // private variable for String array for search circle open items
    private String [] searchCircleOpenStatusOptions;


    // form input variables
    private AutoCompleteTextView mAutoCompleteTextView;
    private Spinner mSpinner;
    private CardListView mListView;
    private CardArrayAdapter mCardArrayAdapter;
    private Button searchButton;
    private Button displaySearchOptionsButton;

    private TextView mTextViewSearchCircles;
    private View mViewSearchCircles;
    private ImageView mImageViewSearchCircleLocation;
    private ImageView mImageViewSearchCirclesOpen;

    // card variable
    private ArrayList<Card> mOldCards;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchCirclesFragment.
     */

    public static SearchCirclesFragment newInstance() {
        SearchCirclesFragment fragment = new SearchCirclesFragment();
        return fragment;
    }

    public SearchCirclesFragment() {
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
        View inflatedView = inflater.inflate(R.layout.fragment_search_circles, container, false);

        // initialise the mActivity variable
        mActivity = getActivity();

        // initialise the searchCircleOpenStatusOptions variable
        searchCircleOpenStatusOptions =mActivity.getResources().getStringArray(R.array.spinner_search_circle_open_items);

        // set up the form
        mAutoCompleteTextView = (AutoCompleteTextView) inflatedView.findViewById(R.id.editText_search_circles_location);
        mSpinner = (Spinner) inflatedView.findViewById(R.id.spinner_search_circles_open);
        mListView = (CardListView) inflatedView.findViewById(R.id.card_search_trust_circles_list);

        searchButton = (Button) inflatedView.findViewById(R.id.button_search_circles);
        displaySearchOptionsButton = (Button) inflatedView.findViewById(R.id.button_show_search_options);

        // additional form view
        mTextViewSearchCircles = (TextView)(inflatedView.findViewById(R.id.textView_search_circles));
        mViewSearchCircles = (View)(inflatedView.findViewById(R.id.divider_search_circles));
        mImageViewSearchCircleLocation = (ImageView)(inflatedView.findViewById(R.id.imageView_search_circles_location));
        mImageViewSearchCirclesOpen = (ImageView) (inflatedView.findViewById(R.id.imageView_search_circles_open));


        // set up auto complete for current location
        String[] locations = getResources().
                getStringArray(R.array.locations);
        ArrayAdapter locationAdapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_list_item_1, locations);
        if (mAutoCompleteTextView != null) {
            mAutoCompleteTextView.setAdapter(locationAdapter);
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_search_circle_open_items, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        // create an adapter for the cards list
        String trustCircleCardResponse = ((CircleActivity)getActivity()).mTrustCircleCardResponse;
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), getTrustCircleCards(trustCircleCardResponse));

        // update the view
        if (mListView!= null) {
            mListView.setAdapter(mCardArrayAdapter);
        }

        // check for search button click action
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search for circles
                searchCircles();
            }
        });

        // check for displaySearchOptionsButton click action
        displaySearchOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewSearchCircles.setVisibility(View.VISIBLE);
                mViewSearchCircles.setVisibility(View.VISIBLE);
                mAutoCompleteTextView.setVisibility(View.VISIBLE);
                mImageViewSearchCircleLocation.setVisibility(View.VISIBLE);
                mImageViewSearchCirclesOpen.setVisibility(View.VISIBLE);
                mSpinner.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                displaySearchOptionsButton.setVisibility(View.GONE);
            }
        });

        Log.d(CLASS_NAME,"trustCircleCardResponse:"+trustCircleCardResponse);

        if(trustCircleCardResponse==null || trustCircleCardResponse==""){
            displaySearchOptionsButton.setVisibility(View.GONE);
        }else{

            ((TextView)(inflatedView.findViewById(R.id.textView_search_circles))).setVisibility(View.GONE);
            ((View)(inflatedView.findViewById(R.id.divider_search_circles))).setVisibility(View.GONE);
            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_circles_location))).setVisibility(View.GONE);
            mAutoCompleteTextView.setVisibility(View.GONE);
            ((ImageView)(inflatedView.findViewById(R.id.imageView_search_circles_open))).setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);

        }



        return inflatedView;
    }


    /**
     * method to search the action available circles based on custom query
     * the result from back end is displayed on the fragment using card view
     */
     public void searchCircles(){

         String location = null;
         String open = null;

         if(mAutoCompleteTextView!=null){
             if(mAutoCompleteTextView.getText().toString()!=null && mAutoCompleteTextView.getText().toString().trim()!="") {
                 location = mAutoCompleteTextView.getText().toString();
             }
         }

         if(mSpinner!=null && searchCircleOpenStatusOptions!=null && searchCircleOpenStatusOptions.length>=2){
             if(mSpinner.getSelectedItem().toString().equals(searchCircleOpenStatusOptions[1])){
                 open ="true";
             }else if ((mSpinner.getSelectedItem().toString().equals(searchCircleOpenStatusOptions[2]))){
                 open = "false";
             }
         }

         Log.d(CLASS_NAME,"value of location is:"+location+":");

         // create JSON object
         final JSONObject jsonObj = new JSONObject();
         final JSONObject condition = new JSONObject();
         try {
             condition.put("active","true");
             if(location!=null && location.trim()!=""){
                 condition.put("location",location);
             }
             if(open!=null){
                 condition.put("open",open);
             }
             jsonObj.put("condition",condition);

         } catch (JSONException e) {
             e.printStackTrace();
         }

         // connect to back end to get current circles
         SmartPoolApplication.cloudCodeService.post("circles/find", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                         Intent intent = new Intent(getActivity(), CircleActivity.class);
                         CircleActivity circleActivity = (CircleActivity) mActivity;
                         intent.putExtra("user",circleActivity.mUser);
                         intent.putExtra("trustCircleCardList",responseString);
                         startActivity(intent);
                         //updateListView(trustCircleList);

                     } else {
                         Log.e(CLASS_NAME, "Unable to search requested details");

                         // send it back to the circle activity with no trustCircleCardList
                         Intent intent = new Intent(getActivity(), CircleActivity.class);
                         CircleActivity circleActivity = (CircleActivity) mActivity;
                         intent.putExtra("user",circleActivity.mUser);
                         startActivity(intent);

                         Toast.makeText(getActivity(), "Unable to search details ,please try again later", Toast.LENGTH_LONG).show();

                     }
                 }
                 return null;
             }
         });

     }

    /**
     * converts string  into List<Card> for updating list view
     * @param responseString
     */
    public List<Card> getTrustCircleCards(String responseString){
        ArrayList<Card> cards = new ArrayList<Card>();
        if(responseString!=null && responseString!=""){
            List<TrustCircle> trustCircleList = JSONUtil.getCurrentTrustCirclesFromCircleList(responseString);
            if(trustCircleList!=null){
                for (TrustCircle trustCircle : trustCircleList) {
                    Log.d(CLASS_NAME, "Trust Circle " + trustCircle);
                    // initialise the card
                    TrustCircleCard card = new TrustCircleCard(getActivity(), trustCircle, TrustCircleCard.AVAILABLE_TRUST_CIRCLE);
                    card.init();

                    // add the card inside the card arraylist
                    cards.add(card);
                }
            }
        }
        return cards;

    }

}
