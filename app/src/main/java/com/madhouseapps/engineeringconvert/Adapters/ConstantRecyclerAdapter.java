package com.madhouseapps.engineeringconvert.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madhouseapps.engineeringconvert.ConstantItem;
import com.madhouseapps.engineeringconvert.R;

import java.util.List;

/**
 * Created by Akshansh on 18-01-2018.
 */

public class ConstantRecyclerAdapter extends RecyclerView.Adapter<ConstantRecyclerAdapter.ConstantViewHolder> {

    private Context context;
    private List<ConstantItem> constantItemList;
    private Typeface quicksand_medium;

    public class ConstantViewHolder extends RecyclerView.ViewHolder {
        public TextView constant, value;

        public ConstantViewHolder(View view) {
            super(view);
            constant = view.findViewById(R.id.constant_desc);
            value = view.findViewById(R.id.constant_value);
        }
    }

    public ConstantRecyclerAdapter(Context context, List<ConstantItem> constantItemList) {
        this.context = context;
        this.constantItemList = constantItemList;
        quicksand_medium = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_medium.ttf");
    }

    @Override
    public ConstantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_constant_item, parent, false);
        return new ConstantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConstantViewHolder holder, int position) {
        ConstantItem item = constantItemList.get(position);

        holder.constant.setTypeface(quicksand_medium);
        holder.constant.setText(item.getConstant());
        holder.value.setTypeface(quicksand_medium);
        holder.value.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        return constantItemList.size();
    }
}
