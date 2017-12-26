package com.madhouseapps.engineeringconvert.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madhouseapps.engineeringconvert.R;

import java.util.List;

/**
 * Created by Akshansh on 26-12-2017.
 */

public class LowerConversionAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> conversionList;
    private Typeface quicksand;

    public LowerConversionAdapter(Context context, List<String> conversionList) {
        super(context, 0, conversionList);
        this.context = context;
        this.conversionList = conversionList;
        quicksand = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_medium.ttf");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = conversionList.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_lower, parent, false);
        }

        TextView spinnerText = view.findViewById(R.id.textView_spinnerItem_lower);
        spinnerText.setTypeface(quicksand);
        spinnerText.setText(item);

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView dropDownText = (TextView) super.getDropDownView(position, convertView, parent);
        dropDownText.setTypeface(quicksand);
        return dropDownText;
    }
}
