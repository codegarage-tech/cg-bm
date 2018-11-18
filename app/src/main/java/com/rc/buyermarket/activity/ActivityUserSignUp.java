package com.rc.buyermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.enumeration.UserType;
import com.rc.buyermarket.model.BuyerLogin;
import com.rc.buyermarket.model.ParamsBuyerSignUp;
import com.rc.buyermarket.model.ParamsSellerSignUp;
import com.rc.buyermarket.model.SellerLogin;
import com.rc.buyermarket.model.card.ParamCheckout;
import com.rc.buyermarket.model.card.StripeCard;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.util.AppUtil;
import com.rc.buyermarket.util.ValidationManager;
import com.rc.buyermarket.view.CanaroTextView;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_USER_TYPE;
import static com.rc.buyermarket.util.AllConstants.STRIPE_PUBLISHABLE_KEY;
import static com.rc.buyermarket.util.AllConstants.STRIPE_SECRET_KEY;

public class ActivityUserSignUp extends BaseActivity {
    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private Button btnUserSignUp;
    private EditText etSignUpFname, etSignUpLname, etSignUpEmail, etSignUpPass, etSignUpPhone;
    private RadioGroup rgSignupPay;
    private RadioButton rbOneDays, rbThirtyDays;
    private AppCompatCheckBox cbSignUpTerms;
    private LinearLayout llCardInfo;
    private TextView tvLogin;
    private CardMultilineWidget mCardMultilineWidget;
    private APIInterface apiInterface;

    //Background task
    GetBuyerSignUpTask buyerSignUpTask;
    GetSellerSignUpTask sellerSignUpTask;
    private UserType userType;
    private ParamCheckout paramCheckout;
    private String signup_day = "";
    private float totalPrice = 0.0f;

    @Override
    public String initActivityTag() {
        return ActivityUserSignUp.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_singup;
    }

