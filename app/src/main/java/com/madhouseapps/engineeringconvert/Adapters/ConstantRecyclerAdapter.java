package com.madhouseapps.engineeringconvert.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        public TextView name, value;
        public ImageView symbol;

        public ConstantViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.constant_name);
            value = view.findViewById(R.id.constant_value);
            symbol = view.findViewById(R.id.constant_symbol);
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
        final ConstantItem item = constantItemList.get(position);

        holder.name.setTypeface(quicksand_medium);
        holder.name.setText(item.getName());
        holder.value.setTypeface(quicksand_medium);
        holder.value.setText(item.getValue());
        holder.symbol.setImageResource(item.getSymbol());

        /*
        Code to copy the value of the constant when clicked.
         */
        holder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Value copied", item.getValue());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, item.getName() + " value copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return constantItemList.size();
    }
}
