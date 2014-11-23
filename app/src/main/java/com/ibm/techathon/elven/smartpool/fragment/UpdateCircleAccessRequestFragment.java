package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
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
 * Use the {@link UpdateCircleAccessRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCircleAccessRequestFragment extends Fragment {


    // class name used for logging purpose
    public static final String CLASS_NAME = "CurrentCirclesFragment";

    //private instance for list of Trust Circle
    private static List<TrustCircle> mTrustCircleList;

    // the fragment initialization parameters
    private static final String ARG_TRUST_CIRCLE = "trustCircleList";

    // static variable to store the activity
    private static Activity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param trustCircleList
     * @return A new instance of fragment UpdateCircleAccessRequestFragment.
     */

    public static UpdateCircleAccessRequestFragment newInstance(List<TrustCircle> trustCircleList) {
        UpdateCircleAccessRequestFragment fragment = new UpdateCircleAccessRequestFragment();
        mTrustCircleList = trustCircleList;
        return fragment;
    }

    public UpdateCircleAccessRequestFragment() {
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
        View inflatedView = inflater.inflate(R.layout.fragment_update_circle_access_request, container, false);
        mActivity = getActivity();
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();
    }

    private void initCards() {
        if(mTrustCircleList!=null) {
            ArrayList<Card> cards = new ArrayList<Card>();
            if(mActivity==null){
                mActivity = getActivity();
            }
            for (TrustCircle trustCircle : mTrustCircleList) {
                Log.d(CLASS_NAME, "Trust Circle " + trustCircle);
                // initialise the card
                TrustCircleCard card = new TrustCircleCard(getActivity(), trustCircle, TrustCircleCard.PENDING_TRUST_CIRCLE_REQUEST);
                card.init(TrustCircleCard.PENDING_TRUST_CIRCLE_REQUEST);

                // add the card inside the card arraylist
                cards.add(card);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.card_update_circle_access_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }
    }


}
