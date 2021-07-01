package com.base.innerClass.bean;

public class StaticInnerClass {
    private static int a;
    private int b;
    public static class Inner{
        public void print(){
            System.out.println(a);

        }

    }
}
