//author: Le Binh Thanh
//Purpose: Make pieces draggable on gameplay screen
//Source: https://gist.github.com/Da9el00/74cc0100b67ade75308f3875c2c681cb

package application;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class DraggableMaker {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable(Node node){

        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
        
        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });
    }
    
    public void makeHoverable(ImageView pieceView, Image pieceImage, GridPane cardGrid, int[][] gridMask) {
    	pieceView.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
    	
    	pieceView.setOnMouseDragged(mouseEvent -> {
    		
    		pieceView.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
    		pieceView.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    		
    		double pieceX = mouseEvent.getSceneX() - pieceImage.getWidth() / 2;
    	    double pieceY = mouseEvent.getSceneY() - pieceImage.getHeight() / 2;
		    List<Rectangle> lastHoveredCells;
		    
		    if(!InitCoreMechanics.isWithinGridBounds(pieceX, pieceY, cardGrid)) return;

		 // Get new hovered cells
		    lastHoveredCells = InitCoreMechanics.getHoveredCells(cardGrid, pieceX, pieceY, gridMask);			    			 

		    // Apply hover effect
		    InitCoreMechanics.highlightCells(lastHoveredCells);
		    
		 // Clear previous highlights
//		    InitCoreMechanics.clearHighlights(lastHoveredCells);
		});
    }
}
