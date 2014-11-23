package com.ibm.techathon.elven.smartpool.cards;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.CircleActivity;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;
import com.ibm.techathon.elven.smartpool.model.TrustCircle;
import com.ibm.techathon.elven.smartpool.util.ColorGenerator;
import com.ibm.techathon.elven.smartpool.util.TextDrawable;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by meshriva on 9/17/2014.
 */
public class CustomCard extends Card {

    //get the base activity
    private Activity mBaseActivity;

    //card Subtitle 1
    private String [] cardSubTitles1;

    //card Subtitle 2
    private String [] cardSubTitles2;

    // count
    private int mCount;

    // subtitle 1
    private TextView mSubtitle1;

    //subtitle 2
    private TextView mSubtitle2;

    //
    private TrustCircle mTrustCircle;

    public CustomCard(Context context){

        super(context, R.layout.bill_charge_details_expand_layout);
        if(context instanceof HomeActivity) {
            mBaseActivity = (HomeActivity) context;
        }else{
            mBaseActivity = (CircleActivity) context;
        }

        cardSubTitles1 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle1);
        cardSubTitles2 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle2);
    }

    public CustomCard(Context context,int count){

        super(context, R.layout.bill_charge_details_expand_layout);
        if(context instanceof HomeActivity) {
            mBaseActivity = (HomeActivity) context;
        }else{
            mBaseActivity = (CircleActivity) context;
        }
        mCount = count;

        cardSubTitles1 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle1);
        cardSubTitles2 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle2);
    }


    public CustomCard(Context context,TrustCircle trustCircle,int type){

        super(context, R.layout.bill_charge_details_expand_layout);
        if(context instanceof HomeActivity) {
            mBaseActivity = (HomeActivity) context;
        }else{
            mBaseActivity = (CircleActivity) context;
        }
        mCount = type;
        mTrustCircle = trustCircle;

        cardSubTitles1 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle1);
        cardSubTitles2 = mBaseActivity.getResources().getStringArray(R.array.account_info_card_expand_subtitle2);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        if (view == null) return;

        //Retrieve TextView elements
        mSubtitle1 = (TextView) view.findViewById(R.id.list_item_title);
        mSubtitle2 = (TextView) view.findViewById(R.id.list_item_subtitle);

        ImageView imageView = (ImageView) view.findViewById(R.id.ListImageView);

        if(mSubtitle1!=null){
            mSubtitle1.setText(mTrustCircle.getDesc());
        }

        /**
        if(mSubtitle2!=null){
            mSubtitle2.setText(mTrustCircle.getDesc());
        }
         **/

        // set the image view
        ColorGenerator generator = ColorGenerator.DEFAULT;
         // generate random color
        int color1 = generator.getRandomColor();
        String firstChar = null;
        if(mSubtitle1!=null && mSubtitle1.getText()!=null) {
            firstChar = new Character(mTrustCircle.getName().toUpperCase().charAt(0)).toString();
        }
        // putting blank space in the circle
        if(firstChar==null){
            firstChar= "";
        }
        // declare the builder object once.
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(firstChar, color1);
        imageView.setImageDrawable(drawable);

    }

    public void init(int i){

        mCount = i;
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(mTrustCircle.getName());
        // based on the admin user decide the menu option default option is available users
        int id = R.menu.trust_circle_user_menu;
        if(mCount!=TrustCircleCard.SUBSCRIBED_TRUST_CIRCLE){
            id = R.menu.trust_circle_available;
        }

        header.setPopupMenu(id, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        addCardHeader(header);
    }
}
