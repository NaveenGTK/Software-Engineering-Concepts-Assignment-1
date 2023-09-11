package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WallSpawner implements Runnable {
    private BlockingQueue<Wall> wallQueue;
    private TextArea logger;
    private JFXArena arena;
    private GridSquare[][] gridArray;
    private Label label;
    private Thread queueShowingThread;

    public WallSpawner(Label label, int maxWalls, TextArea logger, JFXArena arena, GridSquare[][] gridArray) {
        this.label = label;
        this.wallQueue = new ArrayBlockingQueue<>(maxWalls);
        this.logger = logger;
        this.arena = arena;
        this.gridArray = gridArray;
    }

    public void addWall(Wall wall) {
        if (wallQueue.remainingCapacity() > 0) {
            wallQueue.offer(wall);
        }
    }

    public void stop() {
        if (queueShowingThread != null) {
            queueShowingThread.interrupt();
        }
    }

    private Wall pollWall() {
        Wall w = wallQueue.poll();
        return w;
    }

    @Override
    public void run() {

        arena.addListener((x, y) -> {
            System.out.println("Arena click at (" + x + "," + y + ")");
            if ((! gridArray[x][y].hasObject()) && (gridArray[x][y].getWall() == null)){
                Wall wall = new Wall(x, y, gridArray);
                addWall(wall);
            }
        });

        while (! Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            setCommandCount();
            Wall wall = pollWall();
            if (wall != null) {
                if (!wall.isDestroyed()) {
                    Platform.runLater(() -> {
                        gridArray[(int) wall.getX()][(int) wall.getY()].setWall(wall);
                        logger.appendText("Added wall to " + wall.getX() + " " + wall.getY() + "\n");
                        arena.addToWallQueue(wall);
                    });

                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }


    }

    public void setCommandCount(){
        Runnable myTask = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    label.setText("Wall Commands: " + wallQueue.size());
                });
            }
        };
        queueShowingThread = new Thread(myTask, "command-thread");
        queueShowingThread.start();
    }
}
