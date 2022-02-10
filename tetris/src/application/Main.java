package application;
	
import java.util.Arrays;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	protected static final int size = 25;
	protected static final int rows = 25;
	protected static final int col = 20;
	protected static int[][] grid = new int[rows][col];
	protected static final int extra = 100;
	protected static int score = 0;
	protected static int lines = 0;
	
	@Override
	public void start(Stage stage) {
			for(int[] arr: grid) {
				Arrays.fill(arr, 0);
			}
		
			try {
				FXMLLoader loader = new FXMLLoader();
				Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
				LayoutController cntrl = loader.getController();
				Scene scene = new Scene(root, (size * col) + extra, size * rows);
				scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

				scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		            @Override
		            public void handle(KeyEvent key) {
		                // TODO Auto-generated method stub
		               if(key.getCode() == KeyCode.DOWN) {           	   
		            	   cntrl.moveDown();
		               }
		               else if(key.getCode() == KeyCode.RIGHT) {
		            	   cntrl.moveRight();
		               }
		               else if(key.getCode() == KeyCode.LEFT) {
		            	   cntrl.moveLeft();
		               }
		               else if(key.getCode() == KeyCode.UP) {
		            	   cntrl.changeForm();
		               }
		               else if(key.getCode() == KeyCode.SPACE) {
		            	   cntrl.hardDrop();
		               }
		            }
		        });
				
				stage.setScene(scene);
				stage.show();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
