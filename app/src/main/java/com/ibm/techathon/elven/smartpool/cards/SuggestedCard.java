/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.ibm.techathon.elven.smartpool.cards;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.LineMapActivity;
import com.ibm.techathon.elven.smartpool.activity.TripActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.Trip;
import com.ibm.techathon.elven.smartpool.util.IBMHttpResponseUtil;
import com.ibm.techathon.elven.smartpool.util.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import bolts.Continuation;
import bolts.Task;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SuggestedCard extends Card {

    // class name for logging purpose
    public static final String CLASS_NAME="SuggestedCard";

    // public instances to differentiate between current and available card
    public static final int CURRENT_TRIPS = 1;
    public static final int AVAILABLE_TRIPS = 2;

    // static variable for default point
    private static final String DEFAULT_POINT_VALUE = "0.00";

    // instance variable of Trip
    private Trip mTrip;

    // instance variable for type
    private int mType;

    // instance of context
    private static Context mContext;

    public SuggestedCard(Context context) {
        this(context, R.layout.carddemo_suggested_inner_content);
    }

    public SuggestedCard(Context context,Trip trip,int type) {
        this(context, R.layout.carddemo_suggested_inner_content);
        mTrip =trip;
        mType= type;
        mContext = context;


    }

    public SuggestedCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    public void init() {

        //Add a header
        SuggestedCardHeader header = new SuggestedCardHeader(getContext(),mTrip);

        if(mType==AVAILABLE_TRIPS){
           int id = R.menu.trust_circle_available;
            header.setPopupMenu(id, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {

                    if(item.getTitle().toString().equals(mContext.getString(R.string.action_circle_unsubscribe))){
                        Toast.makeText(getContext(),"Unsubscribing from the circle", Toast.LENGTH_SHORT).show();
                        //removeCircleMapping(mTrustCircle);
                    }else if (item.getTitle().toString().equals(mContext.getString(R.string.action_circle_subscribe))){
                        Toast.makeText(getContext(),"Submitting request for subscription", Toast.LENGTH_SHORT).show();
                        submitSubscriptionRequest();

                    }
                }
            });

        }
        addCardHeader(header);



        // TODO make click listener only when lat and long is available
        //Set click listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                if(!mTrip.getStartLocationLat().equals(DEFAULT_POINT_VALUE) && !mTrip.getEndLocationLat().equals(DEFAULT_POINT_VALUE)
                        && !mTrip.getStartLocationLang().equals(DEFAULT_POINT_VALUE) && !mTrip.getEndLocationLang().equals(DEFAULT_POINT_VALUE) ){
                    // call Line Map activity to show the line between start and end location on map
                    Intent intent = new Intent(mContext, LineMapActivity.class);
                    intent.putExtra(LineMapActivity.START_LOCATION_LAT, mTrip.getStartLocationLat());
                    intent.putExtra(LineMapActivity.START_LOCATION_LONG, mTrip.getStartLocationLang());
                    intent.putExtra(LineMapActivity.END_LOCATION_LAT, mTrip.getEndLocationLat());
                    intent.putExtra(LineMapActivity.END_LOCATION_LONG, mTrip.getEndLocationLang());

                    boolean sameCity =false;
                    if(mTrip.getStartLocationCity().equals(mTrip.getEndLocationCity())){
                        sameCity =true;
                    }
                    intent.putExtra(LineMapActivity.LOCATION_SAME_CITY,sameCity);

                    mContext.startActivity(intent);
                }
            }
        });

        //Set swipe on
        //setSwipeable(true);

        //Add thumbnail
        CardThumbnail thumb = new SuggestedCardThumb(getContext());
        thumb.setUrlResource("https://lh5.googleusercontent.com/-N8bz9q4Kz0I/AAAAAAAAAAI/AAAAAAAAAAs/Icl2bQMyK7c/s265-c-k-no/photo.jpg");
        thumb.setErrorResource(R.drawable.ic_error_loadingorangesmall);
        addCardThumbnail(thumb);
    }

    /**
     * send the request to back end when the subscriber action is clicked
     */
    public void submitSubscriptionRequest(){

        // create JSON object
        final JSONObject jsonObj = new JSONObject();

        try {
                jsonObj.put("trustId",mTrip.getTrustId());
                jsonObj.put("tripId",mTrip.getId());
                jsonObj.put("creator",mTrip.getCreator());
                TripActivity activity = (TripActivity)mContext;
                jsonObj.put("user",activity.mUser.getEmail());
                jsonObj.put("status","active");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // connect to back end to get current circles
        SmartPoolApplication.cloudCodeService.post("tripUsers/subscribe", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                        Log.i(CLASS_NAME,"Inside 200 status ");

                        // get TripId from request and subscriber for tag notification
                        String tripId = JSONUtil.getTripId(responseString);
                        if(tripId!=null){
                            Log.d(CLASS_NAME,"Sending a request to subscriber the push notification for "+tripId);
                            TripActivity tripActivity = (TripActivity)mContext;
                            SmartPoolApplication application = (SmartPoolApplication)tripActivity.getApplication();
                            application.push.subscribe(tripId);
                        }

                       Toast.makeText(mContext,"Request has been sent",Toast.LENGTH_SHORT).show();
                        // send it back to the circle activity with no trustCircleCardList
                        Intent intent = new Intent(mContext, TripActivity.class);
                        TripActivity tripActivity = (TripActivity) mContext;
                        intent.putExtra("user",tripActivity.mUser);
                        mContext.startActivity(intent);

                    } else {
                        Log.e(CLASS_NAME, "Unable to complete the request");

                        Toast.makeText(mContext,"Unable to complete the request",Toast.LENGTH_SHORT).show();
                        // send it back to the circle activity with no trustCircleCardList
                        Intent intent = new Intent(mContext, TripActivity.class);
                        TripActivity tripActivity = (TripActivity) mContext;
                        intent.putExtra("user",tripActivity.mUser);
                        mContext.startActivity(intent);
                    }
                }
                return null;
            }
        });

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.carddemo_suggested_title);
            TextView member = (TextView) view.findViewById(R.id.carddemo_suggested_memeber);
            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_suggested_subtitle);
            TextView community = (TextView) view.findViewById(R.id.carddemo_suggested_community);

            if (title != null){
                // set the up the title String
                title.setText(mTrip.getVechileName());
            }


            if (member != null)
                member.setText(mTrip.getOpenSeats()+" seats");

            if (subtitle != null) {
                String subTitleText = "";
                if(mTrip.getStartLocationDate().equals(mTrip.getEndLocationDate())){
                    subTitleText = mTrip.getStartLocationTime()+" to "+mTrip.getEndLocationTime()+" at "+mTrip.getStartLocationDate();
                }else{
                    subTitleText = mTrip.getStartLocationDate()+" "+mTrip.getStartLocationTime()+" to "
                            +mTrip.getEndLocationDate()+" "+mTrip.getEndLocationTime();
                }
                subtitle.setText(subTitleText);
            }

            if (community != null) {
                String communityText = "";
                if(!mTrip.getStartLocationLat().equals("0.00") && !mTrip.getEndLocationLat().equals("0.00")) {
                    community.setText(R.string.view_trip_in_map);
                }
            }
        }
    }


}

