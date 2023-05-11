package com.example.memorygame;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class ScoreBoard extends VBox {
    private final Player player1;
    private final Player player2;

    public ScoreBoard( Player player1, Player player2 ) {
        this.player1 = player1;
        this.player2 = player2;

        HBox player1ScoreBox = new HBox();
        player1ScoreBox.getChildren().add( player1.getScoreLabel() );
        player1ScoreBox.getChildren().add( player1.getScoreValueLabel() );

        HBox player2ScoreBox = new HBox();
        player2ScoreBox.getChildren().add( player2.getScoreLabel() );
        player2ScoreBox.getChildren().add( player2.getScoreValueLabel() );

        this.getChildren().add( player1ScoreBox );
        this.getChildren().add( player2ScoreBox );
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
