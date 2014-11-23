package com.ibm.techathon.elven.smartpool.events;

import android.util.Log;

import com.ibm.mobile.services.data.IBMDataObject;

import bolts.Continuation;
import bolts.Task;

/**
 * Class is used to do the various related CRUD operation
 * uses IBM Mobile data to these operations
 * Each operation return UpdateEventResponse class
 * Created by meshriva on 11/6/2014.
 */
public class IBMDataActionEvent<K extends IBMDataObject> {

    public static final String CLASS_NAME="IBMDataActionEvent";

    /**
     * method to create and persist IBM data object on IBM Mobile cloud
     *
     * @param  ibmDataObject
     * @throws com.ibm.mobile.services.data.IBMDataException
     */
    public void createObject(K ibmDataObject) {

        Log.d(CLASS_NAME, "Attempting to create object");
        try {
            // Use the IBMDataObject to create and persist the Item object.
            ibmDataObject.save().continueWith(new Continuation<IBMDataObject, Void>() {

                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
                    // Log if the save was cancelled.
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the save task fails.
                    else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    }

                    // If the result succeeds, load the list.
                    else {
                        Log.d(CLASS_NAME, "User object successfully created");
                    }
                    return null;
                }

            });

        }catch (Exception error){
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
            Log.e(CLASS_NAME, "Exception hre : " + error);
        }
    }


}
