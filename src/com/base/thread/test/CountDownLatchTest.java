package com.base.thread.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    @Test
    public void test1() throws InterruptedException {
        final CountDownLatch count = new CountDownLatch(2);
        new Thread() {
            public void run() {
                System.out.println("Sub-thread-1 " + Thread.currentThread().getName() + " running...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Sub-thread " + Thread.currentThread().getName() + " ending...");
                count.countDown();
            }

            ;
        }.start();

        new Thread() {
            public void run() {
                System.out.println("Sub-thread-2 " + Thread.currentThread().getName() + " running...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Sub-thread " + Thread.currentThread().getName() + " ending...");
                count.countDown();
            }
        }.start();

        System.out.println("Waiting the 2 threads ending...");
        count.await();
        System.out.println("The 2 threads ended...");
        System.out.println("Continue the main thread...");

    }

}
