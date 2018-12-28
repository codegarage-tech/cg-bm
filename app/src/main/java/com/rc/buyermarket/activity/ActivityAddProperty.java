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
import com.rc.buyermarket.model.Bathroom;
import com.rc.buyermarket.model.Bedroom;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.ParamsAddProperty;
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
import com.rc.buyermarket.util.AppUtil;
import com.rc.buyermarket.util.DataUtil;
import com.rc.buyermarket.view.CanaroTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY_ENUM;

public class ActivityAddProperty extends BaseActivity {
    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private TextView txtPriceValue;
    private Spinner spPurchaseType, spPreapproved, spBuyPropertyType, spCountry, spState,
            spBuyBedrooms, spBuyBathroom, spBuyBasement, spBuyGarage, spBuyStyle, spBuyExterior;
    private EditText etFname, etLname, etEmail, etPhone, etCreditScore, etCity, etZipcode;
    private Button btnSubmit;
    private String preApproveType = "", purchaseType = "", bedroomType = "", bathroomType = "", basementType = "", garageType = "",
            propertyType = "", styleType = "", exteriorType = "", countryName = "", stateName = "";
    private SeekBar seekBarPrice;
    private int priceMinimum = 10000;
    private int priceMaximum = 10000000;
    private CommonSpinnerAdapter preApprovedAdapter, purchaseTypeAdapter, bedroomAdapter, bathroomAdapter, basementAdapter, garageAdapter,
            countryTypeAdapter, stateTypeAdapter, propertyTypeAdapter, styleAdapter, exteriorAdapter;
    private APIInterface apiInterface;
    //Background task
    private GetCountryWithCityTask getCountryWithCityTask;
    private GetPropertyTypeTask getPropertyTypeTask;
    private GetExteriorTask getExteriorTask;
    private GetStyleTask getStyleTask;
    private GetAddPropertyTask getAddPropertyTask;
    private GetBathroomTask getBathroomTask;
    private GetBedroomTask getBedroomTask;
    private GetPurchaseTypeTask getPurchaseTypeTask;

    private AddProperty addPropertyEditData;
    private PropertyEnum propertyEnum;

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
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        if (propertyEnum == PropertyEnum.PROPERTY_ADD) {
            tvTitle.setText(getString(R.string.add_property));
        } else {
            tvTitle.setText(getString(R.string.property_details));
        }

        purchaseTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PURCHASE_TYPE);
        spPurchaseType.setAdapter(purchaseTypeAdapter);

        preApprovedAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PRE_APPROVED);
        spPreapproved.setAdapter(preApprovedAdapter);
        preApprovedAdapter.setData(DataUtil.getAllPreApproved());

        propertyTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.PROPERTY_TYPE);
        spBuyPropertyType.setAdapter(propertyTypeAdapter);

        countryTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.COUNTRY);
        spCountry.setAdapter(countryTypeAdapter);
        spCountry.setEnabled(false);

        stateTypeAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.STATE);
        spState.setAdapter(stateTypeAdapter);

        bedroomAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BEDROOM);
        spBuyBedrooms.setAdapter(bedroomAdapter);

        bathroomAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BATHROOM);
        spBuyBathroom.setAdapter(bathroomAdapter);

        basementAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.BASEMANT);
        spBuyBasement.setAdapter(basementAdapter);
        basementAdapter.setData(DataUtil.getAllBasements());

        garageAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.GARAGE);
        spBuyGarage.setAdapter(garageAdapter);
        garageAdapter.setData(DataUtil.getAllGarages());

        styleAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.STYLE);
        spBuyStyle.setAdapter(styleAdapter);

        exteriorAdapter = new CommonSpinnerAdapter(ActivityAddProperty.this, CommonSpinnerAdapter.ADAPTER_TYPE.EXTERIOR);
        spBuyExterior.setAdapter(exteriorAdapter);

        if (NetworkManager.isInternetAvailable(getActivity())) {
            //Load spinner data
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

            //Load edit property data
            if (addPropertyEditData != null) {
                etFname.setText(addPropertyEditData.getFirst_name());
                etLname.setText(addPropertyEditData.getLast_name());
                etEmail.setText(addPropertyEditData.getEmail());
                etPhone.setText(addPropertyEditData.getContact());
                if (AppUtil.isNullOrEmpty(addPropertyEditData.getPrice_min())) {
                    seekBarPrice.setProgress(priceMinimum);
                    txtPriceValue.setText("$" + priceMinimum + " - " + "$" + priceMaximum);
                } else {
                    seekBarPrice.setProgress(Integer.parseInt(addPropertyEditData.getPrice_min()));
                    txtPriceValue.setText("$" + addPropertyEditData.getPrice_min() + " - " + "$" + addPropertyEditData.getPrice_max());
                }
//                spPurchaseType.setSelection(purchaseTypeAdapter.getItemPosition(addPropertyEditData.getPurchase_type()));
                spPreapproved.setSelection(preApprovedAdapter.getItemPosition(addPropertyEditData.getPrc_approved()));
                etCreditScore.setText(addPropertyEditData.getCredit_score());
//                spBuyPropertyType.setSelection(propertyTypeAdapter.getItemPosition(addPropertyEditData.getProperty_type()));
//                spCountry.setSelection(countryTypeAdapter.getItemPosition(addPropertyEditData.getCountry()));
//                spState.setSelection(stateTypeAdapter.getItemPosition(addPropertyEditData.getState()));
                etCity.setText(addPropertyEditData.getCity());
                etZipcode.setText(addPropertyEditData.getZipcode());
//                spBuyBedrooms.setSelection(bedroomAdapter.getItemPosition(addPropertyEditData.getBedroom()));
//                spBuyBathroom.setSelection(bathroomAdapter.getItemPosition(addPropertyEditData.getBathroom()));
                spBuyBasement.setSelection(basementAdapter.getItemPosition(addPropertyEditData.getBasement()));
                spBuyGarage.setSelection(garageAdapter.getItemPosition(addPropertyEditData.getGarage()));
//                spBuyStyle.setSelection(styleAdapter.getItemPosition(addPropertyEditData.getStyle()));
//                spBuyExterior.setSelection(exteriorAdapter.getItemPosition(addPropertyEditData.getExterior()));
            }
        } else {
            loadOfflineData();
            setPropertyData();
        }
    }

    private void setPropertyData() {
        if (addPropertyEditData != null) {
            etFname.setText(addPropertyEditData.getFirst_name());
            etLname.setText(addPropertyEditData.getLast_name());
            etEmail.setText(addPropertyEditData.getEmail());
            etPhone.setText(addPropertyEditData.getContact());
            if (AppUtil.isNullOrEmpty(addPropertyEditData.getPrice_min())) {
                seekBarPrice.setProgress(priceMinimum);
                txtPriceValue.setText("$" + priceMinimum + " - " + "$" + priceMaximum);
            } else {
                seekBarPrice.setProgress(Integer.parseInt(addPropertyEditData.getPrice_min()));
                txtPriceValue.setText("$" + addPropertyEditData.getPrice_min() + " - " + "$" + addPropertyEditData.getPrice_max());
            }
            spPurchaseType.setSelection(purchaseTypeAdapter.getItemPosition(addPropertyEditData.getPurchase_type()));
            spPreapproved.setSelection(preApprovedAdapter.getItemPosition(addPropertyEditData.getPrc_approved()));
            etCreditScore.setText(addPropertyEditData.getCredit_score());
            spBuyPropertyType.setSelection(propertyTypeAdapter.getItemPosition(addPropertyEditData.getProperty_type()));
            spCountry.setSelection(countryTypeAdapter.getItemPosition(addPropertyEditData.getCountry()));
            spState.setSelection(stateTypeAdapter.getItemPosition(addPropertyEditData.getState()));
            etCity.setText(addPropertyEditData.getCity());
            etZipcode.setText(addPropertyEditData.getZipcode());
            spBuyBedrooms.setSelection(bedroomAdapter.getItemPosition(addPropertyEditData.getBedroom()));
            spBuyBathroom.setSelection(bathroomAdapter.getItemPosition(addPropertyEditData.getBathroom()));
            spBuyBasement.setSelection(basementAdapter.getItemPosition(addPropertyEditData.getBasement()));
            spBuyGarage.setSelection(garageAdapter.getItemPosition(addPropertyEditData.getGarage()));
            spBuyStyle.setSelection(styleAdapter.getItemPosition(addPropertyEditData.getStyle()));
            spBuyExterior.setSelection(exteriorAdapter.getItemPosition(addPropertyEditData.getExterior()));
        }
    }

    private void loadOfflineData() {
        purchaseTypeAdapter.setData(DataUtil.getAllPurchaseTypes(getActivity()));
//        preApprovedAdapter.setData(DataUtil.getAllPreApproved());
        propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(getActivity()));
        countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()));
        stateTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()).get(0).getStates());
        bedroomAdapter.setData(DataUtil.getAllBedrooms(getActivity()));
        bathroomAdapter.setData(DataUtil.getAllBathrooms(getActivity()));
