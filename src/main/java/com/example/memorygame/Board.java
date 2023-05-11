package com.example.memorygame;


import javafx.scene.Node;

public class Board extends Node {
    private final Card[][] cards;
    private final Theme theme;

    public Board( Theme theme ) {
        this.theme = theme;
        this.cards = new Card[4][4];
    }

    public void updateBoard() {
        // board is updated directly in the Card class
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}

