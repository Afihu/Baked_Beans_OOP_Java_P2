/*author: Le Binh Thanh
  code purpose: control GUI interactions
 * */

package application;

import java.awt.Button;
import java.awt.Event;
import java.awt.MediaTracker;
import java.io.File;
//import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.nio.file.attribute.PosixFileAttributes;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class Controller implements Initializable{
	
	public static String currentScreen;
	private Stage stage;
	private Scene scene;
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
//	private Parent root;
	
	//Setup button clicking sounds
	private Media buttonClickSound;
	private MediaPlayer buttonMediaPlayer;

	@FXML
	private Slider volumeSlider;
    @FXML
    private Label volumeLabel;
	
	
	//Initialize background music
	private File directory;
	private File[] files;
	private Media media;
	public static MediaPlayer mediaplayer;
	private ArrayList<File> songs;
	
	//Initialize Card information
	private Image cardImage;
	private ImageView cardbackGroundImageView;
	private GridPane cardGridPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		buttonClickSound = new Media(new File("res/sounds/button-click-sound.mp3").toURI().toString());
		buttonMediaPlayer = new MediaPlayer(buttonClickSound);
		
//		//Initialize card image
//		cardImage = new Image(new File("res/images/AppBackground.jpg").toURI().toString());
//		if(cardImage != null) {
//			GridPane cardGridPane = new GridPane();
//			cardGridPane.setPrefSize(cardImage.getWidth() / 3, cardImage.getHeight() / 3);
//			cardbackGroundImageView = new ImageView(cardImage);
//			
//			// Define grid size (example: 5x5)
//	        int rows = 5;
//	        int cols = 5;
//
//	        // Create and add grid cells (example: using Rectangles)
//	        for (int row = 0; row < rows; row++) {
//	            for (int col = 0; col < cols; col++) {
//	                Rectangle cell = new Rectangle(cardImage.getWidth() / cols, cardImage.getHeight() / rows);
//	                cell.setFill(Color.TRANSPARENT); 
//	                cell.setStroke(Color.LIGHTGRAY); 
//	                // Add event handlers to the cell here
//	                cardGridPane.add(cell, row, col);
//	            }
//	        }
//		} else System.out.println("invalid");
		
		if(!isPlaying(mediaplayer)) {
			songs = new ArrayList<File>();
			directory = new File("res/music");
			files = directory.listFiles();
			
			if(files != null) {
					System.out.println("Music ready");
					for(File file : files) songs.add(file);
			} else System.out.println("null music");
			
			media = new Media(songs.get(0).toURI().toString());
			mediaplayer = new MediaPlayer(media);
		}
		if (volumeSlider != null) {
            volumeSlider.setValue(mediaplayer.getVolume() * 100); // Set initial value in percentage
            volumeLabel.setText(String.format("%.0f%%", volumeSlider.getValue())); // Set initial label text
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                handleVolumeChange();
            });
        }
	}
	
