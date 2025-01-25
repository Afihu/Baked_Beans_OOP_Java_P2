package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


//Not used for anything yet
public class Card{
	private Image cardBackgroundImage;
	private ImageView cardBackgroundImageView;
	private GridPane cardGridPane;
	
	public Card(ImageView cardimageView, GridPane cardGrid) {
		this.cardBackgroundImageView = cardimageView;
		this.cardGridPane = cardGrid;
	}
	
}
