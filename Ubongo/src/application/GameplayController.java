package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GameplayController extends Controller{
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
	private DraggableMaker draggableMaker = new DraggableMaker();
	@FXML private ImageView testPiece;
	
	
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
	

