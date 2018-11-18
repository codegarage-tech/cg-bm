package com.rc.buyermarket.viewholder;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.buyermarket.R;
import com.rc.buyermarket.model.SellerSearchProperty;
import com.rc.buyermarket.util.DataUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SearchPropertyViewHolder extends BaseViewHolder<SellerSearchProperty> {

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

    public SearchPropertyViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_search_property_list_view);
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
    public void setData(final SellerSearchProperty data) {
        tvName.setText(data.getFirst_name() + "  " + data.getLast_name());
        tvAmount.setText("$" + data.getPrice_min() + " - " + "$" + data.getPrice_max());
        tvAddress.setText(data.getZipcode() + " " + data.getCity() + ", " + data.getState() + ", " + data.getCountry());
        tvPreApproved.setText((DataUtil.getPreApproved(data.getPrc_approved()) != null) ? DataUtil.getPreApproved(data.getPrc_approved()).getSp_title() : "");
        tvPropertyType.setText((DataUtil.getPropertyType(getContext(), data.getProperty_type()) != null) ? DataUtil.getPropertyType(getContext(), data.getProperty_type()).getProperty_value() : "");
        tvPurchageType.setText((DataUtil.getPurchaseType(data.getPurchase_type()) != null) ? DataUtil.getPurchaseType(data.getPurchase_type()).getSp_title() : "");
        tvExterior.setText((DataUtil.getExterior(getContext(), data.getExterior()) != null) ? DataUtil.getExterior(getContext(), data.getExterior()).getExterior_value() : "");
        tvBasement.setText((DataUtil.getBasement(data.getBasement()) != null) ? DataUtil.getBasement(data.getBasement()).getSp_title() : "");
        tvCreditScores.setText(data.getCredit_score());
        tvBedroom.setText((DataUtil.getBedroom(data.getBedroom()) != null) ? DataUtil.getBedroom(data.getBedroom()).getSp_title() : "");
        tvBathroom.setText((DataUtil.getBathroom(data.getBathroom()) != null) ? DataUtil.getBathroom(data.getBathroom()).getSp_title() : "");
        tvGarage.setText((DataUtil.getGarage(data.getGarage()) != null) ? DataUtil.getGarage(data.getGarage()).getSp_title() : "");
        tvStyle.setText((DataUtil.getStyle(getContext(), data.getStyle()) != null) ? DataUtil.getStyle(getContext(), data.getStyle()).getStyle_vaue() : "");
    }
}
