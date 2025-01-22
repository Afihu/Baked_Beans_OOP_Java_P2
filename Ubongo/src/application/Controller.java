/*author: Le Binh Thanh
  code purpose: control GUI interactions
 * */

package application;

import java.awt.MediaTracker;
import java.io.File;
//import java.awt.Event;
import java.io.IOException;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

public class Controller implements Initializable{
	
	public static String currentScreen;
	private Stage stage;
	private Scene scene;
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
//	private Parent root;
	
	//Initialize background music
	private File directory;
	private File[] files;
	private Media media;
	public static MediaPlayer mediaplayer;
	private ArrayList<File> songs;
	private int songNumber;
	private boolean running;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
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
		System.out.println("Stored current screen: " + currentScreen);
	}

	public void goBack(ActionEvent e) throws IOException {
		if (!screenHistory.isEmpty()) {
			String previousScreen = screenHistory.pop();
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
		storeCurrentScreen(currentScreen);
		currentScreen = "GameSetup.fxml";
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
		storeCurrentScreen(currentScreen);
		currentScreen = "Settings.fxml";
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
		storeCurrentScreen(currentScreen);
		currentScreen = "MultiConfig.fxml";
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
	
}
