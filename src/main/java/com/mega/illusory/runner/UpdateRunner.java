package com.mega.illusory.runner;

import java.util.ArrayList;
import java.util.List;

public class UpdateRunner {
    public static final List<UpdateRunner> runners = new ArrayList<>();
    public Runnable run;
    public UpdateRunner(Runnable runnable) {
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
