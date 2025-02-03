//author: Huynh Thien Bao
package application; // Make sure this is the correct package

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameScreenController implements Initializable {

    @FXML
    private GridPane cardGridPane; // Assuming you have a GridPane in GameScreen.fxml
    
    //for testing scoring system
    @FXML
    private Button startStopButton;
    @FXML
    private Label colorLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label roundDurationLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label finalResultLabel;
    @FXML
    private Label roundResultLabel;
    
    private long startTime;
    private boolean roundStarted = false;
    
    //for the system
    @FXML
    private Button exitToMainScreenButton;
    @FXML
    private Button exitToSPConfigScreen;
    @FXML
    private Label timerLabel; 
    @FXML
    private Label roundCounterLabel; 
    
    private String receivedColor;
    private String receivedDifficulty;
    private int receivedRoundDuration;
    
    private double totalScore = 0.0;
    private int roundsPlayed = 0;
    private final int totalRounds = 8;		//Interchangeable for MP
    
    private Timeline countdownTimer;
    private int remainingTime;
    
    private static List<String> cardList = new ArrayList<>(); // Stores FXML file names
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	// Hide the labels initially
    	finalResultLabel.setVisible(false);	
    	roundResultLabel.setVisible(false);
    	exitToMainScreenButton.setVisible(false);
    	timerLabel.setVisible(false);
    	roundCounterLabel.setVisible(false);
    	
        // Initialization logic if needed (e.g., setting up the gridPane)
    }

    public void initializeData(String color, String difficulty, int roundDuration) {
        this.receivedColor = color;
        this.receivedDifficulty = difficulty;
        this.receivedRoundDuration = roundDuration;
        
        // Update labels with the received values
        colorLabel.setText("Color: " + this.receivedColor);
        difficultyLabel.setText("Difficulty: " + this.receivedDifficulty);
        roundDurationLabel.setText("Round Duration: " + this.receivedRoundDuration + " seconds");
        
        System.out.println("Color: " + this.receivedColor + ", Difficulty: " + this.receivedDifficulty + ", Round Duration: " + this.receivedRoundDuration);
        
        loadFXMLFilesName();
        //loadCards();
    }
    
    private void loadFXMLFilesName() {
        cardList.clear(); //clear previous data

        if (receivedColor == null || receivedDifficulty == null) {
            System.out.println("Color or difficulty not set yet!");
            return;
        }

        String prefix = "";
        int startIndex = 0;
        int endIndex = 0;

        //determine prefix and index range based on color and difficulty
        if (receivedDifficulty.equalsIgnoreCase("easy")) {
            prefix = "A";
        } else if (receivedDifficulty.equalsIgnoreCase("hard")) {
            prefix = "B";
        } else {
            System.out.println("Invalid difficulty!");
            return;
        }

        switch (receivedColor.toLowerCase()) {
            case "blue":
                startIndex = 1;
                endIndex = 8;
                break;
            case "pink":
                startIndex = 9;
                endIndex = 16;
                break;
            case "green":
                startIndex = 17;
                endIndex = 24;
                break;
            case "yellow":
                startIndex = 25;
                endIndex = 32;
                break;
            default:
                System.out.println("Invalid color!");
                return;
        }

        //populate the list with file names
        for (int i = startIndex; i <= endIndex; i++) {
            cardList.add(prefix + i + ".fxml");
        }

        System.out.println("Loaded FXML files: " + cardList);
    }

    private void startCountdown() {
        timerLabel.setText("Time left: " + remainingTime + "s");
        
        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime--;
            timerLabel.setText("Time left: " + remainingTime + "s");

            if (remainingTime <= 0) {
                stopCountdown();
                endRoundTimeOut();
                timerLabel.setText("Time's up! Too bad!");
            }
        }));

        countdownTimer.setCycleCount(receivedRoundDuration); // Run for the full duration
        countdownTimer.play();
    }
    
    private void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }
    
    private void endRoundEarly() {
        long endTime = System.currentTimeMillis();
        double usedTime = (endTime - startTime) / 1000.0; // Convert to seconds
        processRoundEnd(usedTime);
    }
    
    private void endRoundTimeOut() {
        processRoundEnd(receivedRoundDuration); // Player ran out of time
    }

    private void processRoundEnd(double usedTime) {
    	//stop the round and calculate score
        double score = calculateBaseScore(usedTime);
        totalScore += score;
        
        //modify state of the game
        this.roundStarted = false;
        startStopButton.setText("Start next round!");
        
        //modify state of in-game labels | calculateScore(0.0)*roundsPlayed is the maximum score a player can get
        roundResultLabel.setVisible(true);
        roundResultLabel.setText("Round ends! \n Score this round: " + score  
        		+ "\n" + String.format("[%.2f / 100]",(score/calculateBaseScore(0.0))*100));
        scoreLabel.setText("Score: " + formatToTwoDecimal(this.totalScore) + "/" + calculateBaseScore(0.0) * this.roundsPlayed);

        //print out to the system for debugging
        System.out.println("Round ends!");
        System.out.println("Current total: " + formatToTwoDecimal(this.totalScore) + "/" + calculateBaseScore(0.0) * this.roundsPlayed);	
        
        //if reached the end of game, ends the game and go back to the lobby
        if (roundsPlayed >= totalRounds) {
            endGame();
        }
    }

    
    //for testing scoring system
    @FXML
    private void handleStartStopButtonAction() {
        if (!this.roundStarted) {		//if decide to keep the button, add conditions (ex: !this.roundStarted & cardSolved == True)
            // Start the round
        	roundResultLabel.setVisible(false);
        	
        	timerLabel.setVisible(true);
            startTime = System.currentTimeMillis();
            
            this.roundStarted = true;
            this.roundsPlayed++;
            
            roundCounterLabel.setVisible(true);
        	roundCounterLabel.setText("Round " + this.roundsPlayed + "/" + this.totalRounds);
        	
            startStopButton.setText("Ubongo!");
            
            // Start count down
            this.remainingTime = this.receivedRoundDuration;
            startCountdown(); //If player used all the time given, the "else" path will be initiated
            
            //print out to the system for debugging
            System.out.println("Round starts!");
        } else {
        	// Player pressed the button before timeout
            stopCountdown();
            endRoundEarly();
        }
    }
    
    private void endGame() {
        // Hide all labels and buttons
        //cardGridPane.setVisible(false);
        startStopButton.setVisible(false);
        colorLabel.setVisible(false);
        difficultyLabel.setVisible(false);
        roundDurationLabel.setVisible(false);
        scoreLabel.setVisible(false);
        roundResultLabel.setVisible(false);
        exitToSPConfigScreen.setVisible(false);
        timerLabel.setVisible(false);
    	roundCounterLabel.setVisible(false);

        // Show final result
        finalResultLabel.setText(String.format("Game Finished!\nTotal score: %.2f", this.totalScore) + "/" + calculateBaseScore(0.0) * this.roundsPlayed
        		+ "\n" + String.format("[%.2f / 100]",(this.totalScore/(calculateBaseScore(0.0) * this.roundsPlayed))*100));
        	
        finalResultLabel.setVisible(true);
        exitToMainScreenButton.setVisible(true);
    }
    
    //convert standard time format to usable time format for displaying the scoring system
    public String formatToTwoDecimal(double number) {
        return String.format("%.2f", number);
    }
    
    public double calculateBaseScore(double usedTime) {
        double remainingTime = this.receivedRoundDuration - usedTime;
        double score;
        if (usedTime >= this.receivedRoundDuration) {
            score = 0;
        } else if (usedTime <= 0.25 * this.receivedRoundDuration) {
        	score = (remainingTime * 3);
        } else if (usedTime <= 0.5 * this.receivedRoundDuration) {
        	score = (remainingTime * 2);
        } else if (usedTime <= 0.75 * this.receivedRoundDuration) {
        	score = (remainingTime * 1);
        } else {
        	score = remainingTime;
        }
        if(this.receivedDifficulty == "hard") {
        	score = 1.5 * score;
        }
        BigDecimal bd = new BigDecimal(score).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public void goBack(ActionEvent e) throws IOException {
		if (!Controller.screenHistory.isEmpty()) {
			Controller.screenHistory.pop();
			String previousScreen = Controller.screenHistory.peek(); 
			if (previousScreen == "StartScreen.fxml"){
				Controller.screenHistory.pop();
				Controller.storeCurrentScreen(previousScreen);
			}
			System.out.println("Attempting to go back to: " + previousScreen);
			Parent root = FXMLLoader.load(getClass().getResource("/application/" + previousScreen));
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			System.out.println("Navigated back to: " + previousScreen);
        } else {
            System.out.println("Screen history is empty. Cannot go back.");
		}
	}
    
    public void goBackToMainScreen(ActionEvent e) throws IOException {
    	Controller.screenHistory.clear();
        
    	Controller.storeCurrentScreen("StartScreen.fxml");
        	
        System.out.println("Attempting to go back to: StartScreen.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("/application/" + "StartScreen.fxml"));
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		System.out.println("Returned to Main Menu");
        	
	}
	
    
    private void loadCards() {
        if (cardGridPane == null) {
            System.err.println("cardGridPane is null. Check your FXML.");
            return;
        }

        cardGridPane.getChildren().clear(); // Clear existing cards

        try {
            int rows = 4; // Example
            int cols = 5; // Example

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    String cardImageName = String.format("%s_%s_card%d.png", receivedColor, receivedDifficulty, (row * cols + col + 1)); // Construct image name
                    Image cardImage = new Image(new File("res/images/" + cardImageName).toURI().toString()); // Assumes images are in res/images
                    ImageView cardImageView = new ImageView(cardImage);

                    // Set size and other properties of the ImageView as needed
                    cardImageView.setFitWidth(100); // Example
                    cardImageView.setFitHeight(150); // Example
                    cardImageView.setPreserveRatio(true);

                    cardGridPane.add(cardImageView, col, row); // Add to the grid
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }
}