package com.base.designPattern.abstractFactory.bean;

import com.base.designPattern.abstractFactory.Interface.Shape;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method...");
    }
}
