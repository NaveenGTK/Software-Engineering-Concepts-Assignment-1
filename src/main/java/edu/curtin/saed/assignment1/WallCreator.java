package edu.curtin.saed.assignment1;

import java.util.concurrent.BlockingQueue;

public class WallCreator implements Runnable{
    private BlockingQueue<Wall> wallQueue;

    public WallCreator(BlockingQueue<Wall> wallQueue) {
        this.wallQueue = wallQueue;
    }

    @Override
    public void run() {

    }
}
