package com.ibm.techathon.elven.smartpool.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.application.SmartPoolApplication;
import com.ibm.techathon.elven.smartpool.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // class name used in the logs
    public static final String CLASS_NAME="LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.editText_login_email);
        mPasswordView = (EditText) findViewById(R.id.editText_login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.button_login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //set up the register request section
        Button mRegisterDetailsButton = (Button) findViewById(R.id.button_register);
        // when register button is clicked move to register activity
        mRegisterDetailsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // go the Register Activity
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        // check if the form can be auto completed
        populateAutoComplete();

    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
        // check if the user name is stored in the preference
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        //get the preference values
        String username = sharedPrefs.getString("prefUsername","NULL");
        String password = sharedPrefs.getString("prefUserPass","NULL");
        boolean isAutoLogin = sharedPrefs.getBoolean("prefAutoLogin",false);

        //auto populate the user name and password
        if(username!=null && username!="NULL") {
            mEmailView.setText(username);
        }
        if(password!=null && password!="NULL") {
            mPasswordView.setText(password);
        }

        // check if auto login should be done
        if(isAutoLogin){
            attemptLogin();
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // first make the progress bar active
             //showProgress(true);
            final JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("email", email);
                jsonObj.put("password",password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SmartPoolApplication.cloudCodeService.post("users/auth", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

                @Override
                public Void then(Task<IBMHttpResponse> task) throws Exception {
                    // first remove the progress bar
                    // showProgress(false);
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");
                        mPasswordView.setError(getString(R.string.error_incorrect_email_password));
                        mPasswordView.requestFocus();

                    } else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                        mPasswordView.setError(getString(R.string.error_incorrect_email_password));
                        mPasswordView.requestFocus();

                    } else {
                        InputStream is = task.getResult().getInputStream();
                        String responseString = "";
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(is));
                            String myString = "";
                            while ((myString = in.readLine()) != null)
                                responseString += myString;

                            in.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i(CLASS_NAME, "Response Status: " + task.getResult().getHttpResponseCode()+" Response Body: "+responseString);
                        if (200 == task.getResult().getHttpResponseCode()) {

                            // now de-serialise the data and get the values and set it to UserType data type
                            UserType user = new UserType();
                            JSONObject jObj = new JSONObject(responseString);
                            JSONObject subObj = jObj.getJSONObject("response");
                            if(subObj!=null){
                                user.setEmail(subObj.getString("email")!=null ? subObj.getString("email"):"");
                                user.setName(subObj.getString("name")!=null ? subObj.getString("name"):"");
                                user.setPassword(subObj.getString("password")!=null ? subObj.getString("password"):"");
                                user.setPhoneNumber(subObj.getString("phoneNumber")!=null ? subObj.getString("phoneNumber"):"");
                                user.setCurrentLocation(subObj.getString("currentLocation")!=null ? subObj.getString("currentLocation"):"");
                                user.setVechileRegisterationNumber(subObj.getString("vechileRegisterationNumber")!=null ? subObj.getString("vechileRegisterationNumber"):"");
                            }

                            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                            ((SmartPoolApplication)getApplication()).mUser = user;
                            intent.putExtra("user",user);
                            startActivity(intent);

                        } else {
                            Log.i(CLASS_NAME, "Unable to complete authentication");
                            //Toast.makeText(getApplication(),getString(R.string.error_incorrect_email_password),Toast.LENGTH_LONG).show();
                            //LoginActivity.this.mEmailView.invalidate();
                            LoginActivity.this.mEmailView.setError(getString(R.string.error_incorrect_email_password));
                            LoginActivity.this.mEmailView.requestFocus();
                        }
                    }
                    return null;
                }
            }
            //        , Task.UI_THREAD_EXECUTOR
            );

        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


}