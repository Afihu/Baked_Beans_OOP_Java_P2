module Ubongo {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
}
