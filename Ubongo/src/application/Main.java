package application;
	
import java.awt.*;

//import javafx.scene.image.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Ubongo");
			
			Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
			Scene scene = new Scene(root, 1280, 720);
			String cssString = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(cssString);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
