package com.base.innerClass.bean;

public class LocalInnerClass {
    private static int a;
    private int b;

    public void method(final int c){
        final int d = 1;
        class Inner{
            public void print(){
                System.out.println(c);
            }
        }
    }
}
