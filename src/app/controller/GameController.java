package app.controller;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private TextField tf_name;
    
    @FXML
    private Spinner<Integer> sp_qty_mine;
	
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
    
    public static int qty_mine_overall;
    
    public static String user_name;
    
	public static int [][] tab_location_mine = new int[10][2]; // deklaracja tabli przechowuj�cej wsp�rz�dne losowe (x,y) min
	
	public static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo�liwo�ci wyklosowania wsp�rz�dnych 0,0 jakiej� miny
	
	public static final String mine = "M";
	
	public static int qty_mine_neighborhood;
	
	public static MouseButton button_mouse;
	
	public static Button button_game_panel;
	
    public static Node source;
	
	// metody
	
	public static boolean check_coordiantes_unique(int row, int col){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj�ce wsp��rz�dne kolejnej miny znajduj� si� ju� w tablicy wsp�rz�dnych min
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ // UWAGA zmienna zero_zero jest bardzo wazna. Poniewa� na pocz�tku programu deklaruj� tablic� [10][2] to na wst�pie sk�ada si� ona z samych zer. Nast�pnie losuj� wsp�rz�dne, jezeli wylosuj� 0,0 to jest to po�o�enie sprawdzane czy jest ju� w tablicy. Normalnie skoro tablica na pocz�tku sk�ada si� z samy zer. To w ko�u metoda znajdzie same zera i uzna, �e jest to duplikat. Dlatego w przypadku smaych zer pierwsze por�wnanie nie mo�e by� uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczy� ilo�� por�wna� z tablic�. Tylko warto�� = 1 tej zmiennej powoduje, �e por�wnanie nie zwraca duplikatu
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
	
	public static boolean check_mine_underneath_button(int row, int col){ // metoda do sprawdzania czy wsp�rz�dne klawisza pokrywaj� si� ze wsp�rz�dnymi miny
		boolean check = false;
    	for(int i = 0; i <= tab_location_mine.length - 1; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col){
    			check = true;
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	public static int check_neighborhood(int row, int col){
		qty_mine_neighborhood = 0;
		for(int i = 0; i<= tab_location_mine.length - 1; i++){
			if(tab_location_mine[i][0] == (row - 1) && tab_location_mine[i][1] == (col - 1)){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == (row - 1) && tab_location_mine[i][1] == col){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == (row - 1) && tab_location_mine[i][1] == (col + 1)){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == (col - 1)){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == (col + 1)){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == row + 1 && tab_location_mine[i][1] == (col - 1)){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == row + 1 && tab_location_mine[i][1] == col){
				qty_mine_neighborhood++;
			}
			if(tab_location_mine[i][0] == (row + 1) && tab_location_mine[i][1] == (col + 1)){
				qty_mine_neighborhood++;
			}
		}
		return qty_mine_neighborhood;
	}
	
	public static void clear_panel(int row, int col){
			button_game_panel.setDisable(true);	
	}
	
	// obs�uga zdarze�
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException {
    	
//    	MouseButton button_mouse = (MouseButton)event.getButton();
//    	Button button_game_panel = (Button)event.getSource();
//      Node source = (Node)event.getSource();
        
    	button_mouse = (MouseButton)event.getButton();
    	button_mouse = event.getButton();
    	button_game_panel = (Button) event.getSource();
    	source = (Node) event.getSource();
    	
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);
        
//      System.out.println("Rz�d: "+ row);
//      System.out.println("Kolumna: "+ col);
        
        if(button_mouse == MouseButton.PRIMARY){ // naci�ni�cie lewego przycisku myszy
	        if(check_mine_underneath_button(row, col) == true){
	        	
	        	button_game_panel.setText(mine);
	        	button_game_panel.setDisable(true);
//	        	System.out.println("MINA");
	        	
	        	Stage stageGameOver = new Stage();
	    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameOverView.fxml"));
	    		Scene sceneGameOver = new Scene(parent);
	    		stageGameOver.setScene(sceneGameOver);
	    		stageGameOver.setTitle("Game Over !");
	    		stageGameOver.setResizable(false);
	    		stageGameOver.show();

	        }else if(check_neighborhood(row, col) != 0){
	        	button_game_panel.setText(String.valueOf(qty_mine_neighborhood));
	        	button_game_panel.setDisable(true);
	        }else if(check_neighborhood(row, col) == 0){
	        	clear_panel(row, col);
	        }
        }else if(button_mouse == MouseButton.SECONDARY){ // naci�ni�cie prawego przycisku myszy	
        	if(event.getClickCount() == 1){ // pojedy�cze klikni�cie
        		button_game_panel.setText("X");
        	}else if(event.getClickCount() == 2){ // potr�jne klikni�cie
        		button_game_panel.setText("");
        	}
        }
    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	if(tf_name.getText().equals("")){
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("Je�eli nie masz imienia/nicku podpisz si� XX");
    		a.setTitle("B��d");
    		a.setHeaderText("UWAGA!");
    		a.showAndWait();
    	}else{
        	grid_info_panel.setDisable(false);
        	grid_game_panel.setDisable(false);
    		
        	user_name = tf_name.getText();
        	
        	System.out.println(user_name);
        	
        	Random gen = new Random();
    		
        	int row_random_coordinate; 
        	int col_random_coordinate;
        	
	    	qty_mine_overall = sp_qty_mine.getValue();
	    	
	    	tab_location_mine = new int [qty_mine_overall][2];
	    	
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // p�tla do losowania koordynat�w po�o�enia kolejnych min
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10); 
	    			System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
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
    	}
    	
    }
    
    public void initialize(){
    	
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
    	
    	sp_qty_mine.setValueFactory(valueFactory);
    
    }
    
}

