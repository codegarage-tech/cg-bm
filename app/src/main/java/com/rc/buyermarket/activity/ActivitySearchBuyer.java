package com.rc.buyermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.adapter.CommonSpinnerAdapter;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.model.Bathroom;
import com.rc.buyermarket.model.Bedroom;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.ParamsSellerSearchBuyer;
import com.rc.buyermarket.model.PropertyType;
import com.rc.buyermarket.model.PurchaseType;
import com.rc.buyermarket.model.SPModel;
import com.rc.buyermarket.model.SessionBathroomList;
import com.rc.buyermarket.model.SessionBedroomList;
import com.rc.buyermarket.model.SessionCountryWithAreaList;
import com.rc.buyermarket.model.SessionExteriorList;
import com.rc.buyermarket.model.SessionPropertyTypeList;
import com.rc.buyermarket.model.SessionPurchaseTypeList;
import com.rc.buyermarket.model.SessionStyleList;
import com.rc.buyermarket.model.States;
import com.rc.buyermarket.model.Styles;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.util.DataUtil;
import com.rc.buyermarket.view.CanaroTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_SEARCH_PROPERTY;

public class ActivitySearchBuyer extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private TextView txtPriceValue;
    private Button btnSearchSubmit;
    private String countryName = "", stateName = "", zipCode = "", cityName = "", basementType = "", garageType = "", purchaseType = "", bedroomType = "", bathroomType = "", propertyType = "", styleType = "", exteriorType = "";
    private SeekBar seekBarPrice;
    private int priceRange = 10000;

    // initialize Spinner
    private Spinner spPurchaseType, spBuyPropertyType, spState, spCountry, spGarage, spBasement, spBuyBedrooms, spBuyBathroom, spBuyStyle, spBuyExterior;
    private EditText edtCity, edtZipCode;
    private CommonSpinnerAdapter basementAdapter, garageAdapter, purchaseAdapter, bedroomAdapter, bathroomAdapter, countryTypeAdapter, stateTypeAdapter, propertyTypeAdapter, styleAdapter, exteriorAdapter;
    private APIInterface apiInterface;

    //Background task
    private GetCountryWithCityTask getCountryWithCityTask;
    private GetPropertyTypeTask getPropertyTypeTask;
    private GetExteriorTask getExteriorTask;
    private GetStyleTask getStyleTask;
    private GetBathroomTask getBathroomTask;
    private GetBedroomTask getBedroomTask;
    private GetPurchaseTypeTask getPurchaseTypeTask;

    @Override
    public String initActivityTag() {
        return ActivitySearchBuyer.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_search_buyer;
    }

    @Override
    public void initStatusBarView() {

    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        txtPriceValue = (TextView) findViewById(R.id.txt_price_value);
        btnSearchSubmit = (Button) findViewById(R.id.btn_search_submit);
        spState = (Spinner) findViewById(R.id.sp_select_state);
        spBuyPropertyType = (Spinner) findViewById(R.id.sp_select_property_type);
        spPurchaseType = (Spinner) findViewById(R.id.sp_select_purchase_type);
        seekBarPrice = (SeekBar) findViewById(R.id.seekBar_price);
        spBuyBedrooms = (Spinner) findViewById(R.id.sp_select_bedrooms);
        spBuyBathroom = (Spinner) findViewById(R.id.sp_select_bathrooms);
        spBuyStyle = (Spinner) findViewById(R.id.sp_select_style);
        spBuyExterior = (Spinner) findViewById(R.id.sp_select_exterior);
        spCountry = (Spinner) findViewById(R.id.sp_select_country);
        spBasement = (Spinner) findViewById(R.id.sp_select_basement);
        spGarage = (Spinner) findViewById(R.id.sp_select_garage);
        edtCity = (EditText) findViewById(R.id.et_city);
        edtZipCode = (EditText) findViewById(R.id.et_zipcode);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.search_buyer));
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        seekBarPrice.setProgress(10000);
        txtPriceValue.setText("$10000 - $10000000");

        purchaseAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.PURCHASE_TYPE);
        spPurchaseType.setAdapter(purchaseAdapter);

        bedroomAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.BEDROOM);
        spBuyBedrooms.setAdapter(bedroomAdapter);

        bathroomAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.BATHROOM);
        spBuyBathroom.setAdapter(bathroomAdapter);

        propertyTypeAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.PROPERTY_TYPE);
        spBuyPropertyType.setAdapter(propertyTypeAdapter);

        styleAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.STYLE);
        spBuyStyle.setAdapter(styleAdapter);

        exteriorAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.EXTERIOR);
        spBuyExterior.setAdapter(exteriorAdapter);

        countryTypeAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.COUNTRY);
        spCountry.setAdapter(countryTypeAdapter);
        spCountry.setEnabled(false);

        stateTypeAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.STATE);
        spState.setAdapter(stateTypeAdapter);

        basementAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.BASEMANT);
        spBasement.setAdapter(basementAdapter);
        basementAdapter.setData(DataUtil.getAllBasements());

        garageAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.GARAGE);
        spGarage.setAdapter(garageAdapter);
        garageAdapter.setData(DataUtil.getAllGarages());

        if (NetworkManager.isInternetAvailable(getActivity())) {
            getPropertyTypeTask = new GetPropertyTypeTask(getActivity(), apiInterface);
            getPropertyTypeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getStyleTask = new GetStyleTask(getActivity(), apiInterface);
            getStyleTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getExteriorTask = new GetExteriorTask(getActivity(), apiInterface);
            getExteriorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getCountryWithCityTask = new GetCountryWithCityTask(getActivity(), apiInterface);
            getCountryWithCityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getBathroomTask = new GetBathroomTask(getActivity(), apiInterface);
            getBathroomTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getBedroomTask = new GetBedroomTask(getActivity(), apiInterface);
            getBedroomTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getPurchaseTypeTask = new GetPurchaseTypeTask(getActivity(), apiInterface);
            getPurchaseTypeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            loadOfflineData();
        }
    }

    private void loadOfflineData() {
        purchaseAdapter.setData(DataUtil.getAllPurchaseTypes(getActivity()));
        propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(getActivity()));
        countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()));
        stateTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()).get(0).getStates());
        bedroomAdapter.setData(DataUtil.getAllBedrooms(getActivity()));
        bathroomAdapter.setData(DataUtil.getAllBathrooms(getActivity()));
        styleAdapter.setData(DataUtil.getAllStyles(getActivity()));
        exteriorAdapter.setData(DataUtil.getAllExteriors(getActivity()));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        btnSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                cityName = edtCity.getText().toString();
                zipCode = edtZipCode.getText().toString();
