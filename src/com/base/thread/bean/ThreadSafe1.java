package com.base.thread.bean;

public class ThreadSafe1 extends Thread{
    private volatile boolean exit = false;

    public void run(){
        while(!exit){
            // Do something
        }
    }
}
