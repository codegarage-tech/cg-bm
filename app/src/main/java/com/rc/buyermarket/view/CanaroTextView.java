package com.rc.buyermarket.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.rc.buyermarket.application.BuyerMarketApp;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CanaroTextView extends AppCompatTextView {

    public CanaroTextView(Context context) {
        this(context, null);
    }

    public CanaroTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanaroTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(BuyerMarketApp.canaroExtraBold);
    }
}
