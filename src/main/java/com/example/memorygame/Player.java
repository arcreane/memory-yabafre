package com.example.memorygame;

import javafx.scene.control.Label;
import javafx.scene.Node;

public class Player {
    private final String name;
    private int score;
    private final Label scoreValueLabel;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.scoreValueLabel = new Label("0");
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
        this.scoreValueLabel.setText(Integer.toString(score));
    }

    public Node getScoreLabel() {
        return new Label(name + " Score:");
    }

    public Node getScoreValueLabel() {
        return scoreValueLabel;
    }
}
