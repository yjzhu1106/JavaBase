package com.base.thread.test;

import com.base.thread.bean.bean1.AddRunnable;
import com.base.thread.bean.bean1.DecRunnable;
import com.base.thread.bean.bean1.MyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import javax.management.relation.RelationNotFoundException;

public class ThreadShareTest {
    public static void main(String[] args){
        MyData data = new MyData();
        Runnable add = new AddRunnable(data);
        Runnable dec = new DecRunnable(data);
        for(int i = 0; i < 2; i++){
            new Thread(add).start();
            System.out.println("----");
            new Thread(dec).start();
            System.out.println("----");
        }
    }
    @Test
    public void test1(){
        MyData data = new MyData();
        Runnable add = new AddRunnable(data);
        Runnable dec = new DecRunnable(data);
        for(int i = 0; i < 2; i++){
            new Thread(add).start();

            new Thread(dec).start();

        }
    }

    @Test
    public void test2(){
        MyData data = new MyData();

        for(int i = 0; i < 2; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data.add();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    data.dec();
                }
            }).start();

        }
    }
}
