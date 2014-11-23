package com.ibm.techathon.elven.smartpool.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ibm.techathon.elven.smartpool.R;

/**
 * Created by meshriva on 11/5/2014.
 */
public class SettingsFragment extends PreferenceFragment

{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.my_prefernce);
    }
}

