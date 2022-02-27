package com.shmtu.proxy.example2;

public class Student implements Person {

    private String name;

    public Student(String name) {
        this.name = name;
    }


    @Override
    public void like() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " like study");
    }
}
