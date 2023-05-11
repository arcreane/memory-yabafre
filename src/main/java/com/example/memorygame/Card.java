package com.example.memorygame;

import javafx.scene.Node;
import javafx.scene.image.Image;

public class Card {
    private final Image frontImage;
    private final Image backImage;
    private boolean isFlipped;

    public Card(Image frontImage, Image backImage) {
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.isFlipped = false;
    }

    public void flip() {
        this.isFlipped = !this.isFlipped;
    }

    public Node getRepresentation() {
        return new javafx.scene.image.ImageView( isFlipped ? frontImage : backImage );
    }

    public Object getFrontImage() {
        return frontImage;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
}

