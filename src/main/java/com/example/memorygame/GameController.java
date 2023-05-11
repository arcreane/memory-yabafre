package com.example.memorygame;

import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private Card selectedCard1 = null;
    private Card selectedCard2 = null;
    private final List<Card> cards;
    private final List<Card> player1Hand = new ArrayList<>();
    private final List<Card> player2Hand = new ArrayList<>();
    private boolean isPlayer1Turn;


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
        if (selectedCard1 == null) {
            selectedCard1 = card;
            selectedCard1.flip();
        } else if (selectedCard2 == null) {
            selectedCard2 = card;
            selectedCard2.flip();

            // Adding a delay of 5 seconds before proceeding with the game
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> processCardSelection());
            pause.play();
        }
    }

    public void processCardSelection() {
        if (cardsMatch()) {
            scoreCurrentPlayer();
            addToPlayerHand();
            removeCardsFromBoard();
            if (isGameFinish()) {
                displayGameFinishMessage();
                restartGame();
            }
        } else {
            selectedCard1.flipBack();
            selectedCard2.flipBack();
        }
        selectedCard1 = null;
        selectedCard2 = null;
        isPlayer1Turn = !isPlayer1Turn;  // Alternate turns between players
    }

    private void addToPlayerHand() {
        if (isPlayer1Turn) {
            player1Hand.add(selectedCard1);
            player1Hand.add(selectedCard2);
        } else {
            player2Hand.add(selectedCard1);
            player2Hand.add(selectedCard2);
        }
    }

    private boolean isGameFinish() {
        return gameBoard.getChildren().isEmpty();  // The game is over when there are no more cards
    }

    private void displayGameFinishMessage() {
        // Display a game over message. You might want to customize this, depending on your application's UI.
        System.out.println("Game Finish!");
    }

    private boolean cardsMatch() {
        // Compare the front images of the two cards to see if they match
        return selectedCard1.getFrontImage() == selectedCard2.getFrontImage();
    }

    private void scoreCurrentPlayer() {
        if (isPlayer1Turn) {
            scoreBoard.getPlayer1().incrementScore();
        } else {
            scoreBoard.getPlayer2().incrementScore();
        }
    }

    private void removeCardsFromBoard() {
        gameBoard.getChildren().remove(selectedCard1.getRepresentation());
        gameBoard.getChildren().remove(selectedCard2.getRepresentation());
    }

    public GridPane getPlayerHandPane(Player player) {
        GridPane handPane = new GridPane();
        List<Card> hand = (player == scoreBoard.getPlayer1()) ? player1Hand : player2Hand;
        for (int i = 0; i < hand.size(); i += 2) {
            // Assume that Card has a getCopy method that returns a new Card with the same front and back images
            handPane.add(hand.get(i).getCopy().getRepresentation(), i / 2, 0);
            handPane.add(hand.get(i + 1).getCopy().getRepresentation(), i / 2, 1);
        }
        return handPane;
    }

    public void startGame(Stage stage) {
        VBox root = new VBox();
        root.getChildren().add(scoreBoard);
        root.getChildren().add(gameBoard);

        // Visualize the cards won by players (you can customize this based on your UI design)
        GridPane player1HandPane = getPlayerHandPane(scoreBoard.getPlayer1());
        GridPane player2HandPane = getPlayerHandPane(scoreBoard.getPlayer2());
        root.getChildren().addAll(new Label("Player 1's pairs:"), player1HandPane, new Label("Player 2's pairs:"), player2HandPane);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void restartGame() {
        // Restart the game
        selectedCard1 = null;
        selectedCard2 = null;
        isPlayer1Turn = true;
        mainController.resetGame();
    }

}