//                if (cityName.equalsIgnoreCase("")) {
//                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_city), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (zipCode.equalsIgnoreCase("")) {
//                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_zipcode), Toast.LENGTH_SHORT).show();
//                    return;
//                }

                ParamsSellerSearchBuyer pSearchProperty = new ParamsSellerSearchBuyer(purchaseType, propertyType, countryName, cityName, stateName, bedroomType, bathroomType, basementType, garageType, styleType, exteriorType);
                Log.d(TAG, TAG + ">> pSearchProperty: " + pSearchProperty);
                Intent iSearchProperty = new Intent(getActivity(), ActivityBuyerList.class);
                iSearchProperty.putExtra(INTENT_KEY_SEARCH_PROPERTY, pSearchProperty);
                startActivity(iSearchProperty);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivityBackPress();
            }
        });

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() < 10000) {
                    seekBar.setProgress(10000);
                }

                priceRange = seekBar.getProgress();
                Log.e("priceRange", priceRange + ">>");
                txtPriceValue.setText("$10000" + " - " + "$" + priceRange);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spPurchaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PurchaseType item = (PurchaseType) parent.getItemAtPosition(position);
                purchaseType = item.getPurchase_key();
                Log.d(TAG, "purchaseType= " + purchaseType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Country item = (Country) parent.getItemAtPosition(position);

                List<States> statesList = item.getStates();
                countryName = item.getCountry_name();
                if (statesList != null && statesList.size() > 0) {
                    stateTypeAdapter.setData(item.getStates());
                    Log.d(TAG, "countryName= " + countryName);
                    Log.d(TAG, "statesList= " + statesList.size());
                    spState.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                States item = (States) parent.getItemAtPosition(position);
                stateName = item.getName();
                Log.d(TAG, "stateName= " + stateName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBuyBedrooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Bedroom item = (Bedroom) parent.getItemAtPosition(position);
                bedroomType = item.getBedroom_key();
                Log.d(TAG, "bedroomType= " + bedroomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBuyBathroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Bathroom item = (Bathroom) parent.getItemAtPosition(position);
                bathroomType = item.getBathroom_key();
                Log.d(TAG, "bathroomType= " + bathroomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBuyExterior.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Exterior item = (Exterior) parent.getItemAtPosition(position);
                exteriorType = item.getExterior_key();
                Log.d(TAG, "exteriorType= " + exteriorType);
                // Toast.makeText(getActivity(), exterior, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBuyStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Styles item = (Styles) parent.getItemAtPosition(position);
                styleType = item.getStyle_key();
                Log.d(TAG, "styleType= " + styleType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBuyPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PropertyType item = (PropertyType) parent.getItemAtPosition(position);
                propertyType = item.getProperty_key();
                Log.d(TAG, "propertyType= " + propertyType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBasement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                basementType = item.getId();

                Log.d(TAG, "basementType= " + basementType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGarage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                garageType = item.getId();
//                if (garageType.equalsIgnoreCase("")) {
//                    garageType = "0";
//                }  else {
//                    garageType = item.getId();
//                }
                Log.d(TAG, "garageType= " + garageType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        if (getCountryWithCityTask != null && getCountryWithCityTask.getStatus() == AsyncTask.Status.RUNNING) {
            getCountryWithCityTask.cancel(true);
        }

        if (getExteriorTask != null && getExteriorTask.getStatus() == AsyncTask.Status.RUNNING) {
            getExteriorTask.cancel(true);
        }

        if (getPropertyTypeTask != null && getPropertyTypeTask.getStatus() == AsyncTask.Status.RUNNING) {
            getPropertyTypeTask.cancel(true);
        }

        if (getStyleTask != null && getStyleTask.getStatus() == AsyncTask.Status.RUNNING) {
            getStyleTask.cancel(true);
        }

        if (getBathroomTask != null && getBathroomTask.getStatus() == AsyncTask.Status.RUNNING) {
            getBathroomTask.cancel(true);
        }

        if (getBedroomTask != null && getBedroomTask.getStatus() == AsyncTask.Status.RUNNING) {
            getBedroomTask.cancel(true);
        }

        if (getPurchaseTypeTask != null && getPurchaseTypeTask.getStatus() == AsyncTask.Status.RUNNING) {
            getPurchaseTypeTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /************************
     * Server communication *
     ************************/
    private class GetCountryWithCityTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetCountryWithCityTask(Context context, APIInterface apiInterface) {
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

                Call<APIResponse<List<Country>>> call = apiInterface.doGetListCountryWithState();
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
                    Log.d(TAG, "APIResponse(GetCountryWithCityTask): onResponse-server = " + result.toString());
                    APIResponse<List<Country>> data = (APIResponse<List<Country>>) result.body();
                    Log.e("Country", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        //Save data into the session
                        AppPref.savePreferences(getActivity(), AllConstants.SESSION_CITY_WITH_AREA_LIST, SessionCountryWithAreaList.getResponseString(new SessionCountryWithAreaList(data.getData())));

                        Log.d(TAG, "APIResponse(GetCountryWithCityTask()): onResponse-object = " + data.toString());
                        countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(mContext));
                        spCountry.setSelection(0);

                        stateTypeAdapter.setData(DataUtil.getAllCountryWithStates(mContext).get(0).getStates());
                        spState.setSelection(0);
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

    private class GetPropertyTypeTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetPropertyTypeTask(Context context, APIInterface apiInterface) {
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

                Call<APIResponse<List<PropertyType>>> call = apiInterface.doGetListPropertyType();
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
                    Log.d(TAG, "APIResponse(GetStyleTask): onResponse-server = " + result.toString());
                    APIResponse<List<PropertyType>> data = (APIResponse<List<PropertyType>>) result.body();
                    Log.e("Exterior", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetStyleTask()): onResponse-object = " + data.toString());

                        //Save data into the session
                        AppPref.savePreferences(getActivity(), AllConstants.SESSION_PROPERTY_TYPE_LIST, SessionPropertyTypeList.getResponseString(new SessionPropertyTypeList(data.getData())));

//                        propertyTypesList = getPropertyList(data.getData());
                        propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(mContext));
                        spBuyPropertyType.setSelection(0);
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

    private class GetStyleTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetStyleTask(Context context, APIInterface apiInterface) {
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

                Call<APIResponse<List<Styles>>> call = apiInterface.doGetListStyles();
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
                    Log.d(TAG, "APIResponse(GetStyleTask): onResponse-server = " + result.toString());
                    APIResponse<List<Styles>> data = (APIResponse<List<Styles>>) result.body();
                    Log.e("Exterior", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        //Save data into the session
                        AppPref.savePreferences(getActivity(), AllConstants.SESSION_STYLE_LIST, SessionStyleList.getResponseString(new SessionStyleList(data.getData())));

                        styleAdapter.setData(DataUtil.getAllStyles(mContext));
                        spBuyStyle.setSelection(0);
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

    private class GetExteriorTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetExteriorTask(Context context, APIInterface apiInterface) {
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

                Call<APIResponse<List<Exterior>>> call = apiInterface.doGetListExterior();
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
                    Log.d(TAG, "APIResponse(GetExteriorTask): onResponse-server = " + result.toString());
                    APIResponse<List<Exterior>> data = (APIResponse<List<Exterior>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetExteriorTask()): onResponse-object = " + data.toString());
                        //Save data into the session
                        AppPref.savePreferences(getActivity(), AllConstants.SESSION_EXTERIOR_LIST, SessionExteriorList.getResponseString(new SessionExteriorList(data.getData())));

                        exteriorAdapter.setData(DataUtil.getAllExteriors(mContext));
                        spBuyExterior.setSelection(0);
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

    private class GetBathroomTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetBathroomTask(Context context, APIInterface apiInterface) {
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
                Call<APIResponse<List<Bathroom>>> call = apiInterface.getAllBathroomTypes();
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
                    Log.d(TAG, "APIResponse(GetBathroomTask): onResponse-server = " + result.toString());
                    APIResponse<List<Bathroom>> data = (APIResponse<List<Bathroom>>) result.body();
                    Log.e("Bathroom", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetBathroomTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_BATHROOM_LIST, SessionBathroomList.getResponseString(new SessionBathroomList(data.getData())));

                            bathroomAdapter.setData(DataUtil.getAllBathrooms(mContext));
                        }
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

    private class GetBedroomTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetBedroomTask(Context context, APIInterface apiInterface) {
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
                Call<APIResponse<List<Bedroom>>> call = apiInterface.getAllBedroomTypes();
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
                    Log.d(TAG, "APIResponse(GetBedroomTask): onResponse-server = " + result.toString());
                    APIResponse<List<Bedroom>> data = (APIResponse<List<Bedroom>>) result.body();
                    Log.e("Bedroom", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetBedroomTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_BEDROOM_LIST, SessionBedroomList.getResponseString(new SessionBedroomList(data.getData())));

                            bedroomAdapter.setData(DataUtil.getAllBedrooms(mContext));
                        }
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

    private class GetPurchaseTypeTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetPurchaseTypeTask(Context context, APIInterface apiInterface) {
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
                Call<APIResponse<List<PurchaseType>>> call = apiInterface.getAllPurchaseTypes();
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
                    Log.d(TAG, "APIResponse(GetPurchaseTypeTask): onResponse-server = " + result.toString());
                    APIResponse<List<PurchaseType>> data = (APIResponse<List<PurchaseType>>) result.body();
                    Log.e("Bedroom", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetPurchaseTypeTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_PURCHASE_TYPE_LIST, SessionPurchaseTypeList.getResponseString(new SessionPurchaseTypeList(data.getData())));

                            purchaseAdapter.setData(DataUtil.getAllPurchaseTypes(mContext));
                        }
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