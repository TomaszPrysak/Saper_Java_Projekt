package app.controller;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
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
    
    // deklaracja zmiennej przechowuj�cej warto�� ilo�ci min jak� wybra� gracz
    static int qty_mine_overall_user_chooice;
    
    // deklaracje zmiennej przechowuj�cej nazwe gracza
    static String user_name;
    
    // deklaracja zmiennej do przechowywania klikni�� przyci�ni�cia lewym klawiszem myszy i inicjowanie jej na pocz�tek jako 0 za pomoc� tej zmienej okre�lane jest kiedy rozgrywka ko�czy si� powodzeniem
    int num_click_left = 0;
    
    // deklaracja zmiennej do przechowywania klikni�� przyci�ni�cia prawym klawiszem myszy i inicjowanie jej na pocz�tek jako 0
    int num_click_right = 0;
    
    // deklaracja tablicy przechowuj�cej wsp�rz�dne losowe (x,y) min
	int [][] tab_location_mine = new int[10][2]; 
	
	// deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo�liwo�ci wyklosowania wsp�rz�dnych 0,0 jakiej� miny
	int zero_zero = 0; 
	
	// deklaracja zmiennej przechowuj�cej warto�� ilo�ci min dooko�a sprawdzanego pola, czy to wybranego przez gracza czy to przez metod� do odkrywania p�l
	int qty_mine_neighborhood;
	
	// deklaracja zmiennej do �apania czasu rozpocz�cia rozgrywki
	static LocalTime timeStart;
	
	// deklaracja zmiennej do �apania czasu zako�czenia rozgrywki
	static LocalTime timeStop;
	
	// deklaracja zmiennej przechowuj�cej obiekt klasy MouseButton - przycisk myszy
	MouseButton button_mouse;
	
	// deklaracja zmiennej przechowuj�cej obiekt klasy Button - przycisk�w umieszczonych na plansz
	Button button_game_panel;
	
	// deklaracja zmiennej przechowuj�cej obiekt klasy Node - obiekt zr�d�o gridpanel
    Node source;
	
    ////////////
	// metody //
    ////////////
    
    // metoda do sprawdzania czy wylosowane tymczasowe zminne oznaczaj�ce wsp��rz�dne kolejnej miny znajduj� si� ju� w tablicy wsp�rz�dnych min
    // zmienna zero_zero jest bardzo wazna. Poniewa� na pocz�tku programu deklaruj� tablic� [10][2] to na wst�pie sk�ada si� ona z samych zer. Nast�pnie losuj� wsp�rz�dne, je�eli wylosuj� 0,0 to sprawdza czy po�o�enie to jest ju� w tablicy. Normalnie skoro tablica na pocz�tku sk�ada si� z samy zer to metoda znajdzie same zera i uzna, �e jest to duplikat. Dlatego w przypadku samych zer pierwsze por�wnanie nie mo�e by� uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczy� ilo�� por�wna� z tablic�. Tylko warto�� tej zmienej = 1 powoduje, �e por�wnanie nie zwraca duplikatu
    // metoda zwraca warto�� boolean czy powt�rzy�y si� zmienne miny
    public boolean checkCoordiantesUnique(int row, int col){ 
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ 
    			check = true;
    			
    			// wypisanie, �e wylosowane wsp�rzedne si� dubluj�, pomocne pryz testach
    			// System.out.println("IDENTYCZNE");
    			// System.out.print("Wylosowano ponownie: ");
    			
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	// metoda blizniacza do powy�szej, ta natomiast s�y�y do sprawdzania czy wsp�rz�dne pola kt�re odkrywatmy pokrywaj� si� ze wsp�rz�dnymi miny
    // jednak w tej metodzie nie ma w warunku zmiennej zer_zer
    // metoda zwraca wart�� boolean czy pole odkrywana przez gracza skrywa min�
	public boolean checkMineUnderneathButton(int row, int col){ 
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
	
	// metoda do sprawdzania czy w otoczeniu, s�siedztwie wci�ni�tego pola jest mina i inkrementowanie zmiennej je�eli ona wyst�puje w celu wy�wietlenia ile min znajduje si� dooko�a wci�ni�tego pola
	// metoda zwraca warto�� integer przechowuj�c� ilo�� min dooko�a sprawdzanego pola
	public int countMineNeighborhood(int row, int col){ 
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
	
	// metoda do zwracania obiektu klasy Node z konkretnego po�o�enia w grid panelu
	// w trakcie odrywania p�l na planszy tworzony jest obiekt klasy Node a nast�pnie obiekt klasy Button w celu dokonywania na nim operacji (disable), jednak to jest dokonywane w momencie przyciskania na pole z przyciskiem 
	// aby mo�na by�o dokonywa� operacji na przyciskach znajduj�cych si� na s�siednich polach trzeba najpierw pobra� wsp�rz�dne i na ich podstawie tworzony jest obiekt klasy Node po to aby mo�na by�o utworzy� obiek Button i na nim dzia�a� bez konieczno�ci klikania w niego
	// metoda zwraca obiekt Node
	public Node getButtonByRowColumnIndex(int row, int column, GridPane grid_game_panel){ 
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
	
	// metoda do blizniacza do powy�szej i dzia�a na tej samej zasadzie
	// metoda zwraca warto�� boolean czy na klikni�tym polu mo�na utworzy� obiekt Node i nast�pnie Button. Metoda g��niwe dotyczy p�l odkrywanych na brzegach planszy. Nie b�dzie on mie� s�siedztwa z ka�dej strony.
	public boolean checkButtonByRowColumnIndex(int row, int column, GridPane grid_game_panel){ // 
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
	
	// metoda ta jest wywo�ywana podczas odkyrwania pola kt�re jest puste
	// jako argumenty metody przekazywane s� wsp�rz�dne po�o�enia odkrywanego pola
	// nast�pnie z wykorzystaniem dw�ch p�tli for dokonywany jest przegl�d s�siednich p�l tego pola kt�e odkrywamy
	// przegl�dn nastepuje poprzez dodawanie do wsp�rzednej okre�laj�cej wiersz odpowiednio -1, 0 i +1 oraz wsp�rz�dnej okre�laj�cej kolumn� r�wnie� -1, 0, +1
	// zapisywane s� one do pomocniczych zmiennych
	// dla wsp�rzednych otrzymanych z dodawania warto�ci z p�tli za pomoc� metody checkButtonByRowColumnIndex sprawdzane jest czy na polu (s�siednim) mo�na utworzy� obiekt klasy Node sprawdzana jest 
	// nastepnie pomijane jest pole 0,0 aby nie liczy� pola fizycznie wybranego przez gracza
	// nast�pnie tworzony jest obiekt Button na wsp�rz�dnych s�siada z wykorzystaniem metody getButtonByRowColumnIndex
	// sprawdzane jest czy s�siad dooko�a siebie posiada miny
	// je�eli zwr�cenie przez metod� countMineNeighborhood wartos� = 0 i dodatkowo sprawdzenie czy pole nie jest ju� nieaktywne dokonywane jest wy��czenie pola i jeszcze raz uruchomienie metody samej siebie tylko z nowymi wsp�rzednymi - i tak do skutku a�, wszystkie s�siednie pola kt�re s� puste zostan� odkryte w mi�dzyczasie inkrementowana jest zmienna przechowuj�ca klikni�cia myszk� - lewym klawiszem
	// je�eli zwr�cenie przez metod� countMineNeighborhood wartos� != 0 i dodatkowo sprawdzenie czy pole nie jest ju� nieaktywne dokonywane jest jedynie odkrycie pola z umieszczeniem w nim warto�� ile min znajduje si� dooko�a, NIE JEST WYWO�YWANA KOLEJNY RAZ METODA tylko p�tla for leci dalej
	public void nodeAround(int row, int col){
		for(int i = -1; i < 2; i++){
    		for(int j = -1; j < 2; j++){
    			int rowTemp = row + i;
				int colTemp = col + j;
    			if(checkButtonByRowColumnIndex(rowTemp, colTemp, grid_game_panel)){
        			if(i != 0 || j != 0){
        				button_game_panel = (Button) getButtonByRowColumnIndex(rowTemp, colTemp, grid_game_panel);
        				if(countMineNeighborhood(rowTemp, colTemp) == 0){
        					if(!button_game_panel.isDisable()){
        						button_game_panel.setDisable(true);
        						num_click_left++;
        						
        						// wyspisuje w konsoli imitowane klikni�cia myszk�, pomocne przy testach
        						// System.out.println(num_click_left);
        						
        						nodeAround(rowTemp, colTemp);
        					}
        				}
        				else if((countMineNeighborhood(rowTemp, colTemp) != 0)){
        					if(!button_game_panel.isDisable()){
        						button_game_panel.setText(String.valueOf(qty_mine_neighborhood));
        						button_game_panel.setDisable(true);
        						num_click_left++;
        						
        						// wyspisuje w konsoli imitowane klikni�cia myszk�, pomocne przy testach
        						// System.out.println(num_click_left);
        					}
        				}
        			}
        		}
    		}
    	}
	}
	
	/////////////////////
	// obs�uga zdarze� //
	/////////////////////		
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException, AWTException {
    	
    	// tworzenie obiekt�w wymaganych przy naciskaniu klawiszem myszy oraz przycisk�w na grid panelu
    	button_mouse = (MouseButton)event.getButton();
    	button_mouse = event.getButton();
    	button_game_panel = (Button) event.getSource();
    	source = (Node) event.getSource();
    	
    	// pobieranie wsp�ednych z pola wci�ni�tego przez gracza
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);
   
        // wy�wietlanie wsp�rz�dnych wci�ni�tego przez gracza pola, pomocne w testach
	    // System.out.println("Rz�d: "+ row);
	    // System.out.println("Kolumna: "+ col);
        
	    // naci�ni�cie lewego przycisku myszy
        if(button_mouse == MouseButton.PRIMARY){ 
        	
        	// sprawdza przy pomocy metody checkMineUnderneathButton czy wsp�rz�dne z pola odkrywanego pokrywaj� si� ze wsp�rzednymi miny
	        if(checkMineUnderneathButton(row, col)){
	        	
	        	//je�eli tak to:
	        	// po naci�ni�ciu pola jest ono dezaktywowane
	        	button_game_panel.setDisable(true);
	        	
	        	// wy�wietlane jest okno o przegranej i wy��czane jest okno gry
	        	Stage stageGameOver = (Stage) button_game_panel.getScene().getWindow();
	    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameOverView.fxml"));
	    		Scene sceneGameOver = new Scene(parent);
	    		stageGameOver.setScene(sceneGameOver);
	    		stageGameOver.setTitle("Game Over !");
	    		stageGameOver.setResizable(false);
	    		stageGameOver.show();
	    	
	    	// je�eli nie ma miny pod polem to sprawdza przy pomocy metody countMineNeighborhood ile jest min dooko�a pola odkrywanego przez gracza
	        }else if(countMineNeighborhood(row, col) != 0){
	        	
	        	// je�eli warto�� r�na od 0, co oznacza, �e znajduj� si� jakie� miny
	        	// pole jest dezaktywowane i wy�wietlana jest na nim informacja z ilo�ci� min dooko�a niego oraz inkrementowana jest zmienna przechowuj�ca ilo�� klikni�� myszy - lewym przyciskiem
	        	button_game_panel.setText(String.valueOf(qty_mine_neighborhood)); 
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	
	        	// wy�wietla ilo�� klikni�� myszy, pomocne przy testach
	        	// System.out.println(num_click_left);
	        	
	        	// sprawdza czy suma ilo�ci klikni�� i min ustawionych przez gracza r�wna jest 100 - bo tyle jest p�l na planszy
	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
	        		
	        		// je�eli tak:
	        		// pobieranie czasu ko�ca rozgrywki
	        		timeStop= LocalTime.now();
	        		
	        		// zamykanie okna gry i wy�wietlanie okna z wygran� i rezultatami
	        		Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        	
	        	// je�eli nie ma miny pod polem to sprawdza przy pomocy metody countMineNeighborhood ile jest min dooko�a pola odkrywanego przez gracza
	        }else if(countMineNeighborhood(row, col) == 0){
	        	
	        	// je�eli warto�� r�wna 0, co oznacza, �e nie ma �adnych min
	        	// to pole jest dezaktywowane i inkrementowana jest zmienna przechowuj�ca warto�� klikni�� lewego przycisku myszy 
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	
	        	// wy�wietla ilo�� klikni�� myszy, pomocne przy testach
	        	// System.out.println(num_click_left);
	        	
	        	// pole jest puste wi�c wywo�ywana jest metoda do odkrywania p�l s�siednich
	        	nodeAround(row, col);

	        	// sprawdza czy suma ilo�ci klikni�� i min ustawionych przez gracza r�wna jest 100 - bo tyle jest p�l na planszy
	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
	        		
	        		// je�eli tak:
	        		// pobieranie czasu ko�ca rozgrywki
	        		timeStop= LocalTime.now();
	        		
	        		// zamykanie okna gry i wy�wietlanie okna z wygran� i rezultatami
		        	Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        }
	        
	    // naci�ni�cie prawego przycisku myszy	   
        }else if(button_mouse == MouseButton.SECONDARY){
        	
        	// je�eli jest pojedy�cze klikni�cie i przycisk znajduj�cy si� na polu kt�re klikamy nie ma napisu
        	if(event.getClickCount() == 1 && button_game_panel.getText().equals("")){
        		
        		// to na przycisku stawiany jest X jako oznaczenie, �e tutaj spodziewamy si� miny
        		// inkrementowana jest zmienna przechowuj�ca klikni�cia prawym przyciskiem myszy
        		// wy�weitlana jest w polu informacyjnym gry, �e spodziewamy si� kolejnej miny
        		button_game_panel.setText("X");
        		num_click_right++;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		
        		// wy�wietla ilo�� klikni�� myszy, pomocne przy testach
        		// System.out.println(num_click_right);	
        		
        	// je�eli jest podw�jne klikni�cie i przycisk zawiera napis X
        	}else if(event.getClickCount() == 1 && button_game_panel.getText().equals("X")){
        		
        		// to usuwany jest napis X jako decyzja o odznaczeniu tego przycisku, �e jednak nie spodziwamy si� tutaj miny
        		// dekrementowana jest zmienna przechowuj�ca warto�� klikni�� prawym przyciskiem myszy
        		// o jeden mniejsza jest informacja o spodziwancyh przez gracza minach z polu informacyjnym, �e spodziewamy si� miny
        		button_game_panel.setText("");
        		num_click_right--;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		
        		// wy�wietla ilo�� klikni�� myszy, pomocne przy testach
        		//System.out.println(num_click_right);
        	}
        } 
    }

    @FXML
    void startGame(MouseEvent event) throws ClassNotFoundException, SQLException {	  
    	
    	// przypisujemy do zmiennej nazw� gracza
    	user_name = tf_name.getText();
    	
    	// przypisywana do zmiennej warto�ci ilo�ci min wybranych przez gracza
    	qty_mine_overall_user_chooice = sp_qty_mine.getValue();
    	
    	// po��czenie z baz� danych aby sprawdzi� czy gracz instnieje w bazie danych gry
    	Connection conn = db.Connection();
    	Statement stat = conn.createStatement();
    	ResultSet rs = stat.executeQuery("select * from results where user_name = '" + user_name + "' and qty_mine = " + qty_mine_overall_user_chooice + ";");
    	
    	// je�eli gracz nie poda� nazwy
    	if(user_name.equals("")){
    		
    		// to wy�weitlany jest komunikat o konieczno�ci podania nazwy u�ytkownika
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("Podane imi�/nick ju� istnieje lub nie poda�e� �adnego");
    		a.setTitle("B��d");
    		a.setHeaderText("UWAGA!");
    		a.showAndWait();
    		
    	}else{
    		
    		// je�eli poda� jak�� nazw� to sprawdzane jest aktualne osi�gni�cie gracza i wy�wietlenie informacji czy uda mu si� pobi� aktualny rekord
    		if(rs.next()){
	    		double time_temp = rs.getDouble(3);
	    		Alert a = new Alert(AlertType.INFORMATION);
	    		a.setContentText("Witaj ponownie!\nTw�j dotychczasowy czas, w kategorii " + qty_mine_overall_user_chooice + " min, to: "+ time_temp +"\nDasz rad� go poprawi� ?");
	    		a.setTitle("Witamy");
	    		a.setHeaderText("DZI�KUJEMY");
	    		a.showAndWait();
    		}
    		
    		// pobieranie czasu pocz�tku rozgrywki
    		timeStart= LocalTime.now();
    		
    		// aktywowany panel gry
        	grid_info_panel.setDisable(false);
        	grid_game_panel.setDisable(false);
        	
        	// dezaktywowany panel z nazw� gracza i wyborem ilo�ci min oraz przyciskiem GRAJ
        	hb_name.setDisable(true);
        	hb_qty_mine.setDisable(true);
        	btn_new_game.setDisable(true);
        	
        	// utworzenie obiektu klasy Random do losowania po�o�enia min
        	Random gen = new Random();
    		
        	// deklarowanie zmiennych do losowania wsp�rz�dnych
        	int row_random_coordinate; 
        	int col_random_coordinate;
	    	
        	// ustawienie pola tekstowego z ilo�ci� wybranych min
	    	lb_qty_mine.setText(String.valueOf(qty_mine_overall_user_chooice));
	    	
	    	// stworzenie tablicy o wymiarze ilo�ci wybranych min
	    	tab_location_mine = new int [qty_mine_overall_user_chooice][2];
	    	
	    	// p�tla do losowania wsp�rz�dnych po�o�enia kolejnych min
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ 
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10);
	    			
	    			// wypisanie wsp�rzednych wylosowanych min, pomocne przy testach
	    			// System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
	    			
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
		    			zero_zero++; // je�eli zostan� wlosowane wsp�rz�dne 0,0 to zwi�kszam zmienn� pomocnicz� o jeden 
		    		}
		    	// nast�pnie wylosowane tymczasowe zmienne sprawdzamy czy ju� nie by�y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode checkCoordiantesUnique - je�eli jest true to jeszcze raz s� losowane zmienne i jeszcze raz sprawdzane w metodzie, je�eli false, to znaczy, �e mo�emy wpisa� te wsp�rz�dne do tablicy bo do pory nie by�y wylosowane
	    		}while(checkCoordiantesUnique(row_random_coordinate, col_random_coordinate));
	    		
	    		// przypisanie zmiennych do miejsc w tabeli po wcze�niejszym sprawdzeniu czy by�y wcz�sniej wylosowane
	    		tab_location_mine[i][0] = row_random_coordinate; 
	    		tab_location_mine[i][1] = col_random_coordinate;
	    	}
	    	
	    	// wypisuje wylosowan� lokalizacj� min w konsoli, pomocne przy testach
	    	// for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
	    	// 	for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
	    	// 		System.out.print(tab_location_mine[i][j] + " ");
	    	// 	}
	    	// 	System.out.println();
	    	// }
    	}
    }
    
    ///////////////////////
    // metoda inicjuj�ca //
    ///////////////////////
    
    public void initialize(){
    	
    	db = new DBConnector();
    	
    	// utworzenie obiektu klasy Spinner za pomoc� kt�rego u�ytkownik b�dzie wybiera� ilo�� min
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
    	
    	// przypisanie do Spinera obiektu utworzonego powy�ej
    	sp_qty_mine.setValueFactory(valueFactory);
    
    }
}

