package com.madhouseapps.engineeringconvert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    private void constantsClass(View view) {
        startActivity(new Intent(getApplicationContext(), ConstantsActivity.class));
    }

    private void conversionsClass(View view) {
        startActivity(new Intent(getApplicationContext(), ConversionsActivity.class));
    }
}
