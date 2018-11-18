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
import com.rc.buyermarket.enumeration.PropertyEnum;
import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.ParamsAddProperty;
import com.rc.buyermarket.model.PropertyType;
import com.rc.buyermarket.model.SPModel;
import com.rc.buyermarket.model.SessionCountryWithAreaList;
import com.rc.buyermarket.model.SessionExteriorList;
import com.rc.buyermarket.model.SessionPropertyTypeList;
import com.rc.buyermarket.model.SessionStyleList;
import com.rc.buyermarket.model.States;
import com.rc.buyermarket.model.Styles;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.AllConstants;
import com.rc.buyermarket.util.AppPref;
import com.rc.buyermarket.util.AppUtil;
import com.rc.buyermarket.util.DataUtil;
import com.rc.buyermarket.util.ValidationManager;
import com.rc.buyermarket.view.CanaroTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY_ENUM;

public class ActivityAddProperty extends BaseActivity {
    //Toolbar
    ImageView ivBack;
    CanaroTextView tvTitle;
    TextView txtPriceValue;
    // initialize Spinner
    private Spinner spPurchaseType, spPreapproved, spBuyPropertyType, spCountry, spState,
            spBuyBedrooms, spBuyBathroom, spBuyBasement, spBuyGarage, spBuyStyle, spBuyExterior;
    // initialize EditText and Button
    private EditText etFname, etLname, etEmail, etPhone, etCreditScore, etCity, etZipcode;
    private Button btnSubmit;
    String preApproveType = "", purchaseType = "", bedroomType = "", bathroomType = "", basementType = "", garageType = "",
            propertyType = "", styleType = "", exteriorType = "", countryName = "", countryId = "", stateName = "";
    SeekBar seekBarPrice;
    int priceRange = 0;
    private CommonSpinnerAdapter preApprovedAdapter, purchaseTypeAdapter, bedroomAdapter, bathroomAdapter, basementAdapter, garageAdapter,
            countryTypeAdapter, stateTypeAdapter, propertyTypeAdapter, styleAdapter, exteriorAdapter;
    private APIInterface apiInterface;
    //Background task
    GetCountryWithCityTask getCountryWithCityTask;
    GetPropertyTypeTask getPropertyTypeTask;
    GetExteriorTask getExteriorTask;
    GetStyleTask getStyleTask;
    GetAddPropertyTask getAddPropertyTask;

    AddProperty addPropertyEditData;
    PropertyEnum propertyEnum;


