package com.rc.buyermarket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.buyermarket.R;
import com.rc.buyermarket.adapter.CommonSpinnerAdapter;
import com.rc.buyermarket.base.BaseActivity;
import com.rc.buyermarket.enumeration.UserType;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.ParamsSellerSearchBuyer;
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
import com.rc.buyermarket.view.CanaroTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_SEARCH_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_USER_TYPE;

public class ActivitySellerSearchBuyer extends BaseActivity {

    //Toolbar
    ImageView ivBack;
    CanaroTextView tvTitle;
    TextView txtPriceValue;

    Button btnSearchSubmit;
    String defaultSelectedCountry = "United States", defaultSelectedCity = "detroit", defaultSelectedBasement = "", defaultSelectedGarage = "", purchaseType = "", bedroomType = "", bathroomType = "", propertyType = "", styleType = "", exteriorType = "", stateName = "";
    List<SPModel> purchaseTypeList;
    List<SPModel> bedroomTypeList;
    List<SPModel> bathroomTypeList;
    SeekBar seekBarPrice;

//    List<PropertyType> propertyTypesList;
//    List<Styles> stylesList;
//    List<Exterior> exteriorList;

    // initialize Spinner
    private Spinner spPurchaseType, spBuyPropertyType, spState, spCountry,
            spBuyBedrooms, spBuyBathroom, spBuyStyle, spBuyExterior;
    private Button btnUserLogin;
    private UserType userType;
    // initialize SpinnerAdapter
    private CommonSpinnerAdapter purchaseAdapter, bedroomAdapter, bathroomAdapter, countryTypeAdapter, stateTypeAdapter, propertyTypeAdapter, styleAdapter, exteriorAdapter;
    private APIInterface apiInterface;

    //Background task
    GetCountryWithCityTask getCountryWithCityTask;
    GetPropertyTypeTask getPropertyTypeTask;
    GetExteriorTask getExteriorTask;
    GetStyleTask getStyleTask;
    int priceRange = 0;

    @Override
    public String initActivityTag() {
        return ActivitySellerSearchBuyer.class.getSimpleName();
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_seller_search_buyer;
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

        }
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
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvTitle.setText(getString(R.string.search_buyer));
        apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        initializeAddList();
        setData();

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

        if (!NetworkManager.isInternetAvailable(getActivity())) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            countryTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()));
            spCountry.setSelection(0);

            stateTypeAdapter.setData(DataUtil.getAllCountryWithStates(getActivity()).get(0).getStates());
            spState.setSelection(0);

            propertyTypeAdapter.setData(DataUtil.getAllPropertyTypes(getActivity()));
            spBuyPropertyType.setSelection(0);

            styleAdapter.setData(DataUtil.getAllStyles(getActivity()));
            spBuyStyle.setSelection(0);

            exteriorAdapter.setData(DataUtil.getAllExteriors(getActivity()));
            spBuyExterior.setSelection(0);
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
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        btnSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationUserField();

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivityBackPress();
            }
        });

