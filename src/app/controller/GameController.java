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
    
	public static int [][] tab_location_mine = new int[10][2]; // deklaracja tabli przechowuj¹cej wspó³rzêdne losowe (x,y) min
	
	public static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo¿liwoœci wyklosowania wspó³rzêdnych 0,0 jakiejœ miny
	
	public static final String mine = "M";
	
	public static int qty_mine_neighborhood;
	
	public static MouseButton button_mouse;
	
	public static Button button_game_panel;
	
    public static Node source;
	
	// metody
	
	public static boolean check_coordiantes_unique(int row, int col){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj¹ce wspó³órzêdne kolejnej miny znajduj¹ siê ju¿ w tablicy wspó³rzêdnych min
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ // UWAGA zmienna zero_zero jest bardzo wazna. Poniewa¿ na pocz¹tku programu deklarujê tablicê [10][2] to na wstêpie sk³ada siê ona z samych zer. Nastêpnie losujê wspó³rzêdne, jezeli wylosujê 0,0 to jest to po³o¿enie sprawdzane czy jest ju¿ w tablicy. Normalnie skoro tablica na pocz¹tku sk³ada siê z samy zer. To w koñu metoda znajdzie same zera i uzna, ¿e jest to duplikat. Dlatego w przypadku smaych zer pierwsze porównanie nie mo¿e byæ uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczyæ iloœæ porównañ z tablic¹. Tylko wartoœæ = 1 tej zmiennej powoduje, ¿e porównanie nie zwraca duplikatu
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
	
	public static boolean check_mine_underneath_button(int row, int col){ // metoda do sprawdzania czy wspó³rzêdne klawisza pokrywaj¹ siê ze wspó³rzêdnymi miny
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
	
	// obs³uga zdarzeñ
	
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
        
//      System.out.println("Rz¹d: "+ row);
//      System.out.println("Kolumna: "+ col);
        
        if(button_mouse == MouseButton.PRIMARY){ // naciœniêcie lewego przycisku myszy
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
        }else if(button_mouse == MouseButton.SECONDARY){ // naciœniêcie prawego przycisku myszy	
        	if(event.getClickCount() == 1){ // pojedyñcze klikniêcie
        		button_game_panel.setText("X");
        	}else if(event.getClickCount() == 2){ // potrójne klikniêcie
        		button_game_panel.setText("");
        	}
        }
    }

    @FXML
    void startGame(MouseEvent event) {
    	
    	if(tf_name.getText().equals("")){
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("Je¿eli nie masz imienia/nicku podpisz siê XX");
    		a.setTitle("B³¹d");
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
	    	
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // pêtla do losowania koordynatów po³o¿enia kolejnych min
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10); 
	    			System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
		    			zero_zero++; // je¿eli zostan¹ wlosowane wspó³rzêdne 0,0 to zwiêkszam zmienn¹ pomocnicz¹ o jeden 
		    		}
	    		}while(check_coordiantes_unique(row_random_coordinate, col_random_coordinate)); // nastêpnie wylosowane tymczasowe zmienne sprawdzamy czy ju¿ nie by³y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je¿eli jest true to jeszcze raz s¹ losowane zmienne i jeszcze raz sprawdzane w metodzie, je¿eli false, to znaczy, ¿e mo¿emy wpisaæ te wspó³rzêdne do tablicy bo do pory nie by³y wylosowane
	    		tab_location_mine[i][0] = row_random_coordinate; // przypisanie zmiennych do miejsc w tabeli po wczeœniejszym sprawdzeniu czy by³y wczêsniej wylosowane
	    		tab_location_mine[i][1] = col_random_coordinate;
	    	}
	    	
	    	// wypisuej wylosowan¹ lokalizacjê min
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

