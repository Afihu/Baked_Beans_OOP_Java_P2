package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import application.MultiConfigController.PlayerChoice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.collections.ObservableList;

public class GameplayController extends Controller{
	private String cssString = this.getClass().getResource("/application/application.css").toExternalForm();
	private DraggableMaker draggableMaker = new DraggableMaker();
	
	private ObservableList<PlayerChoice> receivedPlayerChoicesList;
	
    @FXML private AnchorPane rootPane; // Injected from MPGameScreen.fxml
	@FXML private ImageView testPiece;

//    @FXML
//    private GridPane cardGridPane; // Assuming you have a GridPane in GameScreen.fxml
    
    //for testing scoring system
    @FXML
    private Button startStopButton;
//    @FXML
//    private Label colorLabel;
//    @FXML
//    private Label difficultyLabel;
//    @FXML
//    private Label roundDurationLabel;
//    @FXML
//    private Label scoreLabel;
    @FXML
    private Label finalResultLabel;
    @FXML
    private Label roundResultLabel;
    
    private long startTime;
    private boolean roundStarted = false;
    public List<ImageView> pieceViews = new ArrayList<ImageView>();
    //for the system
    @FXML
    private Button exitToMainScreenButton;
    @FXML
    private Button exitToMPConfigScreen;
    @FXML
    private Label timerLabel; 
    @FXML
    private Label roundCounterLabel; 
    @FXML
    private Label player1ScoreLabel; 
    @FXML
    private Label player2ScoreLabel; 
    @FXML
    private Label player3ScoreLabel; 
    @FXML
    private Label player4ScoreLabel; 
    
//    private String receivedColor;
//    private String receivedDifficulty;
    private int receivedRoundDuration;
    
    private int totalPlayers;
    private double[] playerScores;
    private int currentPlayerIndex = 0;
    private int roundsPlayed = 0;
    private int totalRounds = 5;		//Interchangeable for MP
    
    private Timeline countdownTimer;
    private int remainingTime;

    // Replace cardContainer with a list of queues for AnchorPanes
    private List<Queue<AnchorPane>> playerCardQueues;
    
    
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	// Hide the labels initially
		finalResultLabel.setVisible(false);	
    	roundResultLabel.setVisible(false);
    	exitToMainScreenButton.setVisible(false);
    	timerLabel.setVisible(false);
    	roundCounterLabel.setVisible(false);
        // ...other initialization...
    }

    public void initializeData(ObservableList<PlayerChoice> playerChoiceList, int gameDuration ) {
        this.receivedPlayerChoicesList = playerChoiceList;
        this.receivedRoundDuration = gameDuration;
        this.totalPlayers = playerChoiceList.size();
        playerScores = new double[totalPlayers];
        
        // Initialize a card queue for each player.
        playerCardQueues = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++) {
            playerCardQueues.add(new ArrayDeque<>());
        }

        System.out.println("Initialization complete. PlayerChoiceList received.");

        //  Call loadGameCards after the screen is loaded.
         try {
            loadGameCards();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
//        // Update labels with the received values
//        colorLabel.setText("Color: " + this.receivedColor);
//        difficultyLabel.setText("Difficulty: " + this.receivedDifficulty);
//        roundDurationLabel.setText("Round Duration: " + this.receivedRoundDuration + " seconds");
//        
//        System.out.println("Color: " + this.receivedColor + ", Difficulty: " + this.receivedDifficulty + ", Round Duration: " + this.receivedRoundDuration);

        //loadCards();
    }

    //Method to load config.json from resources.
    private JSONObject loadConfigJson() throws IOException {
        InputStream is = getClass().getResourceAsStream("/application/config.json");
        if(is == null) {
            throw new FileNotFoundException("config.json not found in /application folder.");
        }
        String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        return new JSONObject(jsonText);
    }

//     @FXML
// private void loadTestPiece() {
//     try {
//         // Create ImageView for test piece
//         ImageView pieceView = new ImageView();
        
//         // Load image
//         String piecePath = "res/pieces/green/G_Giraffee/00_G_Giraffee.png";
//         Image pieceImage = new Image(new File(piecePath).toURI().toString());
//         pieceView.setImage(pieceImage);
        
