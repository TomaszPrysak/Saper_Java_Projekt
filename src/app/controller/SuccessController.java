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
    	
    	db = new DBConnector();
    	
    	double diffInSeconds = java.time.Duration.between(GameController.timeStart, GameController.timeStop).toMillis();
    	
    	double time = Math.round(diffInSeconds/1000);
    	
    	Connection conn = db.Connection();
    	Statement stat = conn.createStatement();
    	
    	ResultSet rs1 = stat.executeQuery("select * from results where user_name = '" + GameController.user_name + "' and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	
    	if(rs1.next()){
    		double time_temp = rs1.getDouble(3);
    		if(time > time_temp){
    			Alert a = new Alert(AlertType.INFORMATION);
        		a.setContentText("Niestety nie uda³o Ci siê poprawiæ swojego dotychczasowego czasu: "+ time_temp + ", w kategori " + GameController.qty_mine_overall_user_chooice + " min");
        		a.setTitle("Informacja");
        		a.setHeaderText("PRZYKRO");
        		a.showAndWait();
    		}else{
    			Alert a = new Alert(AlertType.INFORMATION);
        		a.setContentText("Pobi³eœ swój dotychczas najlepszy czas !!!\nAktualny czas zostanie zapisany do bazy danych");
        		a.setTitle("Informacja");
        		a.setHeaderText("BRAWO!");
        		a.showAndWait();
        		
        		String sql = "update results set stopwatch = " + time + " where user_name = '" + GameController.user_name + "' and and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";";
            	PreparedStatement ps = conn.prepareStatement(sql);
            	ps.executeUpdate();
    		}
    	}else{
    		String sql = "insert into results (user_name, stopwatch, qty_mine) values ('" + GameController.user_name + "', " + time + ", " + GameController.qty_mine_overall_user_chooice + ");";
        	PreparedStatement ps = conn.prepareStatement(sql);
        	ps.executeUpdate();
    	}
    	
    	ResultSet rs2 = stat.executeQuery("select * from results where user_name = '" + GameController.user_name + "' and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	rs2.next();
    	double time_to_rank = rs2.getDouble(3);
    	
    	lb_name.setText(GameController.user_name);
    	lb_time.setText(String.valueOf(time_to_rank));

    	ResultSet rs3 = stat.executeQuery("select count(*) from results where stopwatch < " + time_to_rank + " and qty_mine = " + GameController.qty_mine_overall_user_chooice + ";");
    	rs3.next();
    	
    	int rank = rs3.getInt(1) + 1;
    	
    	lb_rank.setText(String.valueOf(rank));
    	
    	lb_qty_mine.setText(String.valueOf(GameController.qty_mine_overall_user_chooice));
    	
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