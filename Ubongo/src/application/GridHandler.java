package application;

import java.io.ObjectInputFilter.Status;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridHandler {
	
	private static List<Rectangle> previousCollidedCells;
	
	public static boolean[][] makeGridPaneMap(GridPane cardGridPane) {
		int gridColCount = cardGridPane.getColumnCount();
		int gridRowCount = cardGridPane.getRowCount();
		boolean[][] cardGridPaneMap = new boolean[gridRowCount][gridColCount];
		
		for(int i = 0; i < gridRowCount; i++) {
    		System.out.println();
    		for(int j = 0; j < gridColCount; j++) {
    			if(InitCoreMechanics.getNodeFromGridPane(cardGridPane, j, i) instanceof Node) cardGridPaneMap[i][j] = true;
    		}
    	}
		
		return cardGridPaneMap;
		
	}
	
	public static Coords[][] takeGridCoords(GridPane cardGridPane, boolean[][] cardGridPaneMap){
		Rectangle sampleCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGridPane, 0, 0);
		Bounds bounds = sampleCell.localToScene(sampleCell.getBoundsInLocal());
		
		int gridColCount = cardGridPane.getColumnCount();
		int gridRowCount = cardGridPane.getRowCount();
		double cellWidth = sampleCell.getWidth();
		double cellHeight = sampleCell.getHeight();
		
		Coords sampleCellCoords = new Coords(bounds.getMinX(), bounds.getMinY());
		System.out.print(sampleCellCoords.x + " " + sampleCellCoords.y + " ");
		
		Coords[][] gridCellCoords = new Coords[gridRowCount][gridColCount];
		Coords.InitCoordsArray2d(gridCellCoords, gridRowCount, gridColCount);
//		Coo
//		for(int i = 0; i < gridRowCount; i++) {
//			System.out.println();
//			for(int j = 0; j < gridColCount; j++) {
//				System.out.print(gridCellCoords[i][j].x + " " + gridCellCoords[i][j].y + " ?????");
//			}
//		}
		
		
//	    cardGridPane.applyCss();
//	    cardGridPane.layout();
		
		///////////////////////////////////////////////////////////
		
		System.out.println();
		System.out.println("grid Mask: ");
		for(int i = 0; i < gridRowCount; i++) {
//			System.out.println();
    		for(int j = 0; j < gridColCount; j++) {
    			if(cardGridPaneMap[i][j] == true) {
    				Rectangle currentCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGridPane, j, i);
    				Bounds localBounds = currentCell.localToScene(currentCell.getBoundsInLocal());
    				
//    				currentCell.setOpacity(1.0);
//    				currentCell.setFill(Color.AQUA);    		
    				
    				gridCellCoords[i][j].x = localBounds.getMinX();
    				gridCellCoords[i][j].y = localBounds.getMinY();
    				
    				
//    				System.out.print(gridCellCoords[i][j].x + ", " + gridCellCoords[i][j].y + "   ");
    				
    			} else {
    				gridCellCoords[i][j].x = -1;
    				gridCellCoords[i][j].y = -1;
    			}
    		}
    	}
		
		/////////////////////////////////////////////////////////////
		return gridCellCoords;
	}
	
	public static void cellHighlighter(List<Rectangle> currentCollidedCells, Color choiceColor) {
		if(previousCollidedCells != null) {
			for(Rectangle cell : previousCollidedCells) {
				if(!currentCollidedCells.contains(cell)) {
					cell.setOpacity(0);
				}
			}
			
			for(Rectangle cell : currentCollidedCells) {
				cell.setOpacity(0.7);
				cell.setFill(choiceColor);
			}
		}
		
		previousCollidedCells = new ArrayList<Rectangle>(currentCollidedCells);
	}
	
	public static void clearCellHighlights() {
		if(previousCollidedCells != null) {
			for(Rectangle cell : previousCollidedCells) cell.setOpacity(0);
			previousCollidedCells.clear();
		}
	}
	
}








