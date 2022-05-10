package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import javafx.animation.Animation.Status;
 
public class LayoutController {
    @FXML private Text score;
    @FXML private Text lines;
    @FXML private Pane tetrisGrid;
    @FXML private Pane scoreboard;
    @FXML private AnchorPane root;
    @FXML private VBox key;
    @FXML private HBox overview;
    @FXML private Pane endGame;
    @FXML private ImageView pauseIcon;
 
    private static boolean newBlock = false; //informs us whether we need to create new block
    private static Rectangle[] currBlock; //variable to hold current block that is controlled by player
    private static int startX = 8; //starting x pos of new block
    private static String typeBlock = "";
    private static int form;
    private static PauseTransition game = new PauseTransition(Duration.millis(500)); ;
    private static Image pause = new Image("application/images/pause.png");
    private static Image play = new Image("application/images/play.png");

    @FXML
    private void initialize() {  

    	
    	/*
    	 	created timer that drops a new tetrominoe after fixed interval but only if newBlock is false
    	 	when newBlock is true, then new tetrominoe is created and dropped again
    	 */
    	
        currBlock = addTetro(); //add the first block to the screen
        game.setOnFinished(
            e -> {
            	lines.setText("Lines: " + Main.lines);
            	score.setText("Score: " + Main.score);
            	
            	if(!newBlock) {
            		drop(currBlock);
            		game.playFromStart(); //loop again
            	}
                else {              
                  	if(!gameOver()) { // as long as top row is free, add new block
                  		currBlock = addTetro();
                      	newBlock = false;
                      	game.playFromStart(); 
                	}
                  	else {
                  		ObservableList<Node> nodes = tetrisGrid.getChildren();                  		
                  		for(Node node: nodes) {
                  			if(node instanceof Rectangle) {
                  				Rectangle rect = (Rectangle) node;
                  				rect.setOpacity(0.3);
                  			}
                  		}
                  		endGame.setOpacity(1);
                  		
                  		PauseTransition pause = new PauseTransition(Duration.seconds(2)); //added pause
                  		pause.setOnFinished(event -> {
                  			endGame();
                  		});
                  		pause.play();
                  		
                  		game.playFromStart();
                  	}
                }
            	
            });
        game.play();
    }
    
    protected static void changeForm() {
    	switch(typeBlock) {
    		case "o":
    			break;
    		case "i":
    			if(form == 0) {
    				form = 1;
    			}
    			else {
    				form = 0;
    			}   			
    			
    			changeI();
    			break;
    		case "z":
    			if(form < 3) {
    				form++; 
    			}
    			else {
    				form = 0;
    			}
    			
    			changeZ();
    			break;
    		case "l":
    			if(form < 3) {
    				form++; 
    			}
    			else {
    				form = 0;
    			}			
    			
    			changeL();
    			break;
    		case "t":
    			if(form < 3) {
    				form++; 
    			}
    			else {
    				form = 0;
    			}   			   
    			
    			changeT();
    			break;
    	} 	
    }
    
    private static void changeI() {
    	if(form == 0) {
    		if(checkBoundaries(currBlock[1], -1, 1) && checkBoundaries(currBlock[2], -2, 2) && checkBoundaries(currBlock[3], -3, 3)) {
    			currBlock[1].setX(currBlock[0].getX());
        		currBlock[1].setY(currBlock[0].getY() + Main.size);
        		currBlock[2].setX(currBlock[0].getX());
        		currBlock[2].setY(currBlock[1].getY() + Main.size);
        		currBlock[3].setX(currBlock[0].getX());
        		currBlock[3].setY(currBlock[2].getY() + Main.size);
    		}
    	}
    	else {
    		if(checkBoundaries(currBlock[1], 1, -1) && checkBoundaries(currBlock[2], 2, -2) && checkBoundaries(currBlock[3], 3, -3)) {
    			currBlock[1].setX(currBlock[0].getX() + Main.size);
        		currBlock[1].setY(currBlock[0].getY());
        		currBlock[2].setX(currBlock[1].getX() + Main.size);
        		currBlock[2].setY(currBlock[0].getY());
        		currBlock[3].setX(currBlock[2].getX() + Main.size);
        		currBlock[3].setY(currBlock[0].getY());
    		}
    	}
    }
    
