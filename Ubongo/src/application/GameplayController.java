package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.MultiConfigController.PlayerChoice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.collections.ObservableList;

public class GameplayController extends Controller{
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
	private DraggableMaker draggableMaker = new DraggableMaker();
	
	private ObservableList<PlayerChoice> receivedPlayerChoicesList;
	
	@FXML private ImageView testPiece;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	// Hide the labels initially
    	
    	
        // Initialization logic if needed (e.g., setting up the gridPane)
    }

    public void initializeData(ObservableList<PlayerChoice> playerChoiceList) {
        this.receivedPlayerChoicesList = playerChoiceList;
        System.out.println("Initialization complete. PlayerChoiceList received.");
        
//        // Update labels with the received values
//        colorLabel.setText("Color: " + this.receivedColor);
//        difficultyLabel.setText("Difficulty: " + this.receivedDifficulty);
//        roundDurationLabel.setText("Round Duration: " + this.receivedRoundDuration + " seconds");
//        
//        System.out.println("Color: " + this.receivedColor + ", Difficulty: " + this.receivedDifficulty + ", Round Duration: " + this.receivedRoundDuration);

        //loadCards();
    }
    
    @FXML
    public void goBacktoMainMenu(ActionEvent event) throws IOException {
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
    
    //
	public void initSinglePlayer(ActionEvent e) throws IOException{
		//Since gameplay has already started, there will not be a return button readily available
		
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/GameScreen.fxml"));
//		loader.setController(this);		//Must be done so that the current instance of FXMLLoader can be used, otherwise the varible under the @FXML tag won't be recognized
		
		System.out.println("TestPiece: " + testPiece);
		//Make piece draggable
//		draggableMaker.makeDraggable(testPiece);
		
//		System.out.println("Game Started");
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/GameScreen.fxml"));
//		loader.setController(this);		//Must be done so that the current instance of FXMLLoader can be used, otherwise the varible under the @FXML tag won't be recognized
//		Parent root = loader.load();
////		Parent root = FXMLLoader.load(getClass().getResource("/application/DifficultyScene.fxml"));
//
//		
//		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		scene.getStylesheets().add(cssString);
//		stage.setScene(scene);
//		stage.setResizable(false);
//		stage.show();
//		System.out.println("Singleplayer mode initiated");
	}
	
	
}
	

