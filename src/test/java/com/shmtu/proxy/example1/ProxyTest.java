package com.shmtu.proxy.example1;

import com.shmtu.proxy.example1.Pig;
import org.junit.jupiter.api.Test;

import java.awt.event.InvocationEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTest {



    @Test
    public void testExample1() {
        Person zhangsan = new Student("zhaangsan");

        InvocationHandler stuHandler = new MyInvocationHandler<Person>(zhangsan);

        Person stuProxy = (Person)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, stuHandler);

        stuProxy.like();

    }

    @Test
    public void test2() {
        Pig zhuzhu = new Pig();

        MyInvocationHandler pigHandler = new MyInvocationHandler<Pig>(zhuzhu);

        Animal animal = (Animal) Proxy.newProxyInstance(Animal.class.getClassLoader(), new Class<?>[]{Animal.class}, pigHandler);

//        Animal animal = (Animal) Proxy.newProxyInstance(Animal.class.getClassLoader(), Animal.class.getInterfaces(), pigHandler);

        animal.eat();
    }



}
