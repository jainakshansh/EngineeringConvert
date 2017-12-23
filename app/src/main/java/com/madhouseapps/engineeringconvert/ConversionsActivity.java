package com.madhouseapps.engineeringconvert;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Button;

public class ConversionsActivity extends AppCompatActivity {

    private AppCompatSpinner fromSpinner, toSpinner;
    private AppCompatEditText fromEdit, toEdit;
    private Button swapButton;
    private int from = 0, to = 0;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversions);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromEdit = findViewById(R.id.fromEditText);
        toEdit = findViewById(R.id.toEditText);
        swapButton = findViewById(R.id.swap_button);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/quicksand_medium.ttf");
        fromEdit.setTypeface(typeface);
        toEdit.setTypeface(typeface);


    }
}
