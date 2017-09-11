package app.controller;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import app.database.DBConnector;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private TextField tf_name;
    
    @FXML
    private Spinner<Integer> sp_qty_mine;
	
    @FXML
    private Button btn_new_game;
    
    @FXML
    private GridPane grid_game_panel;
    
    @FXML
    private GridPane grid_info_panel;
    
    @FXML
    private HBox hb_name;
    
    @FXML
    private HBox hb_qty_mine;
    
    @FXML
    private Label lb_stopwatch;

    @FXML
    private Label lb_qty_mine;
    
    @FXML
    private Label lb_mine_suspected;
    
    ////////////
    // zmiene //
    ////////////
    
    public DBConnector db;
    
    static int qty_mine_overall_user_chooice;
    
    static String user_name;
    
    int num_click_left = 0;
    
    int num_click_right = 0;
    
	static int [][] tab_location_mine = new int[10][2]; // deklaracja tablicy przechowuj�cej wsp�rz�dne losowe (x,y) min
	
	static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo�liwo�ci wyklosowania wsp�rz�dnych 0,0 jakiej� miny
	
	static final String mine = "M";
	
	static int qty_mine_neighborhood;
	
	static LocalTime timeStart;
	
	static LocalTime timeStop;
	
	static MouseButton button_mouse;
	
	static Button button_game_panel;
	
    static Node source;
	
    ////////////
	// metody //
    ////////////
    
	public static boolean checkCoordiantesUnique(int row, int col){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj�ce wsp��rz�dne kolejnej miny znajduj� si� ju� w tablicy wsp�rz�dnych min
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ // UWAGA zmienna zero_zero jest bardzo wazna. Poniewa� na pocz�tku programu deklaruj� tablic� [10][2] to na wst�pie sk�ada si� ona z samych zer. Nast�pnie losuj� wsp�rz�dne, jezeli wylosuj� 0,0 to jest to po�o�enie sprawdzane czy jest ju� w tablicy. Normalnie skoro tablica na pocz�tku sk�ada si� z samy zer. To w ko�u metoda znajdzie same zera i uzna, �e jest to duplikat. Dlatego w przypadku samych zer pierwsze por�wnanie nie mo�e by� uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczy� ilo�� por�wna� z tablic�. Tylko warto�� = 1 tej zmiennej powoduje, �e por�wnanie nie zwraca duplikatu
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
	
	public static boolean checkMineUnderneathButton(int row, int col){ // metoda do sprawdzania czy wsp�rz�dne klawisza pokrywaj� si� ze wsp�rz�dnymi miny
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
	
	public static int countMineNeighborhood(int row, int col){ // metoda do sprawdzania czy w otoczeniu wci�ni�tego klawisza jest mina i inkrementowanie zmiennej je�eli ona wyst�puje
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
	
	public static Node getButtonByRowColumnIndex(int row, int column, GridPane grid_game_panel){ // metoda do zwracania obiektu klasy Node z konkretnego po�o�enia w grid panelu
	    Node result = null;
	    
	    ObservableList<Node> childrens = grid_game_panel.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }
	    return result;
	}
	
	public static boolean checkButtonByRowColumnIndex(int row, int column, GridPane grid_game_panel){ // 
	    boolean result = false;
	    
	    ObservableList<Node> childrens = grid_game_panel.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
	            result = true;
	            break;
	        }
	    }
	    return result;
	}
	
	/////////////////////
	// obs�uga zdarze� //
	/////////////////////		
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException, AWTException {
    	
    	button_mouse = (MouseButton)event.getButton();
    	button_mouse = event.getButton();
    	button_game_panel = (Button) event.getSource();
    	source = (Node) event.getSource();
    	
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);
   
