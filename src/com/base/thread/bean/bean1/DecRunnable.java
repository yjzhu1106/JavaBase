package com.base.thread.bean.bean1;

public class DecRunnable implements Runnable{
    private  MyData data;
    public DecRunnable(MyData data){
        this.data  = data;
    }
    @Override
    public void run() {
        data.dec();
    }
}
