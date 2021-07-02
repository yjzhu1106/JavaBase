package com.base.thread.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.fixed.FixedWidthRoutines;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args){
        int n = 4;
        CyclicBarrier barrier = new CyclicBarrier(n);
        for(int i = 0; i < n; i++){
            new Writer(barrier).start();
        }

    }

    static class Writer extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                System.out.println("Thread " + Thread.currentThread().getName() + " write end and waiting other thread write end...");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("All threads write end, continue other task, such data operation...");

        }
    }
}
