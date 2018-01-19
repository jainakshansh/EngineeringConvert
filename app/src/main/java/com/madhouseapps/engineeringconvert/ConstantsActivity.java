package com.madhouseapps.engineeringconvert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.madhouseapps.engineeringconvert.Adapters.ConstantRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConstantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ConstantItem> constantItemList;
    private ConstantRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constants);

        recyclerView = findViewById(R.id.recycler_constants);
        constantItemList = new ArrayList<>();
        adapter = new ConstantRecyclerAdapter(this, constantItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        constantItemList.add(new ConstantItem("Proton Mass (mp)", "1.672621777 E-27"));
        constantItemList.add(new ConstantItem("Neutron Mass (mn)", "1.674927351 E-27"));
        constantItemList.add(new ConstantItem("Electron Mass (me)", "9.10938291 E-31"));
        constantItemList.add(new ConstantItem("Muon Mass (mu)", "1.883531475 E-28"));
        constantItemList.add(new ConstantItem("Planck Constant (h)", "6.62606957 E-34"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
