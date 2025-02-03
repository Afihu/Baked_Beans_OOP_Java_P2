/*author: Duong Vo Thien Bao
 Modified by Huynh Thien Bao*/

package application;

import javafx.scene.control.Label;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultiConfigController {
    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane buttonPane;
    @FXML private TableView<PlayerChoice> choiceTable;
    @FXML private TableColumn<PlayerChoice, Integer> playerColumn;
    @FXML private TableColumn<PlayerChoice, String> colourColumn;
    @FXML private TableColumn<PlayerChoice, String> difficultyColumn;
    @FXML private Spinner<Integer> timeSpinner;

    private ObservableList<PlayerChoice> playerChoicesList = FXCollections.observableArrayList();
    private Map<Integer, String> playerColors = new HashMap<>();
    private Map<Integer, String> playerDifficulties = new HashMap<>();
    private int currentPlayer = 1;
    private int totalPlayers;
    private int gameDuration = 0;

    private enum ButtonType {
        COLOR,
        DIFFICULTY,
        ALL
    }
    

    public static class PlayerChoice {
        private final IntegerProperty playerNumber;
        private final StringProperty colour;
        private final StringProperty difficulty;

        public PlayerChoice(int playerNumber, String colour, String difficulty) {
            this.playerNumber = new SimpleIntegerProperty(playerNumber);
            this.colour = new SimpleStringProperty(colour);
            this.difficulty = new SimpleStringProperty(difficulty);
        }

        public int getPlayerNumber() { return playerNumber.get(); }
        public String getColour() { return colour.get(); }
        public String getDifficulty() { return difficulty.get(); }

        public IntegerProperty playerNumberProperty() { return playerNumber; }
        public StringProperty colourProperty() { return colour; }
        public StringProperty difficultyProperty() { return difficulty; }
    }

    @FXML
    public void initialize() {
        if (choiceTable != null) {
            playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerNumber"));
            colourColumn.setCellValueFactory(new PropertyValueFactory<>("colour"));
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            choiceTable.setItems(playerChoicesList);

            SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 600, 10);
            timeSpinner.setValueFactory(valueFactory);
            timeSpinner.setEditable(true);
            
            timeSpinner.getEditor().setOnAction(event -> {
                try {
                    timeSpinner.increment(0); // Commit value
                    gameDuration = timeSpinner.getValue();
                    System.out.println("Timer confirmed: " + gameDuration + " seconds");
                    checkGameCompletion();
                } catch (Exception e) {
                    System.err.println("Invalid timer input");
                }
            });
        }
    }

    @FXML
    public void handlePlayerCount(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        totalPlayers = Integer.parseInt(clickedButton.getText().split(" ")[0]);
        System.out.println("Selected player count: " + totalPlayers);
        
        Controller.screenHistory.push("MultiConfig.fxml");
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MultiConfig.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleColourChoice(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String color = clickedButton.getText();
        playerColors.put(currentPlayer, color);
        System.out.println("Player " + currentPlayer + " selected color: " + color);
        buttonPane.getChildren().remove(clickedButton);
        setButtonsState(ButtonType.COLOR, true); // Disable remaining color buttons
        updateTableChoice();
    }

    @FXML
    public void handleDifficultyChoice(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String difficulty = clickedButton.getText();
        playerDifficulties.put(currentPlayer, difficulty);
        System.out.println("Player " + currentPlayer + " selected difficulty: " + difficulty);
        updateTableChoice();

        if (currentPlayer > totalPlayers) {
            removeUIElement(UIElementType.BUTTONS);
            if (gameDuration != 0) {
                showContinueButton();
            }
        } else {
            setButtonsState(ButtonType.COLOR, false); // Enable color buttons for next player
        }
    }

    private void setButtonsState(ButtonType type, boolean disabled) {
        buttonPane.getChildren().stream()
            .filter(node -> node instanceof Button)
            .map(node -> (Button) node)
            .filter(button -> 
                button.getText().equals("Green") ||
                button.getText().equals("Blue") ||
                button.getText().equals("Yellow") ||
                button.getText().equals("Pink")
            )
            .forEach(button -> button.setDisable(disabled));
    }

    private void updateTableChoice() {
        if (playerColors.containsKey(currentPlayer) && playerDifficulties.containsKey(currentPlayer)) {
            PlayerChoice choice = new PlayerChoice(
                currentPlayer,
                playerColors.get(currentPlayer),
                playerDifficulties.get(currentPlayer)
            );
            playerChoicesList.add(choice);
            
            if (currentPlayer >= totalPlayers) {
                removeUIElement(UIElementType.BUTTONS);
                checkGameCompletion();
            } else {
                currentPlayer++;
                setButtonsState(ButtonType.COLOR, false);
            }
        }
    }

    private void removeUIElement(UIElementType type) {
        switch(type) {
            case BUTTONS:
                buttonPane.getChildren().removeIf(node -> 
                    node instanceof Button && (
                        ((Button) node).getText().equals("Easy") || 
                        ((Button) node).getText().equals("Hard") ||
                        ((Button) node).getText().equals("Green") ||
                        ((Button) node).getText().equals("Blue") ||
                        ((Button) node).getText().equals("Yellow") ||
                        ((Button) node).getText().equals("Pink")
                    )
                );
                break;
                
            case ALL:
                buttonPane.getChildren().removeIf(node -> 
                    (node instanceof Button && (
                        ((Button) node).getText().equals("Easy") || 
                        ((Button) node).getText().equals("Hard") ||
                        ((Button) node).getText().equals("Green") ||
                        ((Button) node).getText().equals("Blue") ||
                        ((Button) node).getText().equals("Yellow") ||
                        ((Button) node).getText().equals("Pink")
                    )) || 
                    (node instanceof Label && (
                        ((Label) node).getText().equals("Choose Your Colour") ||
                        ((Label) node).getText().equals("Choose Your Difficulty") ||
                        ((Label) node).getText().equals("Set Timer (10 - 600 Seconds)")
                    )) ||
                    node instanceof Spinner<?>
                );
                break;
        }
    }
    
    private enum UIElementType {
        BUTTONS,
        ALL
    }
    
    private void checkGameCompletion() {
        boolean allPlayersChosen = playerChoicesList.size() >= totalPlayers;
        if (allPlayersChosen && gameDuration != 0) {
            removeUIElement(UIElementType.ALL);
            showContinueButton();
        }
    }

    private void showContinueButton() {
        Button continueButton = new Button("Continue");
        continueButton.setLayoutX(540);
        continueButton.setLayoutY(400);
        continueButton.setPrefHeight(70);
        continueButton.setPrefWidth(200);
        continueButton.setOnAction(this::handleContinue);
        buttonPane.getChildren().add(continueButton);
    }

    private void handleContinue(ActionEvent event) {
        System.out.println("MultiConfig successful. Initiating the lobby.");
        loadGameScreen(event);
    }

    private void loadGameScreen(ActionEvent event) {
        try {
        	String currentScreen = "MPGameScreen.fxml";
    		Controller.storeCurrentScreen(currentScreen);
    		
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MPGameScreen.fxml")); // Correct path
            Parent root = loader.load();

            GameplayController gameplayController = loader.getController();
            gameplayController.initializeData(this.playerChoicesList, this.gameDuration);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            System.out.println("MPGameScreen Opened");

        } catch (IOException e) {
            e.printStackTrace();
        }  	
    }
    
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        if (!Controller.screenHistory.isEmpty()) {
            playerColors.clear();
            playerDifficulties.clear();
            playerChoicesList.clear();
            currentPlayer = 1;
            totalPlayers = 0;
            gameDuration = 0;
            
            Controller.screenHistory.pop();
            String previousScreen = Controller.screenHistory.peek();
            
            Parent root = FXMLLoader.load(getClass().getResource("/application/" + previousScreen));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            System.out.println("Navigated back to: " + previousScreen);
        }
    }
    @FXML
    public void goBacktoMainScreen(ActionEvent event) throws IOException {
        // Clear all data
        playerColors.clear();
        playerDifficulties.clear();
        playerChoicesList.clear();
        currentPlayer = 1;
        totalPlayers = 0;
        gameDuration = 0;
        
        // Clear screen history
        Controller.screenHistory.clear();
        
        // Load StartScreen
        Parent root = FXMLLoader.load(getClass().getResource("/application/StartScreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        System.out.println("Returned to Main Menu");
    }
}