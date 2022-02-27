package com.shmtu.proxy.example1;

public class Student implements Person {

    private String name;

    public Student(String name) {
        this.name = name;
    }


    @Override
    public void like() {

        System.out.println("zhuzhu like chouzhuanzhuan...");
    }
}
