package edu.curtin.saed.assignment1;

public class Citadel {

    private double posX;
    private double posY;
    private boolean destroyed;

    public Citadel(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.destroyed = false;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