class SuggestedCardHeader extends CardHeader {

    // instance variable of Trip
    private Trip mTrip;

    public SuggestedCardHeader(Context context,Trip trip) {
        this(context, R.layout.carddemo_suggested_header_inner);
        mTrip = trip;
    }

    public SuggestedCardHeader(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.text_suggested_card1);

            if (textView != null) {
                // set the up the title String
                String titleString="";
                if(mTrip.getStartLocationCity().equals(mTrip.getEndLocationCity())){
                    titleString =  mTrip.getStartLocationPlace()+" to "+mTrip.getEndLocationPlace()
                            +" at "+mTrip.getStartLocationCity();
                }else {
                    titleString = mTrip.getStartLocationPlace() + " " + mTrip.getStartLocationCity() + " to " +
                            mTrip.getEndLocationPlace() + " " + mTrip.getEndLocationCity();
                }
                textView.setText(titleString);
            }
        }
    }
}

class SuggestedCardThumb extends CardThumbnail {

    public SuggestedCardThumb(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        if (viewImage != null) {

            if (parent!=null && parent.getResources()!=null){
                DisplayMetrics metrics=parent.getResources().getDisplayMetrics();

                int base = 100;

                if (metrics!=null){
                    viewImage.getLayoutParams().width = (int)(base*metrics.density);
                    viewImage.getLayoutParams().height = (int)(base*metrics.density);
                }else{
                    viewImage.getLayoutParams().width = 200;
                    viewImage.getLayoutParams().height = 200;
                }
            }
        }
    }
}