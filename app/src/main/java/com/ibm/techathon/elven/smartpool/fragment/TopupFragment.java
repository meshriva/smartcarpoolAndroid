package com.ibm.techathon.elven.smartpool.fragment;

import android.app.Activity;

import com.ibm.techathon.elven.smartpool.activity.HomeActivity;


/**
 * Created by meshriva on 9/4/2014.
 */
public class TopupFragment extends ListColorFragment {

    /**
     * Name of the flow
     */
    public static final String TOPUP_FLOW = "Continue";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public String getFlow(){
        flow = TOPUP_FLOW;
        return flow;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
