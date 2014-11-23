package com.ibm.techathon.elven.smartpool.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import bolts.Continuation;
import bolts.Task;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateUserDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateUserDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserDetailsFragment extends Fragment {

    // class name used for the logger
    public static final String CLASS_NAME = "UpdateUserDetailsFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_TYPE = "user";

    // user type private instance
    private UserType mUserType;

    private static UserType mUser;

    private OnFragmentInteractionListener mListener;

    private HomeActivity mHomeActivity;

    //variables to store the user detail information
    private EditText mEmailAddress;
    private EditText mName;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mPhoneNumber;
    private AutoCompleteTextView mCurrentLocation;
    private EditText mVechileRegisterationNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType
     *
     * @return A new instance of fragment UpdateUserDetailsFragment.
     */
    public static UpdateUserDetailsFragment newInstance(UserType userType) {
        UpdateUserDetailsFragment fragment = new UpdateUserDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        mUser = userType;
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateUserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the user email address from the previous activity
        if (getArguments() != null) {
            mUserType = (UserType)getArguments().getSerializable(USER_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_update_user_details, container, false);

        // set up the update user form
        mEmailAddress = (EditText) inflatedView.findViewById(R.id.editText_update_email);
        mName = (EditText) inflatedView.findViewById(R.id.editText_update_user_name);
        mPassword = (EditText) inflatedView.findViewById(R.id.editText_update_password);
        mConfirmPassword = (EditText) inflatedView.findViewById(R.id.editText_update_confirm_password);
        mPhoneNumber = (EditText) inflatedView.findViewById(R.id.editText_update_phone_number);
        mCurrentLocation = (AutoCompleteTextView) inflatedView.findViewById(R.id.editText_update_current_location);
        mVechileRegisterationNumber = (EditText) inflatedView.findViewById(R.id.editText_update_vechile_registeration_number);

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
            mEmailAddress.setText(mUser.getEmail());
            mName.setText(mUser.getName());
            mPassword.setText(mUser.getPassword());
            mConfirmPassword.setText(mUser.getPassword());
            mPhoneNumber.setText(mUser.getPhoneNumber());
            mCurrentLocation.setText(mUser.getCurrentLocation());
            mVechileRegisterationNumber.setText(mUser.getVechileRegisterationNumber());
        }

        // making email address non editable
        mEmailAddress.setKeyListener(null);

        // set action when update details is clicked from the view fragment
        Button updateDetailsButton = (Button) inflatedView.findViewById(R.id.button_update_me);
        updateDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check if the user data passes the validation
                boolean cancel = false;
                View focusView = null;

                // Check for a valid email address.
                if (TextUtils.isEmpty(mEmailAddress.getText().toString())) {
                    mEmailAddress.setError(getString(R.string.error_field_required));
                    focusView = mEmailAddress;
                    cancel = true;
                } else if (!isEmailValid(mEmailAddress.getText().toString())) {
                    mEmailAddress.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailAddress;
                    cancel = true;
                }

                //Check if name is empty
                if (TextUtils.isEmpty(mName.getText().toString())) {
                    mName.setError(getString(R.string.error_field_required));
                    focusView = mName;
                    cancel = true;
                }

                // Check for a valid password
                if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mPassword.setError(getString(R.string.error_field_required));
                    focusView = mPassword;
                    cancel = true;
                } else if (!isPasswordValid(mPassword.getText().toString())) {
                    mPassword.setError(getString(R.string.error_invalid_password));
                    focusView = mPassword;
                    cancel = true;
                }

                //check for a valid confirm password
                if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
                    mConfirmPassword.setError(getString(R.string.error_field_required));
                    focusView = mConfirmPassword;
                    cancel = true;
                } else if (!isPasswordValid(mConfirmPassword.getText().toString())) {
                    mConfirmPassword.setError(getString(R.string.error_invalid_password));
                    focusView = mConfirmPassword;
                    cancel = true;
                }else if(!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                    mConfirmPassword.setError(getString(R.string.error_password_not_matching));
                    focusView = mConfirmPassword;
                    cancel = true;
                }

                //Check if phone number is empty
                if (TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
                    mPhoneNumber.setError(getString(R.string.error_field_required));
                    focusView = mPhoneNumber;
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
                    Toast.makeText(getActivity(), "Submitting requst to update details", Toast.LENGTH_SHORT).show();

                    // populate JSON object
                    final JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("name", mName.getText().toString());
                        jsonObj.put("password", mPassword.getText().toString());
                        jsonObj.put("phoneNumber", mPhoneNumber.getText().toString());
                        jsonObj.put("currentLocation", mCurrentLocation.getText().toString());
                        jsonObj.put("vechileRegisterationNumber", mVechileRegisterationNumber.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String uri = "users/" + mUser.getEmail();
                    SmartPoolApplication.cloudCodeService.put(uri, jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

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
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mHomeActivity = (HomeActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHomeActivity = null;
    }

    private static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
