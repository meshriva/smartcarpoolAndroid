package com.ibm.techathon.elven.smartpool.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.push.IBMPush;
import com.ibm.mobile.services.push.IBMPushNotificationListener;
import com.ibm.mobile.services.push.IBMSimplePushNotification;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.model.ibmdata.TrustCircle;
import com.ibm.techathon.elven.smartpool.model.ibmdata.User;
import com.ibm.techathon.elven.smartpool.model.ibmdata.UserTrustCircles;

import bolts.Continuation;
import bolts.Task;

/**
 * base application class used to intialize the IBM Mobile Data and other
 * dependencies
 *
 * Created by meshriva on 11/3/2014.
 */
public class SmartPoolApplication extends Application {

    // IBM Mobile data application id
    private static final String APP_ID = "445e9200-f6b5-4f61-8e86-d679dc9badd0";
    // IBM Mobile data application secret id
    private static final String APP_SECRET = "726c5b4e326b90e10f529f58ab696beb55a88376";
    // IBM Mobile data route
    private static final String APP_ROUTE = "restmeshriva.mybluemix.net";

   // device alias to be used while registering the device to IBM Push service
    private static final String deviceAlias = "TargetDevice";
    // consumer id to be used while registering the device to IBM Push service
    private static final String consumerID = "MBaaSListApp";

    public static final int EDIT_ACTIVITY_RC = 1;

    // create a static variable which will store the instance of IBM Push
    // to be used by other activities in the class
    public static IBMPush push = null;

    // create a static variable which will store the instance of IBM CloudCode
    // to be used by other activities in the class
    public static IBMCloudCode cloudCodeService;

    // class name for logging purpose
    private static final String CLASS_NAME = SmartPoolApplication.class
            .getSimpleName();

    //instance of the activity
    private Activity mActivity;
    // initialise the push notification listener which will be invoked
    // when the notification is pushed by GCM to the application
    private IBMPushNotificationListener notificationListener;

    // static variable to store the user information
    public static UserType mUser;

    /**
     * default constructor
     * will be used to register the LifecycleCallbacks
     */
    public SmartPoolApplication() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {
                Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
                mActivity = activity;

                //Define IBMPushNotificationListener behavior on push notifications
                notificationListener = new IBMPushNotificationListener() {
                    @Override
                    public void onReceive(final IBMSimplePushNotification message) {
                        mActivity.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {

                                Log.d(CLASS_NAME, "Notification message received: " + message.toString());

                                    //present the message when message is received from Google cloud messaging system.
                                    if(!message.getAlert().contains("ItemList was updated")){
                                        mActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                new AlertDialog.Builder(mActivity)
                                                        .setTitle("Push notification received")
                                                        .setMessage(message.getAlert())
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                            }
                                                        })
                                                        .show();
                                            }
                                        });

                                    }

                            }
                        });
                    }
                };
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(CLASS_NAME,
                        "Activity started: " + activity.getLocalClassName());
                //Track activity
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(CLASS_NAME,
                        "Activity resumed: " + activity.getLocalClassName());
                //Track activity
                mActivity = activity;
                if (push != null) {
                    push.listen(notificationListener);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity,
                                                    Bundle outState) {
                Log.d(CLASS_NAME,
                        "Activity saved instance state: "
                                + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (push != null) {
                    push.hold();
                }
                Log.d(CLASS_NAME,
                        "Activity paused: " + activity.getLocalClassName());
                if (activity != null && activity.equals(mActivity))
                    mActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(CLASS_NAME,
                        "Activity stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(CLASS_NAME,
                        "Activity destroyed: " + activity.getLocalClassName());
            }
        });
    }

    @Override
    public void onCreate() {
        // call the super onCreate method
        super.onCreate();
        // Initialize the IBM core backend-as-a-service.
        IBMBluemix.initialize(this, APP_ID, APP_SECRET, APP_ROUTE);
        // Initialize the IBM Data Service.
        IBMData.initializeService();
        cloudCodeService = IBMCloudCode.initializeService();
        // Initialize the IBM Push Data Service.
        push = IBMPush.initializeService();

        // Register the data type Specialization classes here .
        User.registerSpecialization(User.class);
        TrustCircle.registerSpecialization(TrustCircle.class);
        UserTrustCircles.registerSpecialization(UserTrustCircles.class);

        // Register the device with the IBM Push service.
        push.register(deviceAlias, consumerID).continueWith(new Continuation<String, Void>() {

            @Override
            public Void then(Task<String> task) throws Exception {
                if (task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                } else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                } else {
                    // device has been successfully registered
                    Log.d(CLASS_NAME, "Device Successfully Registered");
                }
                return null;
            }
        });
    }

}
