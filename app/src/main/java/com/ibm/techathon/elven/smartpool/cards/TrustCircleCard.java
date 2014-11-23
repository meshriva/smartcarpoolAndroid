package com.ibm.techathon.elven.smartpool.cards;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.CircleActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.util.ColorGenerator;
import com.ibm.techathon.elven.smartpool.util.TextDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import bolts.Continuation;
import bolts.Task;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by meshriva on 11/12/2014.
 * Trust Circle cards
 */
public class TrustCircleCard extends Card {

    //static variable which defines the class name for logging purpose
    public static final String CLASS_NAME = "TrustCircleCards";

    // card type to display current subscribed trust circles
    public static final int SUBSCRIBED_TRUST_CIRCLE = 0;
    // card type to display current for available trust circles
    public static final int AVAILABLE_TRUST_CIRCLE = 1;
    // card type to display current for available trust circles
    public static final int PENDING_TRUST_CIRCLE_REQUEST = 2;

    // instance variable to store the context
    private static Context mContext;

    // card header title
    private String mTitle;

    // card subtitle will have condensed content
    private TextView mSubTitle;

    // information about the card status
    private TextView mStatus;

    // trust circle datatype
    private  static TrustCircle mTrustCircle;

    // trust circle datatype
    private TrustCircle trustCircle;

    // check if the circle card is for current or available
    private int mCardType;

    // count of the card
    private int mCount;

    //default constructor
    public TrustCircleCard(Context context){

        super(context, R.layout.fragement_trust_circle_layout);
        mContext =context;
    }

    // parameterised constructor
    public TrustCircleCard(Context context,TrustCircle trustCircle,int cardType){

        super(context, R.layout.fragement_trust_circle_layout);
        mContext =context;
        mTrustCircle = trustCircle;
        this.trustCircle = trustCircle;
        mCardType = cardType;
    }

    // init method to create the card header and header menu action
    public void init(){

        //mCount = i;
        Log.d(CLASS_NAME, "mCardType:"+mCardType);
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        // set the trustCircle name
        header.setTitle(mTrustCircle.getName() != null ? mTrustCircle.getName() : "");
        // based on the admin user decide the menu option default option is available users

        int id = R.menu.trust_circle_user_menu;

        if(mCardType==PENDING_TRUST_CIRCLE_REQUEST){
            id = R.menu.trust_circle_request_update;
        }else if(mCardType==SUBSCRIBED_TRUST_CIRCLE){
            id = R.menu.trust_circle_user_menu;
        }else{
             id = R.menu.trust_circle_available;
        }
        Log.d(CLASS_NAME, "mCardType:"+id);

        header.setPopupMenu(id, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {

                if(item.getTitle().toString().equals(mContext.getString(R.string.action_circle_unsubscribe))){
                    Toast.makeText(getContext(),"Unsubscribing from the circle", Toast.LENGTH_SHORT).show();
                    removeCircleMapping(mTrustCircle);
                }else if (item.getTitle().toString().equals(mContext.getString(R.string.action_circle_subscribe))){
                    Toast.makeText(getContext(),"Submitting request for submission", Toast.LENGTH_SHORT).show();
                    applyForSubscription();

                }else if(item.getTitle().toString().equals(mContext.getString(R.string.action_circle_allow))){
                    Toast.makeText(getContext(),"Submitting request to allow user", Toast.LENGTH_SHORT).show();
                    updateUserAccessToCircle("allow");
                }else if(item.getTitle().toString().equals(mContext.getString(R.string.action_circle_reject))){
                    Toast.makeText(getContext(),"Submitting request not to allow user", Toast.LENGTH_SHORT).show();
                    updateUserAccessToCircle("reject");
                }
            }
        });

        addCardHeader(header);

