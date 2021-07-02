package com.base.thread.bean.bean1;

public class MyData {
    private int i = 0;

    public synchronized void add() {
        i++;
        System.out.println("Thread " + Thread.currentThread().getName() + ", i is " + i);

    }

    public synchronized void dec() {
        i--;
        System.out.println("Thread " + Thread.currentThread().getName() + ", i is " + i);

    }


    public int getData() {
        return i;
    }


}