    private static void changeT() {
    	if(form == 0) {
    		if(checkBoundaries(currBlock[0], 1, -1) && checkBoundaries(currBlock[1], -1, -1) && checkBoundaries(currBlock[3], 1, 1)) {
    			currBlock[0].setX(currBlock[0].getX() + Main.size);
        		currBlock[0].setY(currBlock[0].getY() - Main.size);
    			currBlock[1].setX(currBlock[1].getX() - Main.size);
        		currBlock[1].setY(currBlock[1].getY() - Main.size);
    			currBlock[3].setX(currBlock[3].getX() + Main.size);
        		currBlock[3].setY(currBlock[3].getY() + Main.size);
    		}
    	}
    	else if(form == 1) {
    		if(checkBoundaries(currBlock[1], 0, -2) && checkBoundaries(currBlock[2], -1, -1) && checkBoundaries(currBlock[3], -2, 0)) {
    			currBlock[1].setX(currBlock[1].getX());
        		currBlock[1].setY(currBlock[1].getY() - (2 * Main.size));
    			currBlock[2].setX(currBlock[2].getX() - Main.size);
        		currBlock[2].setY(currBlock[2].getY() - Main.size);
    			currBlock[3].setX(currBlock[3].getX() - ( 2 * Main.size));
        		currBlock[3].setY(currBlock[3].getY());
    		}
    	}
    	else if(form == 2) {
    		if(checkBoundaries(currBlock[0], -1, 1) && checkBoundaries(currBlock[1], 1, 1) && checkBoundaries(currBlock[3], -1, -1)) {
    			currBlock[0].setX(currBlock[0].getX() - Main.size);
        		currBlock[0].setY(currBlock[0].getY() + Main.size);
    			currBlock[1].setX(currBlock[1].getX() + Main.size);
        		currBlock[1].setY(currBlock[1].getY() + Main.size);
    			currBlock[3].setX(currBlock[3].getX() - Main.size);
        		currBlock[3].setY(currBlock[3].getY() - Main.size);
    		}
    	}	
    	else if(form == 3) {
    		if(checkBoundaries(currBlock[0], -1, -1) && checkBoundaries(currBlock[1], -1, 1) && checkBoundaries(currBlock[3], 1, -1)) {
    			currBlock[0].setX(currBlock[0].getX() - Main.size);
        		currBlock[0].setY(currBlock[0].getY() - Main.size);
    			currBlock[1].setX(currBlock[1].getX() - Main.size);
        		currBlock[1].setY(currBlock[1].getY() + Main.size);
    			currBlock[3].setX(currBlock[3].getX() + Main.size);
        		currBlock[3].setY(currBlock[3].getY() - Main.size);
    		}
    	}
    }
    