//	public void playTest() {
//		buttonMediaPlayer.setVolume(1.0);
//		buttonMediaPlayer.play();
//	}

	public static void setVolume(double volume) {
		if (mediaplayer != null) {
			mediaplayer.setVolume(volume);
			// System.out.println("Volume set to: " + volume);
		} else {
			System.out.println("MediaPlayer is not initialized.");
		}
	}

	@FXML
	public void handleVolumeChange() {
		double volume = Math.round(volumeSlider.getValue());
		volumeSlider.setValue(volume); // Set the slider to the rounded value
		setVolume(volume / 100); // Set volume in MediaPlayer (0.0 to 1.0)
		volumeLabel.setText(String.format("%.0f%%", volume)); // Update the label text
	}

    public void AudioSettingsPage(ActionEvent e) throws IOException {
        currentScreen = "AudioSettings.fxml";
        storeCurrentScreen(currentScreen);
        System.out.println("Navigating to AudioSettings.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("/application/AudioSettings.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(cssString);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        System.out.println("Audio Settings Page Opened");
    }

	public static boolean isPlaying(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            return mediaPlayer.getStatus() == Status.PLAYING; 
        }
        return false; 
    }
	
	public static void playMedia() {
		mediaplayer.play();
	}
	
	// Stack to store the previous pages
	private static Stack<String> screenHistory = new Stack<>();
	
	public static void storeCurrentScreen(String currentScreen) {
        screenHistory.push(currentScreen);
		// System.out.println("Stored current screen: " + currentScreen);
		// System.out.println("Current History: " + screenHistory);
		
	}

	public void goBack(ActionEvent e) throws IOException {
		if (!screenHistory.isEmpty()) {
			screenHistory.pop();
			String previousScreen = screenHistory.peek(); 
			if (previousScreen == "StartScreen.fxml"){
				screenHistory.pop();
				storeCurrentScreen(previousScreen);
			}
			System.out.println("Attempting to go back to: " + previousScreen);
			Parent root = FXMLLoader.load(getClass().getResource("/application/" + previousScreen));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(cssString);
			stage.setScene(scene);
			stage.show();
			System.out.println("Navigated back to: " + previousScreen);
        } else {
            System.out.println("Screen history is empty. Cannot go back.");
		}
	}
	
	
	public void QuitGame(ActionEvent e) {
		Platform.exit();
		System.exit(0);
		System.out.println("Stopped");
	}
	
	public void StartGame(ActionEvent e) throws IOException {
		currentScreen = "GameSetup.fxml";
		storeCurrentScreen(currentScreen);
		Parent root = FXMLLoader.load(getClass().getResource("/application/GameSetup.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Commenced Gamesetup");
	}
	

	public void SettingsPage(ActionEvent e) throws IOException {
		currentScreen = "Settings.fxml";
		storeCurrentScreen(currentScreen);
		System.out.println("Navigating to Settings.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("/application/Settings.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Settings Page Opened");
	}
	
	public void MultiConfigScreen(ActionEvent e) throws IOException{
		currentScreen = "MultiConfig.fxml";
		storeCurrentScreen(currentScreen);
		System.out.println("Navigating to MultiConfig.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("/application/MultiConfig.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Multiplayer Configurations Opened");
	}
	
	public void DifficultySetting(ActionEvent e) throws IOException{
		currentScreen = "DifficultyScene.fxml";
		storeCurrentScreen(currentScreen);
		System.out.println("Navigating to DifficultyScene.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("/application/DifficultyScene.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Choose Difficulty");
	}


	@FXML
	public AnchorPane ogPane;
	
	//author:??? - modified by Huynh Thien Bao
	@FXML
	public void SinglePlayer(ActionEvent e) throws IOException{
		//Add current screen to stack
		currentScreen = "GameScreen.fxml";
		storeCurrentScreen(currentScreen);
		
		//Since gameplay has already started, there will not be a return button readily available
		//Man i do not know how i can make a window of difficulty selection for singleplayer but no return button and already start the game
		System.out.println("Game Started");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/GameScreen.fxml"));
		loader.setController(this);		//Must be done so that the current instance of FXMLLoader can be used, the varible under the @FXML tag won't be recognized
		Parent root = loader.load();
		
		cardImage = new Image(new File("res/images/Background.jpg").toURI().toString());
		cardbackGroundImageView = new ImageView(cardImage);
		cardbackGroundImageView.setPreserveRatio(true);

		
		Group testCard = new Group();
		testCard.getChildren().add(cardbackGroundImageView);
		testCard.setScaleX(0.5);
		testCard.setScaleY(0.5);
		testCard.setLayoutX(800);
		
		ogPane.getChildren().add(testCard);

		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Singleplayer mode initiated");
	}
	
}



//StackPane StackPaneroot = new StackPane();

//cardbackGroundImageView.setFitWidth(200); 
//cardbackGroundImageView.setFitHeight(150);

//StackPaneroot.setPadding(new Insets(0, 0, 0, 0));
//StackPaneroot.setAlignment(Pos.CENTER_RIGHT);
//StackPaneroot.getChildren().add(cardbackGroundImageView);














