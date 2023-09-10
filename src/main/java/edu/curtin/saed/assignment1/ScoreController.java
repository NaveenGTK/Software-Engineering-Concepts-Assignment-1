package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ScoreController implements Runnable {
    private int score;
    private Label label;

    public ScoreController(Label label) {
        this.score = 0;
        this.label = label;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        score += points;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Platform.runLater(() -> {
                    addPoints(10);
                    label.setText("Score: " + getScore());
                });
                Thread.sleep(1000);
            }
        } catch (InterruptedException i) {
            Thread.currentThread().interrupt();
        }
    }
}
