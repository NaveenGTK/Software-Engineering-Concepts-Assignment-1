package edu.curtin.saed.assignment1;

import java.util.Random;

public class Robot {

    private int id;
    private int delay;
    private Logger logger;

    public Robot(int id, Logger logger) {
        this.id = id;
        this.logger = logger;
        this.delay = new Random().nextInt(2000-500)+500;
    }
}
