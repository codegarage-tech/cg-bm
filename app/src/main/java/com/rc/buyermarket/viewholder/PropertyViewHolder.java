package com.rc.buyermarket.viewholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.buyermarket.R;
import com.rc.buyermarket.activity.ActivityAddProperty;
import com.rc.buyermarket.activity.MainActivity;
import com.rc.buyermarket.adapter.PropertyListAdapter;
import com.rc.buyermarket.enumeration.PropertyEnum;
import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.model.PropertyDelete;
import com.rc.buyermarket.network.NetworkManager;
import com.rc.buyermarket.retrofit.APIClient;
import com.rc.buyermarket.retrofit.APIInterface;
import com.rc.buyermarket.retrofit.APIResponse;
import com.rc.buyermarket.util.DataUtil;

import retrofit2.Call;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY;
import static com.rc.buyermarket.util.AllConstants.INTENT_KEY_PROPERTY_ENUM;
import static com.rc.buyermarket.util.AllConstants.INTENT_REQUEST_CODE_EDIT_PROPERTY;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PropertyViewHolder extends BaseViewHolder<AddProperty> {

    TextView tvName;
    TextView tvAddress;
    TextView tvAmount;
    TextView tvExterior;
    TextView tvPreApproved;
    TextView tvPropertyType;
    TextView tvPurchageType;
    TextView tvBasement;
    TextView tvCreditScores;
    TextView tvBedroom;
    TextView tvBathroom;
    TextView tvGarage;
    TextView tvStyle;
    LinearLayout llPropertyDelete;
    LinearLayout llPropertyEdit;
    Context mContext;
    private APIInterface apiInterface;
    PropertyEnum propertyEnum;
    public ProgressDialog mProgressDialog;
    String propertyId = "";
    //Background task
    GetDeletePropertyListTask getDeletePropertyListTask;

    public PropertyViewHolder(ViewGroup parent, Context context) {
        super(parent, R.layout.row_list_property);
        this.mContext = context;
        tvName = (TextView) $(R.id.row_txt_property_name);
        tvAddress = (TextView) $(R.id.row_txt_property_address);
        tvAmount = (TextView) $(R.id.row_txt_property_amount);
        tvExterior = (TextView) $(R.id.row_txt_property_exterior);
        tvPreApproved = (TextView) $(R.id.row_txt_property_preapproved);
        tvPropertyType = (TextView) $(R.id.row_txt_property_type);
        tvPurchageType = (TextView) $(R.id.row_txt_purchage_type);
        tvBasement = (TextView) $(R.id.row_txt_property_basement);
        tvCreditScores = (TextView) $(R.id.row_txt_property_credit_scores);
        tvBedroom = (TextView) $(R.id.row_txt_property_bedroom);
        tvBathroom = (TextView) $(R.id.row_txt_property_bathroom);
        tvGarage = (TextView) $(R.id.row_txt_property_garage);
        tvStyle = (TextView) $(R.id.row_txt_property_style);
        llPropertyDelete = (LinearLayout) $(R.id.lin_property_delete);
        llPropertyEdit = (LinearLayout) $(R.id.lin_property_edit);
    }

    @Override
    public void setData(final AddProperty data) {
        apiInterface = APIClient.getClient(mContext).create(APIInterface.class);

        tvName.setText(data.getFirst_name() + "  " + data.getLast_name());
        tvAmount.setText("$" + data.getPrice_min() + " - " + "$" + data.getPrice_max());
        tvAddress.setText(data.getZipcode() + " " + data.getCity() + ", " + data.getState() + ", " + data.getCountry());
        tvPreApproved.setText((DataUtil.getPreApproved(data.getPrc_approved()) != null) ? DataUtil.getPreApproved(data.getPrc_approved()).getSp_title() : "");
        tvPropertyType.setText((DataUtil.getPropertyType(mContext, data.getProperty_type()) != null) ? DataUtil.getPropertyType(mContext, data.getProperty_type()).getProperty_value() : "");
        tvPurchageType.setText((DataUtil.getPurchaseType(mContext, data.getPurchase_type()) != null) ? DataUtil.getPurchaseType(mContext, data.getPurchase_type()).getPurchase_value() : "");
        tvExterior.setText((DataUtil.getExterior(mContext, data.getExterior()) != null) ? DataUtil.getExterior(mContext, data.getExterior()).getExterior_value() : "");
        tvBasement.setText((DataUtil.getBasement(data.getBasement()) != null) ? DataUtil.getBasement(data.getBasement()).getSp_title() : "");
        tvCreditScores.setText(data.getCredit_score());
        tvBedroom.setText((DataUtil.getBedroom(mContext, data.getBedroom()) != null) ? DataUtil.getBedroom(mContext, data.getBedroom()).getBedroom_value() : "");
        tvBathroom.setText((DataUtil.getBathroom(mContext, data.getBathroom()) != null) ? DataUtil.getBathroom(mContext, data.getBathroom()).getBathroom_value() : "");
        tvGarage.setText((DataUtil.getGarage(data.getGarage()) != null) ? DataUtil.getGarage(data.getGarage()).getSp_title() : "");
        tvStyle.setText((DataUtil.getStyle(mContext, data.getStyle()) != null) ? DataUtil.getStyle(mContext, data.getStyle()).getStyle_vaue() : "");

        llPropertyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isInternetAvailable(v.getContext())) {
                    Toast.makeText(v.getContext(), v.getContext().getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getDeletePropertyListTask = new GetDeletePropertyListTask(v.getContext(), apiInterface);
                    getDeletePropertyListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    propertyId = data.getId();
                }
            }
        });

        llPropertyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertyEnum = PropertyEnum.PROPERTY_EDIT;

//                //Why this field is needed to save into the session?????
//                AppPref.saveObject(v.getContext(), AllConstants.SESSION_BUYER_PROPERTY, data);
//                Log.e("llPropertyEdit", data.toString() + "");
                Intent iAddProperty = new Intent(v.getContext(), ActivityAddProperty.class);
                iAddProperty.putExtra(INTENT_KEY_PROPERTY_ENUM, propertyEnum.name());
                iAddProperty.putExtra(INTENT_KEY_PROPERTY, data);
                ((MainActivity) getContext()).startActivityForResult(iAddProperty, INTENT_REQUEST_CODE_EDIT_PROPERTY);
            }
        });
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
            ((MainActivity) mContext).showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Log.e("<<propertyId", propertyId + ">>>");
                Call<APIResponse<PropertyDelete>> call = apiInterface.doGetDeleteListProperty(propertyId);
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
                ((MainActivity) mContext).dismissProgressDialog();

                if (result != null && result.isSuccessful()) {
                    // Log.d(TAG, "APIResponse(GetStyleTask): onResponse-server = " + result.toString());
                    Log.d(TAG, "APIResponse(GetDeletePropertyListTask): onResponse-server = " + result.toString());
                    APIResponse<PropertyDelete> data = (APIResponse<PropertyDelete>) result.body();
                    Log.e("PropertyDelete", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Log.d(TAG, "APIResponse(GetDeletePropertyListTask()): onResponse-object = " + data.toString());

                        ((PropertyListAdapter) getOwnerAdapter()).remove(getAdapterPosition());
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
