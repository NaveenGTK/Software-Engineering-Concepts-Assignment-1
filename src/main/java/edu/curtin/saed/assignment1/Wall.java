package edu.curtin.saed.assignment1;

public class Wall {
    private int health;
    private double x, y;
    private boolean destroyed;

    public Wall(double x, double y) {
        this.health = 2;
        this.destroyed = false;
        this.x = x;
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
