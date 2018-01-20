package com.madhouseapps.engineeringconvert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LandingActivity extends AppCompatActivity {

    private TextView madText, houseText, quote;
    private Button conversion, constant;
    private Button share, rate;

    private Typeface quicksand;

    private SharedPreferences sharedPreferences;
    int appOpened = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting up the activity for full screen mode.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);

        //Checking for the number of times the app has been opened, according to which rate dialog will appear.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        appOpened = sharedPreferences.getInt("appOpened", 0);
        appOpened++;
        editor.putInt("appOpened", appOpened);
        editor.apply();

        quicksand = Typeface.createFromAsset(getAssets(), "fonts/quicksand_medium.ttf");
        madText = findViewById(R.id.mad_text);
        houseText = findViewById(R.id.house_text);
        madText.setTypeface(quicksand);
        houseText.setTypeface(quicksand, Typeface.BOLD);
        conversion = findViewById(R.id.conversion_button);
        conversion.setTypeface(quicksand, Typeface.BOLD);
        constant = findViewById(R.id.constants_button);
        constant.setTypeface(quicksand, Typeface.BOLD);
        quote = findViewById(R.id.quote_landing);
        quote.setTypeface(quicksand);

        share = findViewById(R.id.share_button);
        share.setTypeface(quicksand);
        rate = findViewById(R.id.rate_button);
        rate.setTypeface(quicksand);

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

        conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConversionsActivity.class));
            }
        });

        constant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConstantsActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (appOpened % 9 == 0) {
            MadHouseDialog dialog = new MadHouseDialog(LandingActivity.this);
            dialog.show();
        } else {
            finish();
        }
    }
}
