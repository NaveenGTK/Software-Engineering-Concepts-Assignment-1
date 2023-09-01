package edu.curtin.saed.assignment1;

public class ScoreController {
    private int score;
    private Object mutex = new Object();

    public ScoreController() {
        this.score = 0;
    }

    public int getScore() {
        synchronized (mutex) {
            return score;
        }
    }

    public void addPoints(int points) {
        synchronized (mutex) {
            score += points;
        }
    }
}