    private static void changeL() {
    	if(form == 0) {
    		if(checkBoundaries(currBlock[3], 2, 0)) {
    			currBlock[3].setX(currBlock[3].getX() + (2 * Main.size));
    		}	
    	}
    	else if(form == 1) {
    		if(checkBoundaries(currBlock[0], 2, 2) && checkBoundaries(currBlock[1], 1, 1) && checkBoundaries(currBlock[3], -1, 1)) {
    			currBlock[0].setX(currBlock[0].getX() + (2 * Main.size));
        		currBlock[0].setY(currBlock[0].getY() + (2 * Main.size));
    			currBlock[1].setX(currBlock[1].getX() + Main.size);
        		currBlock[1].setY(currBlock[1].getY() + Main.size);
    			currBlock[3].setX(currBlock[3].getX() - Main.size);
        		currBlock[3].setY(currBlock[3].getY() + Main.size);
    		}
    	}
    	else if(form == 2) {
    		if(checkBoundaries(currBlock[0], -4, 0) && checkBoundaries(currBlock[1], -2, 0)) {
    			currBlock[0].setX(currBlock[0].getX() - ( 4 * Main.size));
    			currBlock[1].setX(currBlock[1].getX() - (2 * Main.size));
    		}	
    	}	
    	else if(form == 3) {
    		if(checkBoundaries(currBlock[0], 2, -2) && checkBoundaries(currBlock[1], 1, -1) && checkBoundaries(currBlock[3], -1, -1)) {
    			currBlock[0].setX(currBlock[0].getX() + (2 * Main.size));
        		currBlock[0].setY(currBlock[0].getY() - (2 * Main.size));
    			currBlock[1].setX(currBlock[1].getX() + Main.size);
        		currBlock[1].setY(currBlock[1].getY() - Main.size);
    			currBlock[3].setX(currBlock[3].getX() - Main.size);
        		currBlock[3].setY(currBlock[3].getY() - Main.size);	
    		}
    	}
    }
    
    private static void changeZ() {
    	if(form == 0) {
    		if(checkBoundaries(currBlock[0], 0, -2) && checkBoundaries(currBlock[1], 1, -1) && checkBoundaries(currBlock[3], 1, 1)) {
    			currBlock[0].setX(currBlock[0].getX());
        		currBlock[0].setY(currBlock[0].getY() -(2 * Main.size));
    			currBlock[1].setX(currBlock[1].getX() + Main.size);
        		currBlock[1].setY(currBlock[1].getY() - Main.size);
    			currBlock[3].setX(currBlock[3].getX() + Main.size);
        		currBlock[3].setY(currBlock[3].getY() + Main.size);
    		}	
    	}
    	else if(form == 1) {
    		if(checkBoundaries(currBlock[0], 2, 0) && checkBoundaries(currBlock[1], 1, 1) && checkBoundaries(currBlock[3], -1, 1)) {
    			currBlock[0].setX(currBlock[0].getX() + (2 * Main.size));
        		currBlock[0].setY(currBlock[0].getY());
    			currBlock[1].setX(currBlock[1].getX() + Main.size);
        		currBlock[1].setY(currBlock[1].getY() + Main.size);
    			currBlock[3].setX(currBlock[3].getX() - Main.size);
        		currBlock[3].setY(currBlock[3].getY() + Main.size);
    		}
    	}
    	else if(form == 2) {
    		if(checkBoundaries(currBlock[1], -1, -1) && checkBoundaries(currBlock[3], -1, -1)) {
    			currBlock[1].setX(currBlock[1].getX() - Main.size);
        		currBlock[1].setY(currBlock[1].getY() - Main.size);
    			currBlock[3].setX(currBlock[3].getX() - Main.size);
        		currBlock[3].setY(currBlock[3].getY() - Main.size);
    		}	
    	}	
    	else if(form == 3) {
    		if(checkBoundaries(currBlock[0], -2, 2) && checkBoundaries(currBlock[1], -1, 1) && checkBoundaries(currBlock[3], 1, -1)) {
    			currBlock[0].setX(currBlock[0].getX() - (2 * Main.size));
        		currBlock[0].setY(currBlock[0].getY() + (2 * Main.size));
    			currBlock[1].setX(currBlock[1].getX() - Main.size);
        		currBlock[1].setY(currBlock[1].getY() + Main.size);
    			currBlock[3].setX(currBlock[3].getX() + Main.size);
        		currBlock[3].setY(currBlock[3].getY() - Main.size);
    		}	
    	}
    }
    
    protected static void moveRight() {
    	if(checkBoundaries(currBlock, 1, 0)) {
    		currBlock[0].setX(currBlock[0].getX() + Main.size);
           	currBlock[1].setX(currBlock[1].getX() + Main.size);
           	currBlock[2].setX(currBlock[2].getX() + Main.size);
           	currBlock[3].setX(currBlock[3].getX() + Main.size);
    	}
    }
    
