package com.base.designPattern.factory;

import com.base.designPattern.factory.Interface.Shape;
import com.base.designPattern.factory.bean.Circle;
import com.base.designPattern.factory.bean.Rectangle;
import com.base.designPattern.factory.bean.Square;

public class ShapeFactory {


    public Shape getShape(String shapeType){
        if(shapeType == null){
            return null;
        }
        if(shapeType.equalsIgnoreCase("CIRCLE")){
            return new Circle();
        }else if(shapeType.equalsIgnoreCase("SQUARE")){
            return new Square();
        }else if(shapeType.equalsIgnoreCase("RECTANGLE")){
            return new Rectangle();
        }


        return null;
    }
}
