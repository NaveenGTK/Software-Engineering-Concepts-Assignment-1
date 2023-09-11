package edu.curtin.saed.assignment1;

public class Wall {
    private int health;
    private double x, y;
    private boolean destroyed;
    private GridSquare[][] gridArray;

    public Wall(double x, double y, GridSquare[][] gridArray) {
        this.health = 2;
        this.destroyed = false;
        this.x = x;
        this.y = y;
        this.gridArray = gridArray;
    }

    public void decrementHealth(){
        health -= 1;
        if (health <= 0) {
            setDestroyed(true);
            gridArray[(int) x][(int) y].setWall(null);
        }
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

    public int getHealth() {return health;}
}
