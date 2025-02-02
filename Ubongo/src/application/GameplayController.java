package application;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.rmi.server.LoaderHandler;
import java.util.ResourceBundle;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GameplayController implements Initializable{
	//Initialize background music
	private File directory;
	private File[] files;
	private Media media;
	public static MediaPlayer mediaplayer;
	private ArrayList<File> songs;
	public JSONObject config;
	
	//FXML injections
	@FXML private Group asset;
	@FXML private GridPane cardGridPane;
	@FXML private AnchorPane container;
	
	public List<ImageView> pieceViews = new ArrayList<ImageView>();
	
	public static void checkPlayerStatus(MediaPlayer mediaPlayer) {
		mediaPlayer.statusProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue == MediaPlayer.Status.PLAYING) {
	            System.out.println("Music just started playing.");
	        } else if (newValue == MediaPlayer.Status.STOPPED || newValue == MediaPlayer.Status.DISPOSED) {
	            System.out.println("Music has stopped.");
	            // Perform actions when music stops, like updating UI, etc.
	        }
	    });
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		// TODO Auto-generated method stub
		System.out.println("Game Started!");
		
		//Music:
//		if(!Controller.isPlaying(mediaplayer)) {
//			songs = new ArrayList<File>();
//			directory = new File("res/music");
//			files = directory.listFiles();
//			
//			if(files != null) {
//					System.out.println("Music ready");
//					for(File file : files) songs.add(file);
//			} else System.out.println("null music");
//			
//			media = new Media(songs.get(0).toURI().toString());
//			mediaplayer = new MediaPlayer(media);
//		}

	}
	
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
	
	public void initSinglePlayer(ActionEvent event) throws IOException{
		//Since gameplay has already started, there will not be a return button readily available
		//load json file:
		config = DraggableMaker.loadJSON();
		DraggableMaker.checkJsonStatus(config);
		
		
		//surrounds with for loop for singleplayer
		String currentFile = "A1.fxml"; // change as neccessary  
		FXMLLoader assetFxmlLoader = new FXMLLoader(new File("res/assests/A/A1.fxml").toURI().toURL()); //change every for loop
		FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("/application/TestSPGameScreen.fxml"));
		assetFxmlLoader.setController(this);
		mainSceneLoader.setController(this);
		Parent assetRoot = assetFxmlLoader.load(); //for accessing assets
		Parent mainSceneRoot = mainSceneLoader.load();
		//Access FXML injected variables below this line
		
		//extract imageViews from assets
		ArrayList<Double> sizeArrayList = new ArrayList<Double>();
		for(Node node : asset.getChildren()) {
			if(node instanceof ImageView) {
				pieceViews.add(((ImageView)node));
				sizeArrayList.add(((ImageView)node).getFitHeight());
			}
		}
		double maxSize = Collections.max(sizeArrayList);
		pieceViews.remove(sizeArrayList.indexOf(maxSize));
//		for(double i : sizeArrayList) System.out.println(i);

		//store piece directories as String
		System.out.println("piece directories: ");
		for(ImageView piece : pieceViews) {
			int currentPieceIndex = pieceViews.indexOf(piece);
			String currentPieceDir = DraggableMaker.jsonProcessor(config, currentFile, currentPieceIndex);
//			System.out.println(piece);
//			System.out.println(currentPieceIndex);
			System.out.println(currentPieceDir);
		}
		
		//Add group to main scene
		container.getChildren().add(asset);
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(mainSceneRoot);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		System.out.println("Singleplayer mode initiated");
	}
	
}
	















