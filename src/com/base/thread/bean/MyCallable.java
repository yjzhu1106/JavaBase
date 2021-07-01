package com.base.thread.bean;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {
    private String s;
    public MyCallable(String s) {
        this.s = s;
    }

    @Override
    public Object call() throws Exception {
//        System.out.println("MyCallable.call()..."+s);
        return s;
    }
}
