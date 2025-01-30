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
    
    private String receivedColor;
    private String receivedDifficulty;
    private int receivedRoundDuration;
    private long startTime;
    private boolean roundStarted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    
    @FXML
    private void handleStartStopButtonAction() {
        if (!this.roundStarted) {
            // Start the round
            startTime = System.currentTimeMillis();
            this.roundStarted = true;
            startStopButton.setText("Stop");
            System.out.println("Round starts!");
        } else {
            // Stop the round and calculate score
            long endTime = System.currentTimeMillis();
            double usedTime = (endTime - startTime) / 1000.0; // Convert to seconds
            double score = calculateScore(usedTime);
            scoreLabel.setText("Score: " + score + " out of " + calculateScore(0.0));
            this.roundStarted = false;
            startStopButton.setText("Start");
            System.out.println("Round ends!");
            System.out.println("Your score: " + score + " out of " + calculateScore(0.0));
        }
    }
    
    //convert standard time format to usable time format for the scoring system
//    public double convertToSeconds(int minutes, double seconds) {
//        return minutes * 60 + seconds;
//    }
    
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