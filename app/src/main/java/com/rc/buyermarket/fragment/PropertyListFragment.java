package com.rc.buyermarket.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.adapter.PropertyListAdapter;
import com.rc.buyermarket.base.BaseFragment;
import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.model.PropertyDelete;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_EDIT_PROPERTY;

public class PropertyListFragment extends BaseFragment {

    RecyclerView rvProperty;
    PropertyListAdapter propertyListAdapter;
    private APIInterface apiInterface;
    //Background task
    GetPropertyListTask getPropertyListTask;
    private String TAG = PropertyListFragment.class.getSimpleName();

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_property_list;
    }

    @Override
    public void initFragmentViews(View parentView) {
        rvProperty = (RecyclerView)parentView.findViewById(R.id.rv_property);
    }

    @Override
    public void initFragmentViewsData() {
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        propertyListAdapter = new PropertyListAdapter(getActivity());
        rvProperty.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProperty.setAdapter(propertyListAdapter);

        if (!NetworkManager.isInternetAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (AppPref.getBooleanSetting(getActivity(), AllConstants.SESSION_IS_LOGGED_IN, false) && AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE).equalsIgnoreCase("0")) {
                Log.e("buyerLoginData", AppPref.getPreferences(getActivity(), AllConstants.SESSION_USER_TYPE) + "");
                getPropertyListTask = new GetPropertyListTask(getActivity(), apiInterface);
                getPropertyListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }  else {
                Log.e("others", "No Profile" + "");
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();

            }
        }


    }

    @Override
    public void initFragmentActions() {

    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, TAG + ">>>>> " + requestCode + " " + resultCode + " ");
        switch (requestCode) {
            case INTENT_REQUEST_CODE_EDIT_PROPERTY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        AddProperty addProperty = data.getParcelableExtra(INTENT_KEY_PROPERTY);
                        if (addProperty != null) {
                            int position = propertyListAdapter.getItemPosition(addProperty);
                            if (position != -1) {
                                propertyListAdapter.update(addProperty, position);
                            }
                        }
                    }
                }
                break;
        }
    }

    /************************
     * Server communication *
     ************************/
    private class GetPropertyListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetPropertyListTask(Context context, APIInterface apiInterface) {
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
                String buyerId= AppPref.getPreferences(getActivity(), AllConstants.SESSION_BUYER_ID);
                Log.e("buyerId",buyerId+"");
                Call<APIResponse<List<AddProperty>>> call = apiInterface.doGetListProperty(buyerId);
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
                    Log.d(TAG, "APIResponse(GetPropertyListTask): onResponse-server = " + result.toString());
                    APIResponse<List<AddProperty>> data = (APIResponse<List<AddProperty>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetPropertyListTask()): onResponse-object = " + data.toString());
                        propertyListAdapter.addAll(data.getData());
                        propertyListAdapter.notifyDataSetChanged();
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

    private class GetDeletePropertyListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetDeletePropertyListTask(Context context, APIInterface apiInterface) {
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

                Call<APIResponse<PropertyDelete>> call = apiInterface.doGetDeleteListProperty("5");
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
                    Log.d(TAG, "APIResponse(GetPropertyListTask): onResponse-server = " + result.toString());
                    APIResponse<PropertyDelete> data = (APIResponse<PropertyDelete>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetPropertyListTask()): onResponse-object = " + data.toString());
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
