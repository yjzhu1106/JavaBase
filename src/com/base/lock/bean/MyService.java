package com.base.lock.bean;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyService {
    private Lock lock = new ReentrantLock();
    //    private Lock lock = new ReentrantLock(true); // fair lock
    //    private Lock lock = new ReentrantLock(false); // no-fair lock
    private Condition condition = lock.newCondition();
    public void testMethod() {
        try {
            lock.lock(); // lock
            // 1、wait方法等待
            System.out.println("Begin wait...");
//            condition.wait();
            // 通过创建condition对象来使线程wait，必须先执行lock.lock获得锁
            // 2、signal方法唤醒
            condition.signal();
            for(int i = 0; i < 5; i++){
                System.out.println("ThreadName: " + Thread.currentThread().getName() + (" " + (i+1)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
}
