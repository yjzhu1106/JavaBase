package com.base.designPattern.factory.bean;

import com.base.designPattern.factory.Interface.Shape;

public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method...");
    }
}
