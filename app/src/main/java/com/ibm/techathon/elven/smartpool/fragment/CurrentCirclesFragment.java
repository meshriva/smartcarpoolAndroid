package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.cards.TrustCircleCard;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentCirclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentCirclesFragment extends Fragment {


    // class name used for logging purpose
    public static final String CLASS_NAME = "CurrentCirclesFragment";

    //private instance for list of Trust Circle
    private static List<com.ibm.techathon.elven.smartpool.model.TrustCircle> mTrustCircleList;

    // the fragment initialization parameters
    private static final String ARG_TRUST_CIRCLE = "trustCircleList";

    // static variable to store the activity
    private static Activity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentCirclesFragment.
     */
    public static CurrentCirclesFragment newInstance(List<TrustCircle> trustCircleList) {
        CurrentCirclesFragment fragment = new CurrentCirclesFragment();
        mTrustCircleList = trustCircleList;
        return fragment;
    }

    public CurrentCirclesFragment() {
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
        View inflatedView = inflater.inflate(R.layout.fragment_current_circles, container, false);
        mActivity = getActivity();
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();
    }

    private void initCards() {
        /**
        if(mTrustCircleList!=null){
            //Init an array of Cards
            ArrayList<Card> cards = new ArrayList<Card>();
            for(TrustCircle trustCircle :mTrustCircleList ){
                Log.d(CLASS_NAME,"Trust Circle "+trustCircle);
                TrustCircleCard card = new TrustCircleCard(getActivity(),trustCircle,TrustCircleCard.SUBSCRIBED_TRUST_CIRCLE);
                card.init();
                cards.add(card);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.card_current_trust_circles_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }
         **/
        if(mTrustCircleList!=null) {
            ArrayList<Card> cards = new ArrayList<Card>();
            if(mActivity==null){
                mActivity = getActivity();
            }
            for (TrustCircle trustCircle : mTrustCircleList) {
                Log.d(CLASS_NAME, "Trust Circle " + trustCircle);
                // initialise the card
                TrustCircleCard card = new TrustCircleCard(getActivity(), trustCircle, TrustCircleCard.SUBSCRIBED_TRUST_CIRCLE);
                card.init();

                // add the card inside the card arraylist
                cards.add(card);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.card_current_trust_circles_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }
    }

}
