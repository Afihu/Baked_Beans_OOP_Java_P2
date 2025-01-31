package application;

public class Coords {
	public double x = 0.0;
	public double y = 0.0;
	
	public Coords(double xCoords, double yCoords) {
		this.x = xCoords;
		this.y = yCoords;
	}
	
	public static void InitCoordsArray2d(Coords[][] coordsArray, int gridRowCount, int gridColCount){
		for(int i = 0; i < gridRowCount; i++) {
		    for(int j = 0; j < gridColCount; j++) {
		        // Initialize each Coords object
		        coordsArray[i][j] = new Coords(-1, -1);
		    }
		}
	}
}
