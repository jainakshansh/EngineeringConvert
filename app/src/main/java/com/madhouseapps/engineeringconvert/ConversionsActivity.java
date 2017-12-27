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
        seeAll = findViewById(R.id.see_all_convs);

        //Initialising the conversion list.
        conversionList = new ArrayList<>();
        conversionList.add("Binary");
        conversionList.add("Octal");
        conversionList.add("Decimal");
        conversionList.add("Hexadecimal");
        conversionList.add("1's Complement");
        conversionList.add("2's Complement");

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
                    fromSpinnerConditions();
                    if (fromEdit.getText().toString().isEmpty()) {
                        toEdit.setText("");
                    }
                    if (fromSpinner.getSelectedItemPosition() == 0) {
                        //Vibrates phone if the input is non-binary and calls snackbar at first wrong input.
                        vibratePhone(isBinary(fromEdit.getText().toString()));
                        callSnackbar();
                    }
                    toEdit.addTextChangedListener(textWatcher);
                } else if (toEdit.getText().hashCode() == s.hashCode()) {
                    fromEdit.removeTextChangedListener(textWatcher);
                    if (toEdit.getText().toString().isEmpty()) {
                        fromEdit.setText("");
                    }
                    if (toSpinner.getSelectedItemPosition() == 0) {
                        //Vibrates phone if the input is non-binary and calls snackbar at first wrong input.
                        vibratePhone(isBinary(toEdit.getText().toString()));
                        callSnackbar();
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
                fromSpinnerConditions();
                if (fromSpinner.getSelectedItemPosition() == 3) {
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
                fromSpinnerConditions();
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
    }

    private void fromSpinnerConditions() {
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
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 4:
                            res = binToones(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = binTotwos(fromEdit.getText().toString());
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
                            break;
                        case 4:
                            break;
                        case 5:
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
                            res = decToones(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = decTotwos(fromEdit.getText().toString());
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
                            res = hexToones(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = hexTotwos(fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                    }
                    break;
                case 4:
                    switch (to) {
                        case 0:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 1:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 2:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 3:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 4:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
                            break;
                        case 5:
                            res = (fromEdit.getText().toString());
                            toEdit.setText(res);
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
        }
    }

    public boolean isBinary(String number) {
        char[] ary = number.toCharArray();
        for (char c : ary) {
            if (!(c == '0' || c == '1'))
                return false;
        }
        return true;
    }

    private void callSnackbar() {
        if (snackCallTrack > 0) {
            return;
        } else {
            Snackbar snackbar = Snackbar.make(constraintParent, "Please enter only binary number", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        snackCallTrack++;
    }

    private String binTobin(String num) {
        return num;
    }

    private String binTooct(String num) {
        Long bin = Long.parseLong(num);
        int octnum = 0, decnum = 0, i = 0;
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
        int decnum = 0, i = 0;
        long remainder;
        while (bin != 0) {
            remainder = bin % 10;
            bin = bin / 10;
            decnum += remainder * Math.pow(2, i);
            ++i;
        }
        return String.valueOf(decnum);
    }

    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }

    private String binToones(String num) {
        String ones = "";
        for (int i = 0; i < num.length(); i++) {
            ones += flip(num.charAt(i));
        }
        return ones;
    }

    private String binTotwos(String num) {
        String ones = binToones(num);
        String twos = "";
        twos = ones;
        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        for (int i = ones.length(); i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        if (!b) {
            twos = '1' + twos;
        }
        return twos;
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
        int decnum = Integer.parseInt(num), rem;
        while (decnum > 0) {
            rem = decnum % 16;
            hexnum = hex[rem] + hexnum;
            decnum = decnum / 16;
        }
        return hexnum;
    }

    private String decToones(String num) {
        String bin = decTobin(num);
        return binToones(bin);
    }

    private String decTotwos(String num) {
        String bin = decTobin(num);
        return binTotwos(bin);
    }

    private String octTobin(String num) {
        String res = "", binary;
        int remainder;
        int octal = Integer.parseInt(num);
        while (octal != 0) {
            remainder = octal % 10;
            binary = decTobin(String.valueOf(remainder));
            if (octal > 8) {
                if (binary.length() == 1) {
                    binary = "00" + binary;
                } else if (binary.length() == 2) {
                    binary = "0" + binary;
                }
            }
            res = binary + res;
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

    private String hexTobin(String num) {
        return new BigInteger(num, 16).toString(2);
    }

    private String hexTodec(String num) {
        String digits = num.toUpperCase();
        int decnum = 0;
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

    private String hexToones(String num) {
        String bin = hexTobin(num);
        return binToones(bin);
    }

    private String hexTotwos(String num) {
        String bin = hexTobin(num);
        return binTotwos(bin);
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
