package com.base.annotation.bean;



public class Apple {
    @FruitProvider(id = 1, name = "Shanghai Factory", address = "Haigang Road")
    private String appleProvider;

    public String getAppleProvider() {
        return appleProvider;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }
}
