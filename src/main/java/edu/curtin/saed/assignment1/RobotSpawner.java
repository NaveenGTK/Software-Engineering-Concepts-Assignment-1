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

    public RobotSpawner(JFXArena arena, TextArea logger) {
        this.arena = arena;
        this.logger = logger;
        logger.appendText("Robot spawner added!");
        this.gridArray = arena.getGridArray();
    }

    public void spawn(ExecutorService exec) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable spawnTask = () -> {
            double randomX = new Random().nextBoolean() ? 0 : arena.getGridWidth()-1;
            double randomY = new Random().nextBoolean() ? 0 : arena.getGridHeight()-1;

            if (! gridArray[(int) randomX][(int) randomY].hasObject()) {
                // Create and spawn a robot
                Robot robot = new Robot(arena.getRobotCount()+1, randomX, randomY, logger, exec);
                arena.addRobotToQueue(robot);
                gridArray[(int) randomX][(int) randomY].setHasObject(true);
                arena.incrementRobotCount();
                arena.layoutChildren();
                logger.appendText("Robot Created! with id " + robot.getId() + "and X and Y" + randomX + " " + randomY + "\n");
            }
        };

        scheduler.scheduleAtFixedRate(spawnTask, 0, 1500, TimeUnit.MILLISECONDS);
    }

    /*
    private void attackCitadel(Robot robot, Citadel citadel, JFXArena arena) {
        double citadelX = citadel.getPosX();
        double citadelY = citadel.getPosY();

        double disXToCitadel = Math.abs(robot.getRobotX() - citadelX);
        double disYToCitadel = Math.abs(robot.getRobotY() - citadelY);

        double gridXToMove = 0;
        double gridYToMove = 0;

        if (disXToCitadel > disYToCitadel)  {
            gridXToMove = robot.getRobotX() + 1;
            gridYToMove = robot.getRobotY();

            if ((gridXToMove > arena.getGridWidth()) || (gridXToMove < 0)){
                gridXToMove = robot.getRobotX();
                gridYToMove = robot.getRobotY();
                arena.setRobotPosition(gridXToMove, gridYToMove);
            } else {
                arena.setRobotPosition(gridXToMove, gridYToMove);
            }

            arena.layoutChildren();

        } else if (disYToCitadel > disXToCitadel) {
            gridXToMove = robot.getRobotX();
            gridYToMove = robot.getRobotY() + 1;

            if ((gridYToMove > arena.getGridHeight()) || (gridYToMove < 0)){
                gridXToMove = robot.getRobotX();
                gridYToMove = robot.getRobotY();
                arena.setRobotPosition(gridXToMove, gridYToMove);
            } else {
                arena.setRobotPosition(gridXToMove, gridYToMove);
            }

            arena.layoutChildren();
        }
    }
    */
}
