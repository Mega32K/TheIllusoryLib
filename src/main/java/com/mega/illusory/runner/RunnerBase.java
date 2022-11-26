package com.mega.illusory.runner;

public abstract class RunnerBase {

    public Runnable run;
    public abstract void add();

    public void update() {
        run.run();
    }

    public void set(Runnable runnable) {
        run = runnable;
    }
}