    @Override
    public String initActivityTag() {
        return ActivityAddProperty.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_add_property;
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
            String propertyType = intent.getStringExtra(INTENT_KEY_PROPERTY_ENUM);
            if (!AppUtil.isNullOrEmpty(propertyType)) {
                propertyEnum = PropertyEnum.valueOf(propertyType);
                Log.d(TAG, TAG + ">>>" + " propertyEnum: " + propertyEnum);
            }

            AddProperty addProperty = intent.getParcelableExtra(INTENT_KEY_PROPERTY);
            if (addProperty != null) {
                addPropertyEditData = addProperty;
                Log.d(TAG, TAG + ">>>" + " addProperty: " + APIResponse.getResponseString(addProperty));
            }
        }
    }

    @Override
    public void initActivityViews() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.tv_title);
        txtPriceValue = (TextView) findViewById(R.id.txt_price_value);
        etFname = (EditText) findViewById(R.id.et_fname);
        etLname = (EditText) findViewById(R.id.et_lname);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCreditScore = (EditText) findViewById(R.id.et_credit_scroe);
        etCity = (EditText) findViewById(R.id.et_city);
        etZipcode = (EditText) findViewById(R.id.et_zipcode);
        spPurchaseType = (Spinner) findViewById(R.id.sp_purchase_type);
        spPreapproved = (Spinner) findViewById(R.id.sp_buy_preapproved);
        seekBarPrice = (SeekBar) findViewById(R.id.seekBar_price);
        spBuyPropertyType = (Spinner) findViewById(R.id.sp_buy_property_type);
        spCountry = (Spinner) findViewById(R.id.sp_country);
        spState = (Spinner) findViewById(R.id.sp_buy_state);
        spBuyBedrooms = (Spinner) findViewById(R.id.sp_buy_bedrooms);
        spBuyBathroom = (Spinner) findViewById(R.id.sp_buy_bathroom);
        spBuyBasement = (Spinner) findViewById(R.id.sp_buy_basement);
        spBuyGarage = (Spinner) findViewById(R.id.sp_buy_garage);
        spBuyStyle = (Spinner) findViewById(R.id.sp_buy_style);
        spBuyExterior = (Spinner) findViewById(R.id.sp_buy_exterior);
        btnSubmit = (Button) findViewById(R.id.btn_property_submit);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.add_property));
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        setData();
        propertyTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PROPERTY_TYPE);
        spBuyPropertyType.setAdapter(propertyTypeAdapter);
        styleAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.STYLE);
        spBuyStyle.setAdapter(styleAdapter);
        exteriorAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.EXTERIOR);
        spBuyExterior.setAdapter(exteriorAdapter);
        countryTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.COUNTRY);
        spCountry.setAdapter(countryTypeAdapter);
        stateTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.STATE);
        spState.setAdapter(stateTypeAdapter);

        if (!NetworkManager.isInternetAvailable(getActivity())) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();

            propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(getActivity()));
            spBuyPropertyType.setSelection((addPropertyEditData != null) ? propertyTypeAdapter.getItemPosition(addPropertyEditData.getProperty_type()) : 0);

            styleAdapter.setData(DataUtil.getAllStyles(getActivity()));
            spBuyStyle.setSelection((addPropertyEditData != null) ? styleAdapter.getItemPosition(addPropertyEditData.getStyle()) : 0);

            exteriorAdapter.setData(DataUtil.getAllExteriors(getActivity()));
            spBuyExterior.setSelection((addPropertyEditData != null) ? exteriorAdapter.getItemPosition(addPropertyEditData.getExterior()) : 0);

            countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()));
            spCountry.setSelection((addPropertyEditData != null) ? countryTypeAdapter.getItemPosition(addPropertyEditData.getCountry()) : 0);
        } else {
            getPropertyTypeTask = new GetPropertyTypeTask(getActivity(), apiInterface);
            getPropertyTypeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getStyleTask = new GetStyleTask(getActivity(), apiInterface);
            getStyleTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getExteriorTask = new GetExteriorTask(getActivity(), apiInterface);
            getExteriorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getCountryWithCityTask = new GetCountryWithCityTask(getActivity(), apiInterface);
            getCountryWithCityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        switch (propertyEnum) {
            case PROPERTY_EDIT:
                tvTitle.setText(getString(R.string.property_details));
                setDataPropertyEdit();
                break;
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    doAddProperty();
                }
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
                priceRange = progress;
                Log.e("priceRange", priceRange + ">>");
                txtPriceValue.setText("$" + priceRange + " - " + "$1000");
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

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                purchaseType = item.getSp_title();
                Log.d(TAG, "purchaseType= " + purchaseType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPreapproved.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                preApproveType = item.getId();
                Log.d(TAG, "preApproveType= " + preApproveType);
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
                    spState.setSelection((addPropertyEditData != null) ? stateTypeAdapter.getItemPosition(addPropertyEditData.getState()) : 0);
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

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                bedroomType = item.getId();
                Log.d(TAG, "bedroomType= " + bedroomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBuyBathroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                bathroomType = item.getId();
                Log.d(TAG, "bathroomType= " + bathroomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBuyBasement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spBuyGarage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        if (getAddPropertyTask != null && getAddPropertyTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAddPropertyTask.cancel(true);
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

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /************************
     * setDataPropertyEdit function  *
     ************************/
    private void setDataPropertyEdit() {
        if (addPropertyEditData != null) {
            Log.e("addPropertyEditData", addPropertyEditData.toString() + "");

            etFname.setText(addPropertyEditData.getFirst_name());
            etLname.setText(addPropertyEditData.getLast_name());
            etEmail.setText(addPropertyEditData.getEmail());
            etPhone.setText(addPropertyEditData.getContact());
            etCreditScore.setText(addPropertyEditData.getCredit_score());
            etZipcode.setText(addPropertyEditData.getZipcode());
            etCity.setText(addPropertyEditData.getCity());
            Log.e("getPurchase_type", addPropertyEditData.getPurchase_type() + "");
            Log.e("getProperty_type", addPropertyEditData.getProperty_type() + "");
            Log.e("getState", addPropertyEditData.getState() + "");
            if (AppUtil.isNullOrEmpty(addPropertyEditData.getPrice_min())) {
                seekBarPrice.setProgress(0);
                txtPriceValue.setText("$0 - $1000");

            } else {
                seekBarPrice.setProgress(Integer.parseInt(addPropertyEditData.getPrice_min()));
                txtPriceValue.setText("$" + addPropertyEditData.getPrice_min() + " - " + "$1000");

            }


            spPurchaseType.setSelection(purchaseTypeAdapter.getItemPosition(addPropertyEditData.getPurchase_type()));
            spPreapproved.setSelection(preApprovedAdapter.getItemPosition(addPropertyEditData.getPrc_approved()));
            spBuyBathroom.setSelection(bathroomAdapter.getItemPosition(addPropertyEditData.getBathroom()));
            spBuyBedrooms.setSelection(bedroomAdapter.getItemPosition(addPropertyEditData.getBedroom()));
            spBuyBasement.setSelection(basementAdapter.getItemPosition(addPropertyEditData.getBasement()));
            spBuyGarage.setSelection(garageAdapter.getItemPosition(addPropertyEditData.getGarage()));
//            spBuyStyle.setSelection(styleAdapter.getItemPosition(addPropertyEditData.getStyle()));
//            spBuyExterior.setSelection(exteriorAdapter.getItemPosition(addPropertyEditData.getExterior()));

        }
    }

    /************************
     * validation check for empty  *
     ************************/
    private void doAddProperty() {
        //  bedroomType = (((SPModel) spBuyBedrooms.getSelectedItem()).getSp_title().equalsIgnoreCase(getString(R.string.txt_default_country)) ? "" : ((SPModel) spBuyBedrooms.getSelectedItem()).getSp_title());
        String fName = etFname.getText().toString();
        String lName = etLname.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhone.getText().toString();
        String city = etCity.getText().toString();
        String zipCode = etZipcode.getText().toString();

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
        if (!AppUtil.isNullOrEmpty(phoneNumber)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_valid_mobile_no), Toast.LENGTH_SHORT).show();
            return;
        }
        if (AppUtil.isNullOrEmpty(city)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_city), Toast.LENGTH_SHORT).show();
            return;
        }

        if (AppUtil.isNullOrEmpty(zipCode)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_zipcode), Toast.LENGTH_SHORT).show();
            return;
        }

