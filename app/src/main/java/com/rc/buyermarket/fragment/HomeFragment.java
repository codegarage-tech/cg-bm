package com.rc.buyermarket.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.rc.buyermarket.R;
import com.rc.buyermarket.activity.ActivityAddProperty;
import com.rc.buyermarket.activity.ActivitySearchBuyer;
import com.rc.buyermarket.activity.ActivityUserLogin;
import com.rc.buyermarket.activity.MainActivity;
import com.rc.buyermarket.base.BaseFragment;
import com.rc.buyermarket.enumeration.PropertyEnum;
import com.rc.buyermarket.enumeration.UserType;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.util.IntentManager;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY_ENUM;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_USER_TYPE;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_ADD_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_LOGIN;

public class HomeFragment extends BaseFragment {

    // initialize CardView
    private CardView cvAddProperty;
    private CardView cvSearchProperty;
    private CardView cvSeller;
    private CardView cvBuyer;
    private CardView cvFaq;

    private UserType userType;
    private PropertyEnum propertyEnum;


    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initFragmentViews(View parentView) {

        //defining cards
        cvAddProperty =(CardView)parentView.findViewById (R.id.bank_cardId);
        cvSearchProperty =(CardView)parentView.findViewById(R.id.search_cardId);
        cvSeller=(CardView)parentView.findViewById(R.id.cv_seller);
        cvBuyer=(CardView)parentView.findViewById(R.id.cv_buyer);
        cvFaq=(CardView)parentView.findViewById(R.id.cv_faq);
    }

    @Override
    public void initFragmentViewsData() {


    }

    @Override
    public void initFragmentActions() {

        //add click listener to the cards
        cvAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPref.getBooleanSetting(getActivity(), AllConstants.SESSION_IS_LOGGED_IN, false)) {
                    if (AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("0")) {
                        propertyEnum = PropertyEnum.PROPERTY_ADD;
                        Intent iAddHome = new Intent(getActivity(), ActivityAddProperty.class);
                        iAddHome.putExtra(INTENT_KEY_PROPERTY_ENUM, propertyEnum.name());
                        getActivity().startActivityForResult(iAddHome, INTENT_REQUEST_CODE_ADD_PROPERTY);
                    } else {
                      //  alertShow(getResources().getString(R.string.title_seller_logout));
                        alertShowDialog(getResources().getString(R.string.mgs_seller_logout));

                    }
                } else {
                    userType = UserType.BUYER;
                    Log.e("BUYER", userType + "");
                    Intent iUserLogin = new Intent(getActivity(), ActivityUserLogin.class);
                    iUserLogin.putExtra(INTENT_KEY_USER_TYPE, userType.name());
                    getActivity().startActivityForResult(iUserLogin, INTENT_REQUEST_CODE_LOGIN);
                }
            }
        });
        cvSearchProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPref.getBooleanSetting(getActivity(), AllConstants.SESSION_IS_LOGGED_IN, false)) {

                    if (AppPref.getPreferences(getActivity(),AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("1")) {
                        Intent iAddHome = new Intent(getActivity(), ActivitySearchBuyer.class);
                        startActivity(iAddHome);
                    } else {
                       // alertShow(getResources().getString(R.string.title_buyer_logout));
                        alertShowDialog(getResources().getString(R.string.mgs_buyer_logout));
                    }
                } else {
                    userType = UserType.SELLER;
                    Log.e("SELLER",userType+"");
                    Intent iUserLogin = new Intent(getActivity(), ActivityUserLogin.class);
                    iUserLogin.putExtra(INTENT_KEY_USER_TYPE, userType.name());
                    getActivity().startActivityForResult(iUserLogin, INTENT_REQUEST_CODE_LOGIN);
                }


            }
        });
        cvSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.openYoutubeVideo(getActivity(), "-zl1r6APkc0");
            }
        });
        cvBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.openYoutubeVideo(getActivity(), "1Q1hl9urB7o");
            }
        });
        cvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * Log out
     */
    public void alertShowDialog(String mgs) {
        PopupDialog.create(this)
                .outsideTouchHide(false)
                .dismissTime(1000 * 10)
                .controller( AlertController.build()
                        .title(getResources().getString(R.string.title_logout))
                        .message(mgs)
                        .negativeButton( getString(R.string.dialog_cancel),null)
                        .positiveButton( getString( R.string.txt_logout ), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MainActivity)getActivity()).doLogout();
                            }
                        } ))
                .show();

    }
}
