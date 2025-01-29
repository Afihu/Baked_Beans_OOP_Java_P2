//author: Huynh Thien Bao
package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.collections.ObservableList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable; 
import java.net.URL;

public class SingleConfigController implements Initializable {
	public static String currentScreen;
	
    @FXML
    public ComboBox<String> colorComboBox; // Make sure it's public or has a getter
    @FXML
    public ComboBox<String> difficultyComboBox; // Make sure it's public or has a getter
    @FXML
    private Button beginButton;

    private String selectedColor;
    private String selectedDifficulty;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Color ComboBox
        ObservableList<String> colorOptions = FXCollections.observableArrayList(
            "blue",
            "yellow",
            "green",
            "pink"
        );
        colorComboBox.setItems(colorOptions);
        // Set a default value (optional but recommended)
        //colorComboBox.setValue("blue"); // Or the first item in the list

        // Difficulty ComboBox
        ObservableList<String> difficultyOptions = FXCollections.observableArrayList(
            "easy",
            "hard"
        );
        difficultyComboBox.setItems(difficultyOptions);
        //difficultyComboBox.setValue("easy"); // set default value
    }
    
    //original bt DVTB - mod by HTB
    @FXML
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
			stage.setResizable(false);
			stage.show();
			System.out.println("Navigated back to: " + previousScreen);
        } else {
            System.out.println("Screen history is empty. Cannot go back.");
		}
	}
    
    
    @FXML
    private void handleBeginButtonAction(ActionEvent event) {
        selectedColor = colorComboBox.getValue();
        selectedDifficulty = difficultyComboBox.getValue();

        if (selectedColor == null || selectedDifficulty == null) {
            // Handle case where user didn't select both
            System.out.println("Please select both color and difficulty.");
            return; // Or show an alert
        }

        loadGameScreen(event);
    }

    private void loadGameScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/SPGameScreen.fxml")); // Correct path
            Parent root = loader.load();

            //GameScreenController gameScreenController = loader.getController();
            //gameScreenController.initializeData(selectedColor, selectedDifficulty);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            System.out.println("SPGameScreen Opened");

        } catch (IOException e) {
            e.printStackTrace();
        }  	
    }
}