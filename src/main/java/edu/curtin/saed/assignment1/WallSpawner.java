package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WallSpawner {
    private BlockingQueue<Wall> wallQueue;
    private int maxWalls;
    private int curWalls;

    public WallSpawner(int maxWalls) {
        this.wallQueue = new ArrayBlockingQueue<>(maxWalls);
        this.maxWalls = maxWalls;
        this.curWalls = 0;
    }

    public void addWall(Wall wall) throws InterruptedException {
        if (curWalls <= maxWalls) {
            wallQueue.put(wall);
            curWalls++;
        }
    }

    public Wall pollWall() throws InterruptedException {
        return wallQueue.take();
    }

    public int getCurrentWalls() {
        return curWalls;
    }
}
