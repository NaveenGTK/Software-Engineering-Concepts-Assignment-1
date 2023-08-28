package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class RobotSpawner{
    private Citadel citadel;
    private JFXArena arena;
    private TextArea logger;

    public RobotSpawner(Citadel citadel, JFXArena arena, TextArea logger) {
        this.citadel = citadel;
        this.arena = arena;
        this.logger = logger;
        logger.appendText("Robot spawner added!");
    }

    public void spawn() {
        double randomX = new Random().nextBoolean() ? 0 : arena.getGridWidth();
        double randomY = new Random().nextBoolean() ? 0 : arena.getGridHeight();
        Robot robot = new Robot(arena.getRobotCount()+1, randomX, randomY, logger);
        arena.incrementRobotCount();
        logger.appendText("Robot Created! with id " + robot.getId() + "\n");
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
