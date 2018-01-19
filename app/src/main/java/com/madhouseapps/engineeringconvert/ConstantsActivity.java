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
        constantItemList.add(new ConstantItem("Mass of Proton", "1.672621777 E-27 \nkg", R.drawable.proton_mass));
        constantItemList.add(new ConstantItem("Mass of Neutron", "1.674927351 E-27 \nkg", R.drawable.neutron_mass));
        constantItemList.add(new ConstantItem("Mass of Electron", "9.10938291 E-31 \nkg", R.drawable.electron_mass));
        constantItemList.add(new ConstantItem("Mass of Muon", "1.883531475 E-28 \nkg", R.drawable.muon_mass));
        constantItemList.add(new ConstantItem("Planck's Constant", "6.62606957 E-34 \nJ/Hz", R.drawable.plancks_constant));
        constantItemList.add(new ConstantItem("Bohr Radius", "5.291772109 E-11 \nm", R.drawable.bohr_radius));
        constantItemList.add(new ConstantItem("Nuclear Magneton", "5.05078353 E-27 \nJ/T", R.drawable.nuclear_magneton));
        constantItemList.add(new ConstantItem("Bohr Magneton", "9.27400968 E-24 \nJ/T", R.drawable.bohr_magenton));
        constantItemList.add(new ConstantItem("Compton Wavelength", "2.426310239 E-12 \nm", R.drawable.compton_wavelength));
        constantItemList.add(new ConstantItem("Rydberg's Constant", "10973731.57 \n1/m", R.drawable.rydbergs_constant));
        constantItemList.add(new ConstantItem("Atomic Mass Unit", "1.660538921 E-27 \nkg", R.drawable.atomic_mass_unit));
        constantItemList.add(new ConstantItem("Faraday's Constant", "96485.3365 \nC/mol", R.drawable.faradays_constant));
        constantItemList.add(new ConstantItem("Avogadro Number", "6.02214129 E23 \n1/mol", R.drawable.avogadro_number));
        constantItemList.add(new ConstantItem("Boltzmann's Constant", "1.3806488 E-23 \nJ/K", R.drawable.boltzmann_constant));
        constantItemList.add(new ConstantItem("Ideal Gas Constant", "8.3144621 \nJ/K/mol", R.drawable.ideal_gas_constant));
        constantItemList.add(new ConstantItem("Stefan's Constant", "5.670373 E-8", R.drawable.stefans_constant));
        constantItemList.add(new ConstantItem("Electric Constant", "8.854187817 E-12 \nF/m", R.drawable.electric_constant));
        constantItemList.add(new ConstantItem("Magnetic Constant", "1.256637061 E-6 \nH/m", R.drawable.magnetic_constant));
        constantItemList.add(new ConstantItem("Quantum of Flux", "2.067833758 E-15 \nWb", R.drawable.quantum_flux));
        constantItemList.add(new ConstantItem("Normal Gravity", "9.80665 \nN/kg", R.drawable.gravity));
        constantItemList.add(new ConstantItem("Newton's Constant", "6.67384 E-11", R.drawable.newtons_constant));
        constantItemList.add(new ConstantItem("Normal Pressure", "101325 \nPa", R.drawable.atmosphere));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
