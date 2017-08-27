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
    
	public static int [][] tab_location_mine = new int[10][2]; // deklaracja tabli przechowuj�cej wsp�rz�dne losowe (x,y) 10-ciu min (bez powt�rze�)
	
	public static boolean check = false; // deklaracja i inicjalizacja zminnej wykorzystywanej w metodzie chcek
	
	public static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo�liwo�ci wyklosowania wsp�rz�dnych 0,0
	
	public static final String mine = "MINA"; // 
	
	// metody
	
	public static boolean check(int check_x, int check_y){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj�ce wsp��rz�dne kolejnej miny znajduj� si� ju� w tablicy wsp�rz�dnych min
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // p�tla do sprawdzania
    		if(tab_location_mine[i][0] == check_x && tab_location_mine[i][1] == check_y && zero_zero != 1){ // je�eli wylosowana liczba (x, y) jest r�wna liczbie znajduj�cej si� w tablicy i dodatkowo pomocnicza zmienna zero_zero pozwalaj�ca na wypisanie raz wsp�rz�dnych 0,0 s� prawd� to:
    			check = true; // zmienna pomocnicza check ustawiana jest na true
    			break;
			}else{
				check = false; // je�eli wartunek w if nie jest spe�niony to pomocnicza zmienna check ustawiana jest na false
			}
    	}
		return check; // zwrotka przez metod�
	}
	
	// obs�uga zdarze�
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) {

    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	grid_info_panel.setDisable(false); // aktywuje panel informacyjny dotycz�cy gry
    	grid_game_panel.setDisable(false); // aktywuje plansz� gry
    	
    	Random gen = new Random(); // import biblioteki do losowania
		
    	int x_temp_coordinate; // zmienna pomocnicza do przypisywania wylosowanej liczby, kt�ra b�dzie oznacza� wsp�rz�dn� "x" miny w grid panel
    	int y_temp_coordinate; // zmienna pomocnicza do przypisywania wylosowanej liczby, kt�ra b�dzie oznacza� wsp�rz�dn� "y" miny w grid panel
    	
    	
    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // p�tla do losowania koordynat�w po�o�enia mini
    		do{ // na pocz�tku wykonywane jest losowanie
    			x_temp_coordinate = gen.nextInt(tab_location_mine.length); // przypisuje wyklosowan� liczb� z przedzia�u 0-9 (mo�liwe wsp�rz�dne grid panel) do tymczasowej zmiennej
    			y_temp_coordinate = gen.nextInt(tab_location_mine.length); // przypisuje wyklosowan� liczb� z przedzia�u 0-9 (mo�liwe wsp�rz�dne grid panel) do tymczasowej zmiennej
	    		if(x_temp_coordinate == 0 && y_temp_coordinate ==0){ // w celu mo�liwo�ci wylosowania raz wsp�rz�dnych (0,0) wprowadzona jest pomocnicza zmienna
	    			zero_zero++; // je�eli wylosujemy 0,0 wowczas zwi�kszamy pomocnicz� zmienn� o jeden
	    		}
    		}while(check(x_temp_coordinate, y_temp_coordinate)); // nast�pnie wylosowane tymczasowe zmienne sprawdzamy czy ju� nie by�y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je�eli jest true to jeszcze raz s� losowane zmienne i jeszcze raz sprawdzane w metodzie, je�eli false, to znaczy, �e mo�emy wpisa� te wsp�rz�dne do tablicy bo ju� do pory nie by�y wylosowane
    		tab_location_mine[i][0] = x_temp_coordinate; // przypisanie zmiennych do miejsc w tabeli po wcze�niejszym sprawdzeniu czy by�y wcz�sniej wylosowane
    		tab_location_mine[i][1] = y_temp_coordinate; // przypisanie zmiennych do miejsc w tabeli po wcze�niejszym sprawdzeniu czy by�y wcz�sniej wylosowane
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

