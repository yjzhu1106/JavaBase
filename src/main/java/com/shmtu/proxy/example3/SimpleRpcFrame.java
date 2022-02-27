package com.shmtu.proxy.example3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SimpleRpcFrame {

    public static <T> T getRemoteProxy(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new RpcHandler(service));
    }

}