//         // Set size
//         pieceView.setFitHeight(225.0);
//         pieceView.setFitWidth(225.0);
//         pieceView.setPreserveRatio(true);
        
//         // Set position
//         pieceView.setLayoutX(500);
//         pieceView.setLayoutY(200);
        
//         // Make piece visible
//         pieceView.setVisible(true);
//         pieceView.setOpacity(1.0);
        
//         // Make draggable
//         draggableMaker.makeDraggable(pieceView);
        
//         // Add to root pane
//         rootPane.getChildren().add(pieceView);
        
//         System.out.println("Test piece loaded successfully");
        
//     } catch (Exception e) {
//         System.out.println("Error loading test piece: " + e.getMessage());
//         e.printStackTrace();
//     }
// }

    // Change data structure
    private List<List<String>> playerCardNames; // Store FXML names instead of AnchorPanes
    
    //Choose cards for players
    private void loadGameCards() throws IOException {
        // Initialize card lists
        playerCardNames = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++) {
            playerCardNames.add(new ArrayList<>());
        }

        // Load config once
        JSONObject config = DraggableMaker.loadJSON();
        DraggableMaker.checkJsonStatus(config);
        
        // For each player
        for (int playerIndex = 0; playerIndex < receivedPlayerChoicesList.size(); playerIndex++) {
            PlayerChoice choice = receivedPlayerChoicesList.get(playerIndex);
            List<String> playerCards = playerCardNames.get(playerIndex);
            
            // Determine card range
            String folder = choice.getDifficulty().equalsIgnoreCase("easy") ? "A" : "B";
            int startNum, endNum;
            switch (choice.getColour().toLowerCase()) {
                case "blue": startNum = 1; endNum = 8; break;
                case "pink": startNum = 9; endNum = 16; break;
                case "green": startNum = 17; endNum = 24; break;
                case "yellow": startNum = 25; endNum = 32; break;
                default: startNum = 1; endNum = 8;
            }
            
            // Select 5 random cards
            List<Integer> cardNumbers = new ArrayList<>();
            for (int i = startNum; i <= endNum; i++) {
                cardNumbers.add(i);
            }
            Collections.shuffle(cardNumbers);
            
            // Store FXML names for first 5 cards
            for (int i = 0; i < 5; i++) {
                String fxmlName = folder + cardNumbers.get(i) + ".fxml";
                playerCards.add(fxmlName);
                System.out.println("Added card " + fxmlName + " for player " + playerIndex);
            }
        }
        
        // Show first player's first card
        showCurrentPlayerCard();
    }

