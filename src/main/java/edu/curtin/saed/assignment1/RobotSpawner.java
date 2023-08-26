package edu.curtin.saed.assignment1;

import java.util.concurrent.ExecutorService;

public class RobotSpawner implements Runnable{
    private ExecutorService executorService;

    public RobotSpawner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {

    }
}
