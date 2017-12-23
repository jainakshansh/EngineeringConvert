package com.madhouseapps.engineeringconvert;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Button;
import android.widget.ImageView;

public class ConversionsActivity extends AppCompatActivity {

    private AppCompatSpinner fromSpinner, toSpinner;
    private AppCompatEditText fromEdit, toEdit;
    private Button swapButton, seeAll;
    private int from = 0, to = 0;
    private Typeface typeface;
    private ImageView share, rate;

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
        share = findViewById(R.id.share_app);
        rate = findViewById(R.id.rate_us);
        seeAll = findViewById(R.id.see_all_convs);
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
        int i;
        for (i = num.length() - 1; i >= 0; i--) {
            if (num.charAt(i) == '1') {
                break;
            }
            if (i == 0) {
                return "1" + num;
            }
        }
        return num;
    }
}
