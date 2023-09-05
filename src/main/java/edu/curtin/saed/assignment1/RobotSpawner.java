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

    public RobotSpawner(JFXArena arena, TextArea logger, Citadel citadel) {
        this.arena = arena;
        this.logger = logger;
        logger.appendText("Robot spawner added!");
        this.gridArray = arena.getGridArray();
        this.citadel = citadel;
    }

    public void spawn(ExecutorService exec) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable spawnTask = () -> {
            double randomX = new Random().nextBoolean() ? 0 : arena.getGridWidth()-1;
            double randomY = new Random().nextBoolean() ? 0 : arena.getGridHeight()-1;

            if (! gridArray[(int) randomX][(int) randomY].hasObject()) {
                Robot robot = new Robot(arena.getRobotCount()+1, randomX, randomY, citadel, logger, exec, arena);
                arena.addRobotToQueue(robot);
                gridArray[(int) randomX][(int) randomY].setHasObject(true);
                arena.incrementRobotCount();
                arena.layoutChildren();
                logger.appendText("Robot Created! with id " + robot.getId() + " with delay " + robot.delay + "and X and Y" + randomX + " " + randomY + "\n");
                robot.startRobotBehavior();
            }
        };

        scheduler.scheduleAtFixedRate(spawnTask, 0, 1500, TimeUnit.MILLISECONDS);
    }
}
