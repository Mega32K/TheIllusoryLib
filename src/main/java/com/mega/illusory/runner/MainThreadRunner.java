package com.mega.illusory.runner;

import java.util.ArrayList;
import java.util.List;

public class MainThreadRunner extends RunnerBase{
    public static final List<MainThreadRunner> runners = new ArrayList<>();
    public Runnable run;
    public MainThreadRunner(Runnable runnable) {
        run = runnable;
        runners.add(this);
    }

    public void add() {
        runners.add(this);
    }

    public void update() {
        run.run();
    }
}
