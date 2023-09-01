package edu.curtin.saed.assignment1;

public class GridSquare {
    private boolean hasObject;
    private Object mutex = new Object();

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
}