//        basementAdapter.setData(DataUtil.getAllBasements());
//        garageAdapter.setData(DataUtil.getAllGarages());
        styleAdapter.setData(DataUtil.getAllStyles(getActivity()));
        exteriorAdapter.setData(DataUtil.getAllExteriors(getActivity()));
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isInternetAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                String fName = etFname.getText().toString();
                String lName = etLname.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNumber = etPhone.getText().toString();
//                String creditScore = etCreditScore.getText().toString();
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
                if (AppUtil.isNullOrEmpty(phoneNumber)) {
                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_mobile_no), Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (AppUtil.isNullOrEmpty(creditScore)) {
//                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_credit_score), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (AppUtil.isNullOrEmpty(stateName)) {
                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_state), Toast.LENGTH_SHORT).show();
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

                getAddPropertyTask = new GetAddPropertyTask(getActivity(), apiInterface);
                getAddPropertyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

                priceMinimum = seekBar.getProgress();
                Log.e("priceMinimum", priceMinimum + ">>");
                txtPriceValue.setText("$" + priceMinimum + " - " + "$" + priceMaximum);
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
                if (!item.getName().toLowerCase().equalsIgnoreCase(getString(R.string.txt_please_select).toLowerCase())) {
                    stateName = item.getName();
                } else {
                    stateName = "";
                }
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
                        Log.d(TAG, "APIResponse(GetCountryWithCityTask()): onResponse-object = " + data.toString());
                        Log.d(TAG, "APIResponse(GetCountryWithCityTask()): onResponse-object = " + data.toString());

                        if (data.getData() != null && data.getData().size() > 0) {
                            //Save data into the session
                            AppPref.savePreferences(getActivity(), AllConstants.SESSION_CITY_WITH_AREA_LIST, SessionCountryWithAreaList.getResponseString(new SessionCountryWithAreaList(data.getData())));

                            countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(mContext));
                            spCountry.setSelection((addPropertyEditData != null) ? countryTypeAdapter.getItemPosition(addPropertyEditData.getCountry()) : 0);

                            stateTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()).get(0).getStates());
                            spState.setSelection((addPropertyEditData != null) ? stateTypeAdapter.getItemPosition(addPropertyEditData.getState()) : 0);
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
                            spBuyBathroom.setSelection((addPropertyEditData != null) ? bathroomAdapter.getItemPosition(addPropertyEditData.getBathroom()) : 0);
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
                            spBuyBedrooms.setSelection((addPropertyEditData != null) ? bedroomAdapter.getItemPosition(addPropertyEditData.getBedroom()) : 0);
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

                            purchaseTypeAdapter.setData(DataUtil.getAllPurchaseTypes(mContext));
                            spPurchaseType.setSelection((addPropertyEditData != null) ? purchaseTypeAdapter.getItemPosition(addPropertyEditData.getPurchase_type()) : 0);
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
                                garageType, styleType, exteriorType, priceMinimum + "", priceMaximum + "");
                        call = apiInterface.doAddPropety(pAddProperty);
                        Log.d(TAG, "pAddProperty: onResponse-server = " + pAddProperty.toString());
                        Log.d(TAG,"pAddProperty: "+ pAddProperty.toString());
                        break;
                    case PROPERTY_EDIT:
                        if (addPropertyEditData != null) {
                            ParamsAddProperty pUpdateProperty = new ParamsAddProperty(addPropertyEditData.getId(), etFname.getText().toString(), etLname.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(),
                                    purchaseType, preApproveType, etCreditScore.getText().toString(), propertyType, buyerId, countryName, etCity.getText().toString(), stateName, etZipcode.getText().toString(), bedroomType, bathroomType, basementType,
                                    garageType, styleType, exteriorType, priceMinimum + "", priceMaximum + "");
                            call = apiInterface.doAddPropety(pUpdateProperty);
                            Log.d(TAG, "pAddProperty: onResponse-server = " + pUpdateProperty.toString());
                            Log.d(TAG,"pAddProperty: "+ pUpdateProperty.toString());
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