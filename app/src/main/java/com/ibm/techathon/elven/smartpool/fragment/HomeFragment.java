package com.ibm.techathon.elven.smartpool.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;
import com.ibm.techathon.elven.smartpool.cards.HomeGridCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Created by meshriva on 9/5/2014.
 */
public class HomeFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //menu items
    private String[] navMenuTitles;

    //  menu inner items
    private String[] navMenuInnerTitles;

    // menu item icons
    private TypedArray navMenuInnerTitlesIcons;


    /**
     * default constructor
     */
    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.demo_fragment_grid_gplay, container, false);


        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuInnerTitles = getResources().getStringArray(R.array.nav_drawer_inner_items);
        navMenuInnerTitlesIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_inner_items_icons);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();
    }

    /**
     * customised method to intialize the card
     *
     */
    public void initCards(){
        ArrayList<Card> cards = new ArrayList<Card>();
        // starting the loop from one to avoid default home options

        for(int i=1;i<navMenuTitles.length;i++){
            HomeGridCard card = new HomeGridCard(getActivity());

            card.setHeaderTitle(navMenuTitles[i]);
            card.setSecondaryTitle(navMenuInnerTitles[i]);
            card.setResourceIdThumbnail(navMenuInnerTitlesIcons.getResourceId(i, -1));

            // initiate the card
            card.init();
            cards.add(card);

        }
        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);
        CardGridView listView = (CardGridView) getActivity().findViewById(R.id.carddemo_grid_base1);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


}
