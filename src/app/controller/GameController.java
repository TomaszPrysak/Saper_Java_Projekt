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
    
	static int [][] tab_location_mine = new int[10][2]; // deklaracja tablicy przechowuj¹cej wspó³rzêdne losowe (x,y) min
	
	static int zero_zero = 0; // deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo¿liwoœci wyklosowania wspó³rzêdnych 0,0 jakiejœ miny
	
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
    
	public static boolean checkCoordiantesUnique(int row, int col){ // metoda do sprawdzania czy wylosowane tymczaswoe zminne oznaczaj¹ce wspó³órzêdne kolejnej miny znajduj¹ siê ju¿ w tablicy wspó³rzêdnych min
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ // UWAGA zmienna zero_zero jest bardzo wazna. Poniewa¿ na pocz¹tku programu deklarujê tablicê [10][2] to na wstêpie sk³ada siê ona z samych zer. Nastêpnie losujê wspó³rzêdne, jezeli wylosujê 0,0 to jest to po³o¿enie sprawdzane czy jest ju¿ w tablicy. Normalnie skoro tablica na pocz¹tku sk³ada siê z samy zer. To w koñu metoda znajdzie same zera i uzna, ¿e jest to duplikat. Dlatego w przypadku samych zer pierwsze porównanie nie mo¿e byæ uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczyæ iloœæ porównañ z tablic¹. Tylko wartoœæ = 1 tej zmiennej powoduje, ¿e porównanie nie zwraca duplikatu
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
	
	public static boolean checkMineUnderneathButton(int row, int col){ // metoda do sprawdzania czy wspó³rzêdne klawisza pokrywaj¹ siê ze wspó³rzêdnymi miny
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
	
	public static int countMineNeighborhood(int row, int col){ // metoda do sprawdzania czy w otoczeniu wciœniêtego klawisza jest mina i inkrementowanie zmiennej je¿eli ona wystêpuje
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
	
	public static Node getButtonByRowColumnIndex(int row, int column, GridPane grid_game_panel){ // metoda do zwracania obiektu klasy Node z konkretnego po³o¿enia w grid panelu
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
	// obs³uga zdarzeñ //
	/////////////////////		
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException, AWTException {
    	
    	button_mouse = (MouseButton)event.getButton();
    	button_mouse = event.getButton();
    	button_game_panel = (Button) event.getSource();
    	source = (Node) event.getSource();
    	
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);
   
//	    System.out.println("Rz¹d: "+ row);
//	    System.out.println("Kolumna: "+ col);
        
        if(button_mouse == MouseButton.PRIMARY){ // naciœniêcie lewego przycisku myszy
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
	        	
	        	// tutaj jest sekcja odpowiedzialna za odkrywanie pól pustych
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
	        	// tutaj jest sekcja odpowiedzialna za odkrywanie pól pustych

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
	        
        }else if(button_mouse == MouseButton.SECONDARY){ // naciœniêcie prawego przycisku myszy	
        	
        	if(event.getClickCount() == 1 && button_game_panel.getText().equals("")){ // pojedyñcze klikniêcie
        		button_game_panel.setText("X");
        		num_click_right++;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		System.out.println(num_click_right);	
        		
        	}else if(event.getClickCount() == 1 && button_game_panel.getText().equals("X")){ // podwójne klikniêcie   	
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
    		a.setContentText("Podane imiê/nick ju¿ istnieje lub nie poda³eœ ¿adnego");
    		a.setTitle("B³¹d");
    		a.setHeaderText("UWAGA!");
    		a.showAndWait();
    		
    	}else{
    		
    		if(rs.next()){
	    		double time_temp = rs.getDouble(3);
	    		Alert a = new Alert(AlertType.INFORMATION);
	    		a.setContentText("Witaj ponownie!\nTwój dotychczasowy czas, w kategorii " + qty_mine_overall_user_chooice + " min, to: "+ time_temp +"\nDasz radê go poprawiæ ?");
	    		a.setTitle("Witamy");
	    		a.setHeaderText("DZIÊKUJEMY");
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
	    	
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ // pêtla do losowania koordynatów po³o¿enia kolejnych min
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10); 
	    			System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
		    			zero_zero++; // je¿eli zostan¹ wlosowane wspó³rzêdne 0,0 to zwiêkszam zmienn¹ pomocnicz¹ o jeden 
		    		}
	    		}while(checkCoordiantesUnique(row_random_coordinate, col_random_coordinate)); // nastêpnie wylosowane tymczasowe zmienne sprawdzamy czy ju¿ nie by³y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode check - je¿eli jest true to jeszcze raz s¹ losowane zmienne i jeszcze raz sprawdzane w metodzie, je¿eli false, to znaczy, ¿e mo¿emy wpisaæ te wspó³rzêdne do tablicy bo do pory nie by³y wylosowane
	    		tab_location_mine[i][0] = row_random_coordinate; // przypisanie zmiennych do miejsc w tabeli po wczeœniejszym sprawdzeniu czy by³y wczêsniej wylosowane
	    		tab_location_mine[i][1] = col_random_coordinate;
	    	}
	    	
	    	// wypisuje wylosowan¹ lokalizacjê min w koncoli
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
	    		for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
	    			System.out.print(tab_location_mine[i][j] + " ");
	    		}
	    		System.out.println();
	    	}
    	}
    }
    
    ///////////////////////
    // metoda inicjuj¹ca //
    ///////////////////////
    
    public void initialize(){
    	
    	db = new DBConnector();
    	
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
    	
    	sp_qty_mine.setValueFactory(valueFactory);
    
    }
}

