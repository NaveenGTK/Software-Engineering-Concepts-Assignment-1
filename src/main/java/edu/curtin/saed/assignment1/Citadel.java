package edu.curtin.saed.assignment1;

public class Citadel {

    private int posX;
    private int posY;
    private boolean destroyed;

    public Citadel(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.destroyed = false;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