//        if (bedroomType.equalsIgnoreCase(getString(R.string.txt_default_bedrooms)) ) {
//            Toast.makeText(getActivity(), "Please enter your bedrooms", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (bathroomType.equalsIgnoreCase(getString(R.string.txt_default_bathrooms)) ) {
//            Toast.makeText(getActivity(), "Please enter your bathrooms", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (basementType.equalsIgnoreCase(getString(R.string.txt_default_basement)) ) {
//            Toast.makeText(getActivity(), "Please enter your basement", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (garageType.equalsIgnoreCase(getString(R.string.txt_default_garage)) ) {
//            Toast.makeText(getActivity(), "Please enter your garage", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Log.e("propertyEnum", propertyEnum + "");
        getAddPropertyTask = new GetAddPropertyTask(getActivity(), apiInterface);
        getAddPropertyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /************************
     * setData in spinner  *
     ************************/
    private void setData() {
        purchaseTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PURCHASE_TYPE);
        spPurchaseType.setAdapter(purchaseTypeAdapter);
        purchaseTypeAdapter.setData(DataUtil.getAllPurchaseTypes());
        spPurchaseType.setSelection(0);

        preApprovedAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PRE_APPROVED);
        spPreapproved.setAdapter(preApprovedAdapter);
        preApprovedAdapter.setData(DataUtil.getAllPreApproved());
        spPreapproved.setSelection(0);

        bedroomAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BEDROOM);
        spBuyBedrooms.setAdapter(bedroomAdapter);
        bedroomAdapter.setData(DataUtil.getAllBedrooms());
        spBuyBedrooms.setSelection(0);

        bathroomAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BATHROOM);
        spBuyBathroom.setAdapter(bathroomAdapter);
        bathroomAdapter.setData(DataUtil.getAllBathrooms());
        spBuyBathroom.setSelection(0);

        basementAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BASEMANT);
        spBuyBasement.setAdapter(basementAdapter);
        basementAdapter.setData(DataUtil.getAllBasements());
        spBuyBasement.setSelection(0);

        garageAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.GARAGE);
        spBuyGarage.setAdapter(garageAdapter);
        garageAdapter.setData(DataUtil.getAllGarages());
        spBuyGarage.setSelection(0);
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
                        Log.d(TAG, "APIResponse(GetCountryWithCityTask()): onResponse-object = " + data.toString());
                        Log.d(TAG, "APIResponse(GetCountryWithCityTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_CITY_WITH_AREA_LIST, SessionCountryWithAreaList.getResponseString(new SessionCountryWithAreaList(data.getData())));

                            countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(mContext));
                            spCountry.setSelection((addPropertyEditData != null) ? countryTypeAdapter.getItemPosition(addPropertyEditData.getCountry()) : 0);
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
                    Log.d(TAG, "APIResponse(GetPropertyTypeTask): onResponse-server = " + result.toString());
                    APIResponse<List<PropertyType>> data = (APIResponse<List<PropertyType>>) result.body();
                    Log.e("PropertyType", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetPropertyTypeTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_PROPERTY_TYPE_LIST, SessionPropertyTypeList.getResponseString(new SessionPropertyTypeList(data.getData())));

                            propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(mContext));
                            //checked selection for both add/update property
                            spBuyPropertyType.setSelection((addPropertyEditData != null) ? propertyTypeAdapter.getItemPosition(addPropertyEditData.getProperty_type()) : 0);
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
                        Log.d(TAG, "APIResponse(GetStyleTask()): onResponse-object = " + data.toString());
                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_STYLE_LIST, SessionStyleList.getResponseString(new SessionStyleList(data.getData())));

                            styleAdapter.setData(DataUtil.getAllStyles(mContext));
                            spBuyStyle.setSelection((addPropertyEditData != null) ? styleAdapter.getItemPosition(addPropertyEditData.getStyle()) : 0);
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
                    Log.e("Exterior", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetExteriorTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_EXTERIOR_LIST, SessionExteriorList.getResponseString(new SessionExteriorList(data.getData())));

                            exteriorAdapter.setData(DataUtil.getAllExteriors(mContext));
                            spBuyExterior.setSelection((addPropertyEditData != null) ? exteriorAdapter.getItemPosition(addPropertyEditData.getExterior()) : 0);
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

    private class GetAddPropertyTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        APIInterface mApiInterface;

        public GetAddPropertyTask(Context context, APIInterface apiInterface) {
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
                String buyerId = AppPref.getPreferences(getActivity(), AllConstants.SESSION_BUYER_ID);
                Call<APIResponse<List<AddProperty>>> call = null;
                switch (propertyEnum) {
                    case PROPERTY_ADD:
                        ParamsAddProperty pAddProperty = new ParamsAddProperty("0", etFname.getText().toString(), etLname.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(),
                                purchaseType, preApproveType, etCreditScore.getText().toString(), propertyType, buyerId, countryName, etCity.getText().toString(), stateName, etZipcode.getText().toString(), bedroomType, bathroomType, basementType,
                                garageType, styleType, exteriorType, String.valueOf(priceRange), "1000");
                        call = apiInterface.doAddPropety(pAddProperty);
                        Log.d(TAG, "pAddProperty: onResponse-server = " + pAddProperty.toString());
                        Log.e("pAddProperty", pAddProperty.toString() + ">>");
                        break;
                    case PROPERTY_EDIT:
                        if (addPropertyEditData != null) {
                            ParamsAddProperty pUpdateProperty = new ParamsAddProperty(addPropertyEditData.getId(), etFname.getText().toString(), etLname.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(),
                                    purchaseType, preApproveType, etCreditScore.getText().toString(), propertyType, buyerId, countryName, etCity.getText().toString(), stateName, etZipcode.getText().toString(), bedroomType, bathroomType, basementType,
                                    garageType, styleType, exteriorType, String.valueOf(priceRange), "1000");
                            call = apiInterface.doAddPropety(pUpdateProperty);
                            Log.d(TAG, "pAddProperty: onResponse-server = " + pUpdateProperty.toString());
                            Log.e("pAddProperty", pUpdateProperty.toString() + ">>");
                        }
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
                    Log.d(TAG, "APIResponse(GetAddPropertyTask): onResponse-server = " + result.toString());
                    APIResponse<List<AddProperty>> data = (APIResponse<List<AddProperty>>) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetAddPropertyTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() == 1) {
                            Intent intentEdit = new Intent();
                            intentEdit.putExtra(INTENT_KEY_PROPERTY, data.getData().get(0));
                            setResult(RESULT_OK, intentEdit);
                            finish();
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
