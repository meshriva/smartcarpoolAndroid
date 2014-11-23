package com.ibm.techathon.elven.smartpool.fragment;


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
import android.widget.Switch;
import android.widget.Toast;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.CircleActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewCircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewCircleFragment extends Fragment {

    // class name used for logging purpose
    public static final String CLASS_NAME="CreateNewCircleFragment";

    // user details
    private static UserType mUser;

    private UserType mUserType;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_TYPE = "user";

    //variables to store the user detail information
    private EditText mName;
    private AutoCompleteTextView mCurrentLocation;
    private Switch mOpenSwitch;
    private EditText mDesc;

    // variable to capture the open settings, default value is true
    private String mOpen = "true";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType
     * @return A new instance of fragment CreateNewCircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewCircleFragment newInstance(UserType userType) {
        CreateNewCircleFragment fragment = new CreateNewCircleFragment();
        mUser = userType;
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_TYPE, userType);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateNewCircleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserType = (UserType)getArguments().getSerializable(ARG_USER_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_create_new_circle, container, false);

        // set up the update user form
        mName = (EditText) inflatedView.findViewById(R.id.editText_register_circle_name);
        mCurrentLocation = (AutoCompleteTextView) inflatedView.findViewById(R.id.editText_register_circle_location);
        mOpenSwitch = (Switch) inflatedView.findViewById(R.id.switch_registeration_circle_open);
        mDesc = (EditText) inflatedView.findViewById(R.id.editText_register_circle_desc);

        // set up auto complete for current location
        String[] locations = getResources().
                getStringArray(R.array.locations);
        ArrayAdapter locationAdapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_list_item_1, locations);
        if (mCurrentLocation != null) {
            mCurrentLocation.setAdapter(locationAdapter);
        }

        // populate the view with current details
        if (mUser != null) {
            mCurrentLocation.setText(mUser.getCurrentLocation());
        }

        // set action to update setting when switched is selected
        mOpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOpen = "false";
                } else {
                    // The toggle is disabled
                    mOpen = "true";
                }
            }
        });

        // set action when update details is clicked from the view fragment
        Button updateDetailsButton = (Button) inflatedView.findViewById(R.id.button_register_circle);
        updateDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check if the user data passes the validation
                boolean cancel = false;
                View focusView = null;

                //Check if name is empty
                if (TextUtils.isEmpty(mName.getText().toString())) {
                    mName.setError(getString(R.string.error_field_required));
                    focusView = mName;
                    cancel = true;
                }

                 // Check if desc is empty
                if (TextUtils.isEmpty(mDesc.getText().toString())) {
                    mDesc.setError(getString(R.string.error_field_required));
                    focusView = mDesc;
                    cancel = true;
                }

                //Check if current location is empty
                if (TextUtils.isEmpty(mCurrentLocation.getText().toString())) {
                    mCurrentLocation.setError(getString(R.string.error_field_required));
                    focusView = mCurrentLocation;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else

                {

                    // show a toast that update is happening
                    Toast.makeText(getActivity(), "Submitting request to update details", Toast.LENGTH_LONG).show();

                    // populate JSON object
                    final JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("name", mName.getText().toString());
                        jsonObj.put("desc",mDesc.getText().toString());
                        jsonObj.put("admin",mUser.getEmail());
                        jsonObj.put("location",mCurrentLocation.getText().toString());
                        jsonObj.put("open",mOpen);
                        // active is true as the circle is getting created
                        jsonObj.put("active","true");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SmartPoolApplication.cloudCodeService.post("/circles/create", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                                    Intent intent = new Intent(getActivity(), CircleActivity.class);
                                    intent.putExtra("user", mUser);
                                    startActivity(intent);

                                } else {
                                    Log.e(CLASS_NAME, "Unable to complete update of details");
                                    Toast.makeText(getActivity(), "Unable to update details ,please try again later", Toast.LENGTH_LONG).show();

                                }
                            }
                            return null;
                        }
                    });
                }
            }
        });

        return inflatedView;
    }


}
