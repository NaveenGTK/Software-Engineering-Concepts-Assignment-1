package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

public class Robot {

    private int id;
    private int delay;
    private double robotX;
    private double robotY;
    private Citadel citadel;
    private JFXArena arena;
    private boolean destroyed;
    private ExecutorService executorService;
    private ScoreController scoreController;
    private App app;
    private TextArea logger;

    public Robot(int id, double robotX, double robotY, Citadel citadel, ExecutorService exec, JFXArena arena, ScoreController scoreController, App app, TextArea logger) {
        this.id = id;
        this.robotX = robotX;
        this.robotY = robotY;
        this.citadel = citadel;
        this.arena = arena;
        this.delay = new Random().nextInt(2000-500)+500;
        this.executorService = exec;
        this.scoreController = scoreController;
        this.destroyed = false;
        this.app = app;
        this.logger = logger;
    }

    public void startRobotBehavior() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (! this.isDestroyed()){
                        Thread.sleep(delay);
                        nextMove();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRobotX() {
        return robotX;
    }

    public void setRobotX(double robotX) {
        this.robotX = robotX;
    }

    public double getRobotY() {
        return robotY;
    }

    public void setRobotY(double robotY) {
        this.robotY = robotY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        if (destroyed) {
            Thread.currentThread().interrupt();
        }
    }

    private void nextMove() {
        double curX = getRobotX();
        double curY = getRobotY();
        double citadelX = citadel.getPosX();
        double citadelY = citadel.getPosY();
        GridSquare[][] gridArray = arena.getGridArray();

        int gridWidth = arena.getGridWidth();
        int gridHeight = arena.getGridHeight();

        double disXToCitadel = citadelX - curX;
        double disYToCitadel = citadelY - curY;

        //Geeting the absolute difference
        double absDisX = Math.abs(disXToCitadel);
        double absDisY = Math.abs(disYToCitadel);


        if (absDisX == 0 && absDisY == 0) {
            // Ending game
            gridArray[(int) curX][(int) curY].setHasObject(false);
            app.stop();

        }

        // Calculate the next position
        double nextX = curX;
        double nextY = curY;

        if (absDisX > absDisY) {
            if (disXToCitadel > 0) {
                nextX = curX + 1; // Move Right
            } else {
                nextX = curX - 1; // Move Left
            }
        } else {
            if (disYToCitadel > 0) {
                nextY = curY + 1; // Move Up
            } else {
                nextY = curY - 1; // Move Down
            }
        }

        /*
            CHECKING BORDER
         */
        if (nextX < 0) {
            nextX = 0; //stay in same position
        } else if (nextX > gridWidth - 1) {
            nextX = gridWidth - 1; // jump to last square
        }

        if (nextY < 0) {
            nextY = 0; //stay in same position
        } else if (nextY > gridHeight - 1) {
            nextY = gridHeight - 1; // jump to last square
        }

        //Check if on wall
        if (gridArray[(int) curX][(int) curY].getWall() != null) {
            Wall wall = gridArray[(int) curX][(int) curY].getWall();
            if (! wall.isDestroyed()){
                wall.decrementHealth();
                logger.appendText("Wall Impacted!!!!\n");
                scoreController.addPoints(100);
                setDestroyed(true);
            }
            gridArray[(int) curX][(int) curY].setHasObject(false);

        }

        // Check if the next position has a robot in it
        if (gridArray[(int) nextX][(int) nextY].hasObject()) {
            while (!Thread.currentThread().isInterrupted()) {
                int randomDirection = (int) (Math.random() * 4);

                if (randomDirection == 0) {
                    nextY = curY - 1;
                    nextX = curX;
                } else if (randomDirection == 1) {
                    nextY = curY + 1;
                    nextX = curX;
                } else if (randomDirection == 2) {
                    nextX = curX - 1;
                    nextY = curY;
                } else if (randomDirection == 3) {
                    nextX = curX + 1;
                    nextY = curY;
                }


                /*
                    CHECKING BORDER
                */
                if (nextX < 0) {
                    nextX = 0; //stay in same position
                } else if (nextX > gridWidth - 1) {
                    nextX = gridWidth - 1; // jump to last square
                }

                if (nextY < 0) {
                    nextY = 0; //stay in same position
                } else if (nextY > gridHeight - 1) {
                    nextY = gridHeight - 1; // jump to last square
                }

                if (!gridArray[(int) nextX][(int) nextY].hasObject()) {
                    gridArray[(int) curX][(int) curY].setHasObject(false);
                    gridArray[(int) nextX][(int) nextY].setHasObject(true);
                    animateRobot(curX, curY, nextX, nextY);
                    break;
                }
            }
        } else {
            // Free to move so moving robot
            gridArray[(int) curX][(int) curY].setHasObject(false);
            gridArray[(int) nextX][(int) nextY].setHasObject(true);

            animateRobot(curX, curY, nextX, nextY);
        }
    }

    private void animateRobot(double curX, double curY, double nextX, double nextY) {
        int time = 40;
        int steps =  (400 / time);

        double stepX = (nextX - curX) / steps;
        double stepY = (nextY - curY) / steps;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int step = 0;

            @Override
            public void run() {
                double intermediateX = curX + (stepX * step);
                double intermediateY = curY + (stepY * step);

                setRobotX(intermediateX);
                setRobotY(intermediateY);
                arena.setRobotPosition(intermediateX, intermediateY);
                step++;

                if (step > steps) {
                    timer.cancel();
                    setRobotX(nextX);
                    setRobotY(nextY);
                }
            }
        }, 0, time);
    }

    public int getDelay() {
        return delay;
    }
}