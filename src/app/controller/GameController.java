package app.controller;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private Button btn_new_game;

    @FXML
    private GridPane grid_info_panel;

    @FXML
    private Label lb_time;

    @FXML
    private Label lb_qty_mine;

    @FXML
    private GridPane grid_game_panel;
    
    @FXML
    public void initialize(){
    	

    	
    }
    
    @FXML
    void showWhatIsUnderneath(MouseEvent event) {

    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	grid_info_panel.setDisable(false);
    	grid_game_panel.setDisable(false);
    	
    	Random gen = new Random();
    	
    	int [][] tab_location_mine = new int[10][2];
    	
//    	tab_location_mine[0][0] = 0;
//    	tab_location_mine[0][1] = 1;
//    	
//    	tab_location_mine[1][0] = 0;
//    	tab_location_mine[1][1] = 1;
//    	
//    	tab_location_mine[2][0] = 0;
//    	tab_location_mine[2][1] = 1;
//    	
//    	tab_location_mine[3][0] = 0;
//    	tab_location_mine[3][1] = 1;
//    	
//    	tab_location_mine[4][0] = 0;
//    	tab_location_mine[4][1] = 1;
//    	
//    	tab_location_mine[5][0] = 0;
//    	tab_location_mine[5][1] = 1;
//    	
//    	tab_location_mine[6][0] = 0;
//    	tab_location_mine[6][1] = 1;
//    	
//    	tab_location_mine[7][0] = 0;
//    	tab_location_mine[7][1] = 1;
//    	
//    	tab_location_mine[8][0] = 0;
//    	tab_location_mine[8][1] = 1;
//    	
//    	tab_location_mine[9][0] = 0;
//    	tab_location_mine[9][1] = 1;
    	
    	System.out.println(tab_location_mine[9][1]);
    	
    	for(int i = 0; i < tab_location_mine.length - 1 ; i++){
    		
    	}
    	
    }
}

