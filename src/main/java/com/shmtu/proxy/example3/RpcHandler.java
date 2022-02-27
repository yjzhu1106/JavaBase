package com.shmtu.proxy.example3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcHandler implements InvocationHandler {

    private Class service;

    public RpcHandler(Class service) {
        this.service = service;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        Object result = method.invoke(service, args);
        return result;
    }
}