private void showCurrentPlayerCard() {
    try {
        // Clear existing card
        rootPane.getChildren().removeIf(node -> 
            node instanceof AnchorPane && !node.equals(rootPane));
            pieceViews.clear();

        // Get current card name
        String currentFxml = playerCardNames.get(currentPlayerIndex).get(0);
        String folder = currentFxml.startsWith("A") ? "A" : "B";
        
        // Load FXML
        FXMLLoader loader = new FXMLLoader(new File("res/assests/" + folder + "/" + currentFxml).toURI().toURL());
        loader.setController(this);
        AnchorPane cardPane = loader.load();
        

//        for(Node node : cardPane.getChildren()) {
//            if(node instanceof ImageView) {
//                pieceViews.add(((ImageView)node));
//                System.out.println(node);
//                rootPane.getChildren().add(node);
//            }
//        }
//        
        

        //extract imageViews from assets
        ArrayList<Double> sizeArrayList = new ArrayList<Double>();
        for(Node node : cardPane.getChildren()) {
            if(node instanceof ImageView) {
                pieceViews.add(((ImageView)node));
                sizeArrayList.add(((ImageView)node).getFitHeight());
            }
        }
        double maxSize = Collections.max(sizeArrayList);
        pieceViews.remove(sizeArrayList.indexOf(maxSize));
        //		for(double i : sizeArrayList) System.out.println(i);

        GridPane cardGrid = (GridPane) cardPane.lookup("#cardGridPane");
        // Update piece images with "00_" versions
        JSONObject config = DraggableMaker.loadJSON();
        for (int j = 0; j < pieceViews.size(); j++) {
            String piecePath = DraggableMaker.jsonProcessor(config, currentFxml, j);
            File piecesDirFiles = new File(piecePath);
            File[] files = piecesDirFiles.listFiles();
            ImageView currentPiece = pieceViews.get(j);
            Image currentImage = new Image(new File(files[0].getAbsolutePath()).toURI().toString());
            currentPiece.setImage(currentImage);
            System.out.println("Loading image from: " + files[0].getAbsolutePath());
            draggableMaker.makeHoverable(currentPiece, cardGrid);
            rootPane.getChildren().add(currentPiece);
        }
        rootPane.getChildren().add((cardPane).getChildren().getFirst()); 
        rootPane.getChildren().add(cardGrid); 
        
    } catch (Exception e) {
        System.out.println("Error loading card: " + e.getMessage());
        e.printStackTrace();
    }
}    
    // Update showNextCardForPlayer method
    public void showNextCardForPlayer(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerCardQueues.size()) return;
        
        Queue<AnchorPane> queue = playerCardQueues.get(playerIndex);
        if (!queue.isEmpty()) {
            queue.poll(); // Remove current card
            if (playerIndex == currentPlayerIndex) {
                showCurrentPlayerCard();
            }
        }
    }

    private void setupPiecesForCard(AnchorPane cardPane, List<ImageView> pieceViews) throws IOException {
        // Get the GridPane from card
        GridPane cardGrid = (GridPane) cardPane.lookup("#cardGridPane");
        System.out.println("Found GridPane: " + (cardGrid != null));

        // Make each piece draggable and hoverable, and ensure visibility
//        for (ImageView pieceView : pieceViews) {
//            try {
//                // Show piece
//                pieceView.setVisible(true);
//                pieceView.setOpacity(1.0);
//                
//                // Make interactive
//                draggableMaker.makeHoverable(pieceView, cardGrid);
//                
//                System.out.println("Piece setup complete: " + pieceView);
//            } catch (Exception e) {
//                System.out.println("Error setting up piece: " + e.getMessage());
//            }
//        }
    }
    
    
    
    private void startCountdown() {
        timerLabel.setText("Time left: " + remainingTime + "s");
        
        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime--;
            timerLabel.setText("Time left: " + remainingTime + "s");

            if (remainingTime <= 0) {
                stopCountdown();
                endRoundTimeOut();
                timerLabel.setText("Time's up! Too bad!");
            }
        }));

        countdownTimer.setCycleCount(receivedRoundDuration); // Run for the full duration
        countdownTimer.play();
    }
    
    private void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }
    
    private void endRoundEarly() {
        long endTime = System.currentTimeMillis();
        double usedTime = (endTime - startTime) / 1000.0; // Convert to seconds
        processRoundEnd(usedTime);
    }
    
    private void endRoundTimeOut() {
        processRoundEnd(receivedRoundDuration); // Player ran out of time
    }

    private void processRoundEnd(double usedTime) {
    	//stop the round and calculate score
        double score = calculateBaseScore(usedTime);
        playerScores[currentPlayerIndex] += score;
        
        // Update the score label for the current player
        updatePlayerScoreLabel(currentPlayerIndex);
        
        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
        
        //modify state of the game
        this.roundStarted = false;
        startStopButton.setText("Start next round!");
        
        //modify state of in-game labels | calculateScore(0.0)*roundsPlayed is the maximum score a player can get
        roundResultLabel.setVisible(true);
        roundResultLabel.setText("Your round ends! \n Score this round: " + score  
        		+ "\n" + String.format("[%.2f / 100]",(score/calculateBaseScore(0.0))*100));
        

        //print out to the system for debugging
        System.out.println("Round ends!");	
        
        //if reached the end of game, ends the game and go back to the lobby
        if (currentPlayerIndex == 0) {
            roundsPlayed++;
            if (roundsPlayed >= totalRounds) {
                endGame();
            }
        }
    }
    
    private void updatePlayerScoreLabel(int playerIndex) {
        switch (playerIndex) {
            case 0:
                player1ScoreLabel.setText("Player 1 Score: " + formatToTwoDecimal(playerScores[playerIndex]));
                break;
            case 1:
                player2ScoreLabel.setText("Player 2 Score: " + formatToTwoDecimal(playerScores[playerIndex]));
                break;
            case 2:
                player3ScoreLabel.setText("Player 3 Score: " + formatToTwoDecimal(playerScores[playerIndex]));
                break;
            case 3:
                player4ScoreLabel.setText("Player 4 Score: " + formatToTwoDecimal(playerScores[playerIndex]));
                break;
        }
    }
    
