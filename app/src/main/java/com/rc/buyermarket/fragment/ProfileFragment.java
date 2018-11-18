package com.rc.buyermarket.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rc.buyermarket.DashBoardMenu.AddHouse;
import com.rc.buyermarket.DashBoardMenu.Links;
import com.rc.buyermarket.DashBoardMenu.Login;
import com.rc.buyermarket.DashBoardMenu.Wifi;
import com.rc.buyermarket.R;
import com.rc.buyermarket.activity.ActivityUserLogin;
import com.rc.buyermarket.activity.ActivityUserSignUp;
import com.rc.buyermarket.activity.MainActivity;
import com.rc.buyermarket.base.BaseFragment;
import com.rc.buyermarket.enumeration.UserType;
import com.rc.buyermarket.model.BuyerLogin;
import com.rc.buyermarket.model.ParamsBuyerSignUp;
import com.rc.buyermarket.model.ParamsSellerSignUp;
import com.rc.buyermarket.model.ParamsUpdateSellerProfile;
import com.rc.buyermarket.model.SellerLogin;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_USER_TYPE;
import static com.rc.buyermarket.util.AllConstants.SESSION_USER_BUYER;

public class ProfileFragment extends BaseFragment {

    private Button btnProfileUpdate;
    private EditText etProfileFname,etProfileLname,etProfileEmail,etProfilePass,etProfilePhone;
    private int userId;
    private UserType userType;
    List<BuyerLogin> buyerLoginData;
    private APIInterface apiInterface;
    //Background task
    GetBuyerProfileUpdateTask buyerLoginTask;
    GetSellerProfileUpdateTask sellerSignUpTask;
    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initFragmentViews(View parentView) {

        etProfileFname = (EditText)parentView.findViewById(R.id.et_profile_fname);
        etProfileLname = (EditText)parentView.findViewById(R.id.et_profile_lname);
        etProfileEmail = (EditText)parentView.findViewById(R.id.et_profile_email);
        etProfilePass = (EditText)parentView.findViewById(R.id.et_profile_pass);
        etProfilePhone = (EditText)parentView.findViewById(R.id.et_profile_phone);
        btnProfileUpdate = (Button)parentView.findViewById(R.id.btn_profile_update);
    }

    @Override
    public void initFragmentViewsData() {
        buyerLoginData = AppPref.getObjectsList(getActivity(), SESSION_USER_BUYER, BuyerLogin.class);
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        updateSetData(buyerLoginData);
    }

    @Override
    public void initFragmentActions() {

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("0")) {
                        Log.e("buyerLoginData", AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE) + "");
                        buyerLoginTask = new GetBuyerProfileUpdateTask(getActivity(), apiInterface);
                        buyerLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else if (AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("1")) {
                        sellerSignUpTask = new GetSellerProfileUpdateTask(getActivity(), apiInterface);
                        sellerSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        Log.e("SellerLoginData", AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE) + "");
                    } else {
                        Log.e("others", "No Profile" + "");
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

   private void updateSetData(List<BuyerLogin> buyerLoginData){
       if (buyerLoginData!= null){
           etProfileFname.setText(buyerLoginData.get(0).getFirst_name());
           etProfileLname.setText(buyerLoginData.get(0).getLast_name());
           etProfileEmail.setText(buyerLoginData.get(0).getEmail());
           etProfilePass.setText(buyerLoginData.get(0).getPassword());
           etProfilePhone.setText(buyerLoginData.get(0).getPhone());
           userId = Integer.parseInt(buyerLoginData.get(0).getId());
       }
   }

    /************************
     * Server communication *
     ************************/
    private class GetBuyerProfileUpdateTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetBuyerProfileUpdateTask(Context context, APIInterface apiInterface) {
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

                ParamsBuyerSignUp pBuyerSignUp = new ParamsBuyerSignUp(etProfileFname.getText().toString(),etProfileLname.getText().toString(), etProfileEmail.getText().toString(),
                                                                       etProfilePhone.getText().toString(),etProfilePass.getText().toString().trim(),userId);
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
                    Log.d(TAG, "APIResponse(GetBuyerProfileUpdateTask): onResponse-server = " + result.toString());
                    APIResponse<List<BuyerLogin>> data = (APIResponse<List<BuyerLogin>>) result.body();
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetBuyerProfileUpdateTask()): onResponse-object = " + data.toString());
                        buyerLoginData = data.getData();

                        AppPref.saveObjectsList( getActivity(), AllConstants.SESSION_USER_BUYER, buyerLoginData);
                        updateSetData(buyerLoginData);
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

    private class GetSellerProfileUpdateTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetSellerProfileUpdateTask(Context context, APIInterface apiInterface) {
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

                ParamsUpdateSellerProfile pSellerupdate = new ParamsUpdateSellerProfile(etProfileFname.getText().toString(),etProfileLname.getText().toString(), etProfileEmail.getText().toString(),
                                                                          etProfilePhone.getText().toString(),etProfilePass.getText().toString().trim(),userId);
                Log.d(TAG, "pSellerUpdate: onResponse-server = " + pSellerupdate.toString());
                Call<APIResponse<List<SellerLogin>>> call  = apiInterface.doSellerProfileUpdate(pSellerupdate);

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
                    Log.d(TAG, "APIResponse(GetSellerProfileUpdateTask): onResponse-server = " + result.toString());
                    APIResponse<List<SellerLogin>> data = (APIResponse<List<SellerLogin>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetSellerProfileUpdateTask()): onResponse-object = " + data.toString());

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
