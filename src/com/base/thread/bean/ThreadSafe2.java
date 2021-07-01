package com.base.thread.bean;

import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Test;

public class ThreadSafe2 extends Thread{
    @Test
    public void run(){
        while(!isInterrupted()){
            try{

                Thread.sleep(5*1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
