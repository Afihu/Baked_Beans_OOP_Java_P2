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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.awt.datatransfer.SystemFlavorMap;
import java.io.File;
import java.io.IOException;
import javafx.collections.ObservableList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable; 
import java.net.URL;
import java.time.chrono.IsoChronology;

public class SingleConfigController implements Initializable {
	public static String currentScreen;
	
	@FXML
    private Spinner<Integer> timeSpinner; // Spinner for setting the timer
    @FXML
    public ComboBox<String> colorComboBox;
    @FXML
    public ComboBox<String> difficultyComboBox;
    @FXML
    private Button beginButton;
    
    private int selectedRoundDuration;
    private String selectedColor;
    private String selectedDifficulty;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	// Initialize the Spinner with a range from 1 to 600 seconds and a default value of 10
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 600, 60);
        timeSpinner.setValueFactory(valueFactory);
        timeSpinner.setEditable(true);
        
        // Color ComboBox
        ObservableList<String> colorOptions = FXCollections.observableArrayList(
            "blue",
            "yellow",
            "green",
            "pink"
        );
        colorComboBox.setItems(colorOptions);
        //set a default value (optional but recommended)
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
        this.selectedColor = colorComboBox.getValue();
        this.selectedDifficulty = difficultyComboBox.getValue();
        this.selectedRoundDuration = timeSpinner.getValue();
        
        if (selectedColor == null || selectedDifficulty == null) {
            // Handle case where user didn't select both
            System.out.println("Please select both color and difficulty.");
            return; // Or show an alert
        }

        loadGameScreen(event);
    }

    private void loadGameScreen(ActionEvent event) {
        try {
        	currentScreen = "SPGameScreen.fxml";
    		Controller.storeCurrentScreen(currentScreen);
    		
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/SPGameScreen.fxml")); // Correct path
            Parent root = loader.load();

            GameScreenController gameScreenController = loader.getController();
            gameScreenController.initializeData(this.selectedColor, this.selectedDifficulty,this.selectedRoundDuration);

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
    
    
	//For drag and drop functionality
	@FXML private ImageView testPiece;
	@FXML private Rectangle testCell;
	@FXML private GridPane cardGrid;
	DraggableMaker draggableMaker = new DraggableMaker();
	private Coords[][] pieceMask;
	private boolean[][] gridMap;
	private Coords[][] gridCellMask;
	
	private Image pieceImage = new Image(new File("res/pieces/green/G_FireFly.png").toURI().toString()); //Loading image to create mask before attaching to imageView
	
	
	public void testDragDrop(ActionEvent event) throws IOException{
		//Since gameplay has already started, there will not be a return button readily available
		System.out.println("Game Started");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/GameScreen.fxml"));
		loader.setController(this);		//Must be done so that the current instance of FXMLLoader can be used, otherwise the varible under the @FXML tag won't be recognized
		Parent root = loader.load();	//**Extremely important** Everything related to @FXML injected fields must be written below this line
    	
    	Rectangle testNode = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGrid, 0, 0);
    	System.out.println("rectangle width: " + testNode.getWidth());
    	System.out.println("rectangle height: " + testNode.getHeight());
    	System.out.println("actual rectangle width + height: " + testCell.getWidth() + " + " + testCell.getHeight());
		
		if(pieceImage != null && testCell != null && cardGrid != null) {
			
			testPiece.setFitHeight(pieceImage.getHeight() - 20); testPiece.setFitWidth(pieceImage.getWidth() - 20); //account for image size and imageView size inconsistency
			testPiece.setPreserveRatio(true);
			testPiece.setOpacity(1.0);
			testPiece.setImage(pieceImage);
			
			//apply hover effect (incomplete)
			if(testPiece != null && cardGrid != null) {
				draggableMaker.makeHoverable(testPiece, cardGrid);
			} else System.out.println("Error: testPiece or cardGrid null");
			
		} else System.out.println("Error: pieceImage, gridpane or Cell can't load");
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Singleplayer mode initiated");
	}
}