package com.base.designPattern.factory;

import com.base.designPattern.factory.Interface.Shape;

public class ShapeFactoryDemo {
    public static void main(String[] args){
        ShapeFactory factory = new ShapeFactory();

        Shape shape1 = factory.getShape("CIRCLE");
        shape1.draw();

        Shape shape2 = factory.getShape("SQUARE");
        shape2.draw();

        Shape shape3 = factory.getShape("RECTANGLE");
        shape3.draw();
    }

}
