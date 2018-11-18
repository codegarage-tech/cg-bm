package com.rc.buyermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.enumeration.UserType;
import com.rc.buyermarket.model.BuyerLogin;
import com.rc.buyermarket.model.ParamsBuyerLogin;
import com.rc.buyermarket.model.ParamsSellerLogin;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.util.AppUtil;
import com.rc.buyermarket.util.ValidationManager;
import com.rc.buyermarket.view.CanaroTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_USER_TYPE;

public class ActivityUserLogin extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private Button btnUserLogin;
    private TextView txtUserSignUp;
    private EditText etUsername, etUserPass;
    private APIInterface apiInterface;
    private UserType userType;

    //Background task
    GetBuyerLoginTask buyerLoginTask;

    @Override
    public String initActivityTag() {
        return ActivityUserLogin.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_login;
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
        etUsername = (EditText) findViewById(R.id.et_login_username);
        etUserPass = (EditText) findViewById(R.id.et_login_pass);
        btnUserLogin = (Button) findViewById(R.id.btn_user_login);
        txtUserSignUp = (TextView) findViewById(R.id.txt_user_signUp);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.login));

        switch (userType) {

            case BUYER:
                if (AppUtil.isDebug(getActivity())) {
                    etUsername.setText("rashed.droid1@gmail.com");
                    etUserPass.setText("123456");
                }
                break;
            case SELLER:
                if (AppUtil.isDebug(getActivity())) {
                    etUsername.setText("niloy.cste@gmail.com");
                    etUserPass.setText("123456");
                }
                break;

        }

        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //Check empty field
                    doLogin();

                }
            }
        });

        txtUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSignUP = new Intent(getActivity(), ActivityUserSignUp.class);
                iSignUP.putExtra(INTENT_KEY_USER_TYPE, userType.name());
                startActivity(iSignUP);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivityBackPress();
            }
        });
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

        if (buyerLoginTask != null && buyerLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
            buyerLoginTask.cancel(true);
        }

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /************************
     * validation check for empty  *
     ************************/
    private void doLogin() {
        String email = etUsername.getText().toString();
        String password = etUserPass.getText().toString();

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

        buyerLoginTask = new GetBuyerLoginTask(getActivity(), apiInterface);
        buyerLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    /************************
     * Server communication *
     ************************/
    private class GetBuyerLoginTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetBuyerLoginTask(Context context, APIInterface apiInterface) {
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


                Call<APIResponse<List<BuyerLogin>>> call = null;

                switch (userType) {

                    case BUYER:
                        ParamsBuyerLogin pBuyerLogin = new ParamsBuyerLogin(etUsername.getText().toString(), etUserPass.getText().toString().trim());
                        Log.d(TAG, "pBuyerLogin: onResponse-server = " + pBuyerLogin.toString());
                        call = apiInterface.doBuyerLogin(pBuyerLogin);
                        break;

                    case SELLER:
                        ParamsSellerLogin pSellerLogin = new ParamsSellerLogin(etUsername.getText().toString(), etUserPass.getText().toString().trim());
                        Log.d(TAG, "pSellerLogin: onResponse-server = " + pSellerLogin.toString());
                        call = apiInterface.doSellerLogin(pSellerLogin);
                        break;
                }

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
                        List<BuyerLogin> buyerLoginList = data.getData();
                        AppPref.saveObjectsList(getActivity(), AllConstants.SESSION_USER_BUYER, buyerLoginList);
                        AppPref.setBooleanSetting(getActivity(), AllConstants.SESSION_IS_LOGGED_IN, true);
                        if (userType.equals(UserType.BUYER)) {
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_BUYER_ID, buyerLoginList.get(0).getId());
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_USER_TYPE, "0");
                        } else if (userType.equals(UserType.SELLER)) {
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_SELLER_ID, buyerLoginList.get(0).getId());
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_USER_TYPE, "1");
                        }

                        //return to the home activity
                        Intent intentLogin = new Intent();
                        setResult(RESULT_OK, intentLogin);
                        Log.d(ActivityUserLogin.class.getSimpleName(), "(ActivityUserLogin)finishing login");
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


}
