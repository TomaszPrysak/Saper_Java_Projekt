package app.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import app.database.DBConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button btn_new_game;

    @FXML
    private Button btn_exit;

    DBConnector db;
    
    @FXML
    void exitAction(MouseEvent eSvent) {
    	System.exit(0);
    }

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

    public void initialize() throws ClassNotFoundException, SQLException{
    	
    	db = new DBConnector();
    	
    	double diffInSeconds = java.time.Duration.between(GameController.timeStart, GameController.timeStop).toMillis();
    	
    	float time = Math.round(diffInSeconds/1000);
    	
    	System.out.println(Math.round(diffInSeconds/1000));
    	
    	lb_name.setText(GameController.user_name);
    	lb_time.setText(String.valueOf(diffInSeconds/1000));
    	
    	Connection conn = db.Connection();
    	String sql = "insert into results (user_name, stopwatch) values ('" + GameController.user_name + "', " + time + ");";
    	PreparedStatement ps = conn.prepareStatement(sql);
    	ps.executeUpdate();
    	
    }
    
}