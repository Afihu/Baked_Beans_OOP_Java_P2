package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerChoice {
    private final IntegerProperty playerNumber;
    private final StringProperty colour;
    private final StringProperty difficulty;
    
    public PlayerChoice(int playerNumber, String colour, String difficulty) {
        this.playerNumber = new SimpleIntegerProperty(playerNumber);
        this.colour = new SimpleStringProperty(colour);
        this.difficulty = new SimpleStringProperty(difficulty);
    }
    
    public int getPlayerNumber() { return playerNumber.get(); }
    public String getColour() { return colour.get(); }
    public String getDifficulty() { return difficulty.get(); }
    
    public IntegerProperty playerNumberProperty() { return playerNumber; }
    public StringProperty colourProperty() { return colour; }
    public StringProperty difficultyProperty() { return difficulty; }
}