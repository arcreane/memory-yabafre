package com.example.memorygame;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {

    private final ScoreBoard scoreBoard;
    private final GridPane gameBoard;
    private Card selectedCard = null;
    private final List<Card> cards;
    private final boolean isPlayer1Turn;

    public GameController(String player1Name, String player2Name, Theme theme, Difficulty difficulty) {
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);
        this.scoreBoard = new ScoreBoard(player1, player2);
        this.gameBoard = new GridPane();
        this.cards = new ArrayList<>();
        this.isPlayer1Turn = true;
        createCards(theme, difficulty);
        addCardsToBoard();
    }

    private void createCards(Theme theme, Difficulty difficulty) {
        int numPairs = difficulty.getPairs();
        for (int i = 1; i <= numPairs; i++) {
            System.out.println("i: " + i);
            System.out.println("Attempting to open resource: " + "/img/" + i + ".png");
            try {
                Image frontImage = new Image(getClass().getResource("/img/" + i + ".png").openStream(), 100, 100, true, true);
                Image backImage = new Image(getClass().getResource("/img/back.png").openStream(), 100, 100, true, true);
                Card card1 = new Card(frontImage, backImage);
                Card card2 = new Card(frontImage, backImage);
                cards.add(card1);
                cards.add(card2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.shuffle(cards);
    }



    private void addCardsToBoard() {
        int numCards = cards.size();
        int numRows = (int) Math.sqrt(numCards);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                int cardIndex = i * numRows + j;
                Card card = cards.get(cardIndex);
                // Suppose que Card hérite de Node ou a une méthode pour obtenir une représentation de Node
                gameBoard.add(card.getRepresentation(), j, i);
                gameBoard.setHgap(10); // set horizontal gap
                gameBoard.setVgap(10); // set vertical gap
            }
        }
    }

    public void selectCard(Card card) {
        if (selectedCard == null) {
            selectedCard = card;
        } else {
            if (cardsMatch(selectedCard, card)) {
                scoreCurrentPlayer();
                removeCardsFromBoard(selectedCard, card);
            }
            selectedCard = null;
        }
    }

    private boolean cardsMatch(Card card1, Card card2) {
        // Suppose que Card a une méthode pour obtenir l'image de face
        return card1.getFrontImage().equals(card2.getFrontImage());
    }

    private void scoreCurrentPlayer() {
        if (isPlayer1Turn) {
            scoreBoard.getPlayer1().incrementScore();
        } else {
            scoreBoard.getPlayer2().incrementScore();
        }
    }

    private void removeCardsFromBoard(Card card1, Card card2) {
        gameBoard.getChildren().remove(card1.getRepresentation());
        gameBoard.getChildren().remove(card2.getRepresentation());
    }

    public void startGame(Stage stage) {
        VBox root = new VBox();
        root.getChildren().add(scoreBoard);
        root.getChildren().add(gameBoard);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
