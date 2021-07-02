package com.base.designPattern.factory.bean;

import com.base.designPattern.factory.Interface.Shape;

public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method...");
    }
}
