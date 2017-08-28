package app;

import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class TestClass_Random_Location_Mine {
	
	public static boolean check(int check_x, int check_y){
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == check_x && tab_location_mine[i][1] == check_y && zero_zero != 1){
    			check = true;
    			System.out.println("IDENTYCZNE");
    			System.out.print("Wylosowano ponownie: ");
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	public static Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane){
		
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();
	
		for (Node node : childrens) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				result = node;
		        break;
		    }
		}
		
	    return result; 
	}

	static int [][] tab_location_mine = new int[10][2];
	
	static boolean check = false;
	
	static int zero_zero = 0;
	
	public static void main(String[] args) {
		
    	Random gen = new Random();
		
    	int x;
    	
    	int y;
    	
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		do{
	    		x = gen.nextInt(10);
	    		y = gen.nextInt(10);
	    		System.out.println("Wylosowano: " + x + " " + y);
	    		if(x == 0 && y ==0){
	    			zero_zero++;
	    		}
    		}while(check(x, y));
    		tab_location_mine[i][0] = x;
    		tab_location_mine[i][1] = y;
    	}
    	
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
    			System.out.print(tab_location_mine[i][j] + " ");
    		}
    		System.out.println();
    	}
    	
    	
    	
    	
    	
    }	
}
