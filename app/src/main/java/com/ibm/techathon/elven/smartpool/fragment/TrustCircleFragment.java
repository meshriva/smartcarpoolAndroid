package com.ibm.techathon.elven.smartpool.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.cards.CustomCard;
import com.ibm.techathon.elven.smartpool.model.UserType;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrustCircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrustCircleFragment extends Fragment {

    // user details
    private static UserType mUserType;

    protected ArrayList<CustomCard> colorCards;
    protected String flow ;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType
     * @return A new instance of fragment TrustCircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrustCircleFragment newInstance(UserType userType) {
        TrustCircleFragment fragment = new TrustCircleFragment();
        mUserType = userType;
        return fragment;
    }

    public TrustCircleFragment() {
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
        View inflatedView =  inflater.inflate(R.layout.fragment_trust_circles, container, false);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCards();

    }


    private void initCards() {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        colorCards = new ArrayList<CustomCard>();

        for (int i = 0; i < 5; i++) {
            CustomCard card = new CustomCard(this.getActivity());
            card.init(i);
            cards.add(card);
            colorCards.add(card);
        }
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.card_active_trust_circles_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }




    public String getFlow(){
        flow = "base";
        return flow;
    }
}
