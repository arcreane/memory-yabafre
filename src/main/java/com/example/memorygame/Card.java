package com.example.memorygame;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Card {
    private final Image frontImage;
    private final Image backImage;
    private final ImageView imageView;
    private boolean isFlipped;
    private final CardSelectionListener listener;

    public Card(Image frontImage, Image backImage, CardSelectionListener listener) {
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.imageView = new ImageView(backImage);
        this.isFlipped = false;
        this.listener = listener;

        imageView.setOnMouseClicked(e -> {
            flip();
            if (listener != null) {
                listener.onCardSelected(this);
            }
        });
    }


    public void flip() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), imageView);
        st.setToX(0);  // Set the target width to 0

        st.setOnFinished(e -> {
            this.isFlipped = !this.isFlipped;
            imageView.setImage(isFlipped ? frontImage : backImage);

            ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.5), imageView);
            st2.setToX(1);  // Set the target width back to 1
            st2.play();
        });

        st.play();
    }

    public void setSize(double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    public Node getRepresentation() {
        return imageView;
    }

    public Image getFrontImage() {
        return frontImage;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
}
