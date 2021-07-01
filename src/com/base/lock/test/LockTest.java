package com.base.lock.test;

import com.base.lock.bean.MyService;
import org.junit.jupiter.api.Test;

public class LockTest {
    @Test
    public void test1(){
        MyService myService = new MyService();
        myService.testMethod();
    }
}
