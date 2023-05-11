package com.example.memorygame;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TextField player1NameField;
    @FXML
    private TextField player2NameField;
    @FXML
    private ComboBox<Theme> themeComboBox;
    @FXML
    private ComboBox<Difficulty> difficultyComboBox;
    @FXML
    private Button startButton;

    @FXML
    private void initialize() {
        themeComboBox.getItems().addAll(Theme.values());
        difficultyComboBox.getItems().addAll(Difficulty.values());
        startButton.setOnAction(event -> startGame());
    }

    public void startGame() {
        // Modifier le thème et la difficulté en fonction des choix des utilisateurs
        String player1Name = player1NameField.getText();
        String player2Name = player2NameField.getText();
        Theme theme = themeComboBox.getValue();
        Difficulty difficulty = difficultyComboBox.getValue();
        GameController gameController = new GameController(player1Name, player2Name, theme, difficulty);
        Stage stage = (Stage) startButton.getScene().getWindow();
        gameController.startGame(stage);
    }
}