    @Override
    public void initStatusBarView() {

    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

        if (intent != null && !AppUtil.isNullOrEmpty(intent.getStringExtra(INTENT_KEY_USER_TYPE))) {

            userType = UserType.valueOf(intent.getStringExtra(INTENT_KEY_USER_TYPE));
            Log.d(TAG, TAG + ">>>" + " userType: " + userType);
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        etSignUpFname = (EditText) findViewById(R.id.et_signup_fname);
        etSignUpLname = (EditText) findViewById(R.id.et_signup_lname);
        etSignUpEmail = (EditText) findViewById(R.id.et_signup_email);
        etSignUpPass = (EditText) findViewById(R.id.et_signup_pass);
        etSignUpPhone = (EditText) findViewById(R.id.et_signup_phone);
        btnUserSignUp = (Button) findViewById(R.id.btn_user_signUp);
        llCardInfo = (LinearLayout) findViewById(R.id.ll_card_info);
        rgSignupPay = (RadioGroup) findViewById(R.id.rg_signup_pay);
        rbOneDays = (RadioButton) findViewById(R.id.rb_one_days);
        rbThirtyDays = (RadioButton) findViewById(R.id.rb_thirty_days);
        cbSignUpTerms = (AppCompatCheckBox) findViewById(R.id.cb_sign_up_terms);
        tvLogin = (TextView) findViewById(R.id.txt_user_login);

        if (userType.equals(UserType.BUYER)) {
            llCardInfo.setVisibility(View.GONE);
        } else if (userType.equals(UserType.SELLER)) {
            llCardInfo.setVisibility(View.VISIBLE);
            btnUserSignUp.setText(getResources().getString(R.string.title_signup_and_pay));
        }
        mCardMultilineWidget = findViewById(R.id.card_multiline_widget);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.signup));
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        //set radio button selected price
        setTotalPrice();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        btnUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    doSignUp();
                }

            }
        });
        rgSignupPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setTotalPrice();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivityBackPress();
            }
        });
    }

    private void setTotalPrice(){
        if (rbOneDays.isChecked()) {
            signup_day = "1";
            totalPrice = 2.99f;
            Log.e("signup_day>>>", signup_day + "");
            Log.d(TAG, TAG + ">>>" + " totalPrice: " + totalPrice);
        } else {
            signup_day = "30";
            totalPrice = 5.99f;
            Log.e("signup_day>>>", signup_day + "");
            Log.d(TAG, TAG + ">>>" + " totalPrice: " + totalPrice);
        }
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();

        if (buyerSignUpTask != null && buyerSignUpTask.getStatus() == AsyncTask.Status.RUNNING) {
            buyerSignUpTask.cancel(true);
        }

        if (sellerSignUpTask != null && sellerSignUpTask.getStatus() == AsyncTask.Status.RUNNING) {
            sellerSignUpTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /************************
     * A validation check for empty and calling AsyncTask *
     ************************/
    private void doSignUp() {
        String fName = etSignUpFname.getText().toString();
        String lName = etSignUpLname.getText().toString();
        String password = etSignUpPass.getText().toString();
        String email = etSignUpEmail.getText().toString();
        String phoneNumber = etSignUpPhone.getText().toString();

        if (AppUtil.isNullOrEmpty(fName)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_fname), Toast.LENGTH_SHORT).show();
            return;
        }
        if (AppUtil.isNullOrEmpty(lName)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_lname), Toast.LENGTH_SHORT).show();
            return;
        }

        if (AppUtil.isNullOrEmpty(email)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidationManager.isValidEmail(email)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_valid_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (AppUtil.isNullOrEmpty(password)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!AppUtil.isNullOrEmpty(phoneNumber)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_valid_mobile_no), Toast.LENGTH_SHORT).show();
            return;
        }

        switch (userType) {
            case BUYER:
                buyerSignUpTask = new GetBuyerSignUpTask(getActivity(), apiInterface);
                buyerSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Log.d(TAG, TAG + "BUYER>>>" + " userType: " + userType);
                break;
            case SELLER:
                doSellerSignUp();
                Log.d(TAG, TAG + "SELLER>>>" + " userType: " + userType);
                break;
        }

    }

    private void doSellerSignUp() {
        if (rbOneDays.getText().toString().equalsIgnoreCase(getResources().getString(R.string.radio_one))) {

        } else if (rbThirtyDays.getText().toString().equalsIgnoreCase(getResources().getString(R.string.radio_two))) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_select_any_days), Toast.LENGTH_SHORT).show();

        }
        if (!mCardMultilineWidget.validateAllFields()) {
            Toast.makeText(ActivityUserSignUp.this, getString(R.string.toast_please_input_valid_card_information), Toast.LENGTH_SHORT).show();
            return;
        }
        Card card = mCardMultilineWidget.getCard();
        if (card == null) {
            Toast.makeText(ActivityUserSignUp.this, getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cbSignUpTerms.isChecked()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_select_terms_conditions), Toast.LENGTH_SHORT).show();
            cbSignUpTerms.setHintTextColor(Color.RED);
            return;
        }
        StripeCard stripeCard = new StripeCard(card.getNumber(), card.getLast4(), card.getName(), card.getExpMonth(), card.getExpYear(), card.getCVC(), card.getAddressZip());
        Log.e("stripeCard", stripeCard.toString() + "");
        submitCardInfo(stripeCard);

    }

    /******************
     * Stripe methods *
     ******************/
    private void submitCardInfo(StripeCard stripeCard) {
        paramCheckout = new ParamCheckout();
        paramCheckout.setFirst_name(etSignUpFname.getText().toString());
        paramCheckout.setLast_name(etSignUpLname.getText().toString());
        paramCheckout.setEmail(etSignUpEmail.getText().toString());
        paramCheckout.setPhone(etSignUpPhone.getText().toString());

        if (stripeCard != null) {
            Card card = new Card(stripeCard.getCardNumber(), stripeCard.getCardExpireMonth(), stripeCard.getCardExpireYear(), stripeCard.getCardCvc());

            if (card.validateCard()) {
                showProgressDialog();

                Stripe stripe = new Stripe(ActivityUserSignUp.this, STRIPE_PUBLISHABLE_KEY);
                stripe.createToken(card, new TokenCallback() {
                            public void onSuccess(Token token) {
                                //Token successfully created.
                                //Create a charge or save token to the server and use it later
                                Log.e("totalPrice>>>", totalPrice + "");

                                new doCharge(ActivityUserSignUp.this, token, totalPrice).execute();
                            }

                            public void onError(Exception error) {
                                dismissProgressDialog();

                                Toast.makeText(getApplicationContext(), getString(R.string.toast_error_on_creating_token), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } else {
                Toast.makeText(ActivityUserSignUp.this, getString(R.string.toast_invalid_card), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ActivityUserSignUp.this, getString(R.string.toast_please_select_card), Toast.LENGTH_SHORT).show();
        }
    }

    private class doCharge extends AsyncTask<String, Long, Charge> {

        private Context mContext;
        private Token mToken;
        private float mAmount = 0.0f;

        public doCharge(Context context, Token token, float amount) {
            mContext = context;
            mToken = token;
            mAmount = amount;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Charge doInBackground(String... params) {
            try {
                int amount = (int) (mAmount * 100);
                Log.d(TAG, "ChargeAmount: " + amount + " cent");
                Log.d(TAG, "ChargeAmount: $" + (int) mAmount);

                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", amount);
                chargeParams.put("currency", "usd");
                chargeParams.put("description", "Charged $" + (int) mAmount + " from Android");
                chargeParams.put("source", mToken.getId());

                com.stripe.Stripe.apiKey = STRIPE_SECRET_KEY;

                return Charge.create(chargeParams);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Charge charge) {
            if (charge != null && charge.getPaid() && charge.getStatus().equalsIgnoreCase("succeeded")) {
                Log.d(TAG, "ChargeInfo: " + charge.toString() + "\nTransaction_id: " + charge.getId());

                //Set transaction id
                paramCheckout.setTransaction_id(charge.getId());
                Log.d(TAG, "paramCheckout: " + paramCheckout.toString() + "");

                sellerSignUpTask = new GetSellerSignUpTask(getActivity(), apiInterface);
                sellerSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //new DoOrderTask(mContext, paramCheckout).execute();
            } else {
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.toast_payment_is_not_successful), Toast.LENGTH_LONG).show();
            }
        }
    }

    /************************
     * Server communication *
     ************************/
    private class GetBuyerSignUpTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetBuyerSignUpTask(Context context, APIInterface apiInterface) {
            mContext = context;
            mApiInterface = apiInterface;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {

                ParamsBuyerSignUp pBuyerSignUp = new ParamsBuyerSignUp(etSignUpFname.getText().toString(), etSignUpLname.getText().toString(), etSignUpEmail.getText().toString(),
                        etSignUpPhone.getText().toString(), etSignUpPass.getText().toString().trim(), 0);
                Log.d(TAG, "pBuyerSignUp: onResponse-server = " + pBuyerSignUp.toString());

                Call<APIResponse<List<BuyerLogin>>> call = apiInterface.doBuyerSignUp(pBuyerSignUp);
                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissProgressDialog();

                if (result != null && result.isSuccessful()) {
                    Log.d(TAG, "APIResponse(GetBuyerLoginTask): onResponse-server = " + result.toString());
                    APIResponse<List<BuyerLogin>> data = (APIResponse<List<BuyerLogin>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetBuyerLoginTask()): onResponse-object = " + data.toString());
//                        Intent intentLogin = new Intent(getActivity(), ActivityUserLogin.class);
//                        intentLogin.putExtra(INTENT_KEY_USER_TYPE, userType.name());
//                        startActivity(intentLogin);
                        finish();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetSellerSignUpTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetSellerSignUpTask(Context context, APIInterface apiInterface) {
            mContext = context;
            mApiInterface = apiInterface;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Response doInBackground(String... params) {
            try {

                ParamsSellerSignUp pSellerSignUp = new ParamsSellerSignUp(etSignUpFname.getText().toString(), etSignUpLname.getText().toString(), etSignUpEmail.getText().toString(),
                        etSignUpPhone.getText().toString().trim(), etSignUpPass.getText().toString(), 0);
                Log.d(TAG, "pSellerSignUp: onResponse-server = " + pSellerSignUp.toString());
                Call<APIResponse<List<SellerLogin>>> call = apiInterface.doSellerSignUp(pSellerSignUp);

                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissProgressDialog();

                if (result != null && result.isSuccessful()) {
                    Log.d(TAG, "APIResponse(GetBuyerLoginTask): onResponse-server = " + result.toString());
                    APIResponse<List<SellerLogin>> data = (APIResponse<List<SellerLogin>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetSellerLoginTask()): onResponse-object = " + data.toString());
                        Toast.makeText(ActivityUserSignUp.this, getString(R.string.toast_payment_is_successful) + "\n" + data.getMessage(), Toast.LENGTH_LONG).show();

//                        Intent intentLogin = new Intent(getActivity(), ActivityUserLogin.class);
//                        intentLogin.putExtra(INTENT_KEY_USER_TYPE, userType.name());
//                        startActivity(intentLogin);
                        finish();
                        Log.d(TAG, "UserType.SELLER= " + AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE));

                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
