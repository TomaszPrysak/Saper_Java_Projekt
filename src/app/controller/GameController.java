package app.controller;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    
    //zmiene
    
	public static int [][] tab_location_mine = new int[10][2]; // deklaracja tabli przechowuj�cej wsp�rz�dne losowe (x,y) 10-ciu min
	
	public static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo�liwo�ci wyklosowania wsp�rz�dnych 0,0 jakiej� miny
	
	public static final String mine = "M";
	
	// metody
	
	public static boolean check_coordiantes_unique(int row, int col){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj�ce wsp��rz�dne kolejnej miny znajduj� si� ju� w tablicy wsp�rz�dnych min
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ // UWAGA zmienna zero_zero jest bardzo wazna. Poniewa� na pocz�tku programu deklaruj� tablic� [10][2] to na wst�pie sk�ada si� ona z samych zer. Nast�pnie losuj� wsp�rz�dne, jezeli wylosuj� 0,0 to jest to po�o�enie sprawdzane czy jest ju� w tablicy. Normalnie skoro tablica na pocz�tku sk�ada si� z samy zer. To w ko�u metoda znajdzie same zera i uzna, �e jest to duplikat. Dlatego w przypadku smaych zer pierwsze por�wnanie nie mo�e by� uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczy� ilo�� por�wna� z tablic�. Tylko warto�� = 1 tej zmiennej powoduje, �e por�wnanie nie zwraca duplikatu
    			check = true;
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	public static boolean check_mine_underneath_button(int row, int col){ // metoda do sprawdzania czy wsp�rz�dne klawisza pokrywaj� si� ze wsp�rz�dnymi miny
		boolean check = false;
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col){
    			check = true;
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	public static boolean check_neighborhood(int row, int col){
		return false;
	}
	
	// obs�uga zdarze�
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException {
    	
    	Button przycisk = (Button)event.getSource();
        Node zrodlo = (Node)event.getSource();
        
        int mine_row = GridPane.getRowIndex(zrodlo);
        int mine_col = GridPane.getColumnIndex(zrodlo);

        System.out.println("Rz�d: "+ mine_row);
        System.out.println("Kolumna: "+ mine_col);
        
        if(check_mine_underneath_button(mine_row, mine_col) == true){
        	przycisk.setText(mine);
        	przycisk.setDisable(true);
        	System.out.println("MINA");
        	
        	Stage stageGameOver = new Stage();
    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameOverView.fxml"));
    		Scene sceneGameOver = new Scene(parent);
    		stageGameOver.setScene(sceneGameOver);
    		stageGameOver.setTitle("Game Over !");
    		stageGameOver.setResizable(false);
    		stageGameOver.show();
        }else if(check_neighborhood(mine_row, mine_col)){
        	
        }
       
    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	grid_info_panel.setDisable(false);
    	grid_game_panel.setDisable(false);
    	
    	Random gen = new Random();
		
    	int row_random_coordinate; 
    	int col_random_coordinate;
    	
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // p�tla do losowania koordynat�w po�o�enia kolejnych min
    		do{
    			row_random_coordinate = gen.nextInt(tab_location_mine.length); 
    			col_random_coordinate = gen.nextInt(tab_location_mine.length); 
	    		if(row_random_coordinate == 0 && col_random_coordinate ==0){ 
	    			zero_zero++; // je�eli zostan� wlosowane wsp�rz�dne 0,0 to zwi�kszam zmienn� pomocnicz� o jeden 
	    		}
    		}while(check_coordiantes_unique(row_random_coordinate, col_random_coordinate)); // nast�pnie wylosowane tymczasowe zmienne sprawdzamy czy ju� nie by�y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je�eli jest true to jeszcze raz s� losowane zmienne i jeszcze raz sprawdzane w metodzie, je�eli false, to znaczy, �e mo�emy wpisa� te wsp�rz�dne do tablicy bo do pory nie by�y wylosowane
    		tab_location_mine[i][0] = row_random_coordinate; // przypisanie zmiennych do miejsc w tabeli po wcze�niejszym sprawdzeniu czy by�y wcz�sniej wylosowane
    		tab_location_mine[i][1] = col_random_coordinate;
    	}
    	
    	// wypisuej wylosowan� lokalizacj� min
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
    			System.out.print(tab_location_mine[i][j] + " ");
    		}
    		System.out.println();
    	} 
    
// End of Game Controller  	
    }
}

