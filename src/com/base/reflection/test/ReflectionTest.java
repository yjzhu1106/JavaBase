package com.base.reflection.test;

import com.base.reflection.bean.Person;
import com.base.reflection.bean.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionTest {

    @Test
    public void Test() throws ClassNotFoundException {
//        Person p = new Student();
        Class clazz = Class.forName("com.base.reflection.bean.Student");

        System.out.println("Methods....");
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            System.out.println(m.toString());
        }

        System.out.println("Constructors...");
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for(Constructor c: declaredConstructors){
            System.out.println(c.toString());
        }

    }

}
