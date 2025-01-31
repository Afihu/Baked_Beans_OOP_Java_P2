package application;

import java.util.Iterator;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridHandler {
	
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
		for(int i = 0; i < gridRowCount; i++) {
			System.out.println();
			for(int j = 0; j < gridColCount; j++) {
				System.out.print(gridCellCoords[i][j].x + " " + gridCellCoords[i][j].y + " ");
			}
		}
		
		
//	    cardGridPane.applyCss();
//	    cardGridPane.layout();
		
		for(int i = 0; i < gridRowCount; i++) {
			System.out.println();
    		for(int j = 0; j < gridColCount; j++) {
    			if(cardGridPaneMap[i][j] == true) {
//    				Rectangle currentCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGridPane, j, i);
//    				Bounds bounds = currentCell.localToScene(currentCell.getBoundsInLocal());
    				
//    				currentCell.setOpacity(1.0);
//    				currentCell.setFill(Color.AQUA);    		
    				
    				gridCellCoords[i][j].x = sampleCellCoords.x + i * cellWidth;
    				gridCellCoords[i][j].y = sampleCellCoords.y + j * cellHeight;
    				
    				System.out.print(gridCellCoords[i][j].x + ", " + gridCellCoords[i][j].y + "   ");
    				
    			} else {
    				gridCellCoords[i][j].x = -1;
    				gridCellCoords[i][j].y = -1;
    			}
    		}
    	}
		
		
		return gridCellCoords;
	}
	
}
