package com.base.thread.bean;

import javax.net.ssl.SSLSession;

public class MyThreadLocal {
    private static final ThreadLocal threadSession = new ThreadLocal();
    public static SSLSession getSession(){
        SSLSession s = (SSLSession) threadSession.get();
        if(s == null){
//            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
        return s;
    }
}
