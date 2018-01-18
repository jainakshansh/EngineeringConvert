package com.madhouseapps.engineeringconvert;

/**
 * Created by Akshansh on 18-01-2018.
 */

public class ConstantItem {

    private String constant;
    private String value;

    private ConstantItem() {
    }

    public ConstantItem(String constant, String value) {
        this.constant = constant;
        this.value = value;
    }

    public String getConstant() {
        return constant;
    }

    public String getValue() {
        return value;
    }
}