//	    System.out.println("Rz�d: "+ row);
//	    System.out.println("Kolumna: "+ col);
        
        if(button_mouse == MouseButton.PRIMARY){ // naci�ni�cie lewego przycisku myszy
	        if(checkMineUnderneathButton(row, col) == true){
	        	
	        	button_game_panel.setText(mine);
	        	button_game_panel.setDisable(true);
//	        	System.out.println("MINA");
	        	
	        	Stage stageGameOver = (Stage) button_game_panel.getScene().getWindow();
//	        	Stage stageGameOver = new Stage();
	    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameOverView.fxml"));
	    		Scene sceneGameOver = new Scene(parent);
	    		stageGameOver.setScene(sceneGameOver);
	    		stageGameOver.setTitle("Game Over !");
	    		stageGameOver.setResizable(false);
	    		stageGameOver.show();
	    		
	        }else if(countMineNeighborhood(row, col) != 0){
	        	
	        	button_game_panel.setText(String.valueOf(qty_mine_neighborhood));
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	System.out.println(num_click_left);
	        	
	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
        
	        		timeStop= LocalTime.now();
	        		
	        		Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
//		        	Stage stageResult = new Stage();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        	
	        }else if(countMineNeighborhood(row, col) == 0){
	        	
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	System.out.println(num_click_left);
	        	
	        	// tutaj jest sekcja odpowiedzialna za odkrywanie p�l pustych
	        	// | | |
	        	// V V V
	        	
	        	int z = -1;
	        	
	        	ArrayList<Integer> rowList = new ArrayList<Integer>();
	        	ArrayList<Integer> colList = new ArrayList<Integer>();
	        	
	        	for(int i = z; i < Math.abs(z) + 1; i++){
	        		for(int j = z; j < Math.abs(z) + 1; j++){
	        			if(i != 0 || j != 0){
	        				
	        				int rowTemp = row + i;
	        				int colTemp = col + j;
	        				
					        if(countMineNeighborhood(rowTemp, colTemp) == 0){
					        	try{
						        	button_game_panel = (Button) getButtonByRowColumnIndex(rowTemp, colTemp, grid_game_panel);
							        if(!button_game_panel.isDisable()){
							        	button_game_panel.setDisable(true);
								        num_click_left++;
								        System.out.println(num_click_left);
							        }
					        	}catch(Exception e){
						        }
					        }
//					        else if((countMineNeighborhood(rowTemp, colTemp) != 0)){
//					        	button_game_panel.setText(String.valueOf(qty_mine_neighborhood));
//					        	button_game_panel.setDisable(true);
//					        	num_click_left++;
//					        	rowList.add(rowTemp);
//					        	colList.add(colTemp);
//					        	System.out.println(rowList);
//					        	System.out.println(colList);
//					        }
	        			}
	        		}
	        	}
	        	
//	        	int row_temp = -1;
//	        	int col_temp = -1;
//	        	
//	        	while(checkButtonByRowColumnIndex(row + row_temp, col + col_temp, grid_game_panel)){
//		        	for(int i = -1; i < 2; i++){
//		        		for(int j = -1; j < 2; j++){
//		        			if(i != 0 || j != 0){
//						        if(countMineNeighborhood(row + i, col + j) == 0){
//						        	try{
//							        	button_game_panel = (Button) getButtonByRowColumnIndex(row + i, col + j, grid_game_panel);
//								        row_temp = row + i;
//								        col_temp = col + j;
//								        break;
//								    }catch(Exception e){
//							        }
//						        }
//		        			}
//		        		}
//		        	}
//	        	}
	        	
		        // A A A
		        // | | |		
	        	// tutaj jest sekcja odpowiedzialna za odkrywanie p�l pustych

	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
	        		
	        		timeStop= LocalTime.now();
	        		
		        	Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
//		        	Stage stageResult = new Stage();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        }
	        
        }else if(button_mouse == MouseButton.SECONDARY){ // naci�ni�cie prawego przycisku myszy	
        	
        	if(event.getClickCount() == 1 && button_game_panel.getText().equals("")){ // pojedy�cze klikni�cie
        		button_game_panel.setText("X");
        		num_click_right++;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		System.out.println(num_click_right);	
        		
        	}else if(event.getClickCount() == 1 && button_game_panel.getText().equals("X")){ // podw�jne klikni�cie   	
        			button_game_panel.setText("");
        			num_click_right--;
        			lb_mine_suspected.setText(String.valueOf(num_click_right));
        			System.out.println(num_click_right);
        	}
        } 
    }

    @FXML
    void startGame(MouseEvent event) throws ClassNotFoundException, SQLException {	  
    	
    	user_name = tf_name.getText();
    	
    	qty_mine_overall_user_chooice = sp_qty_mine.getValue();
    	
    	Connection conn = db.Connection();
    	Statement stat = conn.createStatement();
    	ResultSet rs = stat.executeQuery("select * from results where user_name = '" + user_name + "' and qty_mine = " + qty_mine_overall_user_chooice + ";");
    	
    	if(user_name.equals("")){
    		
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("Podane imi�/nick ju� istnieje lub nie poda�e� �adnego");
    		a.setTitle("B��d");
    		a.setHeaderText("UWAGA!");
    		a.showAndWait();
    		
    	}else{
    		
    		if(rs.next()){
	    		double time_temp = rs.getDouble(3);
	    		Alert a = new Alert(AlertType.INFORMATION);
	    		a.setContentText("Witaj ponownie!\nTw�j dotychczasowy czas, w kategorii " + qty_mine_overall_user_chooice + " min, to: "+ time_temp +"\nDasz rad� go poprawi� ?");
	    		a.setTitle("Witamy");
	    		a.setHeaderText("DZI�KUJEMY");
	    		a.showAndWait();
    		}
    		
    		timeStart= LocalTime.now();
    		
        	grid_info_panel.setDisable(false);
        	grid_game_panel.setDisable(false);
        	
        	hb_name.setDisable(true);
        	hb_qty_mine.setDisable(true);
        	
        	btn_new_game.setDisable(true);
        	
        	Random gen = new Random();
    		
        	int row_random_coordinate; 
        	int col_random_coordinate;
	    	
	    	lb_qty_mine.setText(String.valueOf(qty_mine_overall_user_chooice));
	    	
	    	tab_location_mine = new int [qty_mine_overall_user_chooice][2];
	    	
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // p�tla do losowania koordynat�w po�o�enia kolejnych min
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10); 
	    			System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
		    			zero_zero++; // je�eli zostan� wlosowane wsp�rz�dne 0,0 to zwi�kszam zmienn� pomocnicz� o jeden 
		    		}
	    		}while(checkCoordiantesUnique(row_random_coordinate, col_random_coordinate)); // nast�pnie wylosowane tymczasowe zmienne sprawdzamy czy ju� nie by�y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je�eli jest true to jeszcze raz s� losowane zmienne i jeszcze raz sprawdzane w metodzie, je�eli false, to znaczy, �e mo�emy wpisa� te wsp�rz�dne do tablicy bo do pory nie by�y wylosowane
	    		tab_location_mine[i][0] = row_random_coordinate; // przypisanie zmiennych do miejsc w tabeli po wcze�niejszym sprawdzeniu czy by�y wcz�sniej wylosowane
	    		tab_location_mine[i][1] = col_random_coordinate;
	    	}
	    	
	    	// wypisuje wylosowan� lokalizacj� min w koncoli
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
	    		for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
	    			System.out.print(tab_location_mine[i][j] + " ");
	    		}
	    		System.out.println();
	    	}
    	}
    }
    
    ///////////////////////
    // metoda inicjuj�ca //
    ///////////////////////
    
    public void initialize(){
    	
    	db = new DBConnector();
    	
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
    	
    	sp_qty_mine.setValueFactory(valueFactory);
    
    }
}

