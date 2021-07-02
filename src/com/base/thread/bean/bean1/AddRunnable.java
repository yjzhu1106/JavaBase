package com.base.thread.bean.bean1;

public class AddRunnable implements Runnable {
    private MyData data;

    public AddRunnable(MyData data) {
        this.data = data;
    }

    @Override
    public void run() {
        data.add();
    }
}