    protected static void moveLeft() {
    	if(checkBoundaries(currBlock, -1, 0)) {
    		currBlock[0].setX(currBlock[0].getX() - Main.size);
           	currBlock[1].setX(currBlock[1].getX() - Main.size);
           	currBlock[2].setX(currBlock[2].getX() - Main.size);
           	currBlock[3].setX(currBlock[3].getX() - Main.size);
    	}
    }
    
    protected static void moveDown() {
    	if(checkBoundaries(currBlock, 0, 1)) {
    		currBlock[0].setY(currBlock[0].getY() + Main.size);
           	currBlock[1].setY(currBlock[1].getY() + Main.size);
           	currBlock[2].setY(currBlock[2].getY() + Main.size);
           	currBlock[3].setY(currBlock[3].getY() + Main.size);
    	}
    }
    
    private Rectangle[] addTetro() {
    	int randPiece = new Random().nextInt(100); //randomly generated color
    	int startPosX = startX * Main.size; //initial horizontal pos of dropping tetris piece    	
    	Rectangle a = new Rectangle(Main.size - 1, Main.size - 1, Main.size - 1, Main.size - 1);
    	Rectangle b = new Rectangle(Main.size - 1, Main.size - 1, Main.size - 1, Main.size - 1);
    	Rectangle c = new Rectangle(Main.size - 1, Main.size - 1, Main.size - 1, Main.size - 1);
    	Rectangle d = new Rectangle(Main.size - 1, Main.size - 1, Main.size - 1, Main.size - 1);
    	
    	if(randPiece >=0 && randPiece < 20) {
    		a.getStyleClass().add("o");
    		b.getStyleClass().add("o");
    		c.getStyleClass().add("o");
    		d.getStyleClass().add("o");
    		typeBlock = "o";
    		form = 0;
    		
    		a.setX(startPosX);
    		a.setY(0);
        	b.setX(a.getX() + Main.size);
        	b.setY(0);
        	c.setX(a.getX());
        	c.setY(a.getY() + Main.size);
        	d.setX(c.getX() + Main.size);
        	d.setY(c.getY());
    	}
    	else if(randPiece >= 20 && randPiece < 40) {
    		a.getStyleClass().add("i");
    		b.getStyleClass().add("i");
    		c.getStyleClass().add("i");
    		d.getStyleClass().add("i");
    		typeBlock = "i";
    		form = 0;
    	
    		a.setX(startPosX);
    		a.setY(0);
        	b.setX(a.getX() + Main.size);
        	b.setY(0);
        	c.setX(b.getX() + Main.size);
        	c.setY(0);
        	d.setX(c.getX() + Main.size);
        	d.setY(0);
    	}
    	else if(randPiece >= 40 && randPiece < 60) {
    		a.getStyleClass().add("z");
    		b.getStyleClass().add("z");
    		c.getStyleClass().add("z");
    		d.getStyleClass().add("z");
    		typeBlock = "z";
    		form = 0;
    		
    		a.setX(startPosX);
    		a.setY(0);
        	b.setX(a.getX() + Main.size);
        	b.setY(0);
        	c.setX(b.getX());
        	c.setY(b.getY() + Main.size);
        	d.setX(c.getX() + Main.size);
        	d.setY(c.getY());
    	}
    	else if(randPiece >= 60 && randPiece < 80) {
    		a.getStyleClass().add("l");
    		b.getStyleClass().add("l");
    		c.getStyleClass().add("l");
    		d.getStyleClass().add("l");
    		typeBlock = "l";
    		form = 0;
    	
    		a.setX(startPosX);
    		a.setY(0);
        	b.setX(a.getX());
        	b.setY(a.getY() + Main.size);
        	c.setX(a.getX());
        	c.setY(b.getY() + Main.size);
        	d.setX(a.getX() + Main.size);
        	d.setY(c.getY());
    	}
    	else if(randPiece >= 80 && randPiece < 100) {
    		a.getStyleClass().add("t");
    		b.getStyleClass().add("t");
    		c.getStyleClass().add("t");
    		d.getStyleClass().add("t");
    		typeBlock = "t";
    		form = 0;
    			
    		a.setX(startPosX);
    		a.setY(0);
        	b.setX(a.getX() - Main.size);
        	b.setY(a.getY() + Main.size);
        	c.setX(a.getX());
        	c.setY(a.getY() + Main.size);
        	d.setX(a.getX() + Main.size);
        	d.setY(a.getY() + Main.size);
    	}
    	
    	Rectangle[] tetro = new Rectangle[] {a,b,c,d};
    	
    	if(checkBoundaries(tetro, 0, 0)) {
    		tetrisGrid.getChildren().addAll(a, b, c, d);
    	}
    	return tetro;
    }
    
