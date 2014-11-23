package com.ibm.techathon.elven.smartpool.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ibm.techathon.elven.smartpool.fragment.SettingsFragment;

/**
 * Created by meshriva on 11/5/2014.
 */
public class UserSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
