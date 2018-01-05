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
        conversionList.add("Gray");
        conversionList.add("ASCII");

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
                if (toSpinner.getSelectedItemPosition() == 3 || fromSpinner.getSelectedItemPosition() == 5) {
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
                        case 4:
                            res = binTogray(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = binToascii(fromEdit.getText().toString());
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
                        case 4:
                            res = octTogray(octTobin(fromEdit.getText().toString()));
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = octToascii(octTobin(fromEdit.getText().toString()));
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
                        case 4:
                            res = decTogray(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = decToascii(fromEdit.getText().toString());
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
                        case 4:
                            res = hexTogray(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = hexToascii(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 4:
                    switch (to) {
                        case 0:
                            res = grayTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = grayTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = grayTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = grayTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 4:
                            res = grayTogray(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = grayToascii(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 5:
                    switch (to) {
                        case 0:
                            res = asciiTobin(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = asciiTooct(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = asciiTodec(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = asciiTohex(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 4:
                            res = asciiTogray(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = asciiToascii(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
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
                        case 4:
                            res = binTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = binToascii(toEdit.getText().toString().trim());
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
                        case 4:
                            res = octTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = octToascii(toEdit.getText().toString().trim());
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
                        case 4:
                            res = decTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = decToascii(toEdit.getText().toString().trim());
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
                        case 4:
                            res = hexTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = hexToascii(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
                case 4:
                    switch (to) {
                        case 0:
                            res = grayTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = grayTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = grayTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = grayTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 4:
                            res = grayTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = grayToascii(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                    }
                    break;
                case 5:
                    switch (to) {
                        case 0:
                            res = asciiTobin(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 1:
                            res = asciiTooct(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 2:
                            res = asciiTodec(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 3:
                            res = asciiTohex(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 4:
                            res = asciiTogray(toEdit.getText().toString().trim());
                            fromEdit.setText(res);
                            break;
                        case 5:
                            res = asciiToascii(toEdit.getText().toString().trim());
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

    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }

    private char xor_c(char a, char b) {
        return (a == b) ? '0' : '1';
    }

    private String binTobin(String num) {
        return num;
    }

    private String binTooct(String num) {
        Long bin = Long.parseLong(num);
        int octnum = 0, i = 0;
        decnum = 0;
        while (bin != 0) {
            decnum += (bin % 10) * Math.pow(2, i);
            ++i;
            bin = bin / 10;
        }
        i = 1;
        while (decnum != 0) {
            octnum += (decnum % 8) * i;
            decnum = decnum / 8;
            i = i * 10;
        }
        return String.valueOf(octnum);
    }

    private String binTodec(String num) {
        Long bin = Long.parseLong(num);
        decnum = 0;
        int i = 0;
        long remainder;
        while (bin != 0) {
            remainder = bin % 10;
            bin = bin / 10;
            decnum += remainder * Math.pow(2, i);
            ++i;
        }
        return String.valueOf(decnum);
    }

    private String binTohex(String num) {
        int binnum = Integer.parseInt(num), rem;
        String hexnum = "";
        char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (binnum > 0) {
            rem = binnum % 16;
            hexnum = hex[rem] + hexnum;
            binnum = binnum % 16;
        }
        return hexnum;
    }

    private String binTogray(String num) {
        String gray = "";
        gray += num.charAt(0);
        for (int i = 1; i < num.length(); i++) {
            gray += xor_c(num.charAt(i - 1), num.charAt(i));
        }
        return gray;
    }

    private String binToascii(String num) {
        String lastString = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lastString.length(); i += 8) {
            builder.append((char) Integer.parseInt(lastString.substring(i, i + 8), 2));
        }
        return builder.toString();
    }

    private String decTobin(String num) {
        long binnum = 0, remainder;
        int i = 1;
        long n = Long.parseLong(num);
        while (n != 0) {
            remainder = n % 2;
            n = n / 2;
            binnum += remainder * i;
            i = i * 10;
        }
        return String.valueOf(binnum);
    }

    private String decTooct(String num) {
        int i = 1, octnum = 0;
        int decimal = Integer.parseInt(num);
        while (decimal != 0) {
            octnum += (decimal % 8) * i;
            i = i * 10;
        }
        return String.valueOf(octnum);
    }

    private String decTodec(String num) {
        return num;
    }

    private String decTohex(String num) {
        String hexnum = "";
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        decnum = Integer.parseInt(num);
        int rem;
        while (decnum > 0) {
            rem = decnum % 16;
            hexnum = hex[rem] + hexnum;
            decnum = decnum / 16;
        }
        return hexnum;
    }

    private String decTogray(String num) {
        bin = decTobin(num);
        return binTogray(bin);
    }

    private String decToascii(String num) {
        bin = decTobin(num);
        return binToascii(bin);
    }

    private String octTobin(String num) {
        res = "";
        int remainder;
        int octal = Integer.parseInt(num);
        while (octal != 0) {
            remainder = octal % 10;
            bin = decTobin(String.valueOf(remainder));
            if (octal > 8) {
                if (bin.length() == 1) {
                    bin = "00" + bin;
                } else if (bin.length() == 2) {
                    bin = "0" + bin;
                }
            }
            res = bin + res;
            octal = octal / 10;
        }
        return res;
    }

    private String octTooct(String num) {
        return num;
    }

    private String octTodec(String num) {
        int decnum = 0, i = 0;
        Long octal = Long.parseLong(num);
        while (octal != 0) {
            decnum += (octal % 10) * Math.pow(8, i);
            ++i;
            octal = octal / 10;
        }
        return String.valueOf(decnum);
    }

    private String octTohex(String num) {
        String dec = octTodec(num);
        return decTohex(dec);
    }

    private String octTogray(String num) {
        String dec = octTodec(num);
        return decTogray(dec);
    }

    private String octToascii(String num) {
        String dec = octTodec(num);
        return decToascii(dec);
    }

    private String hexTobin(String num) {
        String decnum = hexTodec(num);
        return decTobin(decnum);
    }

    private String hexTodec(String num) {
        String digits = num.toUpperCase();
        decnum = 0;
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);
            int d = digits.indexOf(c);
            decnum = (16 * decnum) + d;
        }
        return String.valueOf(decnum);
    }

    private String hexTooct(String num) {
        String dec = hexTodec(num);
        return decTooct(dec);
    }

    private String hexTohex(String num) {
        return num;
    }

    private String hexTogray(String num) {
        bin = hexTobin(num);
        return binTogray(bin);
    }

    private String hexToascii(String num) {
        bin = hexTobin(num);
        return binToascii(bin);
    }

    private String grayTobin(String num) {
        bin = "";
        bin += num.charAt(0);
        for (int i = 1; i < num.length(); i++) {
            if (num.charAt(i) == '0') {
                bin += bin.charAt(i - 1);
            } else {
                bin += flip(bin.charAt(i - 1));
            }
        }
        return bin;
    }

    private String grayTooct(String num) {
        bin = grayTobin(num);
        return binTooct(bin);
    }

    private String grayTodec(String num) {
        bin = grayTobin(num);
        return binTodec(bin);
    }

    private String grayTohex(String num) {
        bin = grayTobin(num);
        return binTohex(bin);
    }

    private String grayTogray(String num) {
        return num;
    }

    private String grayToascii(String num) {
        bin = grayTobin(num);
        return binToascii(bin);
    }

    private String asciiTobin(String num) {
        byte[] bytes = num.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    private String asciiTooct(String num) {
        bin = asciiTobin(num);
        return binTooct(bin);
    }

    private String asciiTodec(String num) {
        bin = asciiTobin(num);
        return binTodec(bin);
    }

    private String asciiTohex(String num) {
        bin = asciiTobin(num);
        return binTohex(bin);
    }

    private String asciiTogray(String num) {
        bin = asciiTobin(num);
        return binTogray(bin);
    }

    private String asciiToascii(String num) {
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
