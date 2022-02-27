package com.shmtu.utils;

import javax.swing.plaf.TableHeaderUI;

public class MonitorUtil {

    private static ThreadLocal<Long> t1 = new ThreadLocal();


    public static void start() {
        t1.set(System.currentTimeMillis());
    }

    public static void finish(String methodName) {
        long finish = System.currentTimeMillis();
        System.out.println(methodName + " method spent time: " + (finish - t1.get()) + "ms");
    }
}
