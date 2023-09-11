package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RobotSpawner{
    private JFXArena arena;
    private TextArea logger;
    private GridSquare[][] gridArray;
    private Citadel citadel;
    private ScoreController scoreController;
    private App app;
    private ScheduledExecutorService scheduler;

    public RobotSpawner(JFXArena arena, TextArea logger, Citadel citadel, ScoreController scoreController, App app) {
        this.arena = arena;
        this.logger = logger;
        this.gridArray = arena.getGridArray();
        this.citadel = citadel;
        this.scoreController = scoreController;
        this.app = app;
    }

    public void spawn(ExecutorService exec) {
        scheduler = Executors.newScheduledThreadPool(1);

        Runnable spawnTask = () -> {
            Random random = new Random();
            double randomX;
            double randomY;

            if (random.nextBoolean()) {
                randomX = 0;
            } else {
                randomX = arena.getGridWidth() - 1;
            }

            if (random.nextBoolean()) {
                randomY = 0;
            } else {
                randomY = arena.getGridHeight() - 1;
            }

            if (! gridArray[(int) randomX][(int) randomY].hasObject()) {
                Robot robot = new Robot(arena.getRobotCount()+1, randomX, randomY, citadel, exec, arena, scoreController, app, logger);
                arena.addRobotToQueue(robot);
                gridArray[(int) randomX][(int) randomY].setHasObject(true);
                arena.incrementRobotCount();
                arena.layoutChildren();
                logger.appendText("Robot Created" + " with delay " + robot.getDelay() + "\n");
                robot.startRobotBehavior();
            }
        };

        scheduler.scheduleAtFixedRate(spawnTask, 0, 1500, TimeUnit.MILLISECONDS);
    }

    public void stop(){
        scheduler.shutdownNow();
    }
}