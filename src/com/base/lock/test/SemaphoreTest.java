package com.base.lock.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    @Test
    public void test(){
        Semaphore semap = new Semaphore(5);

        try {
            semap.acquire();
            try {
                System.out.println("业务逻辑");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                semap.release();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
