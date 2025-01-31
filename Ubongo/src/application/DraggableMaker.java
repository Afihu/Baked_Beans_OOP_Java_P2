//author: Le Binh Thanh
//Purpose: Make pieces draggable on gameplay screen
//Source: https://gist.github.com/Da9el00/74cc0100b67ade75308f3875c2c681cb

package application;

import javafx.scene.paint.Color;
import java.util.List;

import javafx.geometry.Bounds;
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
    
    public void makeHoverable(ImageView pieceView, Image pieceImage, GridPane cardGrid, Rectangle gridCell, boolean[][] gridMask) {
    	
    	pieceView.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
    	
    	pieceView.setOnMouseDragged(mouseEvent -> {
    		
    		pieceView.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
    		pieceView.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    		
    		double pieceX = pieceView.getLayoutX();
    	    double pieceY = pieceView.getLayoutY();
    	    Coords pieceCoords = new Coords(pieceX, pieceY);
    	    
    	    System.out.print("Position: " + pieceX + ", " + pieceY + " ");
    	    Coords[][] pieceMask = InitCoreMechanics.generatePieceHitBoxLive(pieceImage, gridCell,pieceX,pieceY);
    	    
    	    for(int i = 0; i < 2; i++) {
				System.out.println();
				for(int j = 0; j < 3; j++) {
					System.out.print(pieceMask[i][j].x + " " + pieceMask[i][j].y + " ");
				}
			}
    	    
    	    Bounds bounds = gridCell.localToScene(gridCell.getBoundsInLocal());
    	    double gridCellX = bounds.getMinX();
    	    double gridCellY = bounds.getMinY();
    	    System.out.println("Cell position: " + gridCellX + ", " + gridCellY);
    	    
    	    ////////////////////////Test out isWithinCellBounds(unfinished)/////////////////////////////////////////////////////////////////////////////////
    	    ///null error, will fix later
//    	    System.out.println("current cell =" + InitCoreMechanics.isWithinCellBounds(pieceCoords, null, cardGrid, gridCell));
    	    
    	    //test movement:
    	    double pieceULSectionX = pieceX + gridCell.getWidth() / 2;
    	    double pieceULSectionY = pieceY + gridCell.getHeight() / 2;
    	    
    	    double gridCellWidth = gridCell.getWidth();
    	    double gridCellHeight = gridCell.getHeight();
		   
    	    
    	    if((pieceULSectionX > gridCellX && pieceULSectionY > gridCellY)) {
    	    	gridCell.setOpacity(0.5);
    	    	gridCell.setFill(Color.rgb(255, 165, 0));
    	    } else gridCell.setOpacity(0);
		   
		    
		 // Clear previous highlights
//		    InitCoreMechanics.clearHighlights(lastHoveredCells);
		});
    }
}
