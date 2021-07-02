package com.base.designPattern.abstractFactory.bean;

import com.base.designPattern.abstractFactory.Interface.Shape;

public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method...");
    }
}
