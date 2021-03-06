package com.rc.buyermarket.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.buyermarket.model.SellerSearchBuyer;
import com.rc.buyermarket.viewholder.SearchBuyerViewHolder;

import java.security.InvalidParameterException;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class SearchPropertyListAdapter extends RecyclerArrayAdapter<SellerSearchBuyer> {

    private static final int VIEW_TYPE_REGULAR = 1;
    Context context;
    public SearchPropertyListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new SearchBuyerViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }
}