package com.ibm.techathon.elven.smartpool.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by meshriva on 9/9/2014.
 */
public class GplayGridThumb extends CardThumbnail {

    public GplayGridThumb(Context context) {
        super(context);
    }
    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        //viewImage.getLayoutParams().width = 196;
        //viewImage.getLayoutParams().height = 196;
    }
}