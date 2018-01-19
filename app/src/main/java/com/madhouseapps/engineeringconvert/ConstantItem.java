package com.madhouseapps.engineeringconvert;

/**
 * Created by Akshansh on 18-01-2018.
 */

public class ConstantItem {

    private String name;
    private int symbol;
    private String value;

    private ConstantItem() {
    }

    public ConstantItem(String name, String value, int symbol) {
        this.name = name;
        this.value = value;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public int getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }
}