    protected static void hardDrop() {
    	while(checkBoundaries(currBlock, 0, 1)) {
    		currBlock[0].setY(currBlock[0].getY() + Main.size);
    		currBlock[1].setY(currBlock[1].getY() + Main.size);
    		currBlock[2].setY(currBlock[2].getY() + Main.size);
    		currBlock[3].setY(currBlock[3].getY() + Main.size);
    	}
    }
    
    private boolean drop(Rectangle[] piece) {	
    	if(checkBoundaries(piece, 0, 1)) {
    		piece[0].setY(piece[0].getY() + Main.size);
           	piece[1].setY(piece[1].getY() + Main.size);
           	piece[2].setY(piece[2].getY() + Main.size);
           	piece[3].setY(piece[3].getY() + Main.size);
           
           	newBlock = false;          	
    	}
     	else {
       		for(int i = 0; i < piece.length; i++) {
       			int xGridPos = (int) (piece[i].getX() / Main.size);
       	    	int yGridPos = (int) (piece[i].getY() / Main.size);
       	   		Main.grid[yGridPos][xGridPos] = 1; //piece occupies permanent spot on grid
       		} 
       		
       		for(int i = 0; i < Main.grid.length; i++) {
       			boolean destroy = true;
       			
       			for(int j = 0; j < Main.grid[i].length; j++) {
       				if(Main.grid[i][j] != 1) {
       					destroy = false;
       					break;
       				}
       			}
       			
       			if(destroy) {
       				breakLevel(i);
       				break;
       			}
       		}
       		
           	scoreGame(piece);
       		newBlock = true;
       		return false;
      	}
    	
    	return true;
    }
    
    private void scoreGame(Rectangle[] block) {
    	int maxY = 0;
    	
    	for(int k = 0; k < block.length; k++) {
    		maxY = Math.max(maxY, (int) block[k].getY()); //get ypos of block dropped furthest down screen
    	}
    	
    	int maxScore = maxY / Main.size;
    	Main.score += maxScore;
    }
    
