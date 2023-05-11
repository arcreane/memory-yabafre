package com.example.memorygame;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class GameController implements CardSelectionListener {

    private final ScoreBoard scoreBoard;
    private final GridPane gameBoard;
    private final MainController mainController;
    private Card selectedCard = null;
    private final List<Card> cards;
    private boolean isPlayer1Turn;
    private PauseTransition pause;


    public GameController(String player1Name, String player2Name, Theme theme, Difficulty difficulty, MainController mainController) {
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);
        this.scoreBoard = new ScoreBoard(player1, player2);
        this.mainController = mainController;
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
                Image frontImage = new Image(getClass().getResource("/img/" + i + ".png").openStream());
                Image backImage = new Image(getClass().getResource("/img/back.png").openStream());
                Card card1 = new Card(frontImage, backImage, this);
                Card card2 = new Card(frontImage, backImage, this);
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
        double cardHeight = Math.min(500 / numRows, 100);  // Limit the height to 500 or less
        double cardWidth = Math.min(800 / numRows, 100);  // Limit the width to 800 or less

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                int cardIndex = i * numRows + j;
                Card card = cards.get(cardIndex);
                card.setSize(cardWidth, cardHeight);  // Assume that Card has a setSize method
                gameBoard.add(card.getRepresentation(), j, i);
            }
        }
        gameBoard.setHgap(10); // set horizontal gap
        gameBoard.setVgap(10); // set vertical gap
    }

    @Override
    public void onCardSelected(Card card) {
        selectCard(card);
    }
    public void selectCard(Card card) {
        if (card == selectedCard) {
            return;  // Do nothing if the same card is selected twice
        }

        if (selectedCard == null) {
            selectedCard = card;
            selectedCard.flip();  // Flip the card when it is selected

            // Create a pause transition of 3 seconds
            pause = new PauseTransition(Duration.seconds(3));

            // Set what to do after the pause
            pause.setOnFinished(event -> {
                // If no second card has been selected, flip the first card back over
                if (selectedCard != null) {
                    selectedCard.flip();
                    selectedCard = null;
                }
            });

            // Start the pause
            pause.play();
        } else {
            // If the second card is selected before the pause is over, stop the pause
            if (pause != null) {
                pause.stop();
            }

            if (cardsMatch(selectedCard, card)) {
                scoreCurrentPlayer();
                removeCardsFromBoard(selectedCard, card);
                if (isGameFinish()) {
                    displayGameFinishMessage();
                    restartGame();
                }
            } else {
                selectedCard.flip();  // Flip the first card back over if it doesn't match
                card.flip();  // Flip the second card back over

                // Create a pause transition of 1 second
                pause = new PauseTransition(Duration.seconds(1));

                // Set what to do after the pause
                pause.setOnFinished(event -> {
                    selectedCard.flip();
                    card.flip();
                });

                // Start the pause
                pause.play();
            }

            selectedCard = null;
            isPlayer1Turn = !isPlayer1Turn;  // Alternate turns between players
        }
    }
    private boolean isGameFinish() {
        return gameBoard.getChildren().isEmpty();  // The game is over when there are no more cards
    }

    private void displayGameFinishMessage() {
        // Display a game over message. You might want to customize this, depending on your application's UI.
        System.out.println("Game Finish!");
    }

    private boolean cardsMatch(Card card1, Card card2) {
        // Compare the front images of the two cards to see if they match
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

    private void restartGame() {
        // Restart the game
        selectedCard = null;
        isPlayer1Turn = true;
        mainController.resetGame();
    }

}
