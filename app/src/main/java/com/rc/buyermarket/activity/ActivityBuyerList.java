package com.rc.buyermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.adapter.SearchPropertyListAdapter;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.model.ParamsSellerSearchBuyer;
import com.rc.buyermarket.model.SellerSearchBuyer;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.view.CanaroTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_SEARCH_PROPERTY;

public class ActivityBuyerList extends BaseActivity {

    //Toolbar
    ImageView ivBack;
    CanaroTextView tvTitle;
    LinearLayout llMainSearchList, llShow;
    // initialize SpinnerAdapter
    private SearchPropertyListAdapter searchPropertyListAdapter;
    RecyclerView rvSearchProperty;
    private APIInterface apiInterface;
    SellerSearchBuyer sellerSearchBuyer;
    GetSellerSearchPropertyTask getSellerSearchPropertyTask;
    ParamsSellerSearchBuyer pSellerSearchProperty;

    List<SellerSearchBuyer> questions = new ArrayList<>();

    @Override
    public String initActivityTag() {
        return ActivityBuyerList.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_buyer_list;
    }

    @Override
    public void initStatusBarView() {

    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
            //Intent data
            ParamsSellerSearchBuyer getParamsSellerSearchBuyer = intent.getParcelableExtra(INTENT_KEY_SEARCH_PROPERTY);
            if (getParamsSellerSearchBuyer != null) {
                pSellerSearchProperty = getParamsSellerSearchBuyer;
                Log.d(TAG, TAG + ">>>" + " paramsSellerSearchProperty: " + getParamsSellerSearchBuyer.toString());
            }
        }
    }


    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        rvSearchProperty = (RecyclerView) findViewById(R.id.rv_search_property);
        llMainSearchList = (LinearLayout) findViewById(R.id.ll_main_search_list);
        llShow = (LinearLayout) findViewById(R.id.ll_hide);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.txt_buyer));
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        searchPropertyListAdapter = new SearchPropertyListAdapter(getActivity());
        rvSearchProperty.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSearchProperty.setAdapter(searchPropertyListAdapter);
//        searchPropertyListAdapter = new SearchPropertyListAdapter(getActivity());
//        rvSearchProperty.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvSearchProperty.setAdapter(searchPropertyListAdapter);
        if (!NetworkManager.isInternetAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            getSellerSearchPropertyTask = new GetSellerSearchPropertyTask(getActivity());
            getSellerSearchPropertyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

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
//        dismissProgressDialog();
//
//        if (buyerLoginTask != null && buyerLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
//            buyerLoginTask.cancel(true);
//        }

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    private class GetSellerSearchPropertyTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        public GetSellerSearchPropertyTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<SellerSearchBuyer>>> call = apiInterface.doSellerSearchProperty(pSellerSearchProperty);
                Log.d(TAG, "APIResponse(param): " + pSellerSearchProperty.toString());

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
                    Log.d(TAG, "APIResponse(GetSellerSearchPropertyTask): onResponse-server = " + result.toString());
                    APIResponse<List<SellerSearchBuyer>> data = (APIResponse<List<SellerSearchBuyer>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(SellerSearchBuyer()): onResponse-object = " + data.toString());
                        Log.d(TAG, "SearchProperty" + data.getData().size());
                        if (data.getData().size() > 0) {
                            llMainSearchList.setVisibility(View.VISIBLE);
                            llShow.setVisibility(View.GONE);
                            searchPropertyListAdapter.addAll(data.getData());
                            searchPropertyListAdapter.notifyDataSetChanged();

                        } else {
                            llShow.setVisibility(View.VISIBLE);
                            llMainSearchList.setVisibility(View.GONE);
                        }

//                        setValueClear();
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
