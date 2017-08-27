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
    

    
    //zmiene
    
	public static int [][] tab_location_mine = new int[10][2]; // deklaracja tabli przechowuj¹cej wspó³rzêdne losowe (x,y) 10-ciu min (bez powtórzeñ)
	
	public static boolean check = false; // deklaracja i inicjalizacja zminnej wykorzystywanej w metodzie chcek
	
	public static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo¿liwoœci wyklosowania wspó³rzêdnych 0,0
	
	public static final String mine = "MINA"; // 
	
	// metody
	
	public static boolean check(int check_x, int check_y){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj¹ce wspó³órzêdne kolejnej miny znajduj¹ siê ju¿ w tablicy wspó³rzêdnych min
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // pêtla do sprawdzania
    		if(tab_location_mine[i][0] == check_x && tab_location_mine[i][1] == check_y && zero_zero != 1){ // je¿eli wylosowana liczba (x, y) jest równa liczbie znajduj¹cej siê w tablicy i dodatkowo pomocnicza zmienna zero_zero pozwalaj¹ca na wypisanie raz wspó³rzêdnych 0,0 s¹ prawd¹ to:
    			check = true; // zmienna pomocnicza check ustawiana jest na true
    			break;
			}else{
				check = false; // je¿eli wartunek w if nie jest spe³niony to pomocnicza zmienna check ustawiana jest na false
			}
    	}
		return check; // zwrotka przez metodê
	}
	
	// obs³uga zdarzeñ
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) {

    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	grid_info_panel.setDisable(false); // aktywuje panel informacyjny dotycz¹cy gry
    	grid_game_panel.setDisable(false); // aktywuje planszê gry
    	
    	Random gen = new Random(); // import biblioteki do losowania
		
    	int x_temp_coordinate; // zmienna pomocnicza do przypisywania wylosowanej liczby, która bêdzie oznaczaæ wspó³rzêdn¹ "x" miny w grid panel
    	int y_temp_coordinate; // zmienna pomocnicza do przypisywania wylosowanej liczby, która bêdzie oznaczaæ wspó³rzêdn¹ "y" miny w grid panel
    	
    	
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // pêtla do losowania koordynatów po³o¿enia mini
    		do{ // na pocz¹tku wykonywane jest losowanie
    			x_temp_coordinate = gen.nextInt(tab_location_mine.length); // przypisuje wyklosowan¹ liczbê z przedzia³u 0-9 (mo¿liwe wspó³rzêdne grid panel) do tymczasowej zmiennej
    			y_temp_coordinate = gen.nextInt(tab_location_mine.length); // przypisuje wyklosowan¹ liczbê z przedzia³u 0-9 (mo¿liwe wspó³rzêdne grid panel) do tymczasowej zmiennej
	    		if(x_temp_coordinate == 0 && y_temp_coordinate ==0){ // w celu mo¿liwoœci wylosowania raz wspó³rzêdnych (0,0) wprowadzona jest pomocnicza zmienna
	    			zero_zero++; // je¿eli wylosujemy 0,0 wowczas zwiêkszamy pomocnicz¹ zmienn¹ o jeden
	    		}
    		}while(check(x_temp_coordinate, y_temp_coordinate)); // nastêpnie wylosowane tymczasowe zmienne sprawdzamy czy ju¿ nie by³y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je¿eli jest true to jeszcze raz s¹ losowane zmienne i jeszcze raz sprawdzane w metodzie, je¿eli false, to znaczy, ¿e mo¿emy wpisaæ te wspó³rzêdne do tablicy bo ju¿ do pory nie by³y wylosowane
    		tab_location_mine[i][0] = x_temp_coordinate; // przypisanie zmiennych do miejsc w tabeli po wczeœniejszym sprawdzeniu czy by³y wczêsniej wylosowane
    		tab_location_mine[i][1] = y_temp_coordinate; // przypisanie zmiennych do miejsc w tabeli po wczeœniejszym sprawdzeniu czy by³y wczêsniej wylosowane
    	}
    	
    	// wypisuej wylosowan¹ lokalizacjê min
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
    			System.out.print(tab_location_mine[i][j] + " ");
    		}
    		System.out.println();
    	}
    	
    	
    	  	
// End of Game Controller  	
    }
}

