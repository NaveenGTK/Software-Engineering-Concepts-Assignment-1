package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

import java.util.Random;

public class Robot {

    private int id;
    private int delay;
    private double robotX;
    private double robotY;
    private TextArea logger;
    private static final String IMAGE_FILE = "1554047213.png";
    private Image robot1;

    public Robot(int id, double robotX, double robotY, TextArea logger) {
        this.id = id;
        this.robotX = robotX;
        this.robotY = robotY;
        this.logger = logger;
        this.delay = new Random().nextInt(2000-500)+500;
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
}
