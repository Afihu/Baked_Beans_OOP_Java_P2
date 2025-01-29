package application;


import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InitCoreMechanics {
	
	public static void highlightCells(List<Rectangle> hoveredCells) {
	    for (Rectangle cell : hoveredCells) {
	    	cell.setOpacity(6);
	        cell.setFill(Color.YELLOW);
	    }
	}
	
	public static void clearHighlights(List<Rectangle> lastHoveredCells) {
	    for (Rectangle cell : lastHoveredCells) {
	        cell.setFill(Color.TRANSPARENT); // Reset to original color
	    }
	}
	
	public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
//	    	System.out.println(gridPane.getColumnIndex(node));
	        if (gridPane.getColumnIndex(node) == col && gridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
	public static boolean isWithinGridBounds(double pieceX, double pieceY, GridPane cardGrid) {
	    double gridMinX = cardGrid.getLayoutX();
	    double gridMinY = cardGrid.getLayoutY();
	    double gridMaxX = gridMinX + cardGrid.getWidth();
	    double gridMaxY = gridMinY + cardGrid.getHeight();

	    return pieceX >= gridMinX && pieceX <= gridMaxX &&
	           pieceY >= gridMinY && pieceY <= gridMaxY;
	}
	
	public static boolean isOverlapping(Rectangle gridCell, double pieceX, double pieceY, int[][] cellHitbox) {
		int cellWidth = (int)gridCell.getWidth();
		int cellHeight = (int)gridCell.getHeight();
		
		//Convert grid cells
		Bounds bounds = gridCell.localToScene(gridCell.getBoundsInLocal());
	    double gridCellX = bounds.getMinX();
	    double gridCellY = bounds.getMinY();
		
	    for (int row = 0; row < cellHitbox.length; row++) {
	        for (int col = 0; col < cellHitbox[row].length; col++) {
	            if (cellHitbox[row][col] == 1) { // Check occupied cells
	                double pieceCellX = pieceX + col * cellWidth;
	                double pieceCellY = pieceY + row * cellHeight;

	                if (pieceCellX < gridCellX + cellWidth &&
	                    pieceCellX + cellWidth > gridCellX &&
	                    pieceCellY < gridCellY + cellHeight &&
	                    pieceCellY + cellHeight > gridCellY) {
	                    return true;
	                }
	            }
	        }
	    }
	    return false;
	}
	
	
	public static List<Rectangle> getHoveredCells(GridPane cardGridPane, double pieceX, double pieceY, int[][] hitbox) {
	    List<Rectangle> hoveredCells = new ArrayList<>();
 
	    int gridRow = cardGridPane.getRowCount();
	    int gridCol = cardGridPane.getColumnCount();	    
	    
	    for(int row = 0; row < gridRow; row++) {
	    	for(int col = 0; col < gridCol; col++) {
	    		if(getNodeFromGridPane(cardGridPane, col, row) != null) {
	    			Node currentNode = getNodeFromGridPane(cardGridPane, col, row);  
		    		if(currentNode instanceof Rectangle) {
		    			Rectangle currentCell = (Rectangle)currentNode;
		    			if(isOverlapping(currentCell, pieceX, pieceY, hitbox)) {
		    				hoveredCells.add(currentCell);
		    			}
		    		}
	    		}
	    	}
	    }

	    return hoveredCells;
	}
	
	
	public static boolean[][] generatePieceHitBox(Image piece, Rectangle cell) {
		int pieceHeight = (int)piece.getHeight();
		int pieceWidth = (int)piece.getWidth() - 10; // account for tiny inconsistencies in image sizes
		int cellWidth = (int)cell.getWidth();
		int cellHeight = (int)cell.getHeight();
		
		boolean[][] mask = new boolean[pieceHeight/cellHeight][pieceWidth/cellWidth];
//		System.out.println("Piece hit box size: (" + pieceHeight/cellHeight+ ", " + pieceWidth/cellWidth + ")"); 
		
		PixelReader pReader = piece.getPixelReader();
		
		for(int height = cellHeight / 2; height < pieceHeight; height = height + cellHeight) {
//			System.out.println();
			for (int width = cellWidth / 2; width < pieceWidth; width = width + cellWidth) {
				if(pReader.getColor(width, height).getOpacity() > 0.1) 
					mask[height/cellHeight][width/cellWidth] = true;
			}
		}
		
		System.out.println("Piece hitbox ready");
		
		return mask;
	}
	
	
	
	//Create solution matrix from card already with grid of rectangles (beta, only works with A21c)
	public static int[][] generateCardHitbox(GridPane cardGrid){
		//Solution grid will be generated along with the rectangles on the card, details **** will be decided later
		int[][] solution = { // Solution grid: 1 means the piece should go there
	            {1, 0, 0},
	            {1, 1, 0},
	            {1, 1, 0},
	            {1, 1, 1}
	    };
		return solution;
	}
	
}


//For debugging
//System.out.println("piece height: " + pieceHeight);
//System.out.println("piece width: " + pieceWidth);
//System.out.print(mask[height/cellHeight][width/cellWidth] + " ");
//System.out.print("(" + height/cellHeight + ", " + width/cellWidth + ") ");