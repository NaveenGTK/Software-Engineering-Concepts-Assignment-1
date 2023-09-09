package edu.curtin.saed.assignment1;

public class GridSquare {
    private boolean hasObject;
    private Object mutex = new Object();
    private Wall wall;

    public GridSquare(boolean hasObject) {
        this.hasObject = hasObject;
    }

    public boolean hasObject() {
        synchronized (mutex) {
            return hasObject;
        }
    }

    public void setHasObject(boolean hasObject) {
        synchronized (mutex) {
            this.hasObject = hasObject;
        }
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }
}