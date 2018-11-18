package com.rc.buyermarket.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.buyermarket.model.AddProperty;
import com.rc.buyermarket.viewholder.PropertyViewHolder;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class PropertyListAdapter extends RecyclerArrayAdapter<AddProperty> {

    private static final int VIEW_TYPE_REGULAR = 1;
    Context mContext;
    public PropertyListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new PropertyViewHolder(parent, mContext);
            default:
                throw new InvalidParameterException();
        }
    }

    public int getItemPosition(AddProperty addProperty) {
        if (addProperty != null) {
            List<AddProperty> properties = getAllData();
            for (int i = 0; i < properties.size(); i++) {
                if (addProperty.getId().equalsIgnoreCase(properties.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }
}