//        seekBarPrice.setMin(10000);
//        seekBarPrice.setMax(10000000);
        seekBarPrice.setProgress(10000);
        txtPriceValue.setText("$10000 - $10000000");
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() < 10000) {
                    seekBar.setProgress(10000);
                }

                priceRange = seekBar.getProgress();
                Log.e("priceRange", priceRange + ">>");
                txtPriceValue.setText("$" + priceRange + " - " + "$10000000");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        spBuyPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PropertyType item = (PropertyType) parent.getItemAtPosition(position);
                propertyType = item.getProperty_value();
                if (propertyType.equalsIgnoreCase("Select property type")) {
                    propertyType = "";
                } else {
                    propertyType = item.getProperty_value();
                }

                Log.d(TAG, "propertyType= " + propertyType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPurchaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                purchaseType = item.getSp_title();
                if (purchaseType.equalsIgnoreCase("Select purchase type")) {
                    purchaseType = "";
                } else {
                    purchaseType = item.getSp_title();
                }
                Log.d(TAG, "purchaseType= " + purchaseType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBuyBedrooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SPModel item = (SPModel) parent.getItemAtPosition(position);
                bedroomType = AppUtil.SplitDateList(item.getSp_title());
                if (bedroomType.equalsIgnoreCase("Select bedroom")) {
                    bedroomType = "";
                } else {
                    bedroomType = AppUtil.SplitDateList(item.getSp_title());
                }

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
                bathroomType = AppUtil.SplitDateList(item.getSp_title());
                if (bathroomType.equalsIgnoreCase("Select bathroom")) {
                    bathroomType = "";
                } else {
                    bathroomType = AppUtil.SplitDateList(item.getSp_title());
                }
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
                exteriorType = item.getExterior_value();
                if (exteriorType.equalsIgnoreCase("Select exterior")) {
                    exteriorType = "";
                } else {
                    exteriorType = item.getExterior_value();
                }
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
                styleType = item.getStyle_vaue();
                if (styleType.equalsIgnoreCase("Select style")) {
                    styleType = "";
                } else {
                    styleType = item.getStyle_vaue();
                }

                Log.d(TAG, "styleType= " + styleType);
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
//        dismissProgressDialog();
//
//        if (buyerLoginTask != null && buyerLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
//            buyerLoginTask.cancel(true);
//        }

    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /************************
     * validation check for empty  *
     ************************/
    private void validationUserField() {
//        if (bedroomType.equalsIgnoreCase(getString(R.string.txt_default_bedrooms))) {
//            Toast.makeText(getActivity(), "Please enter your bedrooms", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (bathroomType.equalsIgnoreCase(getString(R.string.txt_default_bathrooms))) {
//            Toast.makeText(getActivity(), "Please enter your bathrooms", Toast.LENGTH_SHORT).show();
//            return;
//        }

        ParamsSellerSearchBuyer pSearchProperty = new ParamsSellerSearchBuyer(purchaseType, propertyType, defaultSelectedCountry, defaultSelectedCity, stateName, bedroomType, bathroomType, defaultSelectedBasement, defaultSelectedGarage, styleType, exteriorType);

        Intent iSearchProperty = new Intent(getActivity(), ActivitySearchBuyerList.class);
        iSearchProperty.putExtra(INTENT_KEY_SEARCH_PROPERTY, pSearchProperty);
        startActivity(iSearchProperty);
    }

    private void setData() {
        purchaseAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.PURCHASE_TYPE);
        spPurchaseType.setAdapter(purchaseAdapter);
        purchaseAdapter.setData(purchaseTypeList);
        spPurchaseType.setSelection(0);

        bedroomAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.BEDROOM);
        spBuyBedrooms.setAdapter(bedroomAdapter);
        bedroomAdapter.setData(bedroomTypeList);
        spBuyBedrooms.setSelection(0);

        bathroomAdapter = new CommonSpinnerAdapter(getActivity(), CommonSpinnerAdapter.ADAPTER_TYPE.BATHROOM);
        spBuyBathroom.setAdapter(bathroomAdapter);
        bathroomAdapter.setData(bathroomTypeList);
        spBuyBathroom.setSelection(0);

    }

    private void initializeAddList() {
        purchaseTypeList = new ArrayList<>();
        purchaseTypeList.clear();
        bedroomTypeList = new ArrayList<>();
        bedroomTypeList.clear();
        bathroomTypeList = new ArrayList<>();
        bathroomTypeList.clear();
//        exteriorList = new ArrayList<>();
//        exteriorList.clear();
//        stylesList = new ArrayList<>();
//        stylesList.clear();
//        propertyTypesList = new ArrayList<>();
//        propertyTypesList.clear();
        //purchase type
//        purchaseTypeList.add(new SPModel("", "Select purchase type"));
        purchaseTypeList.add(new SPModel("", "Cash"));
        purchaseTypeList.add(new SPModel("", "Mortgage"));
        purchaseTypeList.add(new SPModel("", "Land Contract"));

        // bedroom type
        bedroomTypeList.add(new SPModel("", "Select bedroom"));
        bedroomTypeList.add(new SPModel("2", "2+"));
        bedroomTypeList.add(new SPModel("3", "3+"));
        bedroomTypeList.add(new SPModel("4", "4+"));
        bedroomTypeList.add(new SPModel("5", "5+"));

        // bathroom type
        bathroomTypeList.add(new SPModel("", "Select bathroom"));
        bathroomTypeList.add(new SPModel("2", "2+"));
        bathroomTypeList.add(new SPModel("3", "3+"));
        bathroomTypeList.add(new SPModel("4", "4+"));
        bathroomTypeList.add(new SPModel("5", "5+"));


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

//    private List<Exterior> getExteriorList(List<Exterior> data) {
//        List<Exterior> exList = new ArrayList<Exterior>();
//        Exterior exterior = new Exterior("999999", "Select Exterior", "Select exterior");
//        exList.add(exterior);
//        for (int i = 0; i < data.size(); i++) {
//            exterior = new Exterior(data.get(i).getId(), data.get(i).getExterior_key(), data.get(i).getExterior_value());
//            exList.add(exterior);
//        }
//        return exList;
//    }
//
//    private List<Styles> getStylesList(List<Styles> data) {
//        List<Styles> stylesList = new ArrayList<Styles>();
//        Styles exterior = new Styles("9999999", "Select Style", "Select style");
//        stylesList.add(exterior);
//        for (int i = 0; i < data.size(); i++) {
//            exterior = new Styles(data.get(i).getId(), data.get(i).getStyle_key(), data.get(i).getStyle_vaue());
//            stylesList.add(exterior);
//        }
//        return stylesList;
//    }
//
//
//    private List<PropertyType> getPropertyList(List<PropertyType> data) {
//        List<PropertyType> propertyList = new ArrayList<PropertyType>();
//        PropertyType propertyType = new PropertyType("99999999", "Select Property Type", "Select property type");
//        propertyList.add(propertyType);
//        for (int i = 0; i < data.size(); i++) {
//            propertyType = new PropertyType(data.get(i).getId(), data.get(i).getProperty_key(), data.get(i).getProperty_value());
//            propertyList.add(propertyType);
//        }
//        return propertyList;
//    }
}
