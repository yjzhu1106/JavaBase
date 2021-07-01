package com.base.thread.test;

import com.base.thread.bean.MyCallable;
import com.base.thread.bean.MyThread;
import com.base.thread.bean.MyThreadRunnable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadTest {
    @Test
    public void testThreadExtends() {
        MyThread myThread1 = new MyThread();
        myThread1.start();
    }


    @Test
    public void testThreadImplements() {

        MyThreadRunnable myThreadRunnable = new MyThreadRunnable();
        Thread thread = new Thread(myThreadRunnable);
        thread.start();
    }

    @Test
    public void testExecutorService() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < 5; i++) {
            MyCallable myCallable = new MyCallable(i + "");
            Future submit = pool.submit(myCallable);
            list.add(submit);
        }

        pool.shutdown();

        for (Future f : list) {
            Object o = f.get();
            System.out.println("res: " + f.get().toString());
        }

    }


    @Test
    public void testThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while (true) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is running...");
                    try {
                        Thread.sleep(30000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Test
    public void testScheduledThreadPool() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
        threadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sleep 3s...");
            }
        }, 3, TimeUnit.SECONDS);

        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sleep 1s and running every 3s...");
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Test
    public void testJoin() throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+" thread begin running...");
        MyThread thread = new MyThread();

        thread.setName("thread-B");
        thread.join();
//        thread.notify();
        if(thread.isAlive()){
            System.out.println("thread-B alive...");
        }
        System.out.println("The active count: "+Thread.activeCount());

        System.out.println("This thread-B ending and beginning the main thread....");

    }


}
