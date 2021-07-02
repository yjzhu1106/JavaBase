package com.base.thread.test;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest {
    @Test
    public void testCreate(){
        ArrayBlockingQueue fairQueue = new ArrayBlockingQueue(1000, true);
    }

}
