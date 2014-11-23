/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.ibm.techathon.elven.smartpool.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.techathon.elven.smartpool.R;
import com.ibm.techathon.elven.smartpool.activity.HomeActivity;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Simple Colored card
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ColorCard extends Card {

    protected String mTitle1;
    protected String mTitle2;
    protected int mImageViewResourceId;
    protected int mValueViewResourceId;
    protected int count;
    private HomeActivity mBaseActivity;
    private Button button;
    private boolean isCardSelected;
    private String flow;

    public ColorCard(Context context) {
        this(context, R.layout.carddemo_color_inner_base_main);
    }

    public ColorCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
        mBaseActivity = (HomeActivity)context;
    }

    private void init() {

        //Add ClickListener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //Toast.makeText(getContext(), "Click Listener card="+count, Toast.LENGTH_SHORT).show();
                //card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
            }
        });

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        final TextView title1 = (TextView) parent.findViewById(R.id.card_main_inner_title1);
        final TextView title2 = (TextView) parent.findViewById(R.id.card_main_inner_title2);
        final ImageView imageView = (ImageView) parent.findViewById(R.id.ColorImageView);
        final CheckBox checkBox = (CheckBox) parent.findViewById(R.id.color_select_checkBox);

        if (title1 != null) {
            title1.setText(mTitle1);
        }

        if(title2!=null){
            title2.setText(mTitle2);
        }

        if(imageView!=null){
            imageView.setImageResource(mImageViewResourceId);
        }


        // check if the check box on one of the cards had been selected
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // get the button value
                final Button button = (Button) mBaseActivity.findViewById(R.id.submit_request_button);

                if(isChecked){

                   String text = "Activate";
                   button.setText(text);
                   button.setVisibility(View.VISIBLE);
                    isCardSelected = true;
                }else{
                    button.setVisibility(View.GONE);
                    isCardSelected = false;
                }
            }
        });


    }

    public String getTitle() {
        return mTitle1;
    }

    public void setTitle(String title) {
        mTitle1 = title;
    }

    public String getPriceTitle() {
        return mTitle2;
    }

    public void setPriceTitle(String title) {
        mTitle2 = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getImageViewResourceId() {
        return mImageViewResourceId;
    }

    public void setImageViewResourceId(int mImageViewResourceId) {
        this.mImageViewResourceId = mImageViewResourceId;
    }

    public int getValueViewResourceId() {
        return mValueViewResourceId;
    }

    public void setValueViewResourceId(int mValueViewResourceId) {
        this.mValueViewResourceId = mValueViewResourceId;
    }

    public boolean isCardSelected(){
        return isCardSelected;
    }

    public void setFlow(String flow){
        this.flow =flow;
    }

    public String getFlow(){
        return flow;
    }
}
