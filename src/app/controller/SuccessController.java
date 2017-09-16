package app.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import app.database.DBConnector;
import app.model.DBModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SuccessController {

    @FXML
    private ImageView imag_success;

    @FXML
    private Label lb_name;

    @FXML
    private Label lb_time;

    @FXML
    private Label lb_rank;

    @FXML
    private Label lb_qty_mine;

    @FXML
    private TableView<DBModel> table_rank;

    @FXML
    private TableColumn<DBModel, Integer> col_id;

    @FXML
    private TableColumn<DBModel, String> col_user_name;

    @FXML
    private TableColumn<DBModel, Double> col_stopwatch;
    
    @FXML
    private TableColumn<DBModel, Integer> col_qty_mine;

    @FXML
    private Button btn_new_game;

    @FXML
    private Button btn_exit;

    public DBConnector db;
    
    public ObservableList<DBModel> data;
    
    @FXML
    void exitAction(MouseEvent eSvent) {
    	System.exit(0);
    }

	/////////////////////
	// obs³uga zdarzeñ //
	/////////////////////
    
    @FXML
    void newGameAction(MouseEvent event) throws IOException {
    	
    	// przy klikniêciu przycisku Nowa Gra zamykane jest okno z rezulatetem ostatniej gry i uruchamiane okno z now¹ gr¹
    	Stage stageGame = (Stage) btn_new_game.getScene().getWindow();
		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameView.fxml"));
		Scene sceneGame = new Scene(parent);
		stageGame.setScene(sceneGame);
		stageGame.setTitle("WYGRANA!");
		stageGame.setResizable(false);
		stageGame.show();
    }

    ///////////////////////
    // metoda inicjuj¹ca //
    ///////////////////////
    
    public void initialize() throws ClassNotFoundException, SQLException{
    	
    	// po³¹czenie z baz¹ danych
    	db = new DBConnector();
    	Connection conn = db.Connection();
    	Statement stat = conn.createStatement();
    	
    	// obliczanie czasu rozgrywki na podstawie czasu pocz¹tku ku rozgrywki i koñca w GameControllerze
    	// zaokr¹glanie czasu rozgrywki do 0,001
    	double diffInSeconds = java.time.Duration.between(GameController.timeStart, GameController.timeStop).toMillis();
    	double time = Math.round(diffInSeconds/1000);
    	
    	// pobieranie dadnych z bazy danych o wprowadzonej nazwie uzytkownika i iloœci wybranych min
    	ResultSet rs1 = stat.executeQuery("select * from results where user_name = '" + GameController.user_name + "' and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	
    	// sprawdzenie czy taki rekord istnieje to znaczy, czy taki u¿ytkownik ju¿ kiedyœ gra³
    	if(rs1.next()){
    		
    		// je¿eli tak to przypisywanie do zmiennej informacji o osi¹gniêtym czasie
    		double time_temp = rs1.getDouble(3);
    		
    		// sprawdzenie czy osi¹gniêty w aktualnej rozgrywce czas jest lepszy od tego zapisanego w bazie dla tego u¿ytkownika i tej iloœci min
    		if(time > time_temp){
    			
    			// je¿eli nie to wyœwietlany jest komunikat, ¿e w bazie danych pozostanie ten dotychczasowy
    			Alert a = new Alert(AlertType.INFORMATION);
        		a.setContentText("Osiogn¹³eœ czas: " + time + "\nJest to gorzej ni¿ Twój dotychczasowy najlepszy czas: "+ time_temp + ", w kategori " + GameController.qty_mine_overall_user_chooice + " min");
        		a.setTitle("Informacja");
        		a.setHeaderText("PRZYKRO NAM");
        		a.showAndWait();
    		}else{
    			
    			// je¿eli tak to informacja, ¿e do bazy danych zostanie zapisany ten lepszy
    			Alert a = new Alert(AlertType.INFORMATION);
        		a.setContentText("Pobi³eœ swój dotychczas najlepszy czas !!!\nAktualny czas zostanie zapisany do bazy danych");
        		a.setTitle("Informacja");
        		a.setHeaderText("BRAWO!");
        		a.showAndWait();
        		
        		// aktualizacja danych w bazie danych
        		String sql = "update results set stopwatch = " + time + " where user_name = '" + GameController.user_name + "' and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";";
            	PreparedStatement ps = conn.prepareStatement(sql);
            	ps.executeUpdate();
    		}
    	}else{
    		
    		// je¿eli u¿ytkownik gra poraz pierwszy wogóle lub poraz pierwszy przy tej iloœci min któr¹ wybra³ to jest zapis do bazy danych
    		String sql = "insert into results (user_name, stopwatch, qty_mine) values ('" + GameController.user_name + "', " + time + ", " + GameController.qty_mine_overall_user_chooice + ");";
        	PreparedStatement ps = conn.prepareStatement(sql);
        	ps.executeUpdate();
    	}
    	
    	// jeszcze raz pobieranie informacji z bazy danych o tym u¿ytkowniku aby pobrac aktualne dane
    	// przypisanie wartoœæi do zmiennej czas z azy danych
    	ResultSet rs2 = stat.executeQuery("select * from results where user_name = '" + GameController.user_name + "' and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	rs2.next();
    	double time_to_rank = rs1.getDouble(3);
    	
    	// ustawienie na polach tekstowych nazwy u¿ytkownika i jego czasi
    	lb_name.setText(GameController.user_name);
    	lb_time.setText(String.valueOf(time_to_rank));
    	
    	// pobranie z bazy danych ile jest lepszych wyników od wyniku uzyskanego przez u¿ytkownika
    	// przypisanie do zmiennej iloœci lepszych wyników i zwiêkszenie jej o jeden aby uzyskaæ pozycjê u¿ytkownika
    	ResultSet rs3 = stat.executeQuery("select count(*) from results where stopwatch < " + time_to_rank + " and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	rs3.next();
    	int rank = rs3.getInt(1) + 1;
    	
    	// ustawienie na polu tekstowym pozycji u¿ytkownika
    	lb_rank.setText(String.valueOf(rank));
    	
    	// ustawienie na polu teksotwym w jakiej kategorii gra³ gracz - iloœæ min
    	lb_qty_mine.setText(String.valueOf(GameController.qty_mine_overall_user_chooice));
    	
    	// zmienna do stworzenia tablicy z ca³ym rankingiem w danej kategorii min
    	// pobranie informacji z bazy danych o wszystkich wynikach graczy w konkretnej kategorii min
    	// zapisywanie do zmiennej tablicowej informacji z bazy danych
    	// wyœwietlanie w konkretnch kolumnach tableli informacji z bazy danych
    	// wyœweitlenie tabli z rankingiem
    	data = FXCollections.observableArrayList();
    	ResultSet rs4 = conn.createStatement().executeQuery("select * from results where qty_mine = " + GameController.qty_mine_overall_user_chooice + " order by stopwatch;");
    	int x = 1;
    	while(rs4.next()){
    		data.add(new DBModel(x, rs4.getString(2), rs4.getDouble(3), rs4.getInt(4)));
    		x++;
    	}
    	col_id.setCellValueFactory(new PropertyValueFactory<DBModel, Integer>("id_result"));
    	col_user_name.setCellValueFactory(new PropertyValueFactory<DBModel, String>("user_name"));
    	col_stopwatch.setCellValueFactory(new PropertyValueFactory<DBModel, Double>("stopwatch"));
    	col_qty_mine.setCellValueFactory(new PropertyValueFactory<DBModel, Integer>("qty_mine"));
    	table_rank.setItems(data);
    	
    }
    
}