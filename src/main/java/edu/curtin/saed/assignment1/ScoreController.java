package edu.curtin.saed.assignment1;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextArea;

public class ScoreController {
    private int score;
    private Object mutex = new Object();
    TextArea txt;

    public ScoreController(TextArea txt) {
        this.score = 0;
        this.txt = txt;
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