    private boolean gameOver() {    
    	for(int i = 0; i < Main.grid[0].length; i++) {
    		if(Main.grid[0][i] == 1) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private void breakLevel(int level) {
    	Main.lines++;
    	Main.score += 100;
    	
    	ObservableList<Node> blocks = (ObservableList<Node>) tetrisGrid.getChildren(); //gets all of the blocks found on tetrisgrid 
    	ArrayList<Rectangle> deleted = new ArrayList<Rectangle>();
    	ArrayList<Rectangle> kept = new ArrayList<Rectangle>();
    	
    	for(Node node: blocks) {
    		if(node instanceof Rectangle) {
    			Rectangle a = (Rectangle) node;	
    			int yPos = (int) (a.getY() / Main.size);
    			
    			if(yPos == level) {
    				deleted.add(a);
    			}
    			else {
    				kept.add(a);
    			}
    		}
    	}
    	
    	for(Rectangle rect: deleted) {
    		int xPos = (int) (rect.getX() / Main.size);
    		int yPos = (int) (rect.getY() / Main.size);
    		
    		Main.grid[yPos][xPos] = 0;
    		tetrisGrid.getChildren().remove(rect);    		    		
    	}
    	
    	for(Rectangle rect: kept) {
    		int xPos = (int) (rect.getX() / Main.size);
        	int yPos = (int) (rect.getY() / Main.size);
        	
        	if(yPos < level) { //if block is above destroyed row, shift it down        		
        		Main.grid[yPos][xPos] = 0;
            	rect.setY(rect.getY() + Main.size);
        	}    	
    	}
    	
    	ObservableList<Node> newGrid = (ObservableList<Node>) tetrisGrid.getChildren(); //add the rectangle nodes with updated pos to list
    	
    	for(Node node: newGrid) { //fill grid with updated positions
    		if(node instanceof Rectangle) {
    			Rectangle a = (Rectangle) node;
    			int xPos = (int) (a.getX() / Main.size);
            	int yPos = (int) (a.getY() / Main.size);
            	
            	Main.grid[yPos][xPos] = 1;
    		} 		
    	}
    }
 
  //check boundaries method for when tetro piece changes form
    private static boolean checkBoundaries(Rectangle piece, int xMove, int yMove) {
    	
    	if(game.getStatus() == Status.PAUSED) {
    		return false;
    	}
    	
    	try {
    		int xGridPos = (int) (piece.getX() / Main.size);
       		int yGridPos = (int) (piece.getY() / Main.size);
       		
           	if(xGridPos + xMove < 0 || xGridPos + xMove > Main.col - 1) {  //we have to offset by one col otherwise piece will eclipse screen
           		return false;
           	}
           	
           	if(yGridPos + yMove > Main.rows - 1) { //we have to offset by one row otherwise piece will sink beneath screen
           		return false;
           	}	   	    
           	
           	if(Main.grid[yGridPos+yMove][xGridPos+xMove] == 1 ) {           		
           		return false;
        	} 
    	}
    	catch(ArrayIndexOutOfBoundsException e) {
    		e.printStackTrace();
    	}
    	
    	return true;
    }
    
    //check boundaries method for when tetro piece is dropped or moved
    private static boolean checkBoundaries(Rectangle[] piece, int xMove, int yMove) {
    	
    	if(game.getStatus() == Status.PAUSED) {
    		return false;
    	}
    	
    	for(int i = 0; i < piece.length; i++) { 		
       		try {
       			int xGridPos = (int) (piece[i].getX() / Main.size);
           		int yGridPos = (int) (piece[i].getY() / Main.size);
       			
               	if(xGridPos + xMove < 0 || xGridPos + xMove > Main.col - 1) {  //we have to offset by one col otherwise piece will eclipse screen
               		return false;
               	}
               	
               	if(yGridPos + yMove > Main.rows - 1) { //we have to offset by one row otherwise piece will sink beneath screen
               		return false;
               	}	
               	
               	if(Main.grid[yGridPos+yMove][xGridPos+xMove] == 1) {
               		return false;
            	} 
            	    
        	}
        	catch(ArrayIndexOutOfBoundsException e) {
        		e.printStackTrace();
        	}
    	}
    	
    	return true;
    }
    
    protected void endGame() {
    	tetrisGrid.getChildren().clear();
			for(int[] arr: Main.grid) {
			Arrays.fill(arr, 0);
		}
			
			newBlock = true;
			Main.score = 0;
			Main.lines = 0;
    }
    
    @FXML private void restart() {
    	endGame();
    }
    
   @FXML private void stopGame() {
    	if(game.getStatus() == Status.RUNNING) {
    		game.pause();
    		pauseIcon.setImage(play);
    	}
    	else {
    		game.play();
    		pauseIcon.setImage(pause);
    	}
    }
}
