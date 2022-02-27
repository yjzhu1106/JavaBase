package com.shmtu.proxy.example2;


import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTest {



    @Test
    public void testExample() {




    }

    public class GetT<T> {
        T target;

        public GetT(T aClass) {
            this.target = getInstance(aClass);
        }

        public T getInstance(T classs){

            if (classs == Person.class) {
                return (T) new Student("zhangsan");
            }
            if (classs == Animal.class) {
                return (T) new Pig("pipi");
            }
            return null;
        }

    }






}
