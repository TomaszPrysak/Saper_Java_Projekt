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
    
    // deklaracja zmiennej przechowuj¹cej wartoœæ iloœci min jak¹ wybra³ gracz
    static int qty_mine_overall_user_chooice;
    
    // deklaracje zmiennej przechowuj¹cej nazwe gracza
    static String user_name;
    
    // deklaracja zmiennej do przechowywania klikniêæ przyciœniêcia lewym klawiszem myszy i inicjowanie jej na pocz¹tek jako 0 za pomoc¹ tej zmienej okreœlane jest kiedy rozgrywka koñczy siê powodzeniem
    int num_click_left = 0;
    
    // deklaracja zmiennej do przechowywania klikniêæ przyciœniêcia prawym klawiszem myszy i inicjowanie jej na pocz¹tek jako 0
    int num_click_right = 0;
    
    // deklaracja tablicy przechowuj¹cej wspó³rzêdne losowe (x,y) min
	int [][] tab_location_mine = new int[10][2]; 
	
	// deklaracja i inicjalizacja zmiennej wykorzystywanej w celu mo¿liwoœci wyklosowania wspó³rzêdnych 0,0 jakiejœ miny
	int zero_zero = 0; 
	
	// deklaracja zmiennej przechowuj¹cej wartoœæ iloœci min dooko³a sprawdzanego pola, czy to wybranego przez gracza czy to przez metodê do odkrywania pól
	int qty_mine_neighborhood;
	
	// deklaracja zmiennej do ³apania czasu rozpoczêcia rozgrywki
	static LocalTime timeStart;
	
	// deklaracja zmiennej do ³apania czasu zakoñczenia rozgrywki
	static LocalTime timeStop;
	
	// deklaracja zmiennej przechowuj¹cej obiekt klasy MouseButton - przycisk myszy
	MouseButton button_mouse;
	
	// deklaracja zmiennej przechowuj¹cej obiekt klasy Button - przycisków umieszczonych na plansz
	Button button_game_panel;
	
	// deklaracja zmiennej przechowuj¹cej obiekt klasy Node - obiekt zród³o gridpanel
    Node source;
	
    ////////////
	// metody //
    ////////////
    
    // metoda do sprawdzania czy wylosowane tymczasowe zminne oznaczaj¹ce wspó³órzêdne kolejnej miny znajduj¹ siê ju¿ w tablicy wspó³rzêdnych min
    // zmienna zero_zero jest bardzo wazna. Poniewa¿ na pocz¹tku programu deklarujê tablicê [10][2] to na wstêpie sk³ada siê ona z samych zer. Nastêpnie losujê wspó³rzêdne, je¿eli wylosujê 0,0 to sprawdza czy po³o¿enie to jest ju¿ w tablicy. Normalnie skoro tablica na pocz¹tku sk³ada siê z samy zer to metoda znajdzie same zera i uzna, ¿e jest to duplikat. Dlatego w przypadku samych zer pierwsze porównanie nie mo¿e byæ uznane za duplikat. I do tego jest potrzebna ta zmienna pomocnicza zero_zero, aby zliczyæ iloœæ porównañ z tablic¹. Tylko wartoœæ tej zmienej = 1 powoduje, ¿e porównanie nie zwraca duplikatu
    // metoda zwraca wartoœæ boolean czy powtórzy³y siê zmienne miny
    public boolean checkCoordiantesUnique(int row, int col){ 
		boolean check = false;
		for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
    		if(tab_location_mine[i][0] == row && tab_location_mine[i][1] == col && zero_zero != 1){ 
    			check = true;
    			
    			// wypisanie, ¿e wylosowane wspó³rzedne siê dubluj¹, pomocne pryz testach
    			// System.out.println("IDENTYCZNE");
    			// System.out.print("Wylosowano ponownie: ");
    			
    			break;
			}else{
				check = false;
			}
    	}
		return check;
	}
	
	// metoda blizniacza do powy¿szej, ta natomiast s³y¿y do sprawdzania czy wspó³rzêdne pola które odkrywatmy pokrywaj¹ siê ze wspó³rzêdnymi miny
    // jednak w tej metodzie nie ma w warunku zmiennej zer_zer
    // metoda zwraca wartœæ boolean czy pole odkrywana przez gracza skrywa minê
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
	
	// metoda do sprawdzania czy w otoczeniu, s¹siedztwie wciœniêtego pola jest mina i inkrementowanie zmiennej je¿eli ona wystêpuje w celu wyœwietlenia ile min znajduje siê dooko³a wciœniêtego pola
	// metoda zwraca wartoœæ integer przechowuj¹c¹ iloœæ min dooko³a sprawdzanego pola
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
	
	// metoda do zwracania obiektu klasy Node z konkretnego po³o¿enia w grid panelu
	// w trakcie odrywania pól na planszy tworzony jest obiekt klasy Node a nastêpnie obiekt klasy Button w celu dokonywania na nim operacji (disable), jednak to jest dokonywane w momencie przyciskania na pole z przyciskiem 
	// aby mo¿na by³o dokonywaæ operacji na przyciskach znajduj¹cych siê na s¹siednich polach trzeba najpierw pobraæ wspó³rzêdne i na ich podstawie tworzony jest obiekt klasy Node po to aby mo¿na by³o utworzyæ obiek Button i na nim dzia³aæ bez koniecznoœci klikania w niego
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
	
	// metoda do blizniacza do powy¿szej i dzia³a na tej samej zasadzie
	// metoda zwraca wartoœæ boolean czy na klikniêtym polu mo¿na utworzyæ obiekt Node i nastêpnie Button. Metoda g³óniwe dotyczy pól odkrywanych na brzegach planszy. Nie bêdzie on mieæ s¹siedztwa z ka¿dej strony.
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
	
	// metoda ta jest wywo³ywana podczas odkyrwania pola które jest puste
	// jako argumenty metody przekazywane s¹ wspó³rzêdne po³o¿enia odkrywanego pola
	// nastêpnie z wykorzystaniem dwóch pêtli for dokonywany jest przegl¹d s¹siednich pól tego pola któe odkrywamy
	// przegl¹dn nastepuje poprzez dodawanie do wspó³rzednej okreœlaj¹cej wiersz odpowiednio -1, 0 i +1 oraz wspó³rzêdnej okreœlaj¹cej kolumnê równie¿ -1, 0, +1
	// zapisywane s¹ one do pomocniczych zmiennych
	// dla wspó³rzednych otrzymanych z dodawania wartoœci z pêtli za pomoc¹ metody checkButtonByRowColumnIndex sprawdzane jest czy na polu (s¹siednim) mo¿na utworzyæ obiekt klasy Node sprawdzana jest 
	// nastepnie pomijane jest pole 0,0 aby nie liczyæ pola fizycznie wybranego przez gracza
	// nastêpnie tworzony jest obiekt Button na wspó³rzêdnych s¹siada z wykorzystaniem metody getButtonByRowColumnIndex
	// sprawdzane jest czy s¹siad dooko³a siebie posiada miny
	// je¿eli zwrócenie przez metodê countMineNeighborhood wartosæ = 0 i dodatkowo sprawdzenie czy pole nie jest ju¿ nieaktywne dokonywane jest wy³¹czenie pola i jeszcze raz uruchomienie metody samej siebie tylko z nowymi wspó³rzednymi - i tak do skutku a¿, wszystkie s¹siednie pola które s¹ puste zostan¹ odkryte w miêdzyczasie inkrementowana jest zmienna przechowuj¹ca klikniêcia myszk¹ - lewym klawiszem
	// je¿eli zwrócenie przez metodê countMineNeighborhood wartosæ != 0 i dodatkowo sprawdzenie czy pole nie jest ju¿ nieaktywne dokonywane jest jedynie odkrycie pola z umieszczeniem w nim wartoœæ ile min znajduje siê dooko³a, NIE JEST WYWO£YWANA KOLEJNY RAZ METODA tylko pêtla for leci dalej
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
        						
        						// wyspisuje w konsoli imitowane klikniêcia myszk¹, pomocne przy testach
        						// System.out.println(num_click_left);
        						
        						nodeAround(rowTemp, colTemp);
        					}
        				}
        				else if((countMineNeighborhood(rowTemp, colTemp) != 0)){
        					if(!button_game_panel.isDisable()){
        						button_game_panel.setText(String.valueOf(qty_mine_neighborhood));
        						button_game_panel.setDisable(true);
        						num_click_left++;
        						
        						// wyspisuje w konsoli imitowane klikniêcia myszk¹, pomocne przy testach
        						// System.out.println(num_click_left);
        					}
        				}
        			}
        		}
    		}
    	}
	}
	
	/////////////////////
	// obs³uga zdarzeñ //
	/////////////////////		
	
    @FXML
    void showWhatIsUnderneath(MouseEvent event) throws IOException, AWTException {
    	
    	// tworzenie obiektów wymaganych przy naciskaniu klawiszem myszy oraz przycisków na grid panelu
    	button_mouse = (MouseButton)event.getButton();
    	button_mouse = event.getButton();
    	button_game_panel = (Button) event.getSource();
    	source = (Node) event.getSource();
    	
    	// pobieranie wspó³¿ednych z pola wciœniêtego przez gracza
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);
   
        // wyœwietlanie wspó³rzêdnych wciœniêtego przez gracza pola, pomocne w testach
	    // System.out.println("Rz¹d: "+ row);
	    // System.out.println("Kolumna: "+ col);
        
	    // naciœniêcie lewego przycisku myszy
        if(button_mouse == MouseButton.PRIMARY){ 
        	
        	// sprawdza przy pomocy metody checkMineUnderneathButton czy wspó³rzêdne z pola odkrywanego pokrywaj¹ siê ze wspó³rzednymi miny
	        if(checkMineUnderneathButton(row, col)){
	        	
	        	//je¿eli tak to:
	        	// po naciœniêciu pola jest ono dezaktywowane
	        	button_game_panel.setDisable(true);
	        	
	        	// wyœwietlane jest okno o przegranej i wy³¹czane jest okno gry
	        	Stage stageGameOver = (Stage) button_game_panel.getScene().getWindow();
	    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameOverView.fxml"));
	    		Scene sceneGameOver = new Scene(parent);
	    		stageGameOver.setScene(sceneGameOver);
	    		stageGameOver.setTitle("Game Over !");
	    		stageGameOver.setResizable(false);
	    		stageGameOver.show();
	    	
	    	// je¿eli nie ma miny pod polem to sprawdza przy pomocy metody countMineNeighborhood ile jest min dooko³a pola odkrywanego przez gracza
	        }else if(countMineNeighborhood(row, col) != 0){
	        	
	        	// je¿eli wartoœæ ró¿na od 0, co oznacza, ¿e znajduj¹ siê jakieœ miny
	        	// pole jest dezaktywowane i wyœwietlana jest na nim informacja z iloœci¹ min dooko³a niego oraz inkrementowana jest zmienna przechowuj¹ca iloœæ klikniêæ myszy - lewym przyciskiem
	        	button_game_panel.setText(String.valueOf(qty_mine_neighborhood)); 
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	
	        	// wyœwietla iloœæ klikniêæ myszy, pomocne przy testach
	        	// System.out.println(num_click_left);
	        	
	        	// sprawdza czy suma iloœci klikniêæ i min ustawionych przez gracza równa jest 100 - bo tyle jest pól na planszy
	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
	        		
	        		// je¿eli tak:
	        		// pobieranie czasu koñca rozgrywki
	        		timeStop= LocalTime.now();
	        		
	        		// zamykanie okna gry i wyœwietlanie okna z wygran¹ i rezultatami
	        		Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        	
	        	// je¿eli nie ma miny pod polem to sprawdza przy pomocy metody countMineNeighborhood ile jest min dooko³a pola odkrywanego przez gracza
	        }else if(countMineNeighborhood(row, col) == 0){
	        	
	        	// je¿eli wartoœæ równa 0, co oznacza, ¿e nie ma ¿adnych min
	        	// to pole jest dezaktywowane i inkrementowana jest zmienna przechowuj¹ca wartoœæ klikniêæ lewego przycisku myszy 
	        	button_game_panel.setDisable(true);
	        	num_click_left++;
	        	
	        	// wyœwietla iloœæ klikniêæ myszy, pomocne przy testach
	        	// System.out.println(num_click_left);
	        	
	        	// pole jest puste wiêc wywo³ywana jest metoda do odkrywania pól s¹siednich
	        	nodeAround(row, col);

	        	// sprawdza czy suma iloœci klikniêæ i min ustawionych przez gracza równa jest 100 - bo tyle jest pól na planszy
	        	if((num_click_left + qty_mine_overall_user_chooice) == 100){
	        		
	        		// je¿eli tak:
	        		// pobieranie czasu koñca rozgrywki
	        		timeStop= LocalTime.now();
	        		
	        		// zamykanie okna gry i wyœwietlanie okna z wygran¹ i rezultatami
		        	Stage stageResult = (Stage) button_game_panel.getScene().getWindow();
		    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/SuccessView.fxml"));
		    		Scene sceneResult = new Scene(parent);
		    		stageResult.setScene(sceneResult);
		    		stageResult.setTitle("Brawo!");
		    		stageResult.setResizable(false);
		    		stageResult.show();
	        	}
	        }
	        
	    // naciœniêcie prawego przycisku myszy	   
        }else if(button_mouse == MouseButton.SECONDARY){
        	
        	// je¿eli jest pojedyñcze klikniêcie i przycisk znajduj¹cy siê na polu które klikamy nie ma napisu
        	if(event.getClickCount() == 1 && button_game_panel.getText().equals("")){
        		
        		// to na przycisku stawiany jest X jako oznaczenie, ¿e tutaj spodziewamy siê miny
        		// inkrementowana jest zmienna przechowuj¹ca klikniêcia prawym przyciskiem myszy
        		// wyœweitlana jest w polu informacyjnym gry, ¿e spodziewamy siê kolejnej miny
        		button_game_panel.setText("X");
        		num_click_right++;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		
        		// wyœwietla iloœæ klikniêæ myszy, pomocne przy testach
        		// System.out.println(num_click_right);	
        		
        	// je¿eli jest podwójne klikniêcie i przycisk zawiera napis X
        	}else if(event.getClickCount() == 1 && button_game_panel.getText().equals("X")){
        		
        		// to usuwany jest napis X jako decyzja o odznaczeniu tego przycisku, ¿e jednak nie spodziwamy siê tutaj miny
        		// dekrementowana jest zmienna przechowuj¹ca wartoœæ klikniêæ prawym przyciskiem myszy
        		// o jeden mniejsza jest informacja o spodziwancyh przez gracza minach z polu informacyjnym, ¿e spodziewamy siê miny
        		button_game_panel.setText("");
        		num_click_right--;
        		lb_mine_suspected.setText(String.valueOf(num_click_right));
        		
        		// wyœwietla iloœæ klikniêæ myszy, pomocne przy testach
        		//System.out.println(num_click_right);
        	}
        } 
    }

    @FXML
    void startGame(MouseEvent event) throws ClassNotFoundException, SQLException {	  
    	
    	// przypisujemy do zmiennej nazwê gracza
    	user_name = tf_name.getText();
    	
    	// przypisywana do zmiennej wartoœci iloœci min wybranych przez gracza
    	qty_mine_overall_user_chooice = sp_qty_mine.getValue();
    	
    	// po³¹czenie z baz¹ danych aby sprawdziæ czy gracz instnieje w bazie danych gry
    	Connection conn = db.Connection();
    	Statement stat = conn.createStatement();
    	ResultSet rs = stat.executeQuery("select * from results where user_name = '" + user_name + "' and qty_mine = " + qty_mine_overall_user_chooice + ";");
    	
    	// je¿eli gracz nie poda³ nazwy
    	if(user_name.equals("")){
    		
    		// to wyœweitlany jest komunikat o koniecznoœci podania nazwy u¿ytkownika
    		Alert a = new Alert(AlertType.WARNING);
    		a.setContentText("Podane imiê/nick ju¿ istnieje lub nie poda³eœ ¿adnego");
    		a.setTitle("B³¹d");
    		a.setHeaderText("UWAGA!");
    		a.showAndWait();
    		
    	}else{
    		
    		// je¿eli poda³ jak¹œ nazwê to sprawdzane jest aktualne osi¹gniêcie gracza i wyœwietlenie informacji czy uda mu siê pobiæ aktualny rekord
    		if(rs.next()){
	    		double time_temp = rs.getDouble(3);
	    		Alert a = new Alert(AlertType.INFORMATION);
	    		a.setContentText("Witaj ponownie!\nTwój dotychczasowy czas, w kategorii " + qty_mine_overall_user_chooice + " min, to: "+ time_temp +"\nDasz radê go poprawiæ ?");
	    		a.setTitle("Witamy");
	    		a.setHeaderText("DZIÊKUJEMY");
	    		a.showAndWait();
    		}
    		
    		// pobieranie czasu pocz¹tku rozgrywki
    		timeStart= LocalTime.now();
    		
    		// aktywowany panel gry
        	grid_info_panel.setDisable(false);
        	grid_game_panel.setDisable(false);
        	
        	// dezaktywowany panel z nazw¹ gracza i wyborem iloœci min oraz przyciskiem GRAJ
        	hb_name.setDisable(true);
        	hb_qty_mine.setDisable(true);
        	btn_new_game.setDisable(true);
        	
        	// utworzenie obiektu klasy Random do losowania po³o¿enia min
        	Random gen = new Random();
    		
        	// deklarowanie zmiennych do losowania wspó³rzêdnych
        	int row_random_coordinate; 
        	int col_random_coordinate;
	    	
        	// ustawienie pola tekstowego z iloœci¹ wybranych min
	    	lb_qty_mine.setText(String.valueOf(qty_mine_overall_user_chooice));
	    	
	    	// stworzenie tablicy o wymiarze iloœci wybranych min
	    	tab_location_mine = new int [qty_mine_overall_user_chooice][2];
	    	
	    	// pêtla do losowania wspó³rzêdnych po³o¿enia kolejnych min
	    	for(int i = 0; i <= tab_location_mine.length - 1 ; i++){ 
	    		do{
	    			row_random_coordinate = gen.nextInt(10); 
	    			col_random_coordinate = gen.nextInt(10);
	    			
	    			// wypisanie wspó³rzednych wylosowanych min, pomocne przy testach
	    			// System.out.println("Wylosowano: " + row_random_coordinate + " " + col_random_coordinate);
	    			
		    		if(row_random_coordinate == 0 && col_random_coordinate == 0){ 
		    			zero_zero++; // je¿eli zostan¹ wlosowane wspó³rzêdne 0,0 to zwiêkszam zmienn¹ pomocnicz¹ o jeden 
		    		}
		    	// nastêpnie wylosowane tymczasowe zmienne sprawdzamy czy ju¿ nie by³y kiedys wylosowane i zapisane w tablicy - uruchamiamy metode checkCoordiantesUnique - je¿eli jest true to jeszcze raz s¹ losowane zmienne i jeszcze raz sprawdzane w metodzie, je¿eli false, to znaczy, ¿e mo¿emy wpisaæ te wspó³rzêdne do tablicy bo do pory nie by³y wylosowane
	    		}while(checkCoordiantesUnique(row_random_coordinate, col_random_coordinate));
	    		
	    		// przypisanie zmiennych do miejsc w tabeli po wczeœniejszym sprawdzeniu czy by³y wczêsniej wylosowane
	    		tab_location_mine[i][0] = row_random_coordinate; 
	    		tab_location_mine[i][1] = col_random_coordinate;
	    	}
	    	
	    	// wypisuje wylosowan¹ lokalizacjê min w konsoli, pomocne przy testach
	    	// for(int i = 0; i <= tab_location_mine.length - 1 ; i++){
	    	// 	for(int j = 0; j <= tab_location_mine[0].length - 1; j++){
	    	// 		System.out.print(tab_location_mine[i][j] + " ");
	    	// 	}
	    	// 	System.out.println();
	    	// }
    	}
    }
    
    ///////////////////////
    // metoda inicjuj¹ca //
    ///////////////////////
    
    public void initialize(){
    	
    	db = new DBConnector();
    	
    	// utworzenie obiektu klasy Spinner za pomoc¹ którego u¿ytkownik bêdzie wybiera³ iloœæ min
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10);
    	
    	// przypisanie do Spinera obiektu utworzonego powy¿ej
    	sp_qty_mine.setValueFactory(valueFactory);
    
    }
}

