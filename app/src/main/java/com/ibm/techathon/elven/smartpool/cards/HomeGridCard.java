package com.ibm.techathon.elven.smartpool.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by meshriva on 9/11/2014.
 */
public class HomeGridCard extends Card {

    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected RatingBar mRatingBar;
    public int resourceIdThumbnail = -1;
    public int count;
    public String headerTitle;
    public String secondaryTitle;
    public float rating;

    private String [] navMenuTitles;

    //private variable to store context
    private HomeActivity mActivity;

    public HomeGridCard(Context context) {
        super(context, R.layout.fragment_home_card_inner_content);
        mActivity=(HomeActivity)context;
    }
    public HomeGridCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public void init() {
        if(navMenuTitles==null) {
            navMenuTitles = getContext().getResources().getStringArray(R.array.nav_drawer_items);
        }
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(headerTitle);
        /**
        header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
         **/
        addCardHeader(header);

        GplayGridThumb thumbnail = new GplayGridThumb(getContext());
        if (resourceIdThumbnail > -1)
            thumbnail.setDrawableResource(resourceIdThumbnail);
        else
            thumbnail.setDrawableResource(R.drawable.ic_ic_launcher_web);
        addCardThumbnail(thumbnail);
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
               String title = card.getCardHeader().getTitle();
               for(int i=1;i<navMenuTitles.length;i++) {
                   if (title.equals(navMenuTitles[i])) {
                       mActivity.onNavigationDrawerItemSelected(i);
                       mActivity.onSectionAttached(i);
                       mActivity.restoreActionBar();
                   }
               }
            }
        });
    }
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView subtitle = (TextView) view.findViewById(R.id.card_main_inner_subtitle);
        subtitle.setText(secondaryTitle);
        /**
        TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
        title.setText("FREE");

        RatingBar mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_gplay_main_inner_ratingBar);
        mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(0.5f);
        mRatingBar.setRating(rating);
         **/
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getResourceIdThumbnail() {
        return resourceIdThumbnail;
    }

    public void setResourceIdThumbnail(int resourceIdThumbnail) {
        this.resourceIdThumbnail = resourceIdThumbnail;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
