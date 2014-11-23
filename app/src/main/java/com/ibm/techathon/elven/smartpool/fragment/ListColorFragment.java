package com.ibm.techathon.elven.smartpool.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;
import com.ibm.techathon.elven.smartpool.cards.ColorCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by meshriva on 9/19/2014.
 */
public class ListColorFragment extends Fragment {

    protected ScrollView mScrollView;
    protected ArrayList<ColorCard> colorCards;
    protected String flow ;

    public int getTitleResourceId() {
        return R.string.carddemo_title_list_color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment_colors, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCards();
        final Button button = (Button) getActivity().findViewById(R.id.submit_request_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorCards!=null){
                    for(ColorCard card:colorCards){
                        if(card.isCardSelected()){


                                Toast.makeText(getActivity(), "Topup for selected amount is done", Toast.LENGTH_SHORT).show();

                            //going back to the base activity home fragment.
                            ((HomeActivity) getActivity()).onNavigationDrawerItemSelected(0);

                        }
                    }
                }
            }
        });
    }


    private void initCards() {

        //Init an array of Cards
        ArrayList<Card> cards = new ArrayList<Card>();
        colorCards = new ArrayList<ColorCard>();

            for (int i = 0; i < 5; i++) {
                ColorCard card = new ColorCard(this.getActivity());
                card.setImageViewResourceId(R.drawable.ic_euro);
                //card.setValueViewResourceId(R.drawable.ic_number_5);
                card.setCount(i);
                card.setBackgroundResourceId(R.drawable.demo_card_selector_color1);
                card.setFlow(getFlow());

                // configure the value which is dynamic
                // for now it is hardcoded
                String title = "";
                String priceTitle = "";

                switch (i) {
                    case 0:
                        title = "5 Euro Balance recharge";
                        priceTitle = "€ 5.00";
                        break;
                    case 1:
                        title = "10 Euro Balance recharge";
                        priceTitle = "€ 10.00";
                        break;
                    case 2:
                        title = "20 Euro Balance recharge";
                        priceTitle = "€ 20.00";
                        break;
                    case 3:
                        title = "30 Euro Balance recharge";
                        priceTitle = "€ 30.00";
                        break;
                    case 4:
                        title = "50 Euro Balance recharge";
                        priceTitle = "€ 50.00";
                        break;
                    default:
                        title = "Sorry some issue happened";
                        priceTitle = "please try later";
                        break;
                }

                card.setTitle(title);
                card.setPriceTitle(priceTitle);
                /**
                 switch (i) {
                 case 0:
                 card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
                 break;
                 case 1:
                 card.setBackgroundResourceId(R.drawable.demo_card_selector_color4);
                 break;
                 case 2:
                 card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
                 break;
                 case 3:
                 card.setBackgroundResourceId(R.drawable.demo_card_selector_color2);
                 break;
                 case 4:
                 card.setBackgroundResourceId(R.drawable.demo_card_selector_color1);
                 break;
                 } **/

                cards.add(card);
                colorCards.add(card);

                CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

                CardListView listView = (CardListView) getActivity().findViewById(R.id.carddemo_list_colors);
                if (listView != null) {
                    listView.setAdapter(mCardArrayAdapter);
                }
            }

        }



    public String getFlow(){
        flow = "base";
        return flow;
    }

}