//    private double getTotalScore() {
//        double total = 0;
//        for (double score : playerScores) {
//            total += score;
//        }
//        return total;
//    }

    
    //for testing scoring system
    @FXML
    private void handleStartStopButtonAction() {
        if (!this.roundStarted) {		//if decide to keep the button, add conditions (ex: !this.roundStarted & cardSolved == True)
            // Start the round
        	roundResultLabel.setVisible(false);
        	
        	timerLabel.setVisible(true);
            startTime = System.currentTimeMillis();
            
            this.roundStarted = true;
            //this.roundsPlayed++;
            
            roundCounterLabel.setVisible(true);
        	roundCounterLabel.setText("Round " + (this.roundsPlayed+1) + "/" + this.totalRounds);
        	
            startStopButton.setText("Ubongo!");
            
            // Start count down
            this.remainingTime = this.receivedRoundDuration;
            startCountdown(); //If player used all the time given, the "else" path will be initiated
            
            //print out to the system for debugging
            System.out.println("Round starts!");
        } else {
        	// Player pressed the button before timeout
            stopCountdown();
            endRoundEarly();
        }
    }
    
    private void endGame() {
        // Hide all labels and buttons
        //cardGridPane.setVisible(false);
        startStopButton.setVisible(false);
        roundResultLabel.setVisible(false);
        exitToMPConfigScreen.setVisible(false);
        timerLabel.setVisible(false);
    	roundCounterLabel.setVisible(false);

        // Show final result
//        finalResultLabel.setText(String.format("Game Finished!\nTotal score: %.2f", this.totalScore) + "/" + calculateBaseScore(0.0) * this.roundsPlayed
//        		+ "\n" + String.format("[%.2f / 100]",(this.totalScore/(calculateBaseScore(0.0) * this.roundsPlayed))*100));
//        	
    	finalResultLabel.setText("Game done!");
        finalResultLabel.setVisible(true);
        exitToMainScreenButton.setVisible(true);
    }
    
    //convert standard time format to usable time format for displaying the scoring system
    public String formatToTwoDecimal(double number) {
        return String.format("%.2f", number);
    }
    
    public double calculateBaseScore(double usedTime) {
        double remainingTime = this.receivedRoundDuration - usedTime;
        double score;
        if (usedTime >= this.receivedRoundDuration) {
            score = 0;
        } else if (usedTime <= 0.25 * this.receivedRoundDuration) {
        	score = (remainingTime * 3);
        } else if (usedTime <= 0.5 * this.receivedRoundDuration) {
        	score = (remainingTime * 2);
        } else if (usedTime <= 0.75 * this.receivedRoundDuration) {
        	score = (remainingTime * 1);
        } else {
        	score = remainingTime;
        }
        
        BigDecimal bd = new BigDecimal(score).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
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
			stage.show();
			System.out.println("Navigated back to: " + previousScreen);
        } else {
            System.out.println("Screen history is empty. Cannot go back.");
		}
	}
    
    @FXML
    public void goBacktoMainScreen(ActionEvent event) throws IOException {
//        // Clear screen history
//        Controller.screenHistory.clear();
//        
//        // Load StartScreen
//        Parent root = FXMLLoader.load(getClass().getResource("/application/StartScreen.fxml"));
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
//        System.out.println("Returned to Main Menu");
    	
    	Controller.screenHistory.clear();
        
    	Controller.storeCurrentScreen("StartScreen.fxml");
        	
        System.out.println("Attempting to go back to: StartScreen.fxml");
		Parent root = FXMLLoader.load(getClass().getResource("/application/" + "StartScreen.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		System.out.println("Returned to Main Menu");
    }
    
    //
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
	

