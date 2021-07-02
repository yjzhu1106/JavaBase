package com.base.thread.test;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    public static void main(String[] args){
        int N = 5;
        Semaphore semaphore = new Semaphore(N-1);
        for(int i = 0; i < N; i++){
            new Worker(N, semaphore).start();
        }
    }

    static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        public Worker(int num, Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("Worker " + Thread.currentThread().getName() + " use the machine...");
                Thread.sleep(5000);
                System.out.println("Worker " + Thread.currentThread().getName() + " release the machine...");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
