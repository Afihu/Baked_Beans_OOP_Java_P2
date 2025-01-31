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
    
    
	//For drag and drop functionality
	@FXML private ImageView testPiece;
	@FXML private Rectangle testCell;
	@FXML private GridPane cardGrid;
	DraggableMaker draggableMaker = new DraggableMaker();
	private Coords[][] pieceMask;
	private boolean[][] gridMask;
	private Coords[][] gridCellCoords;
	
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
			
			//Generate piece mask and grid mask
			pieceMask = InitCoreMechanics.generatePieceHitBox(pieceImage, testCell);
			
			//Take the map of the gridPane to find cell positions
			gridMask = GridHandler.makeGridPaneMap(cardGrid);
			
			//Use gridMask to find cell coordinates
			gridCellCoords = GridHandler.takeGridCoords(cardGrid, gridMask);
			
//			//debug gridMap
//			for(int i = 0; i < cardGrid.getRowCount(); i++) {
//				System.out.println();
//				for(int j = 0; j < cardGrid.getColumnCount(); j++) {
//					System.out.print(gridMask[i][j] + " ");
//				}
//			}
			
			//print testing for debugging
			for(int i = 0; i < 2; i++) {
				System.out.println();
				for(int j = 0; j < 3; j++) {
					System.out.print(pieceMask[i][j].x + " " + pieceMask[i][j].y + " ");
				}
			}
			
			//debug gridCellCoordinates
			System.out.println();
			System.out.println("debug gridCellCoords: ");
			System.out.println("grid cells' coordinates: ");
			for(int i = 0; i < cardGrid.getRowCount(); i++) {
				System.out.println();
				for(int j = 0; j < cardGrid.getColumnCount(); j++) {
					System.out.print(" (" + gridCellCoords[i][j].x + ", " + gridCellCoords[i][j].y + ") ");
				}
			}
		
			
			testPiece.setFitHeight(pieceImage.getHeight() - 20); testPiece.setFitWidth(pieceImage.getWidth() - 20); //account for image size and imageView size inconsistency
			testPiece.setPreserveRatio(true);
			testPiece.setOpacity(1.0);
			testPiece.setImage(pieceImage);
			
			//apply hover effect (incomplete)
			draggableMaker.makeHoverable(testPiece, pieceImage, cardGrid, testCell, gridMask);
			
			
			
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