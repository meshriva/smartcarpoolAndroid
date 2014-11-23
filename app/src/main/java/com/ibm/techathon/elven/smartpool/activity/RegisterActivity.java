package com.ibm.techathon.elven.smartpool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.UserType;
import com.ibm.techathon.elven.smartpool.model.ibmdata.User;

import bolts.Continuation;
import bolts.Task;

public class RegisterActivity extends Activity {

    // class name used in the logs
    public static final String CLASS_NAME="RegisterActivity";

    private EditText mEmailAddress;
    private EditText mName;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mPhoneNumber;
    private AutoCompleteTextView mCurrentLocation;
    private EditText mVechileRegisterationNumber;

    /**
     * Used to store the result settings value
     *
     */
    private static final int RESULT_SETTINGS = 1;

    /**
     * Keep track of the register task to ensure we can cancel it if requested.
     */
    private RegisterUserTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set up the register form
        mEmailAddress = (EditText) findViewById(R.id.editText_register_email);
        mName = (EditText) findViewById(R.id.editText_register_user_name);
        mPassword = (EditText) findViewById(R.id.editText_register_password);
        mConfirmPassword = (EditText) findViewById(R.id.editText_register_confirm_password);
        mPhoneNumber = (EditText) findViewById(R.id.editText_register_phone_number);
        mCurrentLocation = (AutoCompleteTextView) findViewById(R.id.editText_register_current_location);
        mVechileRegisterationNumber = (EditText) findViewById(R.id.editText_register_vechile_registeration_number);

        // set up auto complete for current location
        String[] locations = getResources().
                getStringArray(R.array.locations);
        ArrayAdapter locationAdapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1,locations);
        mCurrentLocation.setAdapter(locationAdapter);


        // set up action when register button is clicked
        Button mRegisterButton = (Button) findViewById(R.id.button_register_me);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // take action when the register me button is clicked
                attemptToRegister();
            }
        });
    }

    /**
     * Attempts to register the account specified by the register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptToRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailAddress.setError(null);
        mName.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);
        mPhoneNumber.setError(null);
        mCurrentLocation.setError(null);
        mVechileRegisterationNumber.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailAddress.getText() != null ? mEmailAddress.getText().toString() : null;
        String name = mName.getText() != null ? mName.getText().toString() : null;
        String password = mPassword.getText() != null ? mPassword.getText().toString() : "";
        String confirmPassword = mConfirmPassword.getText() != null ? mConfirmPassword.getText().toString() : null;
        String phoneNumber = mPhoneNumber.getText() != null ? mPhoneNumber.getText().toString() : null;
        String currentLocation = mCurrentLocation.getText() != null ? mCurrentLocation.getText().toString() : null;
        String vechileRegisterationNumber = mVechileRegisterationNumber.getText() != null ? mVechileRegisterationNumber.getText().toString() : "";

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailAddress.setError(getString(R.string.error_field_required));
            focusView = mEmailAddress;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailAddress.setError(getString(R.string.error_invalid_email));
            focusView = mEmailAddress;
            cancel = true;
        }

        //Check if name is empty
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        //check for a valid confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = mConfirmPassword;
            cancel = true;
        } else if (!isPasswordValid(confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPassword;
            cancel = true;
        }else if(!password.equals(confirmPassword)){
            mConfirmPassword.setError(getString(R.string.error_password_not_matching));
            focusView = mConfirmPassword;
            cancel = true;
        }

        //Check if phone number is empty
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = mPhoneNumber;
            cancel = true;
        }

        //Check if current location is empty
        if (TextUtils.isEmpty(currentLocation)) {
            mCurrentLocation.setError(getString(R.string.error_field_required));
            focusView = mCurrentLocation;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the register event at the background
            Toast.makeText(this,"Registering your details, please wait",Toast.LENGTH_LONG).show();
            User user = new User(email,name,password,phoneNumber,currentLocation,vechileRegisterationNumber);
            // use IBM mobile data to register details

                Log.d(CLASS_NAME, "Attempting to register the customer");
                try {
                    // Use the IBMDataObject to create and persist the Item object.
                    user.save().continueWith(new Continuation<IBMDataObject, Void>() {

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
                                // create a UserType data type
                                UserType user = new UserType(mEmailAddress.getText().toString(),
                                          mName.getText().toString(),mPassword.getText().toString(),
                                        mPhoneNumber.getText().toString(),
                                        mCurrentLocation.getText().toString()
                                        ,mVechileRegisterationNumber.getText().toString());

                                // moving to the home activity
                                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                                intent.putExtra("user",user);
                                ((SmartPoolApplication)getApplication()).mUser = user;
                                startActivity(intent);
                            }
                            return null;
                        }

                    });

                }catch (Exception error){
                    Log.e(CLASS_NAME, "Exception : " + error.getMessage());
                    Log.e(CLASS_NAME, "Exception hre : " + error);
                }

            }

            //showProgress(true);
           // mAuthTask = new RegisterUserTask(email, password);
           // mAuthTask.execute((Void) null);

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        RegisterUserTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

           // write your registeration code here

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
           // showProgress(false);

            if (success) {
                finish();
            } else {
                //mPassword.setError(R.string.error_incorrect_password);
                //mPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
        public void finish(){
            // if the authentication is done then move to the base activity
            Intent intent = new Intent(getBaseContext(),HomeActivity.class);
            startActivity(intent);

        }
    }
}
