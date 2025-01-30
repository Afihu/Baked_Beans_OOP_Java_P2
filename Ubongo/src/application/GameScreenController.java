package application; // Make sure this is the correct package

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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
    
    private long startTime;
    private boolean roundStarted = false;
    
    //for the system
    private String receivedColor;
    private String receivedDifficulty;
    private int receivedRoundDuration;
    private double totalScore = 0.0;
    private int roundsPlayed = 0;
    private final int totalRounds = 8;		//Interchangeable for MP
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	finalResultLabel.setVisible(false);	// Hide the final result label initially
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
            startTime = System.currentTimeMillis();
            this.roundStarted = true;
            startStopButton.setText("Stop");
            
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
            scoreLabel.setText("Current total: " + formatToTwoDecimal(this.totalScore) + " out of " + calculateScore(0.0) * this.roundsPlayed);
            startStopButton.setText("Start");
            
            //print out to the system for debugging
            System.out.println("Round ends!");
            System.out.println("Current total: " + formatToTwoDecimal(this.totalScore) + " out of " + calculateScore(0.0) * this.roundsPlayed);
            
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

        // Show final result
        finalResultLabel.setText(String.format("Game Finished!\nTotal score: %.2f", this.totalScore) + " out of " + calculateScore(0.0) * this.roundsPlayed);
        finalResultLabel.setVisible(true);
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