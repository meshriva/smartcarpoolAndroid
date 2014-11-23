package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.cards.SuggestedCard;
import com.ibm.techathon.elven.smartpool.model.Trip;
import com.ibm.techathon.elven.smartpool.model.UserType;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentTripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentTripsFragment extends Fragment {

    // class name for logging purpose
    private static final String CLASS_NAME= "CurrentTripsFragment";

    // userType varaible
    private static UserType mUser;

    // instance variable for Trip List
    private static List<Trip> mTripList;

    // instance of activity
    private static Activity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType
     * @return A new instance of fragment CurrentTripsFragment.
     */
    public static CurrentTripsFragment newInstance(UserType userType,List<Trip>tripList) {
        CurrentTripsFragment fragment = new CurrentTripsFragment();
        mUser = userType;
        mTripList = tripList;
        return fragment;
    }

    public CurrentTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_current_trips, container, false);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCardSuggested();
    }

    /**
     * This method builds a suggested card example
     */
    private void initCardSuggested() {

        if(mTripList!=null) {
            ArrayList<Card> cards = new ArrayList<Card>();
            if(mActivity==null){
                mActivity = getActivity();
            }
            for (Trip trip : mTripList) {
                Log.d(CLASS_NAME, "Trip " + trip);
                // initialise the card
                SuggestedCard card = new SuggestedCard(getActivity(), trip, SuggestedCard.CURRENT_TRIPS);
                card.init();
                // add the card inside the card arraylist
                cards.add(card);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.card_current_trips_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }

        /**
        SuggestedCard card = new SuggestedCard(getActivity());
        CardView cardView = (CardView) getActivity().findViewById(R.id.carddemo_suggested);
        cardView.setCard(card);
        **/
    }

}