        if(mCardType!=SUBSCRIBED_TRUST_CIRCLE) {
            // set swipeable to true only for subscriber circles
            setSwipeable(true);
             // listener when swipe action
            setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    //card has been swipped remove the card
                    TrustCircleCard circleCard = (TrustCircleCard) card;
                    removeCircleMapping(circleCard.getmTrustCircle());
                }
            });
        }


    }

    // init method to create the card header and header menu action
    public void init(int i){
        Log.d(CLASS_NAME,"i:"+i);
        mCardType = i;
        Log.d(CLASS_NAME,"mCardType"+mCardType);
        init();
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        if (view == null) return;
        if(mTrustCircle ==null) return;

        //Retrieve TextView elements
        mSubTitle = (TextView) view.findViewById(R.id.trust_circle_desc);
        mStatus = (TextView) view.findViewById(R.id.trust_circle_status);

        ImageView imageView = (ImageView) view.findViewById(R.id.ListImageView);

        if(mSubTitle!=null){
            mSubTitle.setText(trustCircle.getDesc());
        }

        if(mStatus!=null){
            mStatus.setText(trustCircle.getOpen()!= "true" ? "Exclusive" : "Open");
        }

        // set the image view
        ColorGenerator generator = ColorGenerator.DEFAULT;
        // generate random color
        int color1 = generator.getRandomColor();
        String firstChar = null;

        if(trustCircle!=null && trustCircle.getName()!=null) {
            firstChar = new Character(trustCircle.getName().toString().toUpperCase().charAt(0)).toString();
        }
        // putting blank space in the circle
        if(firstChar==null){
            firstChar= "";
        }
        // declare the builder object and create the drawable.
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(firstChar, color1);
        // set the drawable to the image View
        imageView.setImageDrawable(drawable);

    }

    public TrustCircle getmTrustCircle() {
        return mTrustCircle;
    }

    public void setmTrustCircle(TrustCircle mTrustCircle) {
        this.mTrustCircle = mTrustCircle;
    }


    public static void applyForSubscription(){
        if(mTrustCircle!=null){

            // create the payload object
            // create JSON object
            final JSONObject jsonObj = new JSONObject();
            try {

                if(mTrustCircle.getUser()!=null){
                    jsonObj.put("user",mTrustCircle.getUser());
                }

                if(mTrustCircle.getName()!=null){
                    jsonObj.put("circle_name",mTrustCircle.getName());
                }

                if(mTrustCircle.getDesc()!=null){
                    jsonObj.put("circle_desc",mTrustCircle.getDesc());
                }

                if(mTrustCircle.getAdmin()!=null){
                    jsonObj.put("circle_admin",mTrustCircle.getAdmin());
                }

                if(mTrustCircle.getOpen()!=null){
                    jsonObj.put("circle_open",mTrustCircle.getOpen());
                }

                if(mTrustCircle.getActive()!=null){
                    jsonObj.put("circle_active",mTrustCircle.getActive());
                }

                if(mTrustCircle.getLocation()!=null){
                    jsonObj.put("circle_location",mTrustCircle.getLocation());
                }

                if(mTrustCircle.getTrustId()!=null){
                    jsonObj.put("trustId",mTrustCircle.getTrustId());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String uri = "circleUsers/subscribe";

            SmartPoolApplication.cloudCodeService.post(uri,jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                    } else {

                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode());
                        if (200 == task.getResult().getHttpResponseCode()) {

                            // update is done,move to the new home activity
                            Intent intent = new Intent(mContext, CircleActivity.class);
                            intent.putExtra("user", ((CircleActivity)mContext).mUser);
                            mContext.startActivity(intent);

                        } else {
                            Log.e(CLASS_NAME, "Unable to complete delete of details");
                            Toast.makeText(mContext, "Unable to update details ,please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                    return null;
                }
            });

        }
    }

    /**
     *  remove the circle mapping, if the user is also admin of the circle
     *  remove the circle and all its mapping
     * @param trustCircle
     */
    public static void removeCircleMapping(TrustCircle trustCircle){
        if(trustCircle!=null) {

            String uri = "circleUsers/delete/"+trustCircle.getUser()+"/"+trustCircle.getTrustId();
            if(trustCircle.getUser().equals(trustCircle.getAdmin())){
                Log.d(CLASS_NAME, "User is admin of the circle group");
                uri = "circles/"+trustCircle.getTrustId();
            }

            SmartPoolApplication.cloudCodeService.delete(uri).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                    } else {

                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode());
                        if (200 == task.getResult().getHttpResponseCode()) {

                            // update is done,move to the new home activity
                            Intent intent = new Intent(mContext, CircleActivity.class);
                            intent.putExtra("user", ((CircleActivity)mContext).mUser);
                            mContext.startActivity(intent);

                        } else {
                            Log.e(CLASS_NAME, "Unable to complete delete of details");
                            Toast.makeText(mContext, "Unable to update details ,please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                    return null;
                }
            });

        }
    }

    /**
     * add mapping for the user to the circle
     * TODO additional changes to make difference between open and exclusive circles
     * @param trustCircle
     */
    public void addMapping(TrustCircle trustCircle){
        if(trustCircle!=null) {
            // populate JSON object
            UserType userType = ((CircleActivity)mContext).mUser;

            final JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("user",userType.getEmail());
                jsonObj.put("circle_name", trustCircle.getName());
                jsonObj.put("circle_desc",trustCircle.getDesc());
                jsonObj.put("circle_admin",trustCircle.getAdmin());
                jsonObj.put("circle_open",trustCircle.getOpen());
                jsonObj.put("circle_active",trustCircle.getActive());
                jsonObj.put("circle_location",trustCircle.getLocation());
                jsonObj.put("trustId",trustCircle.getTrustId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            SmartPoolApplication.cloudCodeService.post("/circleUsers", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                    } else {

                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode());
                        if (200 == task.getResult().getHttpResponseCode()) {

                            // update is done,move to the new home activity
                            Intent intent = new Intent(mContext, CircleActivity.class);
                            intent.putExtra("user", ((CircleActivity)mContext).mUser);
                            mContext.startActivity(intent);

                        } else {
                            Log.e(CLASS_NAME, "Unable to complete update of details");
                            Toast.makeText(mContext, "Unable to update details ,please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                    return null;
                }
            });

        }
    }


    /**
     * update user access to circle
     * @param action
     */
    public void updateUserAccessToCircle(String action){
        if(mTrustCircle!=null){

            // create the payload object
            // create JSON object
            final JSONObject jsonObj = new JSONObject();
            try {

                if(mTrustCircle.getUser()!=null){
                    jsonObj.put("user",mTrustCircle.getUser());
                }

                if(mTrustCircle.getName()!=null){
                    jsonObj.put("circle_name",mTrustCircle.getName());
                }

                if(mTrustCircle.getDesc()!=null){
                    jsonObj.put("circle_desc",mTrustCircle.getDesc());
                }

                if(mTrustCircle.getAdmin()!=null){
                    jsonObj.put("circle_admin",mTrustCircle.getAdmin());
                }

                if(mTrustCircle.getOpen()!=null){
                    jsonObj.put("circle_open",mTrustCircle.getOpen());
                }

                if(mTrustCircle.getActive()!=null){
                    jsonObj.put("circle_active",mTrustCircle.getActive());
                }

                if(mTrustCircle.getLocation()!=null){
                    jsonObj.put("circle_location",mTrustCircle.getLocation());
                }

                if(mTrustCircle.getTrustId()!=null){
                    jsonObj.put("trustId",mTrustCircle.getTrustId());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String uri = "circleUsersPending/updateStatus";

            SmartPoolApplication.cloudCodeService.post(uri,jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());

                    } else {

                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode());
                        if (200 == task.getResult().getHttpResponseCode()) {

                            // update is done,move to the new home activity
                            Intent intent = new Intent(mContext, CircleActivity.class);
                            intent.putExtra("user", ((CircleActivity)mContext).mUser);
                            mContext.startActivity(intent);

                        } else {
                            Log.e(CLASS_NAME, "Unable to complete delete of details");
                            Toast.makeText(mContext, "Unable to update details ,please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                    return null;
                }
            });

        }

    }
}
