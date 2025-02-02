//author: Le Binh Thanh
//Purpose: Make pieces draggable on gameplay screen
//Source: https://gist.github.com/Da9el00/74cc0100b67ade75308f3875c2c681cb

package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.scene.paint.Color;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class DraggableMaker {
	
	public static JSONObject loadJSON() throws IOException {
		JSONObject jsonObject;
		String currentDir = new File("").getAbsolutePath();
    	String dirString = currentDir + "\\src\\application\\config.json";
    	System.out.println("json directory: "+ dirString);
    	
    	String contentString = new String((Files.readAllBytes(Paths.get(dirString))));
    	jsonObject = new JSONObject(contentString);
    	return jsonObject;
	}
	
	public static void checkJsonStatus(JSONObject json) {
		if(json == null) System.out.println("JSON not working");
		else System.out.println("JSON ready");
	}
	
	//JSON file processing:
	//pass in the jsonObject, the current asset file name and the index of the piece in the piece's ImageView array
	public static String jsonProcessor(JSONObject jsonObject, String currentFXMLFile, int index) {
		JSONArray jsonFxmlArray = jsonObject.getJSONArray(currentFXMLFile);
		String key;
		Iterator<String> iterator;
//		System.out.println("index during json processing: " + index);
		
		JSONObject jsonObjectItem = jsonFxmlArray.getJSONObject(index);
		iterator = jsonObjectItem.keys();
		
		key = iterator.next();
		System.out.println(key);
		
		String assetDir = jsonObjectItem.getString(key);
//		System.out.println("corresponding item: " + assetDir);
		return assetDir;
	}

    private double mouseAnchorX;
    private double mouseAnchorY; 
    
    public List<Rectangle> highlightedRectangles;

    public void makeDraggable(Node node){

        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
            
//            Coords mousePressCoords = new Coords(mouseEvent.getSceneX(), mouseEvent.getSceneY());
//            System.out.println("Mouse: " + mousePressCoords.x + " " + mousePressCoords.y);
//            Bounds bounds = node.localToScene(node.getBoundsInLocal());
//            Coords cellCoords = new Coords(bounds.getMinX(), bounds.getMinY());
//            System.out.println("Cell: " + cellCoords.x + " " + cellCoords.y);
//            System.out.println(InitCoreMechanics.isWithinCellBounds(mousePressCoords, cellCoords, (Rectangle)node));
        });
        
        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });
    }
    
    public void makeHoverable(ImageView pieceView, GridPane cardGrid) throws JSONException, IOException {
    	
    	pieceView.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
    	
    	//double-click-to-spin method
    	pieceView.setOnMouseClicked(mouseEvent -> {
    		Rectangle gridCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGrid, 0, 0);
            if(mouseEvent.getClickCount() == 2) {
            	pieceView.setRotate(pieceView.getRotate() + 90);
            }
            Coords[][] pieceMask = InitCoreMechanics.generatePieceHitBoxLive(pieceView, gridCell, pieceView.getLayoutX(), pieceView.getLayoutY());
        });
    	
    	pieceView.setOnMouseDragged(mouseEvent -> {
    		
//    		pieceView.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
//    		pieceView.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    		
    		double newX = mouseEvent.getSceneX() - mouseAnchorX;
    		double newY = mouseEvent.getSceneY() - mouseAnchorY;
    		
    		newX = Math.max(0, Math.min(newX, 1280 - pieceView.getFitWidth()));
            newY = Math.max(0, Math.min(newY, 720 - pieceView.getFitHeight()));
            pieceView.setLayoutX(newX); 
            pieceView.setLayoutY(newY);
    		
    		List<Rectangle> collidedRectangles = new ArrayList<Rectangle>(); // store rectangles collided
    		
    		Rectangle sampleCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGrid, 0, 0);
    		Image pieceImage = pieceView.getImage();
    		
    		//Take piece upper left corner coordinates
    		double pieceX = pieceView.getLayoutX();
    	    double pieceY = pieceView.getLayoutY();
    	    Coords pieceCoords = new Coords(pieceX, pieceY);
    	    System.out.println("Position: " + pieceCoords.x + ", " + pieceCoords.y + " "); //for debugging
			
    	    //Generate piece segment coordinates
			Coords[][] pieceMask = InitCoreMechanics.generatePieceHitBoxLive(pieceView, sampleCell, pieceX, pieceY);
//			System.out.println("Piece Mask: ");
//			for(int i = 0; i < (int)pieceImage.getHeight()/sampleCell.getHeight(); i++) {
//				System.out.println();
//				for(int j = 0; j < (int)pieceImage.getWidth()/sampleCell.getWidth(); j++) {
//					System.out.print(pieceMask[i][j].x + ", " + pieceMask[i][j].x);
//				}
//			}
			
			//Take the map of the gridPane to find cell positions
			boolean[][] gridMap = GridHandler.makeGridPaneMap(cardGrid);
			for(int i = 0; i < cardGrid.getRowCount(); i++) {
				System.out.println();
				for(int j = 0; j < cardGrid.getColumnCount(); j++) {
					System.out.print(gridMap[i][j] + " " + gridMap[i][j] + " !!!!!!!!!!!!!!!!!!!!");
				}
			}
			
			//Use gridMask to find cell coordinates
			Coords[][] gridCellMask = GridHandler.takeGridCoords(cardGrid, gridMap);
			System.out.println();
			System.out.println("grid Mask: ");
			for(int i = 0; i < cardGrid.getRowCount(); i++) {
				System.out.println();
	    		for(int j = 0; j < cardGrid.getColumnCount(); j++) {
//	    				Rectangle currentCell = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGridPane, j, i);
//	    				Bounds bounds = currentCell.localToScene(currentCell.getBoundsInLocal());
	    				
//	    				currentCell.setOpacity(1.0);
//	    				currentCell.setFill(Color.AQUA); 
	    				System.out.print("( " + gridCellMask[i][j].x + ", " + gridCellMask[i][j].y + ")   ");
	    				
	    			
	    		}
	    	}
			
			
			//get left-most cell coordinates
    	    Bounds bounds = sampleCell.localToScene(sampleCell.getBoundsInLocal());
    	    Coords sampleCellCoords = new Coords(bounds.getMinX(), bounds.getMinY());
    	    System.out.println("Cell position: " + sampleCellCoords.x + ", " + sampleCellCoords.y);
    	    
    	    //Make cells in contact with piece lighten up:
    	    int pieceMaskHeight = (int)(pieceView.getFitHeight() / sampleCell.getHeight());
    	    int pieceMaskWidth = (int)(pieceView.getFitWidth() / sampleCell.getWidth());
    	    
    	    for(int i = 0; i < pieceMaskWidth; i++) {
    	    	for(int j = 0; j < pieceMaskHeight; j++) {
    	    		if(!InitCoreMechanics.isWithinGridBounds(pieceMask[i][j], cardGrid)) {
    	    			continue;
    	    		}
    	    		pieceView.toFront();
    	    		for(int row = 0; row < cardGrid.getRowCount(); row++) {
    	    			for(int col = 0; col < cardGrid.getColumnCount(); col++) {
    	    				Rectangle thisRectangle = (Rectangle)InitCoreMechanics.getNodeFromGridPane(cardGrid, col, row);
    	    				if(InitCoreMechanics.isWithinCellBounds(pieceMask[i][j], gridCellMask[row][col], sampleCell)) 
    	    				{
    	    					collidedRectangles.add(thisRectangle);
    	    					highlightedRectangles = collidedRectangles;
    	    					GridHandler.cellHighlighter(collidedRectangles, Color.AQUA);
    	    				}
    	    			}
    	    		}
    	    	}
    	    }
    	    
//    	    if(collidedRectangles.isEmpty()) {
//    	    	System.out.println("No collision");
//    	    } else {
//    	    	System.out.println("Collided cells: ");
//    	    	for (int i = 0; i < collidedRectangles.size(); i++) {
//    	    		System.out.print(collidedRectangles.get(i) + "; ");
////    	    		collidedRectangles.get(i).setFill(Color.AQUA);
//    	    	}
//    	    	System.out.println();
//    	    }
//		   
		});
    	
    	pieceView.setOnMouseReleased(MouseEvent -> {
    		double pieceWidth = pieceView.getFitWidth();
    		double pieceHeight = pieceView.getFitHeight();
    		
    		Coords pieceCoords = new Coords(pieceView.getLayoutX() + pieceWidth/2, pieceView.getLayoutY() + pieceHeight/2);
    		if(!InitCoreMechanics.isWithinGridBounds(pieceCoords, cardGrid)) GridHandler.clearCellHighlights(); 
    		else GridHandler.cellHighlighter(highlightedRectangles, Color.BLACK);
    		System.out.print(InitCoreMechanics.isWithinGridBounds(pieceCoords, cardGrid));
    	});
    }
}
