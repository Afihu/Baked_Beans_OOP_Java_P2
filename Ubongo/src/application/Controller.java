package application;

//import java.awt.Event;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Stack;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
	
	public static String currentScreen;
	private Stage stage;
	private Scene scene;
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
//	private Parent root;
	
	// Stack to store the previous pages
	private static Stack<String> screenHistory = new Stack<>();

	public void QuitGame(ActionEvent e) {
		Platform.exit();
		System.exit(0);
		System.out.println("Stopped");
	}
	
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
	

	public void StartGame(ActionEvent e) throws IOException {
        storeCurrentScreen(currentScreen);
		currentScreen = "GameSetup.fxml";
		System.out.println("Navigating to GameSetup.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("/application/GameSetup.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(cssString);
        stage.setScene(scene);
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
		stage.show();
		System.out.println("Multiplayer Configurations Opened");
	}
	
}
