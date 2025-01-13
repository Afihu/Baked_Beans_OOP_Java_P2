package application;
	
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
//			Image image = new Image("../../res/images/Ubongo-icon.jpg");
//			primaryStage.getIcons().add(image);
//			System.out.println(getClass().getResource("/images/Ubongo-icon.jpg"));
			
			Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
			Scene scene = new Scene(root, 600, 339);
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
