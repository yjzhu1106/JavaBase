package com.base.innerClass.bean;


public class InnerClass {
    private static int a;
    private int b;
    public class Inner{
        public void print(){
            System.out.println(a);
            System.out.println(b);
        }
    }
}
