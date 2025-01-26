package application;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane backgroundPane;
    @FXML
    private AnchorPane buttonPane;
    @FXML
    private Button twoPlayersButton;
    @FXML
    private Button threePlayersButton;
    @FXML
    private Button fourPlayersButton;

    private List<String> playerChoices = new ArrayList<>();
    private Map<Integer, String> playerColors = new HashMap<>();
    private Map<Integer, String> playerDifficulties = new HashMap<>();
    private int currentPlayer = 1;
    private int totalPlayers;
    private boolean playerCountButtonsFaded = false;

    @FXML
    public void handlePlayerCount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        totalPlayers = Integer.parseInt(clickedButton.getText().split(" ")[0]);
        System.out.println("Selected player count: " + totalPlayers);

        // Fade out the player count buttons
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), buttonPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            playerCountButtonsFaded = true;
            showColorAndDifficultyChoices(totalPlayers);
        });
        fadeOut.play();
    }

    private void showColorAndDifficultyChoices(int playerCount) {
        buttonPane.getChildren().clear();

        // Add buttons for color choices
        Button greenButton = createColorButton("Green", 200);
        Button blueButton = createColorButton("Blue", 300);
        Button yellowButton = createColorButton("Yellow", 400);
        Button pinkButton = createColorButton("Pink", 500);

        buttonPane.getChildren().addAll(greenButton, blueButton, yellowButton, pinkButton);

        // Add buttons for difficulty choices
        Button easyButton = createDifficultyButton("Easy", 200);
        Button hardButton = createDifficultyButton("Hard", 300);

        buttonPane.getChildren().addAll(easyButton, hardButton);

        // Add two empty labels
        Label emptyLabel1 = new Label();
        emptyLabel1.setLayoutX(100);
        emptyLabel1.setLayoutY(100);
        Label emptyLabel2 = new Label();
        emptyLabel2.setLayoutX(100);
        emptyLabel2.setLayoutY(150);

        buttonPane.getChildren().addAll(emptyLabel1, emptyLabel2);

        // Fade in the new choices
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), buttonPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private Button createColorButton(String color, double layoutY) {
        Button button = new Button(color);
        button.setLayoutX(540.0);
        button.setLayoutY(layoutY);
        button.setPrefHeight(70.0);
        button.setPrefWidth(200.0);
        button.setOnAction(e -> handleColorChoice(color, button));
        return button;
    }

    private Button createDifficultyButton(String difficulty, double layoutY) {
        Button button = new Button(difficulty);
        button.setLayoutX(800.0);
        button.setLayoutY(layoutY);
        button.setPrefHeight(70.0);
        button.setPrefWidth(200.0);
        button.setOnAction(e -> handleDifficultyChoice(difficulty));
        return button;
    }

    private void handleColorChoice(String color, Button button) {
        playerColors.put(currentPlayer, color);
        System.out.println("Player " + currentPlayer + " selected color: " + color);
        buttonPane.getChildren().remove(button);
        checkIfPlayerSetupComplete();
    }

    private void handleDifficultyChoice(String difficulty) {
        playerDifficulties.put(currentPlayer, difficulty);
        System.out.println("Player " + currentPlayer + " selected difficulty: " + difficulty);
        checkIfPlayerSetupComplete();
    }

    private void checkIfPlayerSetupComplete() {
        if (playerColors.containsKey(currentPlayer) && playerDifficulties.containsKey(currentPlayer)) {
            System.out.println("Player " + currentPlayer + " setup complete: Color - " + playerColors.get(currentPlayer) + ", Difficulty - " + playerDifficulties.get(currentPlayer));
            currentPlayer++;
            if (currentPlayer > totalPlayers) {
                System.out.println("All players setup complete.");
                // Proceed to the next stage of the game
            } else {
                System.out.println("Next player: " + currentPlayer);
            }
        }
    }

    public List<String> getPlayerChoices() {
        return playerChoices;
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        if (playerCountButtonsFaded) {
            // Clear player choices
            playerColors.clear();
            playerDifficulties.clear();
            currentPlayer = 1;
            playerCountButtonsFaded = false;

            // Reload MultiConfig.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/application/MultiConfig.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            System.out.println("Reloaded MultiConfig.fxml and cleared previous choices");
        } else {
            // Delegate the goBack function to Controller
            Controller controller = new Controller();
            controller.goBack(event);
        }
    }
}