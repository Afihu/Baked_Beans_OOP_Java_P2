//author: Le Binh Thanh
//Purpose: Make pieces draggable on gameplay screen
//Source: https://gist.github.com/Da9el00/74cc0100b67ade75308f3875c2c681cb

package application;

import javafx.scene.Node;

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
}
