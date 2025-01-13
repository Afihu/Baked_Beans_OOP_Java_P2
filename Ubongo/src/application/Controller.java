package application;

//import java.awt.Event;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
	
	private Stage stage;
	private Scene scene;
	private String cssString = this.getClass().getResource("application.css").toExternalForm();
//	private Parent root;
	
	public void QuitGame(ActionEvent e) {
		Platform.exit();
		System.exit(0);
		System.out.println("Stopped");
	}
	
	public void StartGame(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("GameSetup.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.show();
		System.out.println("Commenced Gamesetup");
	}
	
	public void SettingsPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(cssString);
		stage.setScene(scene);
		stage.show();
		System.out.println("Settings Page Opened");
	}
	
}
