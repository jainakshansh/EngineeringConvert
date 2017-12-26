package com.madhouseapps.engineeringconvert;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.madhouseapps.engineeringconvert.Adapters.ConversionAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ConversionsActivity extends AppCompatActivity {

    private AppCompatSpinner fromSpinner, toSpinner;
    private AppCompatEditText fromEdit, toEdit;
    private Button swapButton, seeAll;
    private int from = 0, to = 0;
    private Typeface typeface;
    private ImageView share, rate;
    private List<String> conversionList;
    private ConversionAdapter conversionAdapter;
    private SharedPreferences sharedPreferences;
    int appOpened = 1;
    String res = "";

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
    }

    private void fromSpinnerWorking() {
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = fromSpinner.getSelectedItemPosition();
                to = toSpinner.getSelectedItemPosition();
                if (!fromEdit.getText().toString().isEmpty()) {
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
                                    res = binToones(fromEdit.getText().toString());
                                    toEdit.setText(res);
                                    break;
                                case 4:
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
                                    res = binToones(fromEdit.getText().toString());
                                    toEdit.setText(res);
                                    break;
                                case 4:
                                    res = binTotwos(fromEdit.getText().toString());
                                    toEdit.setText(res);
                                    break;
                            }
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String binTobin(String num) {
        return num;
    }

    private String binTooct(String num) {
        return Integer.toOctalString(Integer.parseInt(num));
    }

    private String binTodec(String num) {
        return String.valueOf(Integer.parseInt(num, 2));
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
        /*
        String res = "";
        int number = Integer.parseInt(num);
        while (number > 0) {
            res = (number % 2) + res;
            number = number / 2;
        }
        return res;
        */
        return String.valueOf(Integer.toBinaryString(Integer.parseInt(num)));
    }

    private String decTooct(String num) {
        return String.valueOf(Integer.toOctalString(Integer.parseInt(num)));
    }

    private String decTodec(String num) {
        return num;
    }

    private String decTohex(String num) {
        return String.valueOf(Integer.toHexString(Integer.parseInt(num)));
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
        return String.valueOf(Integer.parseInt(num, 8));
    }

    private String hexTobin(String num) {
        return new BigInteger(num, 16).toString(2);
    }

    private String hexTodec(String num) {
        return String.valueOf(Integer.parseInt(num, 16));
    }

    private String hexTooct(String num) {
        String dec = hexTodec(num);
        return String.valueOf(Integer.toOctalString(Integer.parseInt(dec)));
    }

    private String hexToones(String num) {
        String bin = hexTobin(num);
        return binToones(bin);
    }

    private String hexTotwos(String num) {
        String bin = hexTobin(num);
        return binTotwos(bin);
    }
}
