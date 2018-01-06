package com.madhouseapps.engineeringconvert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.madhouseapps.engineeringconvert.Adapters.ConversionAdapter;
import com.madhouseapps.engineeringconvert.Adapters.LowerConversionAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ConversionsActivity extends AppCompatActivity {

    private ConstraintLayout constraintParent;
    private AppCompatSpinner fromSpinner, toSpinner;
    private AppCompatEditText fromEdit, toEdit;
    private Button swapButton, seeAll;
    private ImageView share, rate;

    private int from = 0, to = 0;
    private Typeface typeface;

    private TextWatcher textWatcher;
    private List<String> conversionList;
    private ConversionAdapter conversionAdapter;
    private LowerConversionAdapter lowerConversionAdapter;

    private SharedPreferences sharedPreferences;
    int appOpened = 1;
    String res = "";

    int snackCallTrack = 0;

    String bin = "";
    int decnum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversions);

        //Checking for the number of times the app has been opened, according to which rate dialog will appear.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        appOpened = sharedPreferences.getInt("appOpened", 0);
        appOpened++;
        editor.putInt("appOpened", appOpened);
        editor.apply();

        //Referencing all the views from the XML file.
        constraintParent = findViewById(R.id.conversion_constraint_parent);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromEdit = findViewById(R.id.fromEditText);
        toEdit = findViewById(R.id.toEditText);
        swapButton = findViewById(R.id.swap_button);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/quicksand_medium.ttf");
        fromEdit.setTypeface(typeface);
        toEdit.setTypeface(typeface);
        share = findViewById(R.id.share_app);
        rate = findViewById(R.id.rate_us);
        //seeAll = findViewById(R.id.see_all_convs);

        //Initialising the conversion list.
        conversionList = new ArrayList<>();
        conversionList.add("Binary");
        conversionList.add("Octal");
        conversionList.add("Decimal");
        conversionList.add("Hexadecimal");

        conversionAdapter = new ConversionAdapter(getApplicationContext(), conversionList);
        lowerConversionAdapter = new LowerConversionAdapter(getApplicationContext(), conversionList);
        fromSpinner.setAdapter(conversionAdapter);
        conversionAdapter.setDropDownViewResource(R.layout.item_dropdown);
        toSpinner.setAdapter(lowerConversionAdapter);
        lowerConversionAdapter.setDropDownViewResource(R.layout.item_dropdown);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (fromEdit.getText().hashCode() == s.hashCode()) {
                    toEdit.removeTextChangedListener(textWatcher);
                    fromConditions();
                    if (fromEdit.getText().toString().isEmpty()) {
                        toEdit.setText("");
                    }
                    if (fromSpinner.getSelectedItemPosition() == 0) {
                        //Nulls text if the input is non-binary and vibrates at wrong input.
                        if (!isBinary(fromEdit.getText().toString())) {
                            toEdit.setText("");
                        }
                        vibratePhone(isBinary(fromEdit.getText().toString()));
                    }
                    if (fromSpinner.getSelectedItemPosition() == 3) {
                        if (!isHex(fromEdit.getText().toString())) {
                            toEdit.setText("");
                        }
                        vibratePhone(isHex(fromEdit.getText().toString()));
                    }
                    toEdit.addTextChangedListener(textWatcher);
                } else if (toEdit.getText().hashCode() == s.hashCode()) {
                    fromEdit.removeTextChangedListener(textWatcher);
                    toConditions();
                    if (toEdit.getText().toString().isEmpty()) {
                        fromEdit.setText("");
                    }
                    if (toSpinner.getSelectedItemPosition() == 0) {
                        //Nulls text if the input is non-binary and vibrates at wrong input.
                        if (!isBinary(toEdit.getText().toString())) {
                            fromEdit.setText("");
                        }
                        vibratePhone(isBinary(toEdit.getText().toString()));
                    }
                    if (toSpinner.getSelectedItemPosition() == 3) {
                        if (!isHex(toEdit.getText().toString())) {
                            fromEdit.setText("");
                        }
                        vibratePhone(isHex(toEdit.getText().toString()));
                    }
                    fromEdit.addTextChangedListener(textWatcher);
                }
            }
        };
        fromEdit.addTextChangedListener(textWatcher);
        toEdit.addTextChangedListener(textWatcher);

        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removing text watchers before swap.
                fromEdit.removeTextChangedListener(textWatcher);
                toEdit.removeTextChangedListener(textWatcher);
                //Getting all the values to be swapped.
                String fromValue = null, toValue = null;
                if (!fromEdit.getText().toString().trim().isEmpty()) {
                    fromValue = fromEdit.getText().toString().trim();
                }
                if (!toEdit.getText().toString().trim().isEmpty()) {
                    toValue = toEdit.getText().toString().trim();
                }
                int pos1 = fromSpinner.getSelectedItemPosition();
                int pos2 = toSpinner.getSelectedItemPosition();
                //Setting the values for the swap.
                fromEdit.setText(toValue);
                toEdit.setText(fromValue);
                fromSpinner.setSelection(pos2);
                toSpinner.setSelection(pos1);
                //Adding text watchers after swap.
                fromEdit.addTextChangedListener(textWatcher);
                toEdit.addTextChangedListener(textWatcher);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, Check out this amazing Engineering Unit Converter at: https://play.google.com/store/apps/details?id=com.madhouseapps.engineeringconvert");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.madhouseapps.engineeringconvert")));
            }
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Runtime.getRuntime().gc();
                fromConditions();
                if (fromSpinner.getSelectedItemPosition() == 3 || fromSpinner.getSelectedItemPosition() == 5) {
                    fromEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    fromEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Runtime.getRuntime().gc();
                fromConditions();
                if (toSpinner.getSelectedItemPosition() == 3) {
                    toEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    toEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        toSpinner.setSelection(2);
    }

    private void fromConditions() {
        if (!fromEdit.getText().toString().isEmpty()) {
            from = fromSpinner.getSelectedItemPosition();
            to = toSpinner.getSelectedItemPosition();
            switch (from) {
                case 0:
                    switch (to) {
                        case 0:
                            res = binTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = binTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = binTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = binTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 1:
                    switch (to) {
                        case 0:
                            res = octTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = octTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = octTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = octTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 2:
                    switch (to) {
                        case 0:
                            res = decTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = decTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = decTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = decTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 3:
                    switch (to) {
                        case 0:
                            res = hexTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = hexTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = hexTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = hexTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
            }
        }
    }

    private void toConditions() {
        if (!toEdit.getText().toString().trim().isEmpty()) {
            from = toSpinner.getSelectedItemPosition();
            to = fromSpinner.getSelectedItemPosition();
            switch (from) {
                case 0:
                    switch (to) {
                        case 0:
                            res = binTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = binTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = binTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = binTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
                case 1:
                    switch (to) {
                        case 0:
                            res = octTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = octTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = octTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = octTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
                case 2:
                    switch (to) {
                        case 0:
                            res = decTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = decTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = decTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = decTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
                case 3:
                    switch (to) {
                        case 0:
                            res = hexTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = hexTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = hexTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = hexTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
            }
        }
    }

    private void vibratePhone(boolean isBinary) {
        if (!isBinary) {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            }
            callSnackbar();
        }
    }

    public boolean isBinary(String number) {
        char[] array = number.toCharArray();
        for (char c : array) {
            if (!(c == '0' || c == '1'))
                return false;
        }
        return true;
    }

    private boolean isHex(String number) {
        return number.matches("^[0-9a-fA-F]+$");
    }

    private void callSnackbar() {
        if (snackCallTrack > 0) {
            return;
        } else {
            Snackbar snackbar = Snackbar.make(constraintParent, "Please enter valid number w.r.t. category.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        snackCallTrack++;
    }

    private String binTobin(String num) {
        return num;
    }

    private String binTooct(String num) {
        return new BigInteger(num, 2).toString(8);
    }

    private String binTodec(String num) {
        return new BigInteger(num, 2).toString(10);
    }

    private String binTohex(String num) {
        return new BigInteger(num, 2).toString(16);
    }

    private String decTobin(String num) {
        return new BigInteger(num, 10).toString(2);
    }

    private String decTooct(String num) {
        return new BigInteger(num, 10).toString(8);
    }

    private String decTodec(String num) {
        return num;
    }

    private String decTohex(String num) {
        return new BigInteger(num, 10).toString(16);
    }

    private String octTobin(String num) {
        return new BigInteger(num, 8).toString(2);
    }

    private String octTooct(String num) {
        return num;
    }

    private String octTodec(String num) {
        return new BigInteger(num, 8).toString(10);
    }

    private String octTohex(String num) {
        return new BigInteger(num, 8).toString(16);
    }

    private String hexTobin(String num) {
        return new BigInteger(num, 16).toString(2);
    }

    private String hexTodec(String num) {
        return new BigInteger(num, 16).toString(10);
    }

    private String hexTooct(String num) {
        return new BigInteger(num, 16).toString(8);
    }

    private String hexTohex(String num) {
        return num;
    }

    @Override
    public void onBackPressed() {
        if (appOpened % 8 == 0) {
            MadHouseDialog dialog = new MadHouseDialog(ConversionsActivity.this);
            dialog.show();
        } else {
            finish();
        }
    }
}
