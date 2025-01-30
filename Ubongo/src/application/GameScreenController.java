package application; // Make sure this is the correct package

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {

    @FXML
    private GridPane cardGridPane; // Assuming you have a GridPane in GameScreen.fxml

    private String receivedColor;
    private String receivedDifficulty;
    private int receivedRoundDuration;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed (e.g., setting up the gridPane)
    }

    public void initializeData(String color, String difficulty, int roundDuration) {
        this.receivedColor = color;
        this.receivedDifficulty = difficulty;
        this.receivedRoundDuration = roundDuration;

        System.out.println("Color: " + this.receivedColor + ", Difficulty: " + this.receivedDifficulty + ", Round Duration: " + this.receivedRoundDuration);

        //loadCards();
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