package com.rc.buyermarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.buyermarket.R;
import com.rc.buyermarket.model.Bathroom;
import com.rc.buyermarket.model.Bedroom;
import com.rc.buyermarket.model.Country;
import com.rc.buyermarket.model.Exterior;
import com.rc.buyermarket.model.PropertyType;
import com.rc.buyermarket.model.PurchaseType;
import com.rc.buyermarket.model.SPModel;
import com.rc.buyermarket.model.States;
import com.rc.buyermarket.model.Styles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Hozrot Belal
 * Email: belal.cse.brur@gmail.com
 */
public class CommonSpinnerAdapter<T> extends BaseAdapter {

    private Activity mActivity;
    private List<T> mData;
    private static LayoutInflater inflater = null;
    private ADAPTER_TYPE mAdapterType;

    public enum ADAPTER_TYPE {PRE_APPROVED, PURCHASE_TYPE, PRICE_RANGES, BEDROOM, BATHROOM, BASEMANT, GARAGE, PROPERTY_TYPE, COUNTRY, STATE, EXTERIOR, STYLE, FEE_CATEGORY}

    public CommonSpinnerAdapter(Activity activity, ADAPTER_TYPE adapterType) {
        mActivity = activity;
        mAdapterType = adapterType;
        mData = new ArrayList<T>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < mData.size(); i++) {

            if (mAdapterType == ADAPTER_TYPE.PRE_APPROVED) {
                SPModel spModel = (SPModel) mData.get(i);
                if (spModel.getId().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.PURCHASE_TYPE) {
                PurchaseType spModel = (PurchaseType) mData.get(i);
                if (spModel.getPurchase_key().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.PRICE_RANGES) {
                SPModel spModel = (SPModel) mData.get(i);
                if (spModel.getSp_title().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.BEDROOM) {
                Bedroom spModel = (Bedroom) mData.get(i);
                if (spModel.getBedroom_key().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.BATHROOM) {
                Bathroom spModel = (Bathroom) mData.get(i);
                if (spModel.getBathroom_key().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.BASEMANT) {
                SPModel spModel = (SPModel) mData.get(i);
                if (spModel.getId().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.GARAGE) {
                SPModel spModel = (SPModel) mData.get(i);
                if (spModel.getId().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.PROPERTY_TYPE) {
                PropertyType spModel = (PropertyType) mData.get(i);
                if (spModel.getProperty_key().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.EXTERIOR) {
                Exterior spModel = (Exterior) mData.get(i);
                if (spModel.getExterior_key().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.STYLE) {
                Styles spStyles = (Styles) mData.get(i);
                if (spStyles.getStyle_key().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.COUNTRY) {
                Country spModel = (Country) mData.get(i);
                if (spModel.getCountry_name().equalsIgnoreCase(name)) {
                    return i;
                }
            } else if (mAdapterType == ADAPTER_TYPE.STATE) {
                States spModel = (States) mData.get(i);
                Log.e("States", spModel.toString() + "");
                Log.e("nameStates", name + "");
                if (spModel.getName().equalsIgnoreCase(name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.spinner_row_user, null);

        TextView names = (TextView) vi.findViewById(R.id.tv_item_name);

        if (mAdapterType == ADAPTER_TYPE.PRE_APPROVED) {
            SPModel preApprovedType = (SPModel) getItem(position);
            names.setText(preApprovedType.getSp_title());

        } else if (mAdapterType == ADAPTER_TYPE.PURCHASE_TYPE) {
            PurchaseType purchageType = (PurchaseType) getItem(position);
            names.setText(purchageType.getPurchase_value());

        } else if (mAdapterType == ADAPTER_TYPE.PRICE_RANGES) {
            SPModel priceRangesType = (SPModel) getItem(position);
            names.setText(priceRangesType.getSp_title());

        } else if (mAdapterType == ADAPTER_TYPE.BEDROOM) {
            Bedroom bedroomType = (Bedroom) getItem(position);
            names.setText(bedroomType.getBedroom_value());

        } else if (mAdapterType == ADAPTER_TYPE.BATHROOM) {
            Bathroom bathroomType = (Bathroom) getItem(position);
            names.setText(bathroomType.getBathroom_value());

        } else if (mAdapterType == ADAPTER_TYPE.BASEMANT) {
            SPModel basementType = (SPModel) getItem(position);
            names.setText(basementType.getSp_title());

        } else if (mAdapterType == ADAPTER_TYPE.GARAGE) {
            SPModel garageType = (SPModel) getItem(position);
            names.setText(garageType.getSp_title());

        } else if (mAdapterType == ADAPTER_TYPE.PROPERTY_TYPE) {
            PropertyType propertyType = (PropertyType) getItem(position);
            names.setText(propertyType.getProperty_value());

        } else if (mAdapterType == ADAPTER_TYPE.EXTERIOR) {
            Exterior exterior = (Exterior) getItem(position);
            names.setText(exterior.getExterior_value());

        } else if (mAdapterType == ADAPTER_TYPE.STYLE) {
            Styles styles = (Styles) getItem(position);
            names.setText(styles.getStyle_vaue());

        } else if (mAdapterType == ADAPTER_TYPE.COUNTRY) {
            Country district = (Country) getItem(position);
            names.setText(district.getCountry_name());

        } else if (mAdapterType == ADAPTER_TYPE.STATE) {
            States states = (States) getItem(position);
            names.setText(states.getName());

        }
        return vi;
    }
}