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
    
    private String receivedColor;
    private String receivedDifficulty;
    private int receivedRoundDuration;
    private double totalScore = 0.0;
    private int roundsPlayed = 0;
    private final int totalRounds = 8;		//Interchangeable for MP
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	// Hide the labels initially
    	finalResultLabel.setVisible(false);	
    	roundResultLabel.setVisible(false);
    	exitToMainScreenButton.setVisible(false);
    	
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

        //loadCards();
    }
    
    //for testing scoring system
    @FXML
    private void handleStartStopButtonAction() {
        if (!this.roundStarted) {		//if decide to keep the button, add conditions (ex: !this.roundStarted & cardSolved == True)
            // Start the round
        	roundResultLabel.setVisible(false);
            startTime = System.currentTimeMillis();
            this.roundStarted = true;
            startStopButton.setText("Ubongo!");
            
            //print out to the system for debugging
            System.out.println("Round starts!");
        } else {
            // Stop the round and calculate score
            long endTime = System.currentTimeMillis();
            double usedTime = (endTime - startTime) / 1000.0; // Convert to seconds
            double score = calculateScore(usedTime);
            this.totalScore += score;
            
            //modify state of the game
            this.roundStarted = false;
            this.roundsPlayed++;
            
            //modify state of in-game labels | calculateScore(0.0)*roundsPlayed is the maximum score a player can get
            roundResultLabel.setVisible(true);
            roundResultLabel.setText("Well done!\n Score this round: " + score  
            		+ "\n" + String.format("[%.2f / 100]",(score/calculateScore(0.0))*100));
            
            scoreLabel.setText("Score: " + formatToTwoDecimal(this.totalScore) + "/" + calculateScore(0.0) * this.roundsPlayed);
            startStopButton.setText("Start!");
            
            //print out to the system for debugging
            System.out.println("Round ends!");
            System.out.println("Current total: " + formatToTwoDecimal(this.totalScore) + "/" + calculateScore(0.0) * this.roundsPlayed);
            
            //if reached the end of game, ends the game and go back to the lobby
            if (roundsPlayed >= totalRounds) {
                this.endGame();
            }
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

        // Show final result
        finalResultLabel.setText(String.format("Game Finished!\nTotal score: %.2f", this.totalScore) + "/" + calculateScore(0.0) * this.roundsPlayed
        		+ "\n" + String.format("[%.2f / 100]",(this.totalScore/(calculateScore(0.0) * this.roundsPlayed))*100));
        	
        finalResultLabel.setVisible(true);
        exitToMainScreenButton.setVisible(true);
    }
    
    //convert standard time format to usable time format for displaying the scoring system
    public String formatToTwoDecimal(double number) {
        return String.format("%.2f", number);
    }
    
    public double calculateScore(double usedTime) {
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
		if (!Controller.screenHistory.isEmpty()) {
			Controller.screenHistory.pop();
        } else {
        	Controller.storeCurrentScreen("StartScreen.fxml");
        	
        	System.out.println("Attempting to go back to: StartScreen.fxml");
			Parent root = FXMLLoader.load(getClass().getResource("/application/" + "StartScreen.fxml"));
			Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
			System.out.println("Navigated back to: " + "StartScreen.fxml");
        	
		}
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