package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;

import java.util.Random;
import java.util.concurrent.ExecutorService;

public class Robot {

    private int id;
    int delay;
    private double robotX;
    private double robotY;
    private TextArea logger;
    private Citadel citadel;
    private JFXArena arena;
    //private static final String IMAGE_FILE = "1554047213.png";
    //private Image robot1;
    private ExecutorService executorService;

    public Robot(int id, double robotX, double robotY, Citadel citadel, TextArea logger, ExecutorService exec, JFXArena arena) {
        this.id = id;
        this.robotX = robotX;
        this.robotY = robotY;
        this.logger = logger;
        this.citadel = citadel;
        this.arena = arena;
        this.delay = new Random().nextInt(2000-500)+500;
        this.executorService = exec;
    }

    public void startRobotBehavior() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(delay);
                    attackCitadel();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void attackCitadel(){
        nextMove();
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

    public TextArea getLogger() {
        return logger;
    }

    public void setLogger(TextArea logger) {
        this.logger = logger;
    }

    private void nextMove() {
        double curX = getRobotX();
        double curY = getRobotY();
        double citadelX = citadel.getPosX();
        double citadelY = citadel.getPosY();
        GridSquare[][] gridArray = arena.getGridArray();

        int gridWidth = arena.getGridWidth();
        int gridHeight = arena.getGridHeight();

        // Calculate the differences in X and Y distances to the citadel
        double disX = citadelX - curX;
        double disY = citadelY - curY;

        // Calculate the absolute differences
        double absDisX = Math.abs(disX);
        double absDisY = Math.abs(disY);

        // Check if the robot is already at the citadel
        if (absDisX == 0 && absDisY == 0) {
             // No need to move
            setRobotX(20.0);
            setRobotY(20.0);
            gridArray[(int) citadelX][(int) citadelY].setHasObject(false);
            arena.setRobotPosition(20.0, 20.0);
            return;
        }

        // Calculate the next position
        double nextX = curX;
        double nextY = curY;

        if (absDisX > absDisY) {
            // Move in the X direction
            nextX = disX > 0 ? curX + 1 : curX - 1;
        } else {
            // Move in the Y direction
            nextY = disY > 0 ? curY + 1 : curY - 1;
        }

        // Ensure the robot stays within the grid boundaries
        nextX = Math.max(0, Math.min(gridWidth - 1, nextX));
        nextY = Math.max(0, Math.min(gridHeight - 1, nextY));

        // Check if the next position is occupied by another robot
        if (!gridArray[(int) nextX][(int) nextY].hasObject()) {
            // The next position is not occupied, so update the position and set it in the arena
            gridArray[(int) curX][(int) curY].setHasObject(false);
            gridArray[(int) nextX][(int) nextY].setHasObject(true);
            setRobotX(nextX);
            setRobotY(nextY);
            arena.setRobotPosition(nextX, nextY);
        } else {
            // The next position is occupied, so find a random direction to move
            while (true) {
                int randomDirection = (int) (Math.random() * 4); // 0 for up, 1 for down, 2 for left, 3 for right

                switch (randomDirection) {
                    case 0: // Up
                        nextY = curY - 1;
                        break;
                    case 1: // Down
                        nextY = curY + 1;
                        break;
                    case 2: // Left
                        nextX = curX - 1;
                        break;
                    case 3: // Right
                        nextX = curX + 1;
                        break;
                }

                // Ensure the random move stays within the grid boundaries
                nextX = Math.max(0, Math.min(gridWidth - 1, nextX));
                nextY = Math.max(0, Math.min(gridHeight - 1, nextY));

                if (!gridArray[(int) nextX][(int) nextY].hasObject()) {
                    gridArray[(int) curX][(int) curY].setHasObject(false);
                    gridArray[(int) nextX][(int) nextY].setHasObject(true);
                    setRobotX(nextX);
                    setRobotY(nextY);
                    arena.setRobotPosition(nextX, nextY);
                    break;
                }
            }
        }
    }


}
