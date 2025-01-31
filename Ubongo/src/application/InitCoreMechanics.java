package application;


import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
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
	    	int nodeRow, nodeCol;
	    	
	    	//account for when these return null upon seeing 0
	    	if(gridPane.getColumnIndex(node) == null) nodeCol = 0;
	    	else nodeCol = gridPane.getColumnIndex(node); 
	    	if(gridPane.getRowIndex(node) == null) nodeRow = 0;
	    	else nodeRow = gridPane.getRowIndex(node); 
	    	
	        if (nodeCol == col && nodeRow == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
	public static boolean isWithinGridBounds(double pieceX, double pieceY, GridPane cardGrid) {
		Bounds bounds = cardGrid.localToScene(cardGrid.getBoundsInLocal());
	    double gridMinX = bounds.getMinX();
	    double gridMinY = bounds.getMinY();
	    double gridMaxX = gridMinX + cardGrid.getWidth();
	    double gridMaxY = gridMinY + cardGrid.getHeight();

	    return pieceX >= gridMinX && pieceX <= gridMaxX &&
	           pieceY >= gridMinY && pieceY <= gridMaxY;
	}
	
	public static Rectangle isWithinCellBounds(Coords pieceCoordinate, Coords[][] gridCellCoordinates, GridPane cardGrid, Rectangle cell) {
		Rectangle currentCell;
		
		double cellWidth = cell.getWidth();
	    double cellHeight = cell.getHeight();
		
		int gridColCount = cardGrid.getColumnCount();
		int gridRowCount = cardGrid.getRowCount();
		
		for(int i = 0; i < gridRowCount; i++) {
			for(int j = 0; j < gridColCount; j++) {
				Coords currentCellCoords = gridCellCoordinates[i][j];
				
				Coords gridCellUpperLeftCorner = new Coords(currentCellCoords.x, currentCellCoords.y); 
			    Coords gridCellUpperRightCorner = new Coords(currentCellCoords.x + cellWidth, currentCellCoords.y);
			    double gridCellLowerLeftCornerX = currentCellCoords.x;
			    double gridCellLowerLeftCornerY = currentCellCoords.y + cellHeight;    	
			    Coords gridCellLowerLeftCorner = new Coords(gridCellLowerLeftCornerX, gridCellLowerLeftCornerY);
			    double gridCellLowerRightCornerX = gridCellUpperRightCorner.x;
			    double gridCellLowerRightCornerY = gridCellLowerLeftCornerY; 
			    Coords gridCellLowerRightCorner = new Coords(gridCellLowerRightCornerX, gridCellLowerRightCornerY);
				
			    if ((pieceCoordinate.x > currentCellCoords.x && pieceCoordinate.y > gridCellUpperLeftCorner.y)
				&& (pieceCoordinate.x > gridCellLowerLeftCorner.x && pieceCoordinate.y < gridCellLowerLeftCorner.y)
				&& (pieceCoordinate.x > gridCellUpperRightCorner.x && pieceCoordinate.y > gridCellUpperRightCorner.y)
				&& (pieceCoordinate.x < gridCellLowerRightCorner.x && pieceCoordinate.y < gridCellUpperRightCorner.y)){
			    	currentCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGrid, j, i);
			    	return currentCell;
			    }
			}
		}
		
		return null;
	}
	
	
	public static List<Rectangle> getHoveredCells(Coords currentCellCoords, Coords currentPieceCoords, Rectangle gridCell, GridPane cardGrid) {
	    List<Rectangle> hoveredRectangles = new ArrayList<Rectangle>();
	    
	    double cellWidth = gridCell.getWidth();
	    double cellHeight = gridCell.getHeight();
	    
	    Coords gridCellUpperLeftCorner = new Coords(currentCellCoords.x, currentCellCoords.y); 
	    
	    Coords gridCellUpperRightCorner = new Coords(currentCellCoords.x + cellWidth, currentCellCoords.y);
	    
	    double gridCellLowerLeftCornerX = currentCellCoords.x;
	    double gridCellLowerLeftCornerY = currentCellCoords.y + cellHeight;    	
	    Coords gridCellLowerLeftCorner = new Coords(gridCellLowerLeftCornerX, gridCellLowerLeftCornerY);
	    
	    double gridCellLowerRightCornerX = gridCellUpperRightCorner.x;
	    double gridCellLowerRightCornerY = gridCellLowerLeftCornerY; 
	    Coords gridCellLowerRightCorner = new Coords(gridCellLowerRightCornerX, gridCellLowerRightCornerY);
	    
	    if(isWithinGridBounds(gridCellUpperLeftCorner.x, gridCellUpperLeftCorner.y, cardGrid))
	    	
		    if ((currentPieceCoords.x > gridCellUpperLeftCorner.x && currentPieceCoords.y > gridCellUpperLeftCorner.y)
		    && (currentPieceCoords.x > gridCellLowerLeftCorner.x && currentPieceCoords.y < gridCellLowerLeftCorner.y)
		    && (currentPieceCoords.x > gridCellUpperRightCorner.x && currentPieceCoords.y > gridCellUpperRightCorner.y)
		    && (currentPieceCoords.x < gridCellLowerRightCorner.x && currentPieceCoords.y < gridCellUpperRightCorner.y)){
		    	hoveredRectangles.add(gridCell);
		    }
	    
	    return hoveredRectangles;
	}
	
	
	public static Coords[][] generatePieceHitBox(Image piece, Rectangle cell) {
		int pieceHeight = (int)piece.getHeight();
		int pieceWidth = (int)piece.getWidth() - 10; // account for tiny inconsistencies in image sizes
		int cellWidth = (int)cell.getWidth();
		int cellHeight = (int)cell.getHeight();
		
		//generate an array of hit box coordinates (of the center)
		Coords[][] hitBoxCoordinates = new Coords[pieceHeight / cellHeight][pieceWidth / cellWidth];
		Coords.InitCoordsArray2d(hitBoxCoordinates,(int) pieceHeight / cellHeight, (int) pieceWidth / cellWidth);
		
		//boolean[][] mask = new boolean[pieceHeight/cellHeight][pieceWidth/cellWidth];
//		System.out.println("Piece hit box size: (" + pieceHeight/cellHeight+ ", " + pieceWidth/cellWidth + ")"); 
		
		PixelReader pReader = piece.getPixelReader();
		
		for(int height = cellHeight / 2; height < pieceHeight; height = height + cellHeight) {
//			System.out.println();
			for (int width = cellWidth / 2; width < pieceWidth; width = width + cellWidth) {
				if(pReader.getColor(width, height).getOpacity() > 0.1) 
//					mask[height/cellHeight][width/cellWidth] = true;
					hitBoxCoordinates[height/cellHeight][width/cellWidth] = new Coords(width, height);
			}
		}
		
		System.out.println("Piece hitbox ready");
		
		return hitBoxCoordinates; //mask;
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