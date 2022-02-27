package com.shmtu.proxy.example2;

public class Pig {
    private String name;

    public Pig(String name) {
        this.name = name;
    }

    public void like() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " like sleep");
    }
}
