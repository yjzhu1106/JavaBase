package com.base.designPattern.factory.bean;

import com.base.designPattern.factory.Interface.Shape;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method...");
    }
}